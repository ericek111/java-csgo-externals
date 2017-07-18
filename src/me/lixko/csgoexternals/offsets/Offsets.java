package me.lixko.csgoexternals.offsets;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.util.StringFormat;

public final class Offsets {

	public static String OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE = "80 3D ?? ?? ?? ?? 00 0F 85 ?? ?? ?? ?? 85 C9";
	public static String ENTITYLIST_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 48 8B 38 48 8B 07 89 55 B0";
	public static String GLOWOBJECT_SIGNATURE = "E8 ?? ?? ?? ?? 48 8B 3D ?? ?? ?? ?? BE 01 00 00 00 C7";
	public static String PLAYERRESOURCES_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 55 48 89 E5 48 85 C0 74 10 48";
	public static String FORCEJUMP_SIGNATURE = "44 89 e8 c1 e0 1d c1 f8 1f 83 e8 03 45 84 e4 74 08 21 d0";
	public static String ALT1_SIGNATURE = "44 89 e8 c1 e0 11 c1 f8 1f 83 e8 03 45 84 e4 74 ?? 21 d0";
	public static String ALT2_SIGNATURE = "44 89 e8 c1 e0 10 c1 f8 1f 83 e8 03 45 84 e4 74 ?? 21 d0";
	public static String FORCEATTACK_SIGNATURE = "44 89 e8 83 e0 01 f7 d8 83 e8 03 45 84 e4 74 ?? 21 d0";
	public static String LOCALPLAYER_SIGNATURE = "48 89 e5 74 0e 48 8d 05 ?? ?? ?? ??";
	public static String GLOBALVARS_SIGNATURE = "4C 8B 25 ?? ?? ?? ?? F3 0F 10 86 C0 3D 00 00 49";
	public static String GAMERULES_SIGNATURE = "48 8B 05 ?? ?? ?? ?? 48 8B ?? 0F 84";

	public static String GAMEDIRECTORY_SIGNATURE = "F6 48 81 EC 28 02 00 00 48 8B 3D ?? ?? ?? ?? E8";
	public static String ENGINEPOINTER_SIGNATURE = "48 8D 3D ?? ?? ?? ?? 31 F6 48 89 E5 5D E9 8D FF"; // g_SplitScreenMgr
	public static String ISTAKINGSCREENSHOT_SIGNATURE = "55 48 85 FF 48 89 E5 C6 05 ?? ?? ?? ?? 01 C6";

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

	public static long m_dwEnginePointer;
	public static long m_szGameDirectory;

	/*
	 * Static offsets
	 */

	public static long m_dwEntityLoopDistance = 0x20;

	public static long m_viewPunchAngle = 0x68;
	public static long m_aimPunchAngle = 0x74;
	public static long m_iTeamNum = 0x128;
	public static long m_iHealth = 0x134;
	public static long m_fFlags = 0x138;
	public static long m_vecViewOffset = 0x13c;
	public static long m_vecVelocity = 0x148;
	public static long m_vecBaseVelocity = 0x154;
	public static long m_angRotation = 0x160;
	public static long m_vecOrigin = 0x16c;
	public static long m_nModelIndex = 0x28c;
	public static long m_bSpotted = 0xECD;
	public static long m_flFlashMaxAlpha = 0xabf4;
	public static long m_flFlashDuration = 0xabf8;

	public static long m_ArmorValue = 0xb30c;
	public static long m_angEyeAngles = 0xb310;
	public static long m_Local = 0x36f0;
	public static long m_iCrosshairIndex = 0xb398;
	public static long m_iEntityIndex = 0x94;

	public static long m_nDeltaTick = 0x174;

	public static long m_flC4Blow = 0x300c;

	public static void load() {
		/**
		 * Client.dll offsets
		 */
		long foundGlowPointerCall = PatternScanner.getAddressForPattern(Engine.clientModule(), GLOWOBJECT_SIGNATURE);
		System.out.println("Glow Pointer Call Reference: " + StringFormat.hex(foundGlowPointerCall));
		long glowFunctionCall = Engine.clientModule().GetCallAddress(foundGlowPointerCall);
		System.out.println("Glow function call: " + StringFormat.hex(glowFunctionCall) + " / " + StringFormat.hex(glowFunctionCall - Engine.clientModule().start()));
		int addressOfGlowPointerOffset = (int) Engine.clientModule().readPointer(glowFunctionCall + 0x10);
		if (addressOfGlowPointerOffset < 1)
			throw new IllegalStateException("Unable to read address of glow pointer!");
		System.out.println("Glow Pointer offset: " + StringFormat.hex(addressOfGlowPointerOffset));
		m_dwGlowObject = glowFunctionCall + 0x10 + addressOfGlowPointerOffset + 0x4;
		System.out.println("CGlowObjectManager pointer: " + StringFormat.hex(m_dwGlowObject) + " / " + StringFormat.hex(m_dwGlowObject - Engine.clientModule().start()));
		System.out.println("m_dwGlowObject offset: " + StringFormat.hex(m_dwGlowObject - Engine.clientModule().start()));

		long alt1mov = PatternScanner.getAddressForPattern(Engine.clientModule(), ALT1_SIGNATURE);
		input.alt1 = Engine.clientModule().GetCallAddress(alt1mov + 20);

		long foundattackmov = PatternScanner.getAddressForPattern(Engine.clientModule(), FORCEATTACK_SIGNATURE);
		input.attack = Engine.clientModule().GetCallAddress(foundattackmov + 19);

		input.init();

		/*
		 * long alt2mov = PatternScanner.getAddressForPattern(Engine.clientModule(), ALT2_SIGNATURE);
		 * input.alt2 = Engine.clientModule().GetCallAddress(alt2mov + 0x14);
		 * 
		 * System.out.println("> forcejump");
		 * long forcejumpmov = PatternScanner.getAddressForPattern(Engine.clientModule(), FORCEJUMP_SIGNATURE);
		 * input.jump = Engine.clientModule().GetCallAddress(forcejumpmov + 26);
		 */

		long overridepostprocessingdisablemov = PatternScanner.getAddressForPattern(Engine.clientModule(), OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE);
		m_dw_bOverridePostProcessingDisable = Engine.clientModule().GetAbsoluteAddress(overridepostprocessingdisablemov, 2, 7);

		long playerresourcemov = PatternScanner.getAddressForPattern(Engine.clientModule(), PLAYERRESOURCES_SIGNATURE);
		m_dwPlayerResourcesPointer = Engine.clientModule().GetAbsoluteAddress(playerresourcemov, 3, 7);

		System.out.println("----------------------------");
		long entitylistmov = PatternScanner.getAddressForPattern(Engine.clientModule(), ENTITYLIST_SIGNATURE);
		m_dwEntityList = Engine.clientModule().GetAbsoluteAddress(entitylistmov, 3, 7);
		m_dwEntityList = Engine.clientModule().readLong(m_dwEntityList);
		m_dwEntityList = Engine.clientModule().readLong(m_dwEntityList) + 8;
		System.out.println("----------------------------");

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

		long gamedirlea = PatternScanner.getAddressForPattern(Engine.engineModule(), GAMEDIRECTORY_SIGNATURE) + 8;
		long gamedirptr = Engine.engineModule().GetAbsoluteAddress(gamedirlea, 3, 7);
		m_szGameDirectory = Engine.engineModule().readLong(gamedirptr);

		long mapname = PatternScanner.getAddressForPattern(Engine.clientModule(), 0, 0, 0, "de_dust2");
		;
		System.out.println("mapname: " + StringFormat.hex(mapname));

	}

}
