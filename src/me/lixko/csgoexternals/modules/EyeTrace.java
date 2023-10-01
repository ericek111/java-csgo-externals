package me.lixko.csgoexternals.modules;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Studio;
import me.lixko.csgoexternals.structs.Mstudiobone_t;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MathUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;

public class EyeTrace extends Module {
	
	boolean needsDataUpdate = false;
	Module thismodule = this;

	float[][] lineStart = new float[64][3];
	float[][] lineEnd = new float[64][3];
	float[][] lineColor = new float[64][4];
	boolean[] shouldRender = new boolean[64];
	MemoryBuffer anglesBuf = new MemoryBuffer(Float.BYTES * 3);
//	int bonesBufMutex = 0;

	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(5);
					//if (!thismodule.isToggled() || Offsets.m_dwLocalPlayer == 0)
					//	continue;

					updateArray();

				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	});

	@Override
	public void onEngineLoaded() {
		updateLoop.start();
	}

	@Override
	public void onWorldRender() {
		//if (!Client.theClient.isRunning || !thismodule.isToggled())
		//	return;
		
		for (int i = 1; i < lineStart.length; i++) {
			if (!shouldRender[i])
				continue;
			
			float[] start = lineStart[i];
			float[] end = lineEnd[i];
			float[] color = lineColor[i];
			DrawUtils.setColor(color);
			DrawUtils.drawLine(start, end);
			
			DrawUtils.draw3DString("HELOOOOO", 0, 0, 0, 0, 0, 1);
			
			
		}
	}

	public void updateArray() {
		for (int i = 1; i < 64; i++) {
			shouldRender[i] = false;
			
			long entityptr = MemoryUtils.getEntity(i);
			if (entityptr == 0)
				continue;
			if (entityptr == Offsets.m_dwLocalPlayer)
				continue;

			boolean isDormant = Engine.clientModule().readBoolean(entityptr + Offsets.m_bDormant);
			if (isDormant)
				continue;

			int health = Engine.clientModule().readInt(entityptr + Netvars.CBasePlayer.m_iHealth);
			if (health < 1)
				continue;
			
			int team = Engine.clientModule().readInt(entityptr + Netvars.CBaseEntity.m_iTeamNum);
			float[] colorS = {1.0f, 1.0f, 1.0f, 0.7f};
			float[] colorT = {0.878f, 0.686f, 0.337f, 1f};
			float[] colorCT = {0.54f, 0.72f, 1.0f, 1f};
			
			if (team == 1) {
				this.lineColor[i] = colorS;
			} else if (team == 2) {
				this.lineColor[i] = colorT;
			} else if (team == 3) {
				this.lineColor[i] = colorCT;
			}
			
			Engine.clientModule().read(entityptr + Netvars.CBaseEntity.m_vecOrigin, anglesBuf);
			lineStart[i] = anglesBuf.getFloatArray(0, 3);
			Engine.clientModule().read(entityptr +  + Netvars.CBasePlayer.localdata.m_vecViewOffset_0, anglesBuf);
			float[] off = anglesBuf.getFloatArray(0, 3);
			MathUtils.add(lineStart[i], off);
			Engine.clientModule().read(entityptr + Netvars.CCSPlayer.m_angEyeAngles, anglesBuf);
			float[] angles = anglesBuf.getFloatArray(0, 3);
			
			lineEnd[i] = MathUtils.crotateVector(lineStart[i], angles, 2000f);
			shouldRender[i] = true;
		}
	}

}
