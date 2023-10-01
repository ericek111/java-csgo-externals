package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;
import me.lixko.csgoexternals.util.MemoryUtils;

public class FOVChanger extends Module {

	Thread fovLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					if (Offsets.m_dwLocalPlayer == 0)
						continue;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	});

	@Override
	public void onEngineLoaded() {
		// fovLoop.start();
	}
	
	@Override
	public void onDisable() {
		long entityptr = MemoryUtils.getLocalOrSpectated();
		if (entityptr == 0)
			return;
		
		int fov = Engine.clientModule().readInt(entityptr + Netvars.CBasePlayer.m_iDefaultFOV);
		Engine.clientModule().writeInt(entityptr + Netvars.CBasePlayer.m_iFOV, fov);
	}

	@Override
	public void onLoop() {
		if (!this.isToggled())
			return;
		
		long entityptr = MemoryUtils.getLocalOrSpectated();
		if (entityptr == 0)
			return;
		
		// Engine.clientModule().writeFloat(Offsets.m_dwLocalPlayer + 0x36f0 + 0x48, 0.000005f);
		Engine.clientModule().writeInt(entityptr + Netvars.CBasePlayer.m_iFOV, (int) 5);
		// Engine.clientModule().writeInt(entityptr + Netvars.CBasePlayer.m_iFOVStart, (int) 0);
		// Engine.clientModule().writeFloat(entityptr + Netvars.CBasePlayer.m_flFOVTime, 1.0f);
	}

}
