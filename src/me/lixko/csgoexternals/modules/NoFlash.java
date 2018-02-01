package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;

public class NoFlash extends Module {

	@Override
	public void onLoop() {
		if (!this.isToggled() || Offsets.m_dwLocalPlayer == 0)
			return;
		if (Engine.clientModule().readFloat(Offsets.m_dwLocalPlayer + Netvars.CCSPlayer.m_flFlashMaxAlpha) > 70f)
			Engine.clientModule().writeFloat(Offsets.m_dwLocalPlayer + Netvars.CCSPlayer.m_flFlashMaxAlpha, 100f);
	}

	@Override
	public void onDisable() {
		Engine.clientModule().writeFloat(Offsets.m_dwLocalPlayer + Netvars.CCSPlayer.m_flFlashMaxAlpha, 255f);
	}
}
