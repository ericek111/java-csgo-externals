package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.Flags;

public class Bunnyhop extends Module {

	Thread bhopLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(5);

					long localPlayer = Engine.clientModule().readLong(Offsets.m_dwLocalPlayerPointer);

					if (Engine.clientModule().readInt(Offsets.m_dw_iAlt1) == 5) {
						long m_fFlags = Engine.clientModule().readLong(localPlayer + Offsets.m_fFlags);
						if ((m_fFlags & Flags.FL_ONGROUND) > 0)
							Engine.clientModule().writeInt(Offsets.m_dwForceJump, 6);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	});

	@Override
	public void onEngineLoaded() {
		bhopLoop.start();
	}

}
