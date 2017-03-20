package me.lixko.csgoexternals.offsets;

import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.util.StringFormat;

public final class Offsets {

	public static String OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE = " 80 3D 00 00 00 00 00 0F 85 00 00 00 00 85 C9";
	public static String ENTITYLIST_SIGNATURE = " 48 8B 15 00 00 00 00 0F B7 C0 48 C1 E0 04";
	public static String GLOWOBJECT_SIGNATURE = " E8 00 00 00 00 48 8B 3D 00 00 00 00 BE 01 00 00 00 C7";
	public static String PLAYERRESOURCES_SIGNATURE = " 48 8B 05 00 00 00 00 55 48 89 E5 48 85 C0 74 10 48";
	public static String FORCEJUMP_SIGNATURE = " 44 89 e8 c1 e0 1d c1 f8 1f 83 e8 03 45 84 e4 74 08 21 d0";
	public static String ALT1_SIGNATURE = " 44 89 e8 c1 e0 11 c1 f8 1f 83 e8 03 45 84 e4 74 00 21 d0";
	public static String FORCEATTACK_SIGNATURE = " 44 89 e8 83 e0 01 f7 d8 83 e8 03 45 84 e4 74 00 21 d0";
	public static String LOCALPLAYER_SIGNATURE = " 48 89 e5 74 0e 48 8d 05 00 00 00 00";
	public static String CLIENTSTATE_SIGNATURE = "C7 83 00 00 00 00 FF FF FF FF 48 8B 5D 00 48 8D 3D";

	/**
	 * Client.dll offsets
	 */
	public static long m_dwGlowObject;
	public static long m_dw_iAlt1;
	public static long m_dw_bOverridePostProcessingDisable;
	public static long m_dwPlayerResourcesPointer;
	public static long m_dwPlayerResources;
	public static long m_dwForceJump;
	public static long m_dwForceAttack;
	public static long m_dwEntityList;
	public static long m_dwLocalPlayer;
	public static long m_dwLocalPlayerPointer; // < dereference!

	public static long m_dwClientState;

	public static long m_iAlt2;

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

	public static long m_angEyeAngles = 0xb310;
	public static long m_Local = 0x36f0;

	public static long m_dwFullUpdate = 0x1FC;

	public static void load() {
		/**
		 * Client.dll offsets
		 */
		long foundGlowPointerCall = PatternScanner.getAddressForPattern(Engine.clientModule(), GLOWOBJECT_SIGNATURE);
		// System.out.println("Glow Pointer Call Reference: " +
		// PatternScanner.hex(foundGlowPointerCall));
		long glowFunctionCall = Engine.clientModule().GetCallAddress(foundGlowPointerCall);
		// System.out.println("Glow function address: " +
		// PatternScanner.hex(glowFunctionCall));
		long addressOfGlowPointerOffset = (int) Engine.clientModule().readPointer(glowFunctionCall + 0x10);
		if (addressOfGlowPointerOffset < 1)
			throw new IllegalStateException("Unable to read address of glow pointer!");
		// System.out.println("Glow Array offset: " +
		// PatternScanner.hex(addressOfGlowPointerOffset));
		m_dwGlowObject = glowFunctionCall + 0x10 + addressOfGlowPointerOffset + 0x4;
		// System.out.println("Glow Array pointer: " +
		// PatternScanner.hex(m_dwGlowObject));

		long alt1mov = PatternScanner.getAddressForPattern(Engine.clientModule(), ALT1_SIGNATURE);
		m_dw_iAlt1 = Engine.clientModule().GetCallAddress(alt1mov + 20);

		long forcejumpmov = PatternScanner.getAddressForPattern(Engine.clientModule(), FORCEJUMP_SIGNATURE);
		m_dwForceJump = Engine.clientModule().GetCallAddress(forcejumpmov + 26);

		long overridepostprocessingdisablemov = PatternScanner.getAddressForPattern(Engine.clientModule(), OVERRIDEPOSTPROCESSINGDISABLE_SIGNATURE);
		m_dw_bOverridePostProcessingDisable = Engine.clientModule().GetAbsoluteAddress(overridepostprocessingdisablemov, 2, 7);

		long playerresourcemov = PatternScanner.getAddressForPattern(Engine.clientModule(), PLAYERRESOURCES_SIGNATURE);
		m_dwPlayerResourcesPointer = Engine.clientModule().GetAbsoluteAddress(playerresourcemov, 3, 7);

		long foundattackmov = PatternScanner.getAddressForPattern(Engine.clientModule(), FORCEATTACK_SIGNATURE);
		m_dwForceAttack = Engine.clientModule().GetCallAddress(foundattackmov + 19);

		long entitylistmov = PatternScanner.getAddressForPattern(Engine.clientModule(), ENTITYLIST_SIGNATURE);
		m_dwEntityList = Engine.clientModule().GetAbsoluteAddress(entitylistmov, 3, 7);

		long localplayerlea = PatternScanner.getAddressForPattern(Engine.clientModule(), LOCALPLAYER_SIGNATURE);
		m_dwLocalPlayerPointer = Engine.clientModule().GetCallAddress(localplayerlea + 7);

		long clientstateptr = PatternScanner.getAddressForPattern(Engine.engineModule(), CLIENTSTATE_SIGNATURE);
		System.out.println(StringFormat.hex(clientstateptr));
		m_dwClientState = Engine.engineModule().GetAbsoluteAddress(clientstateptr, 1, 5);

		m_dwLocalPlayer = Engine.clientModule().readLong(Offsets.m_dwLocalPlayerPointer);

		/**
		 * Engine.dll offsets
		 */

	}

}
