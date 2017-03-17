package me.lixko.csgoexternals;

import java.io.IOException;
import java.util.Locale;

public final class Main {

	// http://stackoverflow.com/questions/2580279/how-do-i-run-my-application-as-superuser-from-eclipse
	public static void main(String... args) {
		Locale.setDefault(new Locale("en", "US"));
		try {
			Engine engine = new Engine();
			engine.init();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

}
