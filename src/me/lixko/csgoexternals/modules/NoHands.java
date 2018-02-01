package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;

public class NoHands extends Module {

	@Override
	public void onLoop() {
		if (Offsets.m_dwLocalPlayer == 0 || !this.isToggled())
			return;
		Engine.clientModule().writeInt(Offsets.m_dwLocalPlayer + Netvars.CBaseEntity.m_nModelIndex, 20);
	}

}
