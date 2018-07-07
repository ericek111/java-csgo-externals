package me.lixko.csgoexternals.offsets;

import java.util.Arrays;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.StringFormat;

public final class Offsets {

	public static String OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE = "55 48 89 E5 41 57 41 56 41 55 41 54 53 48 81 EC ?? ?? ?? ?? 89 BD 18 FF FF FF 80 3D ?? ?? ?? ?? 00 89 B5 14 FF FF FF";
	public static String ENTITYLIST_SIGNATURE = "55 48 89 E5 48 83 EC 10 8B 47 34 48 8D 75 F0 89 45 F0 48 8B 05 ?? ?? ?? ?? 48 8B 38";
	public static String GLOWOBJECT_SIGNATURE = "E8 ?? ?? ?? ?? 48 8B 3D ?? ?? ?? ?? BE 01 00 00 00 C7";
	public static String PLAYERRESOURCES_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 48 89 E5 48 85 C0 74 10 48";
	public static String ALT1_SIGNATURE = "89 D8 80 CC 40 F6 C2 03 0F 45 D8 44 89 ?? C1 E0 11 C1 F8 1F 83 E8 03";
	public static String FORCEATTACK_SIGNATURE = "89 D8 83 C8 01 F6 C2 03 0F 45 D8 44 89 ?? 83 E0 01 F7 D8 83 E8 03";
	public static String LOCALPLAYER_SIGNATURE = "48 89 e5 74 0e 48 8d 05 ?? ?? ?? ??";
	// public static String GLOBALVARS_SIGNATURE = "4C 8B 25 ?? ?? ?? ?? F3 0F 10 86 C0 3D 00 00 49";
	public static String GLOBALVARS_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 48 89 E5 5D 48 8B 00 F3 0F 10 40 10";
	public static String GAMERULES_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 48 8B ?? 0F 84";
	public static String CLIENTCLASSHEAD_SIGNATURE = "44 89 EA B8 01 00 00 00 44 89 E9 C1 FA 05 D3 E0 48 63 D2 41 09 04 91 48 8B 05 ?? ?? ?? ?? 8B 53 14 48 8B 00 48 85 C0 75 1B E9 F9 01 00 00";

	public static String GAMEDIRECTORY_SIGNATURE = "55 BA 04 01 00 00 48 89 E5 48 81 EC 10 01 00 00 48 8B 3D ?? ?? ?? ?? 48 8D B5 F0 FE FF FF";
	public static String ENGINEPOINTER1_SIGNATURE = "48 8D 3D ?? ?? ?? ?? 31 F6 48 89 E5 5D E9 8D FF"; // g_SplitScreenMgr
	public static String ENGINEPOINTER_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 48 89 E5 48 8B 38 48 8B 07";
	public static String SPLITSCREENMGR_SIGNATURE = "55 89 FE 48 8D 3D ?? ?? ?? ?? 48 89 E5 5D E9 AD FF FF FF";
	public static String MDLCACHE_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 31 F6 48 89 E5 5D 48 8B 38 48 8B 07 48 8B 40 48 FF E0"; // _g_pMDLCache
	public static String ISTAKINGSCREENSHOT_SIGNATURE = "55 48 85 FF 48 89 E5 C6 05 ?? ?? ?? ?? 01 C6";
	public static String SETUPCURRENTVIEW_SIGNATURE = "55 4C 8D 0D ?? ?? ?? ?? 48 89 E5 41 56 41 55 45 89 C5 41 54 4C 8D 05";
	
	/**
	 * Client.dll offsets
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
	public static long m_dwViewAngleBasePointer;

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

	public static long m_dwEntityLoopDistance = 0x20;
	public static long m_dwBoneDistance = 0x30;

	public static long m_viewPunchAngle = 0x68;
	public static long m_aimPunchAngle = 0x74;
	public static long m_bDormant = 0x121;
	public static long m_vecViewOffset = 0x13c;
	public static long m_vecVelocity = 0x148;
	public static long m_vecBaseVelocity = 0x154;
	public static long m_bSpotted = 0xECD;
	public static long m_dwBoneMatrix = 0x2C70; // CBaseAnimating->m_pStudioBones 0x2C44 + 2C

	public static long m_Local = 0x36f0;
	public static long m_iLastCrosshairIndex = 0xBBD4; // outdated
	public static long m_iCrosshairIndex = 0xBBD0;
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
		long clientclassheadlea = PatternScanner.getAddressForPattern(Engine.clientModule(), CLIENTCLASSHEAD_SIGNATURE) + 23;
		m_dwClientClassHead = Engine.clientModule().GetAbsoluteAddress(clientclassheadlea, 3, 7);
		m_dwClientClassHead = Engine.clientModule().readLong(m_dwClientClassHead);
		m_dwClientClassHead = Engine.clientModule().readLong(m_dwClientClassHead);
		
		netvars.initialize();
		
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
		long foundSplitScreenMgrlea = PatternScanner.getAddressForPattern(Engine.engineModule(), ENGINEPOINTER_SIGNATURE);
		long g_SplitScreenMgr = Engine.engineModule().GetAbsoluteAddress(foundSplitScreenMgrlea, 3, 7);
		m_dwClientState = Engine.engineModule().readLong(g_SplitScreenMgr);
		
		/*boolean ispaused = Engine.engineModule().readBoolean(clientstate + 0x220);
		
		int ingame = Engine.engineModule().readInt(clientstate + 0x1A0);
		
		System.out.println(ispaused);
		System.out.println(ingame);
	//	clientstate = Engine.engineModule().readLong(clientstate);
		//long m_dwIsInGame = Engine.engineModule().readLong(clientstate + 0x1A0);
		//System.out.println(StringFormat.hex(m_dwIsInGame));
		//int ingame = Engine.engineModule().readInt(m_dwIsInGame);
		//System.out.println(ingame);

		//String mapname = Engine.engineModule().readString(m_szMapName, 256);
		//System.out.println(mapname);
		
		
		System.exit(0);
		/**
		 * Client.dll offsets
		 */
		long foundGlowPointerCall = PatternScanner.getAddressForPattern(Engine.clientModule(), GLOWOBJECT_SIGNATURE);
		System.out.println("Glow Pointer Call Reference: " + StringFormat.hex(foundGlowPointerCall));
		long glowFunctionCall = Engine.clientModule().GetCallAddress(foundGlowPointerCall);
		m_dwGlowObject = Engine.clientModule().GetAbsoluteAddress(glowFunctionCall + 9, 3, 7);
		System.out.println("CGlowObjectManager pointer: " + StringFormat.hex(m_dwGlowObject) + " / " + StringFormat.hex(m_dwGlowObject - Engine.clientModule().start()));
		
		long alt1mov = PatternScanner.getAddressForPattern(Engine.clientModule(), ALT1_SIGNATURE);
		input.alt1 = Engine.clientModule().GetAbsoluteAddress(alt1mov - 7, 3, 7);

		long foundattackmov = PatternScanner.getAddressForPattern(Engine.clientModule(), FORCEATTACK_SIGNATURE);
		input.attack = Engine.clientModule().GetAbsoluteAddress(foundattackmov - 7, 3, 7);

		input.init();

		long overridepostprocessingdisablemov = PatternScanner.getAddressForPattern(Engine.clientModule(), OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE) + 26;
		m_dw_bOverridePostProcessingDisable = Engine.clientModule().GetAbsoluteAddress(overridepostprocessingdisablemov, 2, 7);

		long playerresourcemov = PatternScanner.getAddressForPattern(Engine.clientModule(), PLAYERRESOURCES_SIGNATURE);
		m_dwPlayerResourcesPointer = Engine.clientModule().GetAbsoluteAddress(playerresourcemov, 3, 7);

		long entitylistmov = PatternScanner.getAddressForPattern(Engine.clientModule(), ENTITYLIST_SIGNATURE) + 18;
		m_dwEntityList = Engine.clientModule().GetAbsoluteAddress(entitylistmov, 3, 7);
		m_dwEntityList = Engine.clientModule().readLong(m_dwEntityList);
		m_dwEntityList = Engine.clientModule().readLong(m_dwEntityList) + 8;

		long localplayerlea = PatternScanner.getAddressForPattern(Engine.clientModule(), LOCALPLAYER_SIGNATURE);
		m_dwLocalPlayerPointer = Engine.clientModule().GetCallAddress(localplayerlea + 7);
		m_dwLocalPlayer = Engine.clientModule().readLong(Offsets.m_dwLocalPlayerPointer);

		long globalvarsmov = PatternScanner.getAddressForPattern(Engine.clientModule(), GLOBALVARS_SIGNATURE);
		long globalvarsptr = Engine.clientModule().GetAbsoluteAddress(globalvarsmov, 3, 7);
		m_dwGlobalVarsPointer = Engine.clientModule().readLong(globalvarsptr);
		m_dwGlobalVars = Engine.clientModule().readLong(m_dwGlobalVarsPointer);

		long gamerulesmov = PatternScanner.getAddressForPattern(Engine.clientModule(), GAMERULES_SIGNATURE);
		long gamerulesptrptr = Engine.clientModule().GetAbsoluteAddress(gamerulesmov, 3, 7);
		long gamerulesptr = Engine.clientModule().readLong(gamerulesptrptr);
		m_dwGameRules = Engine.clientModule().readLong(gamerulesptr);

		/*long gamedirlea = PatternScanner.getAddressForPattern(Engine.engineModule(), GAMEDIRECTORY_SIGNATURE) + 16;
		long gamedirptr = Engine.engineModule().GetAbsoluteAddress(gamedirlea, 3, 7);
		long m_szGameDirectory = Engine.engineModule().readLong(gamedirptr);
		modDirectory = Engine.engineModule().readString(m_szGameDirectory, 256);*/
		// System.out.println(modDirectory);
		
	}
}
