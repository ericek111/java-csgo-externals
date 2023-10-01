package me.lixko.csgoexternals.modules;

import java.util.Arrays;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.structs.VectorMem;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.GLTrace;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;
import me.lixko.csgoexternals.util.XKeySym;

public class MousePath extends Module {
	
	MemoryBuffer viewanglesbuf = new MemoryBuffer(Float.BYTES * 3);
	VectorMem viewangles = new VectorMem(viewanglesbuf);
	float[] startAngles = new float[3];
	float[] angles = new float[3], oldAngles;
	GLTrace trace;
	boolean startDrawing = false;
	
	int startX = 50, startY = 450, width = 720, height = 360;
	
	@Override
	public void onEngineLoaded() {
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
		
		DrawUtils.setAlign(TextAlign.RIGHT);
		DrawUtils.setTextColor(0xFFFFFFFF);
		DrawUtils.enableStringBackground();
		DrawUtils.drawString(startX + width - 2, startY + height + 8, "Engine ViewAngles");
		trace.render();
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
		
		Engine.engineModule().read(Offsets.m_dwClientState + Offsets.m_vecViewAngles, viewanglesbuf);
		viewangles.copyTo(angles);
		
		if (!startDrawing) {
			startDrawing = true;
			trace.empty();
			oldAngles = null;
			startAngles = angles.clone();
		}
		
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
			trace.putSample(angles[1], angles[0]);
			//System.out.println("# sample " + trace.currentSample() + " / " + StringFormat.dump(angles));
			oldAngles = angles.clone();
		}
	}
}
