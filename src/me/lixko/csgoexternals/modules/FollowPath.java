package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.GLGraph;
import me.lixko.csgoexternals.util.GLTrace;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;
import me.lixko.csgoexternals.util.XKeySym;

public class FollowPath extends Module {
	
	MemoryBuffer originbuf = new MemoryBuffer(Float.BYTES * 3);
	VectorMem origin = new VectorMem(originbuf);
	GLTrace trace;
	boolean startDrawing = false;
	
	long trackingEntity = 0;
	int trackingEntityIdx = 0;
	String trackingName = null;
	float[] trackingOrigin = new float[3];
	float trackingDistance = 0f;
	
	int width = 720, height = 360, startX = 1920 - 50 - width, startY = 450;
	GLGraph diffGraph;
	
	@Override
	public void onEngineLoaded() {
		diffGraph = new GLGraph(DrawUtils.gl, startX, startY, width, height, 1.0f, 400);
		diffGraph.setColor(0f, 1f, 1f, 1f);
		
		trace = new GLTrace(DrawUtils.gl, startX, startY, width, height, 400);
		//trace.setAutoscale(true);
		trace.setLineWidth(2.0f);
		trace.setScaleX(1f);
		trace.setScaleY(1f);
		trace.setColor(1f, 0f, 0f, 1f);
	}

	@Override
	public void onUIRender() {
		if(!Client.theClient.keyboardHandler.isPressed(XKeySym.XK_R))
			return;
		DrawUtils.setAlign(TextAlign.LEFT);
		DrawUtils.setTextColor(0xFFFFFFFF);
		DrawUtils.enableStringBackground();
		DrawUtils.drawString(startX + 2, startY + height + 8, this.trackingName == null ? "ent " + this.trackingEntityIdx : this.trackingName + " @ " + trackingDistance);
		trace.render();
		diffGraph.render();
		
		
	}
		
	@Override
	public void onLoop() {
		boolean work = Client.theClient.keyboardHandler.isPressed(XKeySym.XK_R);
		if (!work) {
			startDrawing = false;
			return;
		}
		
		if (!startDrawing) {
			this.trackingEntityIdx = this.getClosestPlayer();
			if (this.trackingEntityIdx == 0) {
				this.trackingEntity = 0;
				return;
			}
			
			this.trackingEntity = MemoryUtils.getEntity(this.trackingEntityIdx);
			
			long nameptr = Engine.clientModule().readLong(Offsets.m_dwPlayerResources + 0xF78 + this.trackingEntityIdx * 8);
			if (nameptr != 0) {
				this.trackingName = Engine.clientModule().readString(nameptr, 64);
				System.out.println("> " + this.trackingName);
			} else {
				this.trackingName = null;
			}
			
			startDrawing = true;
			trace.empty();
			diffGraph.empty();
		}
		
		float[] localOrigin = DrawUtils.lppos.getViewOrigin();
		Engine.engineModule().read(this.trackingEntity + Netvars.CBaseEntity.m_vecOrigin, this.originbuf);
		this.origin.copyTo(this.trackingOrigin);
		
		float distance = MathUtils.VecDist(localOrigin, this.trackingOrigin);
		float[] diffOrigin = MathUtils.csubtract(localOrigin, this.trackingOrigin);
		
		if (distance == this.trackingDistance) {
			return;
		}
		
		trace.putSample(diffOrigin[0], diffOrigin[1]);
		diffGraph.putSample(distance);
		this.trackingDistance = distance;
	}
	
	protected int getClosestPlayer() {
		float[] localOrigin = DrawUtils.lppos.getViewOrigin();
		float bestDistance = Float.MAX_VALUE;
		int bestEntityIdx = 0;
		float[] _tmpOrigin = new float[3];
		
		long localPlayer = MemoryUtils.getLocalOrSpectated();
		for (int i = 1; i < 64; i++) {
			long entityptr = MemoryUtils.getEntity(i);
			if (entityptr == 0 || entityptr == localPlayer)
				continue;
			
			int health = Engine.clientModule().readInt(entityptr + Netvars.CBasePlayer.m_iHealth);
			if (health < 1)
				continue;
			
			Engine.engineModule().read(entityptr + Netvars.CBaseEntity.m_vecOrigin, originbuf);
			origin.copyTo(_tmpOrigin);
			
			float distance = MathUtils.VecDist(localOrigin, _tmpOrigin);
			System.out.println("# " + i + ": " + StringFormat.dump(localOrigin) + " / " + StringFormat.dump(_tmpOrigin) + " = " + distance);
			if (distance < bestDistance) {
				bestDistance = distance;
				bestEntityIdx = i;
			} else
				continue;
		}
		
		return bestEntityIdx;
	}
	
}
