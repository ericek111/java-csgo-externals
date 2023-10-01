package me.lixko.csgoexternals.offsets;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.util.StringFormat;

public final class Offsets {
	
	public static String OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE = "55 48 89 E5 41 57 41 56 41 55 41 54 53 48 81 EC ?? ?? ?? ?? 89 BD 18 FF FF FF 80 3D ?? ?? ?? ?? 00 89 B5 14 FF FF FF";
	public static String ENTITYLIST_SIGNATURE = "55 48 89 E5 48 83 EC 10 8B 47 34 48 8D 75 F0 89 45 F0 48 8D 05 ?? ?? ?? ?? 48 8B 38"; // 2023-07-03
	public static String GLOWOBJECT_SIGNATURE = "E8 ?? ?? ?? ?? 49 8B 7D 00 C7 40 38 00 00 00 00 48 8B 07 FF 90";
	public static String PLAYERRESOURCES_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 48 89 E5 48 85 C0 74 10 48";
	public static String ALT1_SIGNATURE = "89 D8 80 CC 40 F6 C2 03 0F 45 D8 44 89 ?? C1 E0 11 C1 F8 1F 83 E8 03";
	public static String FORCEATTACK_SIGNATURE = "89 D8 83 C8 01 F6 C2 03 0F 45 D8 44 89 ?? 83 E0 01 F7 D8 83 E8 03";
	public static String LOCALPLAYER_SIGNATURE = "48 89 e5 74 0e 48 8d 05 ?? ?? ?? ??";
	public static String GLOBALVARS_SIGNATURE = "48 89 E5 74 16 48 8D 05 ?? ?? ?? ?? 48 8B 00 F3 0F 10 48 10 F3 0F 5C C8 0F 28 C1 5D C3"; // 2023-07-03
	public static String GAMERULES_SIGNATURE = "48 8D 1D ?? ?? ?? ?? 48 8B 3B 48 85 FF 74 06"; // 2023-07-03
	public static String CLIENTCLASSHEAD_SIGNATURE = "44 89 E0 BA 01 00 00 00 44 89 E1 C1 F8 05 D3 E2 48 98 41 09 14 82 48 8D 05 ?? ?? ?? ?? 8B 53 14 48 8B 00 48 85 C0"; // 2023-07-03
	public static String INCROSS_SIGNATURE = "31 C0 0F 2F 83 ?? ?? ?? ?? 76 14 8B 83 ?? ?? ?? ?? 85 C0 75 0A 8B 93";
	public static String VIEWANGLES_SIGNATURE = "e8 ?? ?? ?? ?? f3 0f ?? ?? ?? ?? 00 00 f3 0f";
	public static String MODELINFO_SIGNATURE = "48 83 38 00 0F 84 11 02 00 00 48 8B 83 ?? ?? ?? ?? 48 85 C0 0F 84 10 01 00 00 8B 75 CC 48 89 DF FF D0"; // 2023-07-03
	
	public static String GAMEDIRECTORY_SIGNATURE = "55 BA 04 01 00 00 48 89 E5 48 81 EC 10 01 00 00 48 8B 3D ?? ?? ?? ?? 48 8D B5 F0 FE FF FF";
	public static String ENGINEPOINTER_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 48 89 E5 5D 48 83 C0 08 C3";
	public static String CLIENTSTATE_SIGNATURE =   "C6 05 ?? ?? ?? ?? ?? 48 89 05 ?? ?? ?? ?? 0F 84";
	public static String MDLCACHE_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 31 F6 48 89 E5 5D 48 8B 38 48 8B 07 48 8B 40 48 FF E0"; // _g_pMDLCache
	public static String HLTVCAMERA_SIGNATURE = "48 89 E5 FF 90 80 00 00 00 85 C0 75 0C 0F 57 C0 0F 2F 05 ?? ?? ?? ?? 77 18 5D 48 8D 3D ?? ?? ?? ??";
	/**
	 * Offsets
	 */
	public static long m_dwGlowObject;

	public static final InputOffsets input = new InputOffsets();

	public static long m_dw_bOverridePostProcessingDisable;
	public static long m_dwPlayerResourcesPointer;
	public static long m_dwPlayerResources;
	public static long m_dwEntityList;
	public static long m_dwLocalPlayer;
	public static long m_dwLocalPlayerPointer; // < dereference!
	public static long m_dwGlobalVars;
	public static long m_dwGlobalVarsPointer;
	public static long m_dwGameRules;
	public static long m_dwClientClassHead;
	public static long m_dwServerDetail;
	public static long m_offsetModelInfo;
	public static long m_dwHLTVCamera; // https://www.unknowncheats.me/forum/2488163-post12.html

	public static long m_dwClientState;
	public static long m_dwModelCache;
	//public static long m_szGameDirectory;
	public static String modDirectory;

	// Globals
	public static long g_vecCurrentRenderOrigin;
	public static long g_vecCurrentRenderAngles;
	public static long g_matCurrentCamInverse;
	public static long g_MDLCache; // CMDLCache
	
	/*
	 * Static offsets
	 */

	public static long m_bDormant = 0x121;
	// CBaseAnimating->m_pStudioBones 0x2C44 + 2C, DT_BaseAnimating->m_nForceBone + 2C
	public static long m_dwBoneMatrix = Netvars.CBaseAnimating.m_nForceBone + 0x2c;

	public static long m_iLastCrosshairIndex = 0xBBD4; // outdated
	public static long m_iCrosshairIndex = 0xBBF0;
	public static long m_vecViewAngles = 0x8E20;

	public static long m_nDeltaTick = 0x20C;
	public static long m_szMapFile = 0x220;
	public static long m_bIsInGame = 0x1A0;
	
	public static NetvarDumper netvars = new NetvarDumper();

	public static void load() {
		/*long mdlcachemov = PatternScanner.getAddressForPattern(Engine.engineModule(), MDLCACHE_SIGNATURE);
		long mdlcacheptr = Engine.engineModule().GetAbsoluteAddress(mdlcachemov, 3, 7);
		g_MDLCache = Engine.engineModule().readLong(mdlcacheptr);
		System.out.println(StringFormat.hex(g_MDLCache));
		System.exit(0);*/
		
		//Engine.clientModule().write(Engine.clientModule().start() + 0x789f2B, new MemoryBuffer(new byte[] { (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0x90 }));
		try {
			long clientclassheadlea = PatternScanner.getAddressForPattern(Engine.clientModule(), CLIENTCLASSHEAD_SIGNATURE) + 22;
			m_dwClientClassHead = Engine.clientModule().GetAbsoluteAddress(clientclassheadlea, 3, 7);
			m_dwClientClassHead = Engine.clientModule().readLong(m_dwClientClassHead);
			
			netvars.initialize();	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		for (String arg : Engine.cmdargs) {
			if (arg.equalsIgnoreCase("-netvars"))
				System.exit(0);
		}
		
		// set $engine_addr = 0x7fc51e24f000
		// grep -m1 -ioP '[\da-f]+(?=-.+engine_client\.so)' /proc/$(pidof csgo_linux64)/maps
		// set $unk = (int64_t(*)(int64_t)) $engine_addr+0x2FBC70
		// call $unk(-1)
		// p/f *($unk(0)+0x8E20)
		// p/f *(((int64_t(*)(int64_t)) $engine_addr+0x2FBC70)(0)+0x8E20)
		
		/*GameInterface engineIf = Engine.engineModule().getInterfacePartially("VEngineClient0");
		long GetLocalPlayer = engineIf.getFunction(12);
		long GetLocalClient = Engine.engineModule().GetAbsoluteAddress(GetLocalPlayer + 9, 1, 5);
		System.out.print(StringFormat.hex(GetLocalClient - Engine.engineModule().start()));*/
		
		long foundSplitScreenMgrlea = PatternScanner.getAddressForPattern(Engine.engineModule(), CLIENTSTATE_SIGNATURE) + 7;
		long g_SplitScreenMgr = Engine.engineModule().GetAbsoluteAddress(foundSplitScreenMgrlea, 3, 7);
		m_dwClientState = Engine.engineModule().readLong(g_SplitScreenMgr + 8) + 8;
		

		
		long foundViewAngles = PatternScanner.getAddressForPattern(Engine.engineModule(), VIEWANGLES_SIGNATURE) + 9;
		m_vecViewAngles = Engine.engineModule().readUnsignedInt(foundViewAngles);
		
		/* TODO: FIX: long foundHLTVCameraLea = PatternScanner.getAddressForPattern(Engine.clientModule(), HLTVCAMERA_SIGNATURE) + 26;
		m_dwHLTVCamera = Engine.clientModule().GetAbsoluteAddress(foundHLTVCameraLea, 3, 7); */
		
		/*boolean ispaused = Engine.engineModule().readBoolean(clientstate + 0x220);
		
		int ingame = Engine.engineModule().readInt(clientstate + 0x1A0);
		
		System.out.println(ispaused);
		System.out.println(ingame);
	//	clientstate = Engine.engineModule().readLong(clientstate);
		//long m_dwIsInGame = Engine.engineModule().readLong(clientstate + 0x1A0);
		//System.out.println(StringFormat.hex(m_dwIsInGame));
		//int ingame = Engine.engineModule().readInt(m_dwIsInGame);
		//System.out.println(ingame);

		String mapname = Engine.engineModule().readString(m_szMapName, 256);
		System.out.println(mapname);
		
		
		System.exit(0);
		/**
		 * Client.dll offsets
		 */
		long foundGlowPointerCall = PatternScanner.getAddressForPattern(Engine.clientModule(), GLOWOBJECT_SIGNATURE);
		System.out.println("Glow Pointer Call Reference: " + StringFormat.hex(foundGlowPointerCall));
		long glowFunctionCall = Engine.clientModule().GetCallAddress(foundGlowPointerCall);
		System.out.println("GlowObjectManager constructor address: " + StringFormat.hex(glowFunctionCall));
		m_dwGlowObject = Engine.clientModule().GetAbsoluteAddress(glowFunctionCall + 9, 3, 7);
		System.out.println("CGlowObjectManager pointer: " + StringFormat.hex(m_dwGlowObject) + " / " + StringFormat.hex(m_dwGlowObject - Engine.clientModule().start()));
		
		long alt1mov = PatternScanner.getAddressForPattern(Engine.clientModule(), ALT1_SIGNATURE);
		input.alt1 = Engine.clientModule().GetAbsoluteAddress(alt1mov - 7, 3, 7);
		System.out.println("input.alt1: " + StringFormat.hex(input.alt1 - Engine.clientModule().start()));
		
		long foundattackmov = PatternScanner.getAddressForPattern(Engine.clientModule(), FORCEATTACK_SIGNATURE);
		input.attack = Engine.clientModule().GetAbsoluteAddress(foundattackmov - 7, 3, 7);
		System.out.println("input.attack: " + StringFormat.hex(input.attack - Engine.clientModule().start()));
		input.init();

		// long overridepostprocessingdisablemov = PatternScanner.getAddressForPattern(Engine.clientModule(), OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE) + 26;
		// m_dw_bOverridePostProcessingDisable = Engine.clientModule().GetAbsoluteAddress(overridepostprocessingdisablemov, 2, 7);

		long playerresourcemov = PatternScanner.getAddressForPattern(Engine.clientModule(), PLAYERRESOURCES_SIGNATURE);
		m_dwPlayerResourcesPointer = Engine.clientModule().GetAbsoluteAddress(playerresourcemov, 3, 7);

		long entitylistmov = PatternScanner.getAddressForPattern(Engine.clientModule(), ENTITYLIST_SIGNATURE) + 18;
		m_dwEntityList = Engine.clientModule().GetAbsoluteAddress(entitylistmov, 3, 7);
		m_dwEntityList = Engine.clientModule().readLong(m_dwEntityList);
		m_dwEntityList = Engine.clientModule().readLong(m_dwEntityList) + 8;

		long localplayerlea = PatternScanner.getAddressForPattern(Engine.clientModule(), LOCALPLAYER_SIGNATURE) + 7;
		m_dwLocalPlayerPointer = Engine.clientModule().GetCallAddress(localplayerlea);
		m_dwLocalPlayer = Engine.clientModule().readLong(Offsets.m_dwLocalPlayerPointer);

		long globalvarsmov = PatternScanner.getAddressForPattern(Engine.clientModule(), GLOBALVARS_SIGNATURE) + 5;
		long globalvarsptr = Engine.clientModule().GetAbsoluteAddress(globalvarsmov, 3, 7);
		System.out.println("globalvarsptr: " + StringFormat.hex(globalvarsptr));
		m_dwGlobalVarsPointer = Engine.clientModule().readLong(globalvarsptr);
		System.out.println("m_dwGlobalVarsPointer: " + StringFormat.hex(m_dwGlobalVarsPointer));
		m_dwGlobalVars = m_dwGlobalVarsPointer; // Engine.clientModule().readLong(m_dwGlobalVarsPointer);
		// System.out.println("m_dwGlobalVars: " + StringFormat.hex(m_dwGlobalVars));

		long gamerulesmov = PatternScanner.getAddressForPattern(Engine.clientModule(), GAMERULES_SIGNATURE);
		long gamerulesptrptr = Engine.clientModule().GetAbsoluteAddress(gamerulesmov, 3, 7);
		long gamerulesptr = Engine.clientModule().readLong(gamerulesptrptr);
		m_dwGameRules = Engine.clientModule().readLong(gamerulesptr);
		
		long modelinfomov = PatternScanner.getAddressForPattern(Engine.clientModule(), MODELINFO_SIGNATURE) + 10; // or 0x3038 
		m_offsetModelInfo = Engine.clientModule().readInt(modelinfomov + 3);
		
		// m_offsetModelInfo
		
		/*long inCross = PatternScanner.getAddressForPattern(Engine.clientModule(), INCROSS_SIGNATURE);
		m_iCrosshairIndex = Engine.clientModule().readInt(inCross + 13);
		System.out.println(StringFormat.hex(m_iCrosshairIndex));*/

		/*long gamedirlea = PatternScanner.getAddressForPattern(Engine.engineModule(), GAMEDIRECTORY_SIGNATURE) + 16;
		long gamedirptr = Engine.engineModule().GetAbsoluteAddress(gamedirlea, 3, 7);
		long m_szGameDirectory = Engine.engineModule().readLong(gamedirptr);
		modDirectory = Engine.engineModule().readString(m_szGameDirectory, 256);*/
		// System.out.println(modDirectory);
		
		/*System.out.println(">>>>> Launcher module:");
		Engine.launcherModule().getInterfaces().forEach((k, v) -> System.out.println(k + ": " + v.getName()));
		long fnCreateGameWindow = Engine.launcherModule().readLong(Engine.launcherModule().start() + 0x25EA90 + 8 * 9);
		System.out.print(StringFormat.hex(fnCreateGameWindow - Engine.launcherModule().start()));*/
	}
}
