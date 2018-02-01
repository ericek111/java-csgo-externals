package me.lixko.csgoexternals.modules;

import java.io.File;
import java.io.IOException;

import me.lixko.csgoexternals.util.bsp.BSPParser;

public class MapRender extends Module {
	
	BSPParser bsp;
	
	@Override
	public void onWorldRender() {
		if(bsp == null) return;
		
		
	}
	
	@Override
	public void onEngineLoaded() {
		if(true) return;
		try {
			bsp = new BSPParser(new File("/home/erik/.steam/steam/steamapps/common/Counter-Strike Global Offensive/csgo/maps/de_dust2.bsp"));
			bsp.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
