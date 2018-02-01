package me.lixko.csgoexternals.util;

public class Profiler {
	public static Profiler INSTANCE = null;
	
	static {
		INSTANCE = new Profiler();
	}
}
