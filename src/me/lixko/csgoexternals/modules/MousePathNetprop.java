package me.lixko.csgoexternals.modules;

import java.util.Arrays;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.ItemDefinitionIndex;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.GLGraph;
import me.lixko.csgoexternals.util.GLTrace;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;
import me.lixko.csgoexternals.util.XKeySym;

public class MousePathNetprop extends Module {
	
	MemoryBuffer viewanglesbuf = new MemoryBuffer(Float.BYTES * 3);
	VectorMem viewangles = new VectorMem(viewanglesbuf);
	
	MemoryBuffer punchanglesbuf = new MemoryBuffer(Float.BYTES * 3);
	VectorMem punchangles = new VectorMem(punchanglesbuf);
	
	float[] startAngles = new float[3];
	float[] angles = new float[3], oldAngles;
	float[] punch = new float[3];
	GLTrace trace;
	boolean startDrawing = false;
	
	int width = 720, height = 360, startX = 1920 - 50 - width, startY = 450;
	
	GLGraph diffGraph;
	
	@Override
	public void onEngineLoaded() {
		diffGraph = new GLGraph(DrawUtils.gl, startX, startY, width, height, 5f, 400);
		diffGraph.setColor(0f, 1f, 1f, 1f);
		
		
		trace = new GLTrace(DrawUtils.gl, startX, startY, width, height, 400);
		//trace.setAutoscale(true);
		trace.setLineWidth(2.0f);
		trace.setScaleX(10.0f);
		trace.setScaleY(10.0f);
		trace.setColor(1f, 0f, 0f, 1f);
	}

	@Override
	public void onUIRender() {
		if(!Client.theClient.keyboardHandler.isPressed(XKeySym.XK_R))
			return;
		DrawUtils.setAlign(TextAlign.LEFT);
		DrawUtils.setTextColor(0xFFFFFFFF);
		DrawUtils.enableStringBackground();
		DrawUtils.drawString(startX + 2, startY + height + 8, "CCSPlayer.m_angEyeAngles - m_aimPunchAngle");
		trace.render();
		diffGraph.render();
	}
		
	@Override
	public void onLoop() {
		boolean work = Client.theClient.keyboardHandler.isPressed(XKeySym.XK_R);
		if (!work) {
			startDrawing = false;
			return;
		} else {
			long entityptr = MemoryUtils.getLocalOrSpectated();
			if (entityptr == 0)
				return;
			
			int activeWeapon = Engine.clientModule().readInt(entityptr + Netvars.CBaseCombatCharacter.m_hActiveWeapon) & Const.ENT_ENTRY_MASK;
			long weaponptr = MemoryUtils.getEntity(activeWeapon);
			if (weaponptr == 0)
				return;
			
			float timeNow = Engine.globalVars.curtime.getFloat();
			float timeFired = Engine.clientModule().readFloat(weaponptr + Netvars.CWeaponCSBase.m_fLastShotTime);
			
			if (timeNow - timeFired > 0.5f) {
				// return;
			}
		}
		
		Engine.engineModule().read(Offsets.m_dwLocalPlayer + Netvars.CCSPlayer.m_angEyeAngles, viewanglesbuf);
		viewangles.copyTo(angles);
		Engine.clientModule().read(Offsets.m_dwLocalPlayer + Netvars.CBasePlayer.localdata.m_Local.BASE_OFFSET + Netvars.CBasePlayer.localdata.m_Local.m_aimPunchAngle, punchanglesbuf);
		punchangles.copyTo(punch);
		
		if (angles[0] > 90f)
			angles[0] -= 360f;
		
		if (angles[1] > 180f)
			angles[1] -= 360f;
		
		if (!startDrawing) {
			startDrawing = true;
			trace.empty();
			diffGraph.empty();
			oldAngles = null;
			startAngles = angles.clone();
		}
		
		float[] diffAngles = MathUtils.cadd(startAngles, punch);
		MathUtils.subtract(diffAngles, angles);
		float diff = MathUtils.VecLength(diffAngles) * 2f;
		
		//System.out.println("EEEput sample " + trace.currentSample() + " / " + StringFormat.dump(angles));
		
		MathUtils.subtract(angles, startAngles);
		MathUtils.multiply(angles, -1f);

		if (oldAngles != null && Math.abs(oldAngles[1] - angles[1]) > 60f) {
			if (angles[1] > 0f && oldAngles[1] < 0f) {
				angles[1] -= 360f;				
			} else if (angles[1] < 0f && oldAngles[1] > 0f) {
				angles[1] += 360f;
			}
		}
		
		if (oldAngles == null || !Arrays.equals(oldAngles, angles)) {
			oldAngles = angles.clone();
			
			MathUtils.subtract(angles, punch);
			trace.putSample(angles[1], angles[0]);
			//System.out.println("- sample " + trace.currentSample() + " / " + StringFormat.dump(angles));
			

			diffGraph.putSample(diff);
		}
	}
}
