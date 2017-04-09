package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;

public class FOVChanger extends Module {

	Thread fovLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					if(Offsets.m_dwLocalPlayer == 0) continue;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	});

	@Override
	public void onEngineLoaded() {
		//fovLoop.start();
	}
	
	@Override
	public void onLoop() {
		if(true) return;
		Engine.clientModule().writeFloat(Offsets.m_dwLocalPlayer + 0x36f0 + 0x48, 0.000005f);
		if(Engine.clientModule().readBoolean(Offsets.m_dwLocalPlayer + 0x4164)) return;
		Engine.clientModule().writeInt(Offsets.m_dwLocalPlayer + 0x3998, (int)120);
		Engine.clientModule().writeInt(Offsets.m_dwLocalPlayer + 0x399c, (int)120);
		Engine.clientModule().writeInt(Offsets.m_dwLocalPlayer + 0x3b14, (int)120);
		
	}
	
}
