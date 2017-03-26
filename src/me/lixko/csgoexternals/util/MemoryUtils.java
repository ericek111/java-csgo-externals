package me.lixko.csgoexternals.util;

import me.lixko.csgoexternals.Engine;

public class MemoryUtils {
	
	public static String getEntityClassName(long entityaddr) {
		long vtable = Engine.clientModule().readLong(entityaddr + 8);
		long fn = Engine.clientModule().readLong(vtable - 8);
		long cls = Engine.clientModule().readLong(fn + 8);
		return Engine.clientModule().readString(cls, 64);
	}
	
}
