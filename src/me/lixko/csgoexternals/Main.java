package me.lixko.csgoexternals;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.demo.DemoParser;

public final class Main {

	// http://stackoverflow.com/questions/2580279/how-do-i-run-my-application-as-superuser-from-eclipse
	public static void main(String... args) {		
		Locale.setDefault(new Locale("en", "US"));
		try {
			boolean demop = false;
			for (String arg : args) {
				if (arg.equalsIgnoreCase("-nooverlay") || arg.equalsIgnoreCase("-disableoverlay"))
					DrawUtils.enableOverlay = false;
				if (arg.equalsIgnoreCase("-demop"))
					demop = true;
			}
			//DrawUtils.enableOverlay = false;
			if (!DrawUtils.enableOverlay)
				System.out.println("Disabling overlay!");

			if(demop) {
				//DemoParser demp = new DemoParser(new File("/home/erik/.steam/steam/steamapps/common/Counter-Strike Global Offensive/csgo/7_1_1946.dem"));
				//DemoParser demp = new DemoParser(new File("/home/erik/.steam/steam/steamapps/common/Counter-Strike Global Offensive/csgo/jb.dem"));
				DemoParser demp = new DemoParser(new File("/home/erik/.steam/steam/steamapps/common/Counter-Strike Global Offensive/csgo/jb.dem"));
				demp.parse();
				System.exit(0);				
			}

			Engine engine = new Engine();
			engine.init(args);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

}
