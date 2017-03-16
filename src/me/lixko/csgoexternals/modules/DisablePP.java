package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;

public class DisablePP extends Module {

	int loopwrite = 0;
	
	@Override
	public void onLoop() {
		if(!this.isToggled()) return;
		loopwrite++;		
		if(loopwrite == 300) {
			loopwrite = 0;
			disablePP(true);
		}
		
	}
	
	@Override
	public void onEnable() {
		disablePP(true);
	}
	
	@Override
	public void onDisable() {
		disablePP(false);
	}
	
	private void disablePP(boolean state) {
		Engine.clientModule().writeBoolean(Offsets.m_dw_bOverridePostProcessingDisable, state);
	}
}
