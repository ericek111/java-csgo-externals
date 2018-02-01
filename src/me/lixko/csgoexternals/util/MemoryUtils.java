package me.lixko.csgoexternals.util;

import me.lixko.csgoexternals.Engine;

public class MemoryUtils {

	public static String getEntityClassName(long entityaddr) {
		long vtable = Engine.clientModule().readLong(entityaddr + 8);
		if(vtable == 0) return "";
		long fn = Engine.clientModule().readLong(vtable - 8);
		long cls = Engine.clientModule().readLong(fn + 8);
		String classname = Engine.clientModule().readString(cls, 64);
		// classname = classname.substring(0, classname.indexOf(' '));
		return classname;
	}

	public static int getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Integer.parseInt(processName.split("@")[0]);
	}
}
