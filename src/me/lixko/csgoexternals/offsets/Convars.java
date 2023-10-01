package me.lixko.csgoexternals.offsets;

import java.util.HashMap;

import me.lixko.csgoexternals.elf.ElfModule;
import me.lixko.csgoexternals.elf.GameInterface;
import me.lixko.csgoexternals.util.StringFormat;

public class Convars {
	
	private static HashMap<String, Convar> convars = new HashMap<>();
	private static GameInterface cvarif;
	
	public static void init(ElfModule materialModule) {
		cvarif = materialModule.getInterfacePartially("VEngineCvar");
		// TODO: Check the deref. code in GameInterface()
		System.out.println(cvarif.getName());
		System.out.println("getIfRegBase: " + StringFormat.hex(cvarif.getIfRegBase()));
		System.out.println("getInstantiateFn: " + StringFormat.hex(cvarif.getInstantiateFn()));
		System.out.println("getVFTPointer: " + StringFormat.hex(cvarif.getVFTPointer()));
		System.out.println("getVFTBase: " + StringFormat.hex(cvarif.getVFTBase()));
		long a0 = cvarif.getModule().readLong(cvarif.getVFTBase() + 136); // m_pCVarList
		
		do {
			long strptr = cvarif.getModule().readLong(a0 + 0x18);
			System.out.println("strptr: " + StringFormat.hex(strptr));
			String str = cvarif.getModule().readString(strptr, 256);
			System.out.println("str: " + str);
			Convar cvar = new Convar(cvarif, a0, str);
			convars.put(str, cvar);
		} while ((a0 = cvarif.getModule().readLong(a0 + 0x8)) > 0);
	}
	
	public static Convar getConvar(String name) {
		return convars.get(name);
	}
	
}
