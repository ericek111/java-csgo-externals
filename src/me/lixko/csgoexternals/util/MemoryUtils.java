package me.lixko.csgoexternals.util;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.sdk.Const;

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
	
	public static long getEntity(int idx) {
		if(idx > Engine.entlistbuffer.size() / Long.BYTES / 4) {
			if(Offsets.m_dwEntityList == 0) return 0;
			return Engine.clientModule().readLong(Offsets.m_dwEntityList + idx * Long.BYTES * 4);
		}
		return Engine.entlistbuffer.getLong(idx * Long.BYTES * 4);
	}

	public static int getPID() {
		String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		return Integer.parseInt(processName.split("@")[0]);
	}
	
	public static long getLocalOrSpectated() {
		// isHLTV replay
		// https://www.unknowncheats.me/forum/2488163-post12.html
		if (false) {
			int iTarget1 = Engine.clientModule().readInt(Offsets.m_dwHLTVCamera + 64);
			if (iTarget1 != 0) {
				return MemoryUtils.getEntity(iTarget1);
			}
		}
				
		long entityptr = Offsets.m_dwLocalPlayer;
		int target = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Netvars.CBasePlayer.m_hObserverTarget) & Const.ENT_ENTRY_MASK;
		
		// Engine.clientModule().read(Offsets.m_dwHLTVCamera + 52
		if (target != Const.ENT_ENTRY_MASK) {
			entityptr = MemoryUtils.getEntity(target);
		}
		
		return entityptr;
	}
}
