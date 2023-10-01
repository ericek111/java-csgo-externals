package me.lixko.csgoexternals.offsets;

public class Netvars { 
	public static final class CChicken { // DT_CChicken
		public static final long m_jumpedThisFrame = 0x3074; // int
		public static final long m_leader = 0x3078; // int
	}

	public static final class CFireCrackerBlast { // DT_FireCrackerBlast
	}

	public static final class CInferno { // DT_Inferno
		public static final long m_fireXDelta = 0xf80; // int[100]
		public static final long m_fireYDelta = 0x1110; // int[100]
		public static final long m_fireZDelta = 0x12a0; // int[100]
		public static final long m_bFireIsBurning = 0x1430; // int[100]
		public static final long m_nFireEffectTickBegin = 0x1950; // int
		public static final long m_fireCount = 0x1944; // int
	}

	public static final class CPhysPropRadarJammer { // DT_PhysPropRadarJammer
	}

	public static final class CPhysPropWeaponUpgrade { // DT_PhysPropWeaponUpgrade
	}

	public static final class CPhysPropAmmoBox { // DT_PhysPropAmmoBox
	}

	public static final class CPhysPropLootCrate { // DT_PhysPropLootCrate
		public static final long m_bRenderInPSPM = 0x3080; // int
		public static final long m_bRenderInTablet = 0x3081; // int
		public static final long m_iHealth = 0x138; // int
		public static final long m_iMaxHealth = 0x3084; // int
	}

	public static final class CItemDogtags { // DT_ItemDogtags
		public static final long m_OwningPlayer = 0x3ebc; // int
		public static final long m_KillingPlayer = 0x3ec0; // int
	}

	public static final class CSensorGrenadeProjectile { // DT_SensorGrenadeProjectile
	}

	public static final class CItem_Healthshot { // DT_Item_Healthshot
	}

	public static final class CWeaponBaseItem { // DT_WeaponBaseItem
		public static final long m_bRedraw = 0x3c90; // int
	}

	public static final class CSmokeGrenadeProjectile { // DT_SmokeGrenadeProjectile
		public static final long m_bDidSmokeEffect = 0x30b0; // int
		public static final long m_nSmokeEffectTickBegin = 0x30ac; // int
	}

	public static final class CDecoyProjectile { // DT_DecoyProjectile
	}

	public static final class CSnowballPile { // DT_SnowballPile
	}

	public static final class CSnowballProjectile { // DT_SnowballProjectile
	}

	public static final class CMolotovProjectile { // DT_MolotovProjectile
		public static final long m_bIsIncGrenade = 0x30a9; // int
	}

	public static final class CWeaponNOVA { // DT_WeaponNOVA
		public static final long m_reloadState = 0x3c80; // int
	}

	public static final class CSensorGrenade { // DT_SensorGrenade
	}

	public static final class CDecoyGrenade { // DT_DecoyGrenade
	}

	public static final class CIncendiaryGrenade { // DT_IncendiaryGrenade
	}

	public static final class CMolotovGrenade { // DT_MolotovGrenade
	}

	public static final class CWeaponShield { // DT_WeaponShield
		public static final long m_flDisplayHealth = 0x3c9c; // float
	}

	public static final class CWeaponZoneRepulsor { // DT_WeaponZoneRepulsor
	}

	public static final class CWeaponTaser { // DT_WeaponTaser
		public static final long m_fFireTime = 0x3c9c; // float
	}

	public static final class CWeaponSawedoff { // DT_WeaponSawedoff
		public static final long m_reloadState = 0x3c80; // int
	}

	public static final class CWeaponXM1014 { // DT_WeaponXM1014
		public static final long m_reloadState = 0x3c80; // int
	}

	public static final class CSmokeGrenade { // DT_SmokeGrenade
	}

	public static final class CWeaponSG552 { // DT_WeaponSG552
	}

	public static final class CWeaponM3 { // DT_WeaponM3
		public static final long m_reloadState = 0x3c80; // int
	}

	public static final class CBumpMine { // DT_WeaponBumpMine
	}

	public static final class CBumpMineProjectile { // DT_BumpMineProjectile
		public static final long m_nParentBoneIndex = 0x3074; // int
		public static final long m_vecParentBonePos = 0x3078; // Vector
		public static final long m_bArmed = 0x3084; // int
	}

	public static final class CBreachCharge { // DT_WeaponBreachCharge
	}

	public static final class CBreachChargeProjectile { // DT_BreachChargeProjectile
		public static final long m_bShouldExplode = 0x3070; // int
		public static final long m_weaponThatThrewMe = 0x3074; // int
		public static final long m_nParentBoneIndex = 0x3078; // int
		public static final long m_vecParentBonePos = 0x307c; // Vector
	}

	public static final class CTablet { // DT_WeaponTablet
		public static final long m_flUpgradeExpirationTime_0 = 0x3c90; // float
		public static final long m_flUpgradeExpirationTime = 0x0; // array elements: 4
		public static final long m_vecLocalHexFlags_0 = 0x3ca0; // int
		public static final long m_vecLocalHexFlags = 0x0; // array elements: 42
		public static final long m_nContractKillGridIndex = 0x3d48; // int
		public static final long m_nContractKillGridHighResIndex = 0x3d4c; // int
		public static final long m_bTabletReceptionIsBlocked = 0x3d50; // int
		public static final long m_flScanProgress = 0x3d54; // float
		public static final long m_flBootTime = 0x3d58; // float
		public static final long m_flShowMapTime = 0x3d5c; // float
		public static final long m_vecNotificationIds_0 = 0x3d6c; // int
		public static final long m_vecNotificationIds = 0x0; // array elements: 8
		public static final long m_vecNotificationTimestamps_0 = 0x3d8c; // float
		public static final long m_vecNotificationTimestamps = 0x0; // array elements: 8
		public static final long m_vecPlayerPositionHistory_0 = 0x3db0; // Vector
		public static final long m_vecPlayerPositionHistory = 0x0; // array elements: 24
		public static final long m_nLastPurchaseIndex = 0x3dac; // int
		public static final long m_vecNearestMetalCratePos = 0x3d60; // Vector
	}

	public static final class CFists { // DT_WeaponFists
		public static final long m_bPlayingUninterruptableAct = 0x3c7c; // int
	}

	public static final class CMelee { // DT_WeaponMelee
		public static final long m_flThrowAt = 0x3c7c; // float
	}

	public static final class CKnifeGG { // DT_WeaponKnifeGG
	}

	public static final class CKnife { // DT_WeaponKnife
	}

	public static final class CHEGrenade { // DT_HEGrenade
	}

	public static final class CSnowball { // DT_Snowball
	}

	public static final class CFlashbang { // DT_Flashbang
	}

	public static final class CWeaponElite { // DT_WeaponElite
	}

	public static final class CDEagle { // DT_WeaponDEagle
	}

	public static final class CWeaponUSP { // DT_WeaponUSP
	}

	public static final class CWeaponM249 { // DT_WeaponM249
	}

	public static final class CWeaponUMP45 { // DT_WeaponUMP45
	}

	public static final class CWeaponTMP { // DT_WeaponTMP
	}

	public static final class CWeaponTec9 { // DT_WeaponTec9
	}

	public static final class CWeaponSSG08 { // DT_WeaponSSG08
	}

	public static final class CWeaponSG556 { // DT_WeaponSG556
	}

	public static final class CWeaponSG550 { // DT_WeaponSG550
	}

	public static final class CWeaponScout { // DT_WeaponScout
	}

	public static final class CWeaponSCAR20 { // DT_WeaponSCAR20
	}

	public static final class CSCAR17 { // DT_WeaponSCAR17
	}

	public static final class CWeaponP90 { // DT_WeaponP90
	}

	public static final class CWeaponP250 { // DT_WeaponP250
	}

	public static final class CWeaponP228 { // DT_WeaponP228
	}

	public static final class CWeaponNegev { // DT_WeaponNegev
	}

	public static final class CWeaponMP9 { // DT_WeaponMP9
	}

	public static final class CWeaponMP7 { // DT_WeaponMP7
	}

	public static final class CWeaponMP5Navy { // DT_WeaponMP5Navy
	}

	public static final class CWeaponMag7 { // DT_WeaponMag7
	}

	public static final class CWeaponMAC10 { // DT_WeaponMAC10
	}

	public static final class CWeaponM4A1 { // DT_WeaponM4A1
	}

	public static final class CWeaponHKP2000 { // DT_WeaponHKP2000
	}

	public static final class CWeaponGlock { // DT_WeaponGlock
	}

	public static final class CWeaponGalilAR { // DT_WeaponGalilAR
	}

	public static final class CWeaponGalil { // DT_WeaponGalil
	}

	public static final class CWeaponG3SG1 { // DT_WeaponG3SG1
	}

	public static final class CWeaponFiveSeven { // DT_WeaponFiveSeven
	}

	public static final class CWeaponFamas { // DT_WeaponFamas
	}

	public static final class CWeaponBizon { // DT_WeaponBizon
	}

	public static final class CWeaponAWP { // DT_WeaponAWP
	}

	public static final class CWeaponAug { // DT_WeaponAug
	}

	public static final class CAK47 { // DT_WeaponAK47
	}

	public static final class CWeaponCSBaseGun { // DT_WeaponCSBaseGun
		public static final long m_zoomLevel = 0x3c7c; // int
		public static final long m_iBurstShotsRemaining = 0x3c80; // int
	}

	public static final class CWeaponCSBase { // DT_WeaponCSBase
		public static final long m_weaponMode = 0x3bb4; // int
		public static final long m_fAccuracyPenalty = 0x3bcc; // float
		public static final long m_fLastShotTime = 0x3c58; // float
		public static final long m_iRecoilIndex = 0x3bdc; // int
		public static final long m_flRecoilIndex = 0x3be0; // float
		public static final long m_hPrevOwner = 0x3c24; // int
		public static final long m_bBurstMode = 0x3be4; // int
		public static final long m_flPostponeFireReadyTime = 0x3be8; // float
		public static final long m_bReloadVisuallyComplete = 0x3bec; // int
		public static final long m_bSilencerOn = 0x3bed; // int
		public static final long m_flDoneSwitchingSilencer = 0x3bf0; // float
		public static final long m_iOriginalTeamNumber = 0x3bf8; // int
		public static final long m_iIronSightMode = 0x3c78; // int
	}

	public static final class CC4 { // DT_WeaponC4
		public static final long m_bStartedArming = 0x3c9c; // int
		public static final long m_bBombPlacedAnimation = 0x3ca4; // int
		public static final long m_fArmedTime = 0x3ca0; // float
		public static final long m_bShowC4LED = 0x3ca5; // int
		public static final long m_bIsPlantingViaUse = 0x3ca6; // int
	}

	public static final class CBaseCSGrenade { // DT_BaseCSGrenade
		public static final long m_bRedraw = 0x3c7c; // int
		public static final long m_bIsHeldByPlayer = 0x3c7d; // int
		public static final long m_bPinPulled = 0x3c7e; // int
		public static final long m_fThrowTime = 0x3c80; // float
		public static final long m_bLoopingSoundPlaying = 0x3c84; // int
		public static final long m_flThrowStrength = 0x3c88; // float
		public static final long m_fDropTime = 0x3c8c; // float
	}

	public static final class CInfoMapRegion { // DT_InfoMapRegion
		public static final long m_flRadius = 0xf68; // float
		public static final long m_szLocToken = 0xf6c; // const char *
	}

	public static final class CEnvGasCanister { // DT_EnvGasCanister
		public static final long m_flFlightSpeed = 0x30bc; // float
		public static final long m_flLaunchTime = 0x30c0; // float
		public static final long m_vecParabolaDirection = 0x30d4; // Vector
		public static final long m_flFlightTime = 0x30b8; // float
		public static final long m_flWorldEnterTime = 0x30e0; // float
		public static final long m_flInitialZSpeed = 0x30c4; // float
		public static final long m_flZAcceleration = 0x30c8; // float
		public static final long m_flHorizSpeed = 0x30cc; // float
		public static final long m_bLaunchedFromWithinWorld = 0x30d0; // int
		public static final long m_vecImpactPosition = 0x307c; // Vector
		public static final long m_vecStartPosition = 0x3088; // Vector
		public static final long m_vecEnterWorldPosition = 0x3094; // Vector
		public static final long m_vecDirection = 0x30a0; // Vector
		public static final long m_vecStartAngles = 0x30ac; // Vector
		public static final long m_vecSkyboxOrigin = 0x30e4; // Vector
		public static final long m_flSkyboxScale = 0x30f0; // float
		public static final long m_bInSkybox = 0x30f4; // int
		public static final long m_bDoImpactEffects = 0x30f5; // int
		public static final long m_bLanded = 0x3020; // int
		public static final long m_hSkyboxCopy = 0x3070; // int
		public static final long m_nMyZoneIndex = 0x30f8; // int
		public static final long m_vecOrigin = 0x170; // VectorXY
		public static final long m_vecOrigin_2 = 0x178; // float
	}

	public static final class CDronegun { // DT_Dronegun
		public static final long m_vecAttentionTarget = 0x3020; // Vector
		public static final long m_vecTargetOffset = 0x302c; // Vector
		public static final long m_iHealth = 0x138; // int
		public static final long m_bHasTarget = 0x3038; // int
	}

	public static final class CItemCash { // DT_ItemCash
	}

	public static final class CParadropChopper { // DT_ParadropChopper
		public static final long m_vecOrigin = 0x170; // VectorXY
		public static final long m_vecOrigin_2 = 0x178; // float
		public static final long m_hCallingPlayer = 0x3030; // int
	}

	public static final class CSurvivalSpawnChopper { // DT_SurvivalSpawnChopper
		public static final long m_vecOrigin = 0x170; // VectorXY
		public static final long m_vecOrigin_2 = 0x178; // float
	}

	public static final class CBRC4Target { // DT_BRC4Target
		public static final long m_bBrokenOpen = 0x3028; // int
		public static final long m_flRadius = 0x302c; // float
	}

	public static final class CDrone { // DT_Drone
		public static final long m_hMoveToThisEntity = 0x3078; // int
		public static final long m_hDeliveryCargo = 0x307c; // int
		public static final long m_bPilotTakeoverAllowed = 0x3080; // int
		public static final long m_hPotentialCargo = 0x3084; // int
		public static final long m_hCurrentPilot = 0x3088; // int
		public static final long m_vecTagPositions_0 = 0x308c; // Vector
		public static final long m_vecTagPositions = 0x0; // array elements: 24
		public static final long m_vecTagIncrements_0 = 0x31ac; // int
		public static final long m_vecTagIncrements = 0x0; // array elements: 24
	}

	public static final class CFootstepControl { // DT_FootstepControl
		public static final long m_source = 0xf92; // const char *
		public static final long m_destination = 0xfa2; // const char *
	}

	public static final class CCSGameRulesProxy { // DT_CSGameRulesProxy
		public static final class cs_gamerules_data { // DT_CSGameRules
			public static final long BASE_OFFSET = 0x0;
			public static final long m_bFreezePeriod = 0x38; // int
			public static final long m_bMatchWaitingForResume = 0x59; // int
			public static final long m_bWarmupPeriod = 0x39; // int
			public static final long m_fWarmupPeriodEnd = 0x3c; // float
			public static final long m_fWarmupPeriodStart = 0x40; // float
			public static final long m_bTerroristTimeOutActive = 0x44; // int
			public static final long m_bCTTimeOutActive = 0x45; // int
			public static final long m_flTerroristTimeOutRemaining = 0x48; // float
			public static final long m_flCTTimeOutRemaining = 0x4c; // float
			public static final long m_nTerroristTimeOuts = 0x50; // int
			public static final long m_nCTTimeOuts = 0x54; // int
			public static final long m_bTechnicalTimeOut = 0x58; // int
			public static final long m_iRoundTime = 0x5c; // int
			public static final long m_gamePhase = 0x78; // int
			public static final long m_totalRoundsPlayed = 0x7c; // int
			public static final long m_nOvertimePlaying = 0x80; // int
			public static final long m_timeUntilNextPhaseStarts = 0x74; // float
			public static final long m_flCMMItemDropRevealStartTime = 0x890; // float
			public static final long m_flCMMItemDropRevealEndTime = 0x894; // float
			public static final long m_fRoundStartTime = 0x64; // float
			public static final long m_bGameRestart = 0x6c; // int
			public static final long m_flRestartRoundTime = 0x68; // float
			public static final long m_flGameStartTime = 0x70; // float
			public static final long m_iHostagesRemaining = 0x84; // int
			public static final long m_bAnyHostageReached = 0x88; // int
			public static final long m_bMapHasBombTarget = 0x89; // int
			public static final long m_bMapHasRescueZone = 0x8a; // int
			public static final long m_bMapHasBuyZone = 0x8b; // int
			public static final long m_bIsQueuedMatchmaking = 0x8c; // int
			public static final long m_nQueuedMatchmakingMode = 0x90; // int
			public static final long m_bIsValveDS = 0x94; // int
			public static final long m_bIsQuestEligible = 0x899; // int
			public static final long m_bLogoMap = 0x95; // int
			public static final long m_bPlayAllStepSoundsOnServer = 0x96; // int
			public static final long m_iNumGunGameProgressiveWeaponsCT = 0x98; // int
			public static final long m_iNumGunGameProgressiveWeaponsT = 0x9c; // int
			public static final long m_iSpectatorSlotCount = 0xa0; // int
			public static final long m_bBombDropped = 0x9bc; // int
			public static final long m_bBombPlanted = 0x9bd; // int
			public static final long m_iRoundWinStatus = 0x9c0; // int
			public static final long m_eRoundWinReason = 0x9c4; // int
			public static final long m_flDMBonusStartTime = 0x46c; // float
			public static final long m_flDMBonusTimeLength = 0x470; // float
			public static final long m_unDMBonusWeaponLoadoutSlot = 0x474; // int
			public static final long m_bDMBonusActive = 0x476; // int
			public static final long m_bTCantBuy = 0x9c8; // int
			public static final long m_bCTCantBuy = 0x9c9; // int
			public static final long m_flGuardianBuyUntilTime = 0x9cc; // float
			public static final long m_iMatchStats_RoundResults = 0x9d0; // int[30]
			public static final long m_iMatchStats_PlayersAlive_T = 0xac0; // int[30]
			public static final long m_iMatchStats_PlayersAlive_CT = 0xa48; // int[30]
			public static final long m_GGProgressiveWeaponOrderCT = 0xa4; // int[60]
			public static final long m_GGProgressiveWeaponOrderT = 0x194; // int[60]
			public static final long m_GGProgressiveWeaponKillUpgradeOrderCT = 0x284; // int[60]
			public static final long m_GGProgressiveWeaponKillUpgradeOrderT = 0x374; // int[60]
			public static final long m_MatchDevice = 0x464; // int
			public static final long m_bHasMatchStarted = 0x468; // int
			public static final long m_TeamRespawnWaveTimes = 0xb38; // float[32]
			public static final long m_flNextRespawnWave = 0xbb8; // float[32]
			public static final long m_nNextMapInMapgroup = 0x478; // int
			public static final long m_nEndMatchMapGroupVoteTypes = 0xc40; // int[10]
			public static final long m_nEndMatchMapGroupVoteOptions = 0xc68; // int[10]
			public static final long m_nEndMatchMapVoteWinner = 0xc90; // int
			public static final long m_bIsDroppingItems = 0x898; // int
			public static final long m_iActiveAssassinationTargetMissionID = 0xc38; // int
			public static final long m_fMatchStartTime = 0x60; // float
			public static final long m_szTournamentEventName = 0x47c; // const char *
			public static final long m_szTournamentEventStage = 0x580; // const char *
			public static final long m_szTournamentPredictionsTxt = 0x788; // const char *
			public static final long m_nTournamentPredictionsPct = 0x88c; // int
			public static final long m_szMatchStatTxt = 0x684; // const char *
			public static final long m_nGuardianModeWaveNumber = 0x89c; // int
			public static final long m_nGuardianModeSpecialKillsRemaining = 0x8a0; // int
			public static final long m_nGuardianModeSpecialWeaponNeeded = 0x8a4; // int
			public static final long m_nHalloweenMaskListSeed = 0x9b8; // int
			public static final long m_numGlobalGiftsGiven = 0x8b0; // int
			public static final long m_numGlobalGifters = 0x8b4; // int
			public static final long m_numGlobalGiftsPeriodSeconds = 0x8b8; // int
			public static final long m_arrFeaturedGiftersAccounts = 0x8bc; // int[4]
			public static final long m_arrFeaturedGiftersGifts = 0x8cc; // int[4]
			public static final long m_arrProhibitedItemIndices = 0x8dc; // int[100]
			public static final long m_numBestOfMaps = 0x9b4; // int
			public static final long m_arrTournamentActiveCasterAccounts = 0x9a4; // int[4]
			public static final long m_iNumConsecutiveCTLoses = 0xc94; // int
			public static final long m_iNumConsecutiveTerroristLoses = 0xc98; // int
			public static final class m_SurvivalRules { // DT_SurvivalGameRules
				public static final long BASE_OFFSET = 0xd50;
				public static final long m_vecPlayAreaMins = 0x0; // Vector
				public static final long m_vecPlayAreaMaxs = 0xc; // Vector
				public static final long m_iPlayerSpawnHexIndices = 0x18; // int[64]
				public static final long m_SpawnTileState = 0x118; // int[224]
				public static final long m_flSpawnSelectionTimeStart = 0x1f8; // float
				public static final long m_flSpawnSelectionTimeEnd = 0x1fc; // float
				public static final long m_flSpawnSelectionTimeLoadout = 0x200; // float
				public static final long m_spawnStage = 0x204; // int
				public static final long m_flTabletHexOriginX = 0x208; // float
				public static final long m_flTabletHexOriginY = 0x20c; // float
				public static final long m_flTabletHexSize = 0x210; // float
				public static final long m_roundData_playerXuids = 0x218; // long[65]
				public static final long m_roundData_playerPositions = 0x420; // int[65]
				public static final long m_roundData_playerTeams = 0x524; // int[65]
				public static final long m_SurvivalGameRuleDecisionTypes = 0x628; // int[16]
				public static final long m_SurvivalGameRuleDecisionValues = 0x668; // int[16]
				public static final long m_flSurvivalStartTime = 0x6a8; // float
			}
			public static final class m_RetakeRules { // DT_RetakeGameRules
				public static final long BASE_OFFSET = 0x1420;
				public static final long m_nMatchSeed = 0x170; // int
				public static final long m_bBlockersPresent = 0x174; // int
				public static final long m_bRoundInProgress = 0x175; // int
				public static final long m_iFirstSecondHalfRound = 0x178; // int
				public static final long m_iBombSite = 0x17c; // int
			}
		}
	}

	public static final class CWeaponCubemap { // DT_WeaponCubemap
	}

	public static final class CWeaponCycler { // DT_WeaponCycler
	}

	public static final class CTEPlantBomb { // DT_TEPlantBomb
		public static final long m_vecOrigin = 0x24; // Vector
		public static final long m_iPlayer = 0x20; // int
		public static final long m_option = 0x30; // int
	}

	public static final class CTEFireBullets { // DT_TEFireBullets
		public static final long m_vecOrigin = 0x28; // Vector
		public static final long m_vecAngles_0 = 0x34; // float
		public static final long m_vecAngles_1 = 0x38; // float
		public static final long m_iWeaponID = 0x44; // int
		public static final long m_weapon = 0x40; // int
		public static final long m_iMode = 0x48; // int
		public static final long m_iSeed = 0x4c; // int
		public static final long m_iPlayer = 0x20; // int
		public static final long m_fInaccuracy = 0x50; // float
		public static final long m_fSpread = 0x58; // float
		public static final long m_nItemDefIndex = 0x24; // int
		public static final long m_iSoundType = 0x5c; // int
		public static final long m_flRecoilIndex = 0x54; // float
	}

	public static final class CTERadioIcon { // DT_TERadioIcon
		public static final long m_iAttachToClient = 0x20; // int
	}

	public static final class CPlantedC4 { // DT_PlantedC4
		public static final long m_bBombTicking = 0x3020; // int
		public static final long m_nBombSite = 0x3024; // int
		public static final long m_flC4Blow = 0x3030; // float
		public static final long m_flTimerLength = 0x3034; // float
		public static final long m_flDefuseLength = 0x3048; // float
		public static final long m_flDefuseCountDown = 0x304c; // float
		public static final long m_bBombDefused = 0x3050; // int
		public static final long m_hBombDefuser = 0x3054; // int
	}

	public static final class CCSTeam { // DT_CSTeam
	}

	public static final class CCSPlayerResource { // DT_CSPlayerResource
		public static final long m_iPlayerC4 = 0x1cf4; // int
		public static final long m_iPlayerVIP = 0x1cf8; // int
		public static final long m_bHostageAlive = 0x1d14; // int[12]
		public static final long m_isHostageFollowingSomeone = 0x1d20; // int[12]
		public static final long m_iHostageEntityIDs = 0x1d2c; // int[12]
		public static final long m_bombsiteCenterA = 0x1cfc; // Vector
		public static final long m_bombsiteCenterB = 0x1d08; // Vector
		public static final long m_hostageRescueX = 0x1d5c; // int[4]
		public static final long m_hostageRescueY = 0x1d6c; // int[4]
		public static final long m_hostageRescueZ = 0x1d7c; // int[4]
		public static final long m_iMVPs = 0x1d8c; // int[65]
		public static final long m_iArmor = 0x1f14; // int[65]
		public static final long m_bHasHelmet = 0x1ed1; // int[65]
		public static final long m_bHasDefuser = 0x1e90; // int[65]
		public static final long m_iScore = 0x2018; // int[65]
		public static final long m_iCompetitiveRanking = 0x211c; // int[65]
		public static final long m_iCompetitiveWins = 0x2220; // int[65]
		public static final long m_iCompetitiveRankType = 0x2324; // int[65]
		public static final long m_iCompTeammateColor = 0x2368; // int[65]
		public static final long m_iLifetimeStart = 0x246c; // int[65]
		public static final long m_iLifetimeEnd = 0x2570; // int[65]
		public static final long m_bControllingBot = 0x2674; // int[65]
		public static final long m_iControlledPlayer = 0x26b8; // int[65]
		public static final long m_iControlledByPlayer = 0x27bc; // int[65]
		public static final long m_iBotDifficulty = 0x4940; // int[65]
		public static final long m_szClan = 0x4a44; // const char *[65]
		public static final long m_nCharacterDefIndex = 0x4e54; // int[65]
		public static final long m_iTotalCashSpent = 0x4f58; // int[65]
		public static final long m_iGunGameLevel = 0x505c; // int[65]
		public static final long m_iCashSpentThisRound = 0x5160; // int[65]
		public static final long m_nEndMatchNextMapVotes = 0x7120; // int[65]
		public static final long m_bEndMatchNextMapAllVoted = 0x7224; // int
		public static final long m_nActiveCoinRank = 0x5264; // int[65]
		public static final long m_nMusicID = 0x5368; // int[65]
		public static final long m_nPersonaDataPublicLevel = 0x546c; // int[65]
		public static final long m_nPersonaDataPublicCommendsLeader = 0x5570; // int[65]
		public static final long m_nPersonaDataPublicCommendsTeacher = 0x5674; // int[65]
		public static final long m_nPersonaDataPublicCommendsFriendly = 0x5778; // int[65]
		public static final long m_bHasCommunicationAbuseMute = 0x587c; // int[65]
		public static final long m_szCrosshairCodes = 0x58bd; // const char *[65]
		public static final long m_iMatchStats_Kills_Total = 0x61a0; // int[65]
		public static final long m_iMatchStats_5k_Total = 0x66b4; // int[65]
		public static final long m_iMatchStats_4k_Total = 0x65b0; // int[65]
		public static final long m_iMatchStats_3k_Total = 0x64ac; // int[65]
		public static final long m_iMatchStats_Damage_Total = 0x67b8; // int[65]
		public static final long m_iMatchStats_EquipmentValue_Total = 0x68bc; // int[65]
		public static final long m_iMatchStats_KillReward_Total = 0x69c0; // int[65]
		public static final long m_iMatchStats_LiveTime_Total = 0x6ac4; // int[65]
		public static final long m_iMatchStats_Deaths_Total = 0x63a8; // int[65]
		public static final long m_iMatchStats_Assists_Total = 0x62a4; // int[65]
		public static final long m_iMatchStats_HeadShotKills_Total = 0x6bc8; // int[65]
		public static final long m_iMatchStats_Objective_Total = 0x6ccc; // int[65]
		public static final long m_iMatchStats_CashEarned_Total = 0x6dd0; // int[65]
		public static final long m_iMatchStats_UtilityDamage_Total = 0x6ed4; // int[65]
		public static final long m_iMatchStats_EnemiesFlashed_Total = 0x6fd8; // int[65]
	}

	public static final class CCSPlayer { // DT_CSPlayer
		public static final class cslocaldata { // DT_CSLocalPlayerExclusive
			public static final long BASE_OFFSET = 0x0;
			public static final long m_vecOrigin = 0x170; // VectorXY
			public static final long m_vecOrigin_2 = 0x178; // float
			public static final long m_flStamina = 0x10cf8; // float
			public static final long m_iDirection = 0x10cfc; // int
			public static final long m_iShotsFired = 0x10d00; // int
			public static final long m_nNumFastDucks = 0x10d04; // int
			public static final long m_bDuckOverride = 0x10d08; // int
			public static final long m_flVelocityModifier = 0x10d0c; // float
			public static final long m_bPlayerDominated = 0x12268; // int[65]
			public static final long m_bPlayerDominatingMe = 0x122a9; // int[65]
			public static final long m_iWeaponPurchasesThisRound = 0x11bb8; // int[256]
			public static final long m_unActiveQuestId = 0x120ac; // int
			public static final long m_nQuestProgressReason = 0x120b0; // int
			public static final long m_iRetakesOffering = 0x126a8; // int
			public static final long m_iRetakesOfferingCard = 0x126ac; // int
			public static final long m_bRetakesHasDefuseKit = 0x126b0; // int
			public static final long m_bRetakesMVPLastRound = 0x126b1; // int
			public static final long m_iRetakesMVPBoostItem = 0x126b4; // int
			public static final long m_RetakesMVPBoostExtraUtility = 0x126b8; // int
			public static final long m_unPlayerTvControlFlags = 0x120b4; // int
		}
		public static final class csnonlocaldata { // DT_CSNonLocalPlayerExclusive
			public static final long BASE_OFFSET = 0x0;
			public static final long m_vecOrigin = 0x170; // VectorXY
			public static final long m_vecOrigin_2 = 0x178; // float
		}
		public static final class csteamdata { // DT_CSTeamExclusive
			public static final long BASE_OFFSET = 0x0;
			public static final long m_iWeaponPurchasesThisMatch = 0x113ec; // int[499]
			public static final long m_EquippedLoadoutItemDefIndices = 0x11fd0; // int[57]
		}
		public static final long m_angEyeAngles_0 = 0x12104; // float
		public static final long m_angEyeAngles_1 = 0x12108; // float
		public static final long m_iAddonBits = 0x10ce4; // int
		public static final long m_iPrimaryAddon = 0x10ce8; // int
		public static final long m_iSecondaryAddon = 0x10cec; // int
		public static final long m_iThrowGrenadeCounter = 0xa2ac; // int
		public static final long m_bWaitForNoAttack = 0xa2b0; // int
		public static final long m_bIsRespawningForDMBonus = 0xa2b1; // int
		public static final long m_iPlayerState = 0xa26c; // int
		public static final long m_iAccount = 0x120ec; // int
		public static final long m_iStartAccount = 0x10d14; // int
		public static final long m_totalHitsOnServer = 0x10d18; // int
		public static final long m_bInBombZone = 0xa2a8; // int
		public static final long m_bInBuyZone = 0xa2a9; // int
		public static final long m_bInNoDefuseArea = 0xa2aa; // int
		public static final long m_bKilledByTaser = 0xa2c1; // int
		public static final long m_iMoveState = 0xa2c4; // int
		public static final long m_iClass = 0x120fc; // int
		public static final long m_ArmorValue = 0x12100; // int
		public static final long m_angEyeAngles = 0x12104; // Vector
		public static final long m_bHasDefuser = 0x12110; // int
		public static final long m_bNightVisionOn = 0x10d09; // int
		public static final long m_bHasNightVision = 0x10d0a; // int
		public static final long m_bInHostageRescueZone = 0x12111; // int
		public static final long m_bIsDefusing = 0xa270; // int
		public static final long m_bIsGrabbingHostage = 0xa271; // int
		public static final long m_iBlockingUseActionInProgress = 0xa274; // int
		public static final long m_bIsScoped = 0xa268; // int
		public static final long m_bIsWalking = 0xa269; // int
		public static final long m_nIsAutoMounting = 0xa3e0; // int
		public static final long m_bResumeZoom = 0xa26a; // int
		public static final long m_fImmuneToGunGameDamageTime = 0xa27c; // float
		public static final long m_bGunGameImmunity = 0xa284; // int
		public static final long m_bHasMovedSinceSpawn = 0xa285; // int
		public static final long m_bMadeFinalGunGameProgressiveKill = 0xa286; // int
		public static final long m_iGunGameProgressiveWeaponIndex = 0xa288; // int
		public static final long m_iNumGunGameTRKillPoints = 0xa28c; // int
		public static final long m_iNumGunGameKillsWithCurrentWeapon = 0xa290; // int
		public static final long m_iNumRoundKills = 0xa294; // int
		public static final long m_fMolotovUseTime = 0xa2a0; // float
		public static final long m_fMolotovDamageTime = 0xa2a4; // float
		public static final long m_szArmsModel = 0xa2cb; // const char *
		public static final long m_hCarriedHostage = 0x10d68; // int
		public static final long m_hCarriedHostageProp = 0x10d6c; // int
		public static final long m_bIsRescuing = 0xa278; // int
		public static final long m_flGroundAccelLinearFracLastTime = 0x10d10; // float
		public static final long m_bCanMoveDuringFreezePeriod = 0xa2c8; // int
		public static final long m_isCurrentGunGameLeader = 0xa2c9; // int
		public static final long m_isCurrentGunGameTeamLeader = 0xa2ca; // int
		public static final long m_flGuardianTooFarDistFrac = 0xa2b4; // float
		public static final long m_flDetectedByEnemySensorTime = 0xa2b8; // float
		public static final long m_bIsPlayerGhost = 0xa3dd; // int
		public static final long m_iMatchStats_Kills = 0x10dd4; // int[30]
		public static final long m_iMatchStats_Damage = 0x10e4c; // int[30]
		public static final long m_iMatchStats_EquipmentValue = 0x10ec4; // int[30]
		public static final long m_iMatchStats_MoneySaved = 0x10f3c; // int[30]
		public static final long m_iMatchStats_KillReward = 0x10fb4; // int[30]
		public static final long m_iMatchStats_LiveTime = 0x1102c; // int[30]
		public static final long m_iMatchStats_Deaths = 0x110a4; // int[30]
		public static final long m_iMatchStats_Assists = 0x1111c; // int[30]
		public static final long m_iMatchStats_HeadShotKills = 0x11194; // int[30]
		public static final long m_iMatchStats_Objective = 0x1120c; // int[30]
		public static final long m_iMatchStats_CashEarned = 0x11284; // int[30]
		public static final long m_iMatchStats_UtilityDamage = 0x112fc; // int[30]
		public static final long m_iMatchStats_EnemiesFlashed = 0x11374; // int[30]
		public static final long m_rank = 0x120c0; // int[6]
		public static final long m_passiveItems = 0x120d8; // int[4]
		public static final long m_bHasParachute = 0x120d8; // int
		public static final long m_unMusicID = 0x120dc; // int
		public static final long m_bHasHelmet = 0x120f4; // int
		public static final long m_bHasHeavyArmor = 0x120f5; // int
		public static final long m_nHeavyAssaultSuitCooldownRemaining = 0x120f8; // int
		public static final long m_flFlashDuration = 0x10d90; // float
		public static final long m_flFlashMaxAlpha = 0x10d8c; // float
		public static final long m_iProgressBarDuration = 0x10cf0; // int
		public static final long m_flProgressBarStartTime = 0x10cf4; // float
		public static final long m_hRagdoll = 0x10d5c; // int
		public static final long m_hPlayerPing = 0x10d60; // int
		public static final long m_cycleLatch = 0x12260; // int
		public static final long m_unCurrentEquipmentValue = 0x120b8; // int
		public static final long m_unRoundStartEquipmentValue = 0x120ba; // int
		public static final long m_unFreezetimeEndEquipmentValue = 0x120bc; // int
		public static final long m_bIsControllingBot = 0x123cd; // int
		public static final long m_bHasControlledBotThisRound = 0x123dc; // int
		public static final long m_bCanControlObservedBot = 0x123ce; // int
		public static final long m_iControlledBotEntIndex = 0x123d0; // int
		public static final long m_vecAutomoveTargetEnd = 0xa3f0; // Vector
		public static final long m_flAutoMoveStartTime = 0xa400; // float
		public static final long m_flAutoMoveTargetTime = 0xa404; // float
		public static final long m_bIsAssassinationTarget = 0x123cc; // int
		public static final long m_bHud_MiniScoreHidden = 0x12132; // int
		public static final long m_bHud_RadarHidden = 0x12133; // int
		public static final long m_nLastKillerIndex = 0x12134; // int
		public static final long m_nLastConcurrentKilled = 0x12138; // int
		public static final long m_nDeathCamMusic = 0x1213c; // int
		public static final long m_bIsHoldingLookAtWeapon = 0x122f5; // int
		public static final long m_bIsLookingAtWeapon = 0x122f4; // int
		public static final long m_iNumRoundKillsHeadshots = 0xa298; // int
		public static final long m_unTotalRoundDamageDealt = 0xa29c; // int
		public static final long m_flLowerBodyYawTarget = 0xa3d8; // float
		public static final long m_bStrafing = 0xa3dc; // int
		public static final long m_flThirdpersonRecoil = 0x1236c; // float
		public static final long m_bHideTargetID = 0x12370; // int
		public static final long m_bIsSpawnRappelling = 0x10d1d; // int
		public static final long m_vecSpawnRappellingRopeOrigin = 0x10d20; // Vector
		public static final long m_nSurvivalTeam = 0x10d30; // int
		public static final long m_hSurvivalAssassinationTarget = 0x10d34; // int
		public static final long m_flHealthShotBoostExpirationTime = 0x10d38; // float
		public static final long m_flLastExoJumpTime = 0xa408; // float
		public static final long m_vecPlayerPatchEconIndices = 0x12344; // int[5]
	}

	public static final class CPlayerPing { // DT_PlayerPing
		public static final long m_hPlayer = 0xf80; // int
		public static final long m_hPingedEntity = 0xf84; // int
		public static final long m_iType = 0xf88; // int
		public static final long m_bUrgent = 0xf90; // int
		public static final long m_szPlaceName = 0xf91; // const char *
	}

	public static final class CCSRagdoll { // DT_CSRagdoll
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long m_vecRagdollOrigin = 0x30b8; // Vector
		public static final long m_hPlayer = 0x309c; // int
		public static final long m_nModelIndex = 0x290; // int
		public static final long m_nForceBone = 0x2c54; // int
		public static final long m_vecForce = 0x2c48; // Vector
		public static final long m_vecRagdollVelocity = 0x30ac; // Vector
		public static final long m_iDeathPose = 0x30c4; // int
		public static final long m_iDeathFrame = 0x30c8; // int
		public static final long m_iTeamNum = 0x12c; // int
		public static final long m_bClientSideAnimation = 0x2ee0; // int
		public static final long m_flDeathYaw = 0x30cc; // float
		public static final long m_flAbsYaw = 0x30d0; // float
		public static final long m_bDiedAirborne = 0x30d4; // int
	}

	public static final class CTEPlayerAnimEvent { // DT_TEPlayerAnimEvent
		public static final long m_hPlayer = 0x20; // int
		public static final long m_iEvent = 0x24; // int
		public static final long m_nData = 0x28; // int
	}

	public static final class CHostage { // DT_CHostage
		public static final long m_isRescued = 0x3680; // int
		public static final long m_jumpedThisFrame = 0x3681; // int
		public static final long m_iHealth = 0x138; // int
		public static final long m_iMaxHealth = 0x3664; // int
		public static final long m_lifeState = 0x297; // int
		public static final long m_fFlags = 0x13c; // int
		public static final long m_nHostageState = 0x3684; // int
		public static final long m_flRescueStartTime = 0x3688; // float
		public static final long m_flGrabSuccessTime = 0x368c; // float
		public static final long m_flDropStartTime = 0x3690; // float
		public static final long m_vel = 0x3674; // Vector
		public static final long m_leader = 0x3670; // int
	}

	public static final class CHostageCarriableProp { // DT_HostageCarriableProp
	}

	public static final class CBaseCSGrenadeProjectile { // DT_BaseCSGrenadeProjectile
		public static final long m_vInitialVelocity = 0x3070; // Vector
		public static final long m_nBounces = 0x307c; // int
		public static final long m_nExplodeEffectIndex = 0x3080; // int
		public static final long m_nExplodeEffectTickBegin = 0x3084; // int
		public static final long m_vecExplodeEffectOrigin = 0x3088; // Vector
	}

	public static final class CPredictedViewModel { // DT_PredictedViewModel
	}

	public static final class CBaseTeamObjectiveResource { // DT_BaseTeamObjectiveResource
		public static final long m_iTimerToShowInHUD = 0xf68; // int
		public static final long m_iStopWatchTimer = 0xf6c; // int
		public static final long m_iNumControlPoints = 0xf70; // int
		public static final long m_bPlayingMiniRounds = 0xf78; // int
		public static final long m_bControlPointsReset = 0xf79; // int
		public static final long m_iUpdateCapHudParity = 0xf7c; // int
		public static final long m_vCPPositions_0 = 0xf84; // Vector
		public static final long m_vCPPositions = 0x0; // array elements: 8
		public static final long m_bCPIsVisible = 0xfe4; // int[8]
		public static final long m_flLazyCapPerc = 0xfec; // float[8]
		public static final long m_iTeamIcons = 0x102c; // int[64]
		public static final long m_iTeamOverlays = 0x112c; // int[64]
		public static final long m_iTeamReqCappers = 0x122c; // int[64]
		public static final long m_flTeamCapTime = 0x132c; // float[64]
		public static final long m_iPreviousPoints = 0x142c; // int[192]
		public static final long m_bTeamCanCap = 0x172c; // int[64]
		public static final long m_iTeamBaseIcons = 0x176c; // int[32]
		public static final long m_iBaseControlPoints = 0x17ec; // int[32]
		public static final long m_bInMiniRound = 0x186c; // int[8]
		public static final long m_iWarnOnCap = 0x1874; // int[8]
		public static final long m_iszWarnSound_0 = 0x1894; // const char *
		public static final long m_iszWarnSound = 0x0; // array elements: 8
		public static final long m_flPathDistance = 0x208c; // float[8]
		public static final long m_iNumTeamMembers = 0x20ac; // int[64]
		public static final long m_iCappingTeam = 0x21ac; // int[8]
		public static final long m_iTeamInZone = 0x21cc; // int[8]
		public static final long m_bBlocked = 0x21ec; // int[8]
		public static final long m_iOwner = 0x21f4; // int[8]
		public static final long m_pszCapLayoutInHUD = 0x227c; // const char *
	}

	public static final class CEconWearable { // DT_WearableItem
	}

	public static final class CBaseAttributableItem { // DT_BaseAttributableItem
		public static final class m_AttributeManager { // DT_AttributeContainer
			public static final long BASE_OFFSET = 0x34d8;
			public static final long m_hOuter = 0x2c; // int
			public static final long m_ProviderType = 0x34; // int
			public static final long m_iReapplyProvisionParity = 0x28; // int
			public static final class m_Item { // DT_ScriptCreatedItem
				public static final long BASE_OFFSET = 0x60;
				public static final long m_iItemDefinitionIndex = 0x282; // int
				public static final long m_iEntityLevel = 0x288; // int
				public static final long m_iItemIDHigh = 0x298; // int
				public static final long m_iItemIDLow = 0x29c; // int
				public static final long m_iAccountID = 0x2a0; // int
				public static final long m_iEntityQuality = 0x284; // int
				public static final long m_bInitialized = 0x2b0; // int
				public static final long m_szCustomName = 0x358; // const char *
				public static final class m_NetworkedDynamicAttributesForDemos { // DT_AttributeList
					public static final long BASE_OFFSET = 0x328;
					public static final class m_Attributes { // _ST_m_Attributes_32
						public static final long BASE_OFFSET = 0x0;
						public static final class lengthproxy { // _LPT_m_Attributes_32
							public static final long BASE_OFFSET = 0x0;
							public static final long lengthprop32 = 0x0; // int
						}
					}
				}
			}
		}
		public static final long m_OriginalOwnerXuidLow = 0x3a28; // int
		public static final long m_OriginalOwnerXuidHigh = 0x3a2c; // int
		public static final long m_nFallbackPaintKit = 0x3a30; // int
		public static final long m_nFallbackSeed = 0x3a34; // int
		public static final long m_flFallbackWear = 0x3a38; // float
		public static final long m_nFallbackStatTrak = 0x3a3c; // int
	}

	public static final class CEconEntity { // DT_EconEntity
		public static final class m_AttributeManager { // DT_AttributeContainer
			public static final long BASE_OFFSET = 0x34d8;
			public static final long m_hOuter = 0x2c; // int
			public static final long m_ProviderType = 0x34; // int
			public static final long m_iReapplyProvisionParity = 0x28; // int
			public static final class m_Item { // DT_ScriptCreatedItem
				public static final long BASE_OFFSET = 0x60;
				public static final long m_iItemDefinitionIndex = 0x282; // int
				public static final long m_iEntityLevel = 0x288; // int
				public static final long m_iItemIDHigh = 0x298; // int
				public static final long m_iItemIDLow = 0x29c; // int
				public static final long m_iAccountID = 0x2a0; // int
				public static final long m_iEntityQuality = 0x284; // int
				public static final long m_bInitialized = 0x2b0; // int
				public static final long m_szCustomName = 0x358; // const char *
				public static final class m_NetworkedDynamicAttributesForDemos { // DT_AttributeList
					public static final long BASE_OFFSET = 0x328;
					public static final class m_Attributes { // _ST_m_Attributes_32
						public static final long BASE_OFFSET = 0x0;
						public static final class lengthproxy { // _LPT_m_Attributes_32
							public static final long BASE_OFFSET = 0x0;
							public static final long lengthprop32 = 0x0; // int
						}
					}
				}
			}
		}
		public static final long m_OriginalOwnerXuidLow = 0x3a28; // int
		public static final long m_OriginalOwnerXuidHigh = 0x3a2c; // int
		public static final long m_nFallbackPaintKit = 0x3a30; // int
		public static final long m_nFallbackSeed = 0x3a34; // int
		public static final long m_flFallbackWear = 0x3a38; // float
		public static final long m_nFallbackStatTrak = 0x3a3c; // int
	}

	public static final class NextBotCombatCharacter { // DT_NextBot
	}

	public static final class CTestTraceline { // DT_TestTraceline
		public static final long m_clrRender = 0xa8; // int
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long m_angRotation_0 = 0x164; // float
		public static final long m_angRotation_1 = 0x168; // float
		public static final long m_angRotation_2 = 0x16c; // float
		public static final long moveparent = 0x180; // int
	}

	public static final class CTEWorldDecal { // DT_TEWorldDecal
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_nIndex = 0x2c; // int
	}

	public static final class CTESpriteSpray { // DT_TESpriteSpray
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecDirection = 0x2c; // Vector
		public static final long m_nModelIndex = 0x38; // int
		public static final long m_fNoise = 0x40; // float
		public static final long m_nCount = 0x44; // int
		public static final long m_nSpeed = 0x3c; // int
	}

	public static final class CTESprite { // DT_TESprite
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_nModelIndex = 0x2c; // int
		public static final long m_fScale = 0x30; // float
		public static final long m_nBrightness = 0x34; // int
	}

	public static final class CTESparks { // DT_TESparks
		public static final long m_nMagnitude = 0x2c; // int
		public static final long m_nTrailLength = 0x30; // int
		public static final long m_vecDir = 0x34; // Vector
	}

	public static final class CTESmoke { // DT_TESmoke
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_nModelIndex = 0x2c; // int
		public static final long m_fScale = 0x30; // float
		public static final long m_nFrameRate = 0x34; // int
	}

	public static final class CTEShowLine { // DT_TEShowLine
		public static final long m_vecEnd = 0x2c; // Vector
	}

	public static final class CTEProjectedDecal { // DT_TEProjectedDecal
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_angRotation = 0x2c; // Vector
		public static final long m_flDistance = 0x38; // float
		public static final long m_nIndex = 0x3c; // int
	}

	public static final class CFEPlayerDecal { // DT_FEPlayerDecal
		public static final long m_nUniqueID = 0xf68; // int
		public static final long m_unAccountID = 0xf6c; // int
		public static final long m_unTraceID = 0xf70; // int
		public static final long m_rtGcTime = 0xf74; // int
		public static final long m_vecEndPos = 0xf78; // Vector
		public static final long m_vecStart = 0xf84; // Vector
		public static final long m_vecRight = 0xf90; // Vector
		public static final long m_vecNormal = 0xf9c; // Vector
		public static final long m_nEntity = 0xfac; // int
		public static final long m_nPlayer = 0xfa8; // int
		public static final long m_nHitbox = 0xfb0; // int
		public static final long m_nTintID = 0xfb4; // int
		public static final long m_flCreationTime = 0xfb8; // float
		public static final long m_nVersion = 0xfbc; // int
		public static final long m_ubSignature = 0xfbd; // int[128]
	}

	public static final class CTEPlayerDecal { // DT_TEPlayerDecal
		public static final long m_vecOrigin = 0x24; // Vector
		public static final long m_vecStart = 0x30; // Vector
		public static final long m_vecRight = 0x3c; // Vector
		public static final long m_nEntity = 0x48; // int
		public static final long m_nPlayer = 0x20; // int
		public static final long m_nHitbox = 0x4c; // int
	}

	public static final class CTEPhysicsProp { // DT_TEPhysicsProp
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_angRotation_0 = 0x2c; // float
		public static final long m_angRotation_1 = 0x30; // float
		public static final long m_angRotation_2 = 0x34; // float
		public static final long m_vecVelocity = 0x38; // Vector
		public static final long m_nModelIndex = 0x44; // int
		public static final long m_nFlags = 0x4c; // int
		public static final long m_nSkin = 0x48; // int
		public static final long m_nEffects = 0x50; // int
		public static final long m_clrRender = 0x54; // int
	}

	public static final class CTEParticleSystem { // DT_TEParticleSystem
		public static final long m_vecOrigin_0 = 0x20; // float
		public static final long m_vecOrigin_1 = 0x24; // float
		public static final long m_vecOrigin_2 = 0x28; // float
	}

	public static final class CTEMuzzleFlash { // DT_TEMuzzleFlash
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecAngles = 0x2c; // Vector
		public static final long m_flScale = 0x38; // float
		public static final long m_nType = 0x3c; // int
	}

	public static final class CTELargeFunnel { // DT_TELargeFunnel
		public static final long m_nModelIndex = 0x2c; // int
		public static final long m_nReversed = 0x30; // int
	}

	public static final class CTEKillPlayerAttachments { // DT_TEKillPlayerAttachments
		public static final long m_nPlayer = 0x20; // int
	}

	public static final class CTEImpact { // DT_TEImpact
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecNormal = 0x2c; // Vector
		public static final long m_iType = 0x38; // int
		public static final long m_ucFlags = 0x3c; // int
	}

	public static final class CTEGlowSprite { // DT_TEGlowSprite
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_nModelIndex = 0x2c; // int
		public static final long m_fScale = 0x30; // float
		public static final long m_fLife = 0x34; // float
		public static final long m_nBrightness = 0x38; // int
	}

	public static final class CTEShatterSurface { // DT_TEShatterSurface
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecAngles = 0x2c; // Vector
		public static final long m_vecForce = 0x38; // Vector
		public static final long m_vecForcePos = 0x44; // Vector
		public static final long m_flWidth = 0x50; // float
		public static final long m_flHeight = 0x54; // float
		public static final long m_flShardSize = 0x58; // float
		public static final long m_nSurfaceType = 0x68; // int
		public static final long m_uchFrontColor_0 = 0x6c; // int
		public static final long m_uchFrontColor_1 = 0x6d; // int
		public static final long m_uchFrontColor_2 = 0x6e; // int
		public static final long m_uchBackColor_0 = 0x6f; // int
		public static final long m_uchBackColor_1 = 0x70; // int
		public static final long m_uchBackColor_2 = 0x71; // int
	}

	public static final class CTEFootprintDecal { // DT_TEFootprintDecal
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecDirection = 0x2c; // Vector
		public static final long m_nEntity = 0x44; // int
		public static final long m_nIndex = 0x48; // int
		public static final long m_chMaterialType = 0x4c; // int
	}

	public static final class CTEFizz { // DT_TEFizz
		public static final long m_nEntity = 0x20; // int
		public static final long m_nModelIndex = 0x24; // int
		public static final long m_nDensity = 0x28; // int
		public static final long m_nCurrent = 0x2c; // int
	}

	public static final class CTEExplosion { // DT_TEExplosion
		public static final long m_nModelIndex = 0x2c; // int
		public static final long m_fScale = 0x30; // float
		public static final long m_nFrameRate = 0x34; // int
		public static final long m_nFlags = 0x38; // int
		public static final long m_vecNormal = 0x3c; // Vector
		public static final long m_chMaterialType = 0x48; // int
		public static final long m_nRadius = 0x4c; // int
		public static final long m_nMagnitude = 0x50; // int
	}

	public static final class CTEEnergySplash { // DT_TEEnergySplash
		public static final long m_vecPos = 0x20; // Vector
		public static final long m_vecDir = 0x2c; // Vector
		public static final long m_bExplosive = 0x38; // int
	}

	public static final class CTEEffectDispatch { // DT_TEEffectDispatch
		public static final class m_EffectData { // DT_EffectData
			public static final long BASE_OFFSET = 0x20;
			public static final long m_vOrigin_x = 0x0; // float
			public static final long m_vOrigin_y = 0x4; // float
			public static final long m_vOrigin_z = 0x8; // float
			public static final long m_vStart_x = 0xc; // float
			public static final long m_vStart_y = 0x10; // float
			public static final long m_vStart_z = 0x14; // float
			public static final long m_vAngles = 0x24; // Vector
			public static final long m_vNormal = 0x18; // Vector
			public static final long m_fFlags = 0x30; // int
			public static final long m_flMagnitude = 0x44; // float
			public static final long m_flScale = 0x40; // float
			public static final long m_nAttachmentIndex = 0x4c; // int
			public static final long m_nSurfaceProp = 0x50; // int
			public static final long m_iEffectName = 0x68; // int
			public static final long m_nMaterial = 0x54; // int
			public static final long m_nDamageType = 0x58; // int
			public static final long m_nHitBox = 0x5c; // int
			public static final long entindex = 0x0; // int
			public static final long m_nOtherEntIndex = 0x60; // int
			public static final long m_nColor = 0x64; // int
			public static final long m_flRadius = 0x48; // float
			public static final long m_bPositionsAreRelativeToEntity = 0x65; // int
		}
	}

	public static final class CTEDynamicLight { // DT_TEDynamicLight
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long r = 0x30; // int
		public static final long g = 0x34; // int
		public static final long b = 0x38; // int
		public static final long exponent = 0x3c; // int
		public static final long m_fRadius = 0x2c; // float
		public static final long m_fTime = 0x40; // float
		public static final long m_fDecay = 0x44; // float
	}

	public static final class CTEDecal { // DT_TEDecal
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecStart = 0x2c; // Vector
		public static final long m_nEntity = 0x38; // int
		public static final long m_nHitbox = 0x3c; // int
		public static final long m_nIndex = 0x40; // int
	}

	public static final class CTEClientProjectile { // DT_TEClientProjectile
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecVelocity = 0x2c; // Vector
		public static final long m_nModelIndex = 0x38; // int
		public static final long m_nLifeTime = 0x3c; // int
		public static final long m_hOwner = 0x40; // int
	}

	public static final class CTEBubbleTrail { // DT_TEBubbleTrail
		public static final long m_vecMins = 0x20; // Vector
		public static final long m_vecMaxs = 0x2c; // Vector
		public static final long m_nModelIndex = 0x3c; // int
		public static final long m_flWaterZ = 0x38; // float
		public static final long m_nCount = 0x40; // int
		public static final long m_fSpeed = 0x44; // float
	}

	public static final class CTEBubbles { // DT_TEBubbles
		public static final long m_vecMins = 0x20; // Vector
		public static final long m_vecMaxs = 0x2c; // Vector
		public static final long m_nModelIndex = 0x3c; // int
		public static final long m_fHeight = 0x38; // float
		public static final long m_nCount = 0x40; // int
		public static final long m_fSpeed = 0x44; // float
	}

	public static final class CTEBSPDecal { // DT_TEBSPDecal
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_nEntity = 0x2c; // int
		public static final long m_nIndex = 0x30; // int
	}

	public static final class CTEBreakModel { // DT_TEBreakModel
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_angRotation_0 = 0x2c; // float
		public static final long m_angRotation_1 = 0x30; // float
		public static final long m_angRotation_2 = 0x34; // float
		public static final long m_vecSize = 0x38; // Vector
		public static final long m_vecVelocity = 0x44; // Vector
		public static final long m_nModelIndex = 0x54; // int
		public static final long m_nRandomization = 0x50; // int
		public static final long m_nCount = 0x58; // int
		public static final long m_fTime = 0x5c; // float
		public static final long m_nFlags = 0x60; // int
	}

	public static final class CTEBloodStream { // DT_TEBloodStream
		public static final long m_vecDirection = 0x2c; // Vector
		public static final long r = 0x38; // int
		public static final long g = 0x3c; // int
		public static final long b = 0x40; // int
		public static final long a = 0x44; // int
		public static final long m_nAmount = 0x48; // int
	}

	public static final class CTEBloodSprite { // DT_TEBloodSprite
		public static final long m_vecOrigin = 0x20; // Vector
		public static final long m_vecDirection = 0x2c; // Vector
		public static final long r = 0x38; // int
		public static final long g = 0x3c; // int
		public static final long b = 0x40; // int
		public static final long a = 0x44; // int
		public static final long m_nSprayModel = 0x4c; // int
		public static final long m_nDropModel = 0x48; // int
		public static final long m_nSize = 0x50; // int
	}

	public static final class CTEBeamSpline { // DT_TEBeamSpline
		public static final long m_nPoints = 0xe0; // int
		public static final long m_vecPoints_0 = 0x20; // Vector
		public static final long m_vecPoints = 0x0; // array elements: 16
	}

	public static final class CTEBeamRingPoint { // DT_TEBeamRingPoint
		public static final long m_vecCenter = 0x5c; // Vector
		public static final long m_flStartRadius = 0x68; // float
		public static final long m_flEndRadius = 0x6c; // float
	}

	public static final class CTEBeamRing { // DT_TEBeamRing
		public static final long m_nStartEntity = 0x5c; // int
		public static final long m_nEndEntity = 0x60; // int
	}

	public static final class CTEBeamPoints { // DT_TEBeamPoints
		public static final long m_vecStartPoint = 0x5c; // Vector
		public static final long m_vecEndPoint = 0x68; // Vector
	}

	public static final class CTEBeamLaser { // DT_TEBeamLaser
		public static final long m_nStartEntity = 0x5c; // int
		public static final long m_nEndEntity = 0x60; // int
	}

	public static final class CTEBeamFollow { // DT_TEBeamFollow
		public static final long m_iEntIndex = 0x5c; // int
	}

	public static final class CTEBeamEnts { // DT_TEBeamEnts
		public static final long m_nStartEntity = 0x5c; // int
		public static final long m_nEndEntity = 0x60; // int
	}

	public static final class CTEBeamEntPoint { // DT_TEBeamEntPoint
		public static final long m_nStartEntity = 0x5c; // int
		public static final long m_nEndEntity = 0x60; // int
		public static final long m_vecStartPoint = 0x64; // Vector
		public static final long m_vecEndPoint = 0x70; // Vector
	}

	public static final class CTEBaseBeam { // DT_BaseBeam
		public static final long m_nModelIndex = 0x20; // int
		public static final long m_nHaloIndex = 0x24; // int
		public static final long m_nStartFrame = 0x28; // int
		public static final long m_nFrameRate = 0x2c; // int
		public static final long m_fLife = 0x30; // float
		public static final long m_fWidth = 0x34; // float
		public static final long m_fEndWidth = 0x38; // float
		public static final long m_nFadeLength = 0x3c; // int
		public static final long m_fAmplitude = 0x40; // float
		public static final long m_nSpeed = 0x54; // int
		public static final long r = 0x44; // int
		public static final long g = 0x48; // int
		public static final long b = 0x4c; // int
		public static final long a = 0x50; // int
		public static final long m_nFlags = 0x58; // int
	}

	public static final class CTEArmorRicochet { // DT_TEArmorRicochet
	}

	public static final class CTEMetalSparks { // DT_TEMetalSparks
		public static final long m_vecPos = 0x20; // Vector
		public static final long m_vecDir = 0x2c; // Vector
	}

	public static final class CSteamJet { // DT_SteamJet
		public static final long m_SpreadSpeed = 0x10a0; // float
		public static final long m_Speed = 0x10a4; // float
		public static final long m_StartSize = 0x10a8; // float
		public static final long m_EndSize = 0x10ac; // float
		public static final long m_Rate = 0x10b0; // float
		public static final long m_JetLength = 0x10b4; // float
		public static final long m_bEmit = 0x10b8; // int
		public static final long m_bFaceLeft = 0x10c0; // int
		public static final long m_nType = 0x10bc; // int
		public static final long m_spawnflags = 0x10c4; // int
		public static final long m_flRollSpeed = 0x10c8; // float
	}

	public static final class CSmokeStack { // DT_SmokeStack
		public static final long m_SpreadSpeed = 0x10f8; // float
		public static final long m_Speed = 0x10fc; // float
		public static final long m_StartSize = 0x1100; // float
		public static final long m_EndSize = 0x1104; // float
		public static final long m_Rate = 0x1108; // float
		public static final long m_JetLength = 0x110c; // float
		public static final long m_bEmit = 0x1110; // int
		public static final long m_flBaseSpread = 0x1114; // float
		public static final long m_flTwist = 0x1168; // float
		public static final long m_flRollSpeed = 0x11b0; // float
		public static final long m_iMaterialModel = 0x116c; // int
		public static final long m_AmbientLight_m_vPos = 0x1118; // Vector
		public static final long m_AmbientLight_m_vColor = 0x1124; // Vector
		public static final long m_AmbientLight_m_flIntensity = 0x1130; // float
		public static final long m_DirLight_m_vPos = 0x1134; // Vector
		public static final long m_DirLight_m_vColor = 0x1140; // Vector
		public static final long m_DirLight_m_flIntensity = 0x114c; // float
		public static final long m_vWind = 0x115c; // Vector
	}

	public static final class DustTrail { // DT_DustTrail
		public static final long m_SpawnRate = 0x10a0; // float
		public static final long m_Color = 0x10a4; // Vector
		public static final long m_ParticleLifetime = 0x10b4; // float
		public static final long m_StopEmitTime = 0x10bc; // float
		public static final long m_MinSpeed = 0x10c0; // float
		public static final long m_MaxSpeed = 0x10c4; // float
		public static final long m_MinDirectedSpeed = 0x10c8; // float
		public static final long m_MaxDirectedSpeed = 0x10cc; // float
		public static final long m_StartSize = 0x10d0; // float
		public static final long m_EndSize = 0x10d4; // float
		public static final long m_SpawnRadius = 0x10d8; // float
		public static final long m_bEmit = 0x10e8; // int
		public static final long m_Opacity = 0x10b0; // float
	}

	public static final class CFireTrail { // DT_FireTrail
		public static final long m_nAttachment = 0x10a0; // int
		public static final long m_flLifetime = 0x10a4; // float
	}

	public static final class SporeTrail { // DT_SporeTrail
		public static final long m_flSpawnRate = 0x10a4; // float
		public static final long m_vecEndColor = 0x1098; // Vector
		public static final long m_flParticleLifetime = 0x10a8; // float
		public static final long m_flStartSize = 0x10ac; // float
		public static final long m_flEndSize = 0x10b0; // float
		public static final long m_flSpawnRadius = 0x10b4; // float
		public static final long m_bEmit = 0x10c4; // int
	}

	public static final class SporeExplosion { // DT_SporeExplosion
		public static final long m_flSpawnRate = 0x10a0; // float
		public static final long m_flParticleLifetime = 0x10a4; // float
		public static final long m_flStartSize = 0x10a8; // float
		public static final long m_flEndSize = 0x10ac; // float
		public static final long m_flSpawnRadius = 0x10b0; // float
		public static final long m_bEmit = 0x10b8; // int
		public static final long m_bDontRemove = 0x10b9; // int
	}

	public static final class RocketTrail { // DT_RocketTrail
		public static final long m_SpawnRate = 0x10a0; // float
		public static final long m_StartColor = 0x10a4; // Vector
		public static final long m_EndColor = 0x10b0; // Vector
		public static final long m_ParticleLifetime = 0x10c0; // float
		public static final long m_StopEmitTime = 0x10c4; // float
		public static final long m_MinSpeed = 0x10c8; // float
		public static final long m_MaxSpeed = 0x10cc; // float
		public static final long m_StartSize = 0x10d0; // float
		public static final long m_EndSize = 0x10d4; // float
		public static final long m_SpawnRadius = 0x10d8; // float
		public static final long m_bEmit = 0x10e8; // int
		public static final long m_nAttachment = 0x10ec; // int
		public static final long m_Opacity = 0x10bc; // float
		public static final long m_bDamaged = 0x10e9; // int
		public static final long m_flFlareScale = 0x10fc; // float
	}

	public static final class SmokeTrail { // DT_SmokeTrail
		public static final long m_SpawnRate = 0x10a0; // float
		public static final long m_StartColor = 0x10a4; // Vector
		public static final long m_EndColor = 0x10b0; // Vector
		public static final long m_ParticleLifetime = 0x10c0; // float
		public static final long m_StopEmitTime = 0x10c4; // float
		public static final long m_MinSpeed = 0x10c8; // float
		public static final long m_MaxSpeed = 0x10cc; // float
		public static final long m_MinDirectedSpeed = 0x10d0; // float
		public static final long m_MaxDirectedSpeed = 0x10d4; // float
		public static final long m_StartSize = 0x10d8; // float
		public static final long m_EndSize = 0x10dc; // float
		public static final long m_SpawnRadius = 0x10e0; // float
		public static final long m_bEmit = 0x10f0; // int
		public static final long m_nAttachment = 0x10f4; // int
		public static final long m_Opacity = 0x10bc; // float
	}

	public static final class CPropVehicleDriveable { // DT_PropVehicleDriveable
		public static final long m_hPlayer = 0x3028; // int
		public static final long m_nSpeed = 0x302c; // int
		public static final long m_nRPM = 0x3030; // int
		public static final long m_flThrottle = 0x3034; // float
		public static final long m_nBoostTimeLeft = 0x3038; // int
		public static final long m_nHasBoost = 0x303c; // int
		public static final long m_nScannerDisabledWeapons = 0x3040; // int
		public static final long m_nScannerDisabledVehicle = 0x3044; // int
		public static final long m_bEnterAnimOn = 0x3064; // int
		public static final long m_bExitAnimOn = 0x3065; // int
		public static final long m_bUnableToFire = 0x30cd; // int
		public static final long m_vecEyeExitEndpoint = 0x30c0; // Vector
		public static final long m_bHasGun = 0x30cc; // int
		public static final long m_vecGunCrosshair = 0x306c; // Vector
	}

	public static final class ParticleSmokeGrenade { // DT_ParticleSmokeGrenade
		public static final long m_flSpawnTime = 0x10b0; // float
		public static final long m_FadeStartTime = 0x10b4; // float
		public static final long m_FadeEndTime = 0x10b8; // float
		public static final long m_MinColor = 0x10c0; // Vector
		public static final long m_MaxColor = 0x10cc; // Vector
		public static final long m_CurrentStage = 0x10a0; // int
	}

	public static final class CParticleFire { // DT_ParticleFire
		public static final long m_vOrigin = 0x10b0; // Vector
		public static final long m_vDirection = 0x10bc; // Vector
	}

	public static final class MovieExplosion { // DT_MovieExplosion
	}

	public static final class CTEGaussExplosion { // DT_TEGaussExplosion
		public static final long m_nType = 0x2c; // int
		public static final long m_vecDirection = 0x30; // Vector
	}

	public static final class CEnvQuadraticBeam { // DT_QuadraticBeam
		public static final long m_targetPosition = 0xf68; // Vector
		public static final long m_controlPosition = 0xf74; // Vector
		public static final long m_scrollRate = 0xf80; // float
		public static final long m_flWidth = 0xf84; // float
	}

	public static final class CEmbers { // DT_Embers
		public static final long m_nDensity = 0xf68; // int
		public static final long m_nLifetime = 0xf6c; // int
		public static final long m_nSpeed = 0xf70; // int
		public static final long m_bEmit = 0xf74; // int
	}

	public static final class CEnvWind { // DT_EnvWind
		public static final class m_EnvWindShared { // DT_EnvWindShared
			public static final long BASE_OFFSET = 0xf68;
			public static final long m_iMinWind = 0x10; // int
			public static final long m_iMaxWind = 0x14; // int
			public static final long m_iMinGust = 0x1c; // int
			public static final long m_iMaxGust = 0x20; // int
			public static final long m_flMinGustDelay = 0x24; // float
			public static final long m_flMaxGustDelay = 0x28; // float
			public static final long m_iGustDirChange = 0x30; // int
			public static final long m_iWindSeed = 0xc; // int
			public static final long m_iInitialWindDir = 0x70; // int
			public static final long m_flInitialWindSpeed = 0x74; // float
			public static final long m_flStartTime = 0x8; // float
			public static final long m_flGustDuration = 0x2c; // float
		}
	}

	public static final class CPrecipitation { // DT_Precipitation
		public static final long m_nPrecipType = 0xf94; // int
	}

	public static final class CPrecipitationBlocker { // DT_PrecipitationBlocker
	}

	public static final class CBaseTempEntity { // DT_BaseTempEntity
	}

	public static final class CMapVetoPickController { // DT_MapVetoPickController
		public static final long m_nDraftType = 0xf80; // int
		public static final long m_nTeamWinningCoinToss = 0xf84; // int
		public static final long m_nTeamWithFirstChoice = 0xf88; // int[64]
		public static final long m_nVoteMapIdsList = 0x1088; // int[7]
		public static final long m_nAccountIDs = 0x10a4; // int[64]
		public static final long m_nMapId0 = 0x11a4; // int[64]
		public static final long m_nMapId1 = 0x12a4; // int[64]
		public static final long m_nMapId2 = 0x13a4; // int[64]
		public static final long m_nMapId3 = 0x14a4; // int[64]
		public static final long m_nMapId4 = 0x15a4; // int[64]
		public static final long m_nMapId5 = 0x16a4; // int[64]
		public static final long m_nStartingSide0 = 0x17a4; // int[64]
		public static final long m_nCurrentPhase = 0x18a4; // int
		public static final long m_nPhaseStartTick = 0x18a8; // int
		public static final long m_nPhaseDurationTicks = 0x18ac; // int
	}

	public static final class CVoteController { // DT_VoteController
		public static final long m_iActiveIssueIndex = 0xf78; // int
		public static final long m_iOnlyTeamToVote = 0xf7c; // int
		public static final long m_nVoteOptionCount = 0xf80; // int[5]
		public static final long m_nPotentialVotes = 0xf98; // int
		public static final long m_bIsYesNoVote = 0xf9e; // int
	}

	public static final class CHandleTest { // DT_HandleTest
		public static final long m_Handle = 0xf68; // int
		public static final long m_bSendHandle = 0xf6c; // int
	}

	public static final class CTeamplayRoundBasedRulesProxy { // DT_TeamplayRoundBasedRulesProxy
		public static final class teamplayroundbased_gamerules_data { // DT_TeamplayRoundBasedRules
			public static final long BASE_OFFSET = 0x0;
			public static final long m_iRoundState = 0x40; // int
			public static final long m_bInWaitingForPlayers = 0x50; // int
			public static final long m_iWinningTeam = 0x48; // int
			public static final long m_bInOvertime = 0x44; // int
			public static final long m_bInSetup = 0x45; // int
			public static final long m_bSwitchedTeamsThisRound = 0x46; // int
			public static final long m_bAwaitingReadyRestart = 0x51; // int
			public static final long m_flRestartRoundTime = 0x54; // float
			public static final long m_flMapResetTime = 0x58; // float
			public static final long m_flNextRespawnWave = 0x5c; // float[32]
			public static final long m_TeamRespawnWaveTimes = 0x100; // float[32]
			public static final long m_bTeamReady = 0xdc; // int[32]
			public static final long m_bStopWatch = 0xfc; // int
		}
	}

	public static final class CSpriteTrail { // DT_SpriteTrail
		public static final long m_flLifeTime = 0x1604; // float
		public static final long m_flStartWidth = 0x1608; // float
		public static final long m_flEndWidth = 0x160c; // float
		public static final long m_flStartWidthVariance = 0x1610; // float
		public static final long m_flTextureRes = 0x1614; // float
		public static final long m_flMinFadeLength = 0x1618; // float
		public static final long m_vecSkyboxOrigin = 0x161c; // Vector
		public static final long m_flSkyboxScale = 0x1628; // float
	}

	public static final class CSpriteOriented { // DT_SpriteOriented
	}

	public static final class CSprite { // DT_Sprite
		public static final long m_hAttachedToEntity = 0xf7c; // int
		public static final long m_nAttachment = 0xf80; // int
		public static final long m_flScaleTime = 0xf9c; // float
		public static final long m_flSpriteScale = 0xf98; // float
		public static final long m_flSpriteFramerate = 0xf84; // float
		public static final long m_flGlowProxySize = 0xfa4; // float
		public static final long m_flHDRColorScale = 0xfa8; // float
		public static final long m_flFrame = 0xf88; // float
		public static final long m_flBrightnessTime = 0xf94; // float
		public static final long m_nBrightness = 0xf90; // int
		public static final long m_bWorldSpaceScale = 0xfa0; // int
	}

	public static final class CRagdollPropAttached { // DT_Ragdoll_Attached
		public static final long m_boneIndexAttached = 0x3390; // int
		public static final long m_ragdollAttachedObjectIndex = 0x338c; // int
		public static final long m_attachmentPointBoneSpace = 0x3368; // Vector
		public static final long m_attachmentPointRagdollSpace = 0x3380; // Vector
	}

	public static final class CRagdollProp { // DT_Ragdoll
		public static final long m_ragAngles_0 = 0x3140; // Vector
		public static final long m_ragAngles = 0x0; // array elements: 24
		public static final long m_ragPos_0 = 0x3020; // Vector
		public static final long m_ragPos = 0x0; // array elements: 24
		public static final long m_hUnragdoll = 0x3354; // int
		public static final long m_flBlendWeight = 0x3358; // float
		public static final long m_nOverlaySequence = 0x3360; // int
	}

	public static final class CPoseController { // DT_PoseController
		public static final long m_hProps = 0xf68; // int[4]
		public static final long m_chPoseIndex = 0xf78; // int[4]
		public static final long m_bPoseValueParity = 0xf7c; // int
		public static final long m_fPoseValue = 0xf80; // float
		public static final long m_fInterpolationTime = 0xf84; // float
		public static final long m_bInterpolationWrap = 0xf88; // int
		public static final long m_fCycleFrequency = 0xf8c; // float
		public static final long m_nFModType = 0xf90; // int
		public static final long m_fFModTimeOffset = 0xf94; // float
		public static final long m_fFModRate = 0xf98; // float
		public static final long m_fFModAmplitude = 0xf9c; // float
	}

	public static final class CGameRulesProxy { // DT_GameRulesProxy
	}

	public static final class CInfoLadderDismount { // DT_InfoLadderDismount
	}

	public static final class CFuncLadder { // DT_FuncLadder
		public static final long m_vecPlayerMountPositionTop = 0xf98; // Vector
		public static final long m_vecPlayerMountPositionBottom = 0xfa4; // Vector
		public static final long m_vecLadderDir = 0xf68; // Vector
		public static final long m_bFakeLadder = 0xfb1; // int
	}

	public static final class CTEFoundryHelpers { // DT_TEFoundryHelpers
		public static final long m_iEntity = 0x20; // int
	}

	public static final class CEnvDetailController { // DT_DetailController
		public static final long m_flFadeStartDist = 0xf68; // float
		public static final long m_flFadeEndDist = 0xf6c; // float
	}

	public static final class CWorld { // DT_World
		public static final long m_flWaveHeight = 0xf68; // float
		public static final long m_WorldMins = 0xf6c; // Vector
		public static final long m_WorldMaxs = 0xf78; // Vector
		public static final long m_bStartDark = 0xf84; // int
		public static final long m_flMaxOccludeeArea = 0xf88; // float
		public static final long m_flMinOccluderArea = 0xf8c; // float
		public static final long m_flMaxPropScreenSpaceWidth = 0xf94; // float
		public static final long m_flMinPropScreenSpaceWidth = 0xf90; // float
		public static final long m_iszDetailSpriteMaterial = 0xfa0; // const char *
		public static final long m_bColdWorld = 0xf98; // int
		public static final long m_iTimeOfDay = 0xf9c; // int
	}

	public static final class CWaterLODControl { // DT_WaterLODControl
		public static final long m_flCheapWaterStartDistance = 0xf68; // float
		public static final long m_flCheapWaterEndDistance = 0xf6c; // float
	}

	public static final class CWaterBullet { // DT_WaterBullet
	}

	public static final class CWorldVguiText { // DT_WorldVguiText
		public static final long m_bEnabled = 0xf65; // int
		public static final long m_szDisplayText = 0xf66; // const char *
		public static final long m_szDisplayTextOption = 0x1166; // const char *
		public static final long m_szFont = 0x1266; // const char *
		public static final long m_iTextPanelWidth = 0x12ac; // int
		public static final long m_clrText = 0x12a6; // int
	}

	public static final class CVGuiScreen { // DT_VGuiScreen
		public static final long m_flWidth = 0xf70; // float
		public static final long m_flHeight = 0xf74; // float
		public static final long m_fScreenFlags = 0xf9c; // int
		public static final long m_nPanelName = 0xf78; // int
		public static final long m_nAttachmentIndex = 0xf94; // int
		public static final long m_nOverlayMaterial = 0xf98; // int
		public static final long m_hPlayerOwner = 0x1008; // int
	}

	public static final class CPropJeep { // DT_PropJeep
		public static final long m_bHeadlightIsOn = 0x3198; // int
	}

	public static final class CPropVehicleChoreoGeneric { // DT_PropVehicleChoreoGeneric
		public static final long m_hPlayer = 0x3088; // int
		public static final long m_bEnterAnimOn = 0x3090; // int
		public static final long m_bExitAnimOn = 0x3091; // int
		public static final long m_bForceEyesToAttachment = 0x30a0; // int
		public static final long m_vecEyeExitEndpoint = 0x3094; // Vector
		public static final long m_vehicleView_bClampEyeAngles = 0x3130; // int
		public static final long m_vehicleView_flPitchCurveZero = 0x3134; // float
		public static final long m_vehicleView_flPitchCurveLinear = 0x3138; // float
		public static final long m_vehicleView_flRollCurveZero = 0x313c; // float
		public static final long m_vehicleView_flRollCurveLinear = 0x3140; // float
		public static final long m_vehicleView_flFOV = 0x3144; // float
		public static final long m_vehicleView_flYawMin = 0x3148; // float
		public static final long m_vehicleView_flYawMax = 0x314c; // float
		public static final long m_vehicleView_flPitchMin = 0x3150; // float
		public static final long m_vehicleView_flPitchMax = 0x3154; // float
	}

	public static final class CTriggerSoundOperator { // DT_TriggerSoundOperator
		public static final long m_nSoundOperator = 0xf94; // int
	}

	public static final class CBaseVPhysicsTrigger { // DT_BaseVPhysicsTrigger
	}

	public static final class CTriggerPlayerMovement { // DT_TriggerPlayerMovement
	}

	public static final class CBaseTrigger { // DT_BaseTrigger
		public static final long m_bClientSidePredicted = 0xf91; // int
		public static final long m_spawnflags = 0x314; // int
	}

	public static final class CTest_ProxyToggle_Networkable { // DT_ProxyToggle
		public static final class blah { // DT_ProxyToggle_ProxiedData
			public static final long BASE_OFFSET = 0x0;
			public static final long m_WithProxy = 0xf68; // int
		}
	}

	public static final class CTesla { // DT_Tesla
		public static final long m_SoundName = 0xf98; // const char *
		public static final long m_iszSpriteName = 0xfd8; // const char *
	}

	public static final class CTeam { // DT_Team
		public static final long m_iTeamNum = 0x1104; // int
		public static final long m_bSurrendered = 0x1108; // int
		public static final long m_scoreTotal = 0x10dc; // int
		public static final long m_scoreFirstHalf = 0x10e0; // int
		public static final long m_scoreSecondHalf = 0x10e4; // int
		public static final long m_scoreOvertime = 0x10e8; // int
		public static final long m_iClanID = 0x10f4; // int
		public static final long m_szTeamname = 0xf88; // const char *
		public static final long m_szClanTeamname = 0xfa8; // const char *
		public static final long m_szTeamFlagImage = 0xfc8; // const char *
		public static final long m_szTeamLogoImage = 0xfd0; // const char *
		public static final long m_szTeamMatchStat = 0xfd8; // const char *
		public static final long m_nGGLeaderEntIndex_CT = 0x10ec; // int
		public static final long m_nGGLeaderEntIndex_T = 0x10f0; // int
		public static final long m_numMapVictories = 0x110c; // int
		public static final long player_array_element = 0x0; // int
		public static final long  player_array  = 0x0; // array elements: 64
	}

	public static final class CSunlightShadowControl { // DT_SunlightShadowControl
		public static final long m_shadowDirection = 0xf68; // Vector
		public static final long m_bEnabled = 0xf74; // int
		public static final long m_TextureName = 0xf75; // const char *
		public static final long m_LightColor = 0x1088; // int
		public static final long m_flColorTransitionTime = 0x109c; // float
		public static final long m_flSunDistance = 0x10a0; // float
		public static final long m_flFOV = 0x10a4; // float
		public static final long m_flNearZ = 0x10a8; // float
		public static final long m_flNorthOffset = 0x10ac; // float
		public static final long m_bEnableShadows = 0x10b0; // int
	}

	public static final class CSun { // DT_Sun
		public static final long m_clrRender = 0xa8; // int
		public static final long m_clrOverlay = 0x1118; // int
		public static final long m_vDirection = 0x1124; // Vector
		public static final long m_bOn = 0x1130; // int
		public static final long m_nSize = 0x111c; // int
		public static final long m_nOverlaySize = 0x1120; // int
		public static final long m_nMaterial = 0x1134; // int
		public static final long m_nOverlayMaterial = 0x1138; // int
		public static final long HDRColorScale = 0x0; // float
		public static final long glowDistanceScale = 0x0; // float
	}

	public static final class CParticlePerformanceMonitor { // DT_ParticlePerformanceMonitor
		public static final long m_bMeasurePerf = 0xf66; // int
		public static final long m_bDisplayPerf = 0xf65; // int
	}

	public static final class CSpotlightEnd { // DT_SpotlightEnd
		public static final long m_flLightScale = 0xf68; // float
		public static final long m_Radius = 0xf6c; // float
	}

	public static final class CSpatialEntity { // DT_SpatialEntity
		public static final long m_vecOrigin = 0xf68; // Vector
		public static final long m_minFalloff = 0xf74; // float
		public static final long m_maxFalloff = 0xf78; // float
		public static final long m_flCurWeight = 0xf7c; // float
		public static final long m_bEnabled = 0x1084; // int
	}

	public static final class CSlideshowDisplay { // DT_SlideshowDisplay
		public static final long m_bEnabled = 0xf65; // int
		public static final long m_szDisplayText = 0xf66; // const char *
		public static final long m_szSlideshowDirectory = 0xfe6; // const char *
		public static final long m_chCurrentSlideLists = 0x1088; // int[16]
		public static final long m_fMinSlideTime = 0x10a0; // float
		public static final long m_fMaxSlideTime = 0x10a4; // float
		public static final long m_iCycleType = 0x10ac; // int
		public static final long m_bNoListRepeats = 0x10b0; // int
	}

	public static final class CShadowControl { // DT_ShadowControl
		public static final long m_shadowDirection = 0xf68; // Vector
		public static final long m_shadowColor = 0xf74; // int
		public static final long m_flShadowMaxDist = 0xf78; // float
		public static final long m_bDisableShadows = 0xf7c; // int
		public static final long m_bEnableLocalLightShadows = 0xf7d; // int
	}

	public static final class CSceneEntity { // DT_SceneEntity
		public static final long m_nSceneStringIndex = 0xf7c; // int
		public static final long m_bIsPlayingBack = 0xf70; // int
		public static final long m_bPaused = 0xf71; // int
		public static final long m_bMultiplayer = 0xf72; // int
		public static final long m_flForceClientTime = 0xf78; // float
		public static final class m_hActorList { // _ST_m_hActorList_16
			public static final long BASE_OFFSET = 0x0;
			public static final class lengthproxy { // _LPT_m_hActorList_16
				public static final long BASE_OFFSET = 0x0;
				public static final long lengthprop16 = 0x0; // int
			}
		}
	}

	public static final class CRopeKeyframe { // DT_RopeKeyframe
		public static final long m_nChangeCount = 0x12bc; // int
		public static final long m_iRopeMaterialModelIndex = 0xfa4; // int
		public static final long m_hStartPoint = 0x129c; // int
		public static final long m_hEndPoint = 0x12a0; // int
		public static final long m_iStartAttachment = 0x12a4; // int
		public static final long m_iEndAttachment = 0x12a6; // int
		public static final long m_fLockedPoints = 0x12b8; // int
		public static final long m_Slack = 0x12b0; // int
		public static final long m_RopeLength = 0x12ac; // int
		public static final long m_RopeFlags = 0xfa0; // int
		public static final long m_TextureScale = 0x12b4; // float
		public static final long m_nSegments = 0x1298; // int
		public static final long m_bConstrainBetweenEndpoints = 0x1350; // int
		public static final long m_Subdiv = 0x12a8; // int
		public static final long m_Width = 0x12c0; // float
		public static final long m_flScrollSpeed = 0xf9c; // float
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long moveparent = 0x180; // int
		public static final long m_iParentAttachment = 0x340; // int
		public static final long m_iDefaultRopeMaterialModelIndex = 0xfa8; // int
		public static final long m_nMinCPULevel = 0xf18; // int
		public static final long m_nMaxCPULevel = 0xf19; // int
		public static final long m_nMinGPULevel = 0xf1a; // int
		public static final long m_nMaxGPULevel = 0xf1b; // int
	}

	public static final class CRagdollManager { // DT_RagdollManager
		public static final long m_iCurrentMaxRagdollCount = 0xf68; // int
	}

	public static final class CPhysicsPropMultiplayer { // DT_PhysicsPropMultiplayer
		public static final long m_iPhysicsMode = 0x3058; // int
		public static final long m_fMass = 0x305c; // float
		public static final long m_collisionMins = 0x3060; // Vector
		public static final long m_collisionMaxs = 0x306c; // Vector
	}

	public static final class CPhysBoxMultiplayer { // DT_PhysBoxMultiplayer
		public static final long m_iPhysicsMode = 0xf78; // int
		public static final long m_fMass = 0xf7c; // float
	}

	public static final class CPropDoorRotating { // DT_PropDoorRotating
	}

	public static final class CBasePropDoor { // DT_BasePropDoor
	}

	public static final class CDynamicProp { // DT_DynamicProp
		public static final long m_bUseHitboxesForRenderBox = 0x303c; // int
		public static final long m_flGlowMaxDist = 0x305c; // float
		public static final long m_bShouldGlow = 0x3060; // int
		public static final long m_clrGlow = 0x3061; // int
		public static final long m_nGlowStyle = 0x3068; // int
	}

	public static final class CProp_Hallucination { // DT_Prop_Hallucination
		public static final long m_bEnabled = 0x3039; // int
		public static final long m_fVisibleTime = 0x303c; // float
		public static final long m_fRechargeTime = 0x3040; // float
	}

	public static final class CPostProcessController { // DT_PostProcessController
		public static final long m_flPostProcessParameters = 0xf68; // float[11]
		public static final long m_bMaster = 0xf94; // int
	}

	public static final class CPointWorldText { // DT_PointWorldText
		public static final long m_szText = 0xf98; // const char *
		public static final long m_flTextSize = 0x109c; // float
		public static final long m_textColor = 0x10a0; // int
	}

	public static final class CPointCommentaryNode { // DT_PointCommentaryNode
		public static final long m_bActive = 0x3020; // int
		public static final long m_flStartTime = 0x3024; // float
		public static final long m_iszCommentaryFile = 0x3028; // const char *
		public static final long m_iszCommentaryFileNoHDR = 0x312c; // const char *
		public static final long m_iszSpeakers = 0x3230; // const char *
		public static final long m_iNodeNumber = 0x3330; // int
		public static final long m_iNodeNumberMax = 0x3334; // int
		public static final long m_hViewPosition = 0x3340; // int
	}

	public static final class CPointCamera { // DT_PointCamera
		public static final long m_FOV = 0xf68; // float
		public static final long m_Resolution = 0xf6c; // float
		public static final long m_bFogEnable = 0xf70; // int
		public static final long m_FogColor = 0xf71; // int
		public static final long m_flFogStart = 0xf78; // float
		public static final long m_flFogEnd = 0xf7c; // float
		public static final long m_flFogMaxDensity = 0xf80; // float
		public static final long m_bActive = 0xf84; // int
		public static final long m_bUseScreenAspectRatio = 0xf85; // int
	}

	public static final class CPlayerResource { // DT_PlayerResource
		public static final long m_iPing = 0x11c4; // int[65]
		public static final long m_iKills = 0x12c8; // int[65]
		public static final long m_iAssists = 0x13cc; // int[65]
		public static final long m_iDeaths = 0x14d0; // int[65]
		public static final long m_bConnected = 0x1180; // int[65]
		public static final long m_iTeam = 0x15d4; // int[65]
		public static final long m_iPendingTeam = 0x16d8; // int[65]
		public static final long m_bAlive = 0x17dc; // int[65]
		public static final long m_iHealth = 0x1820; // int[65]
		public static final long m_iCoachingTeam = 0x1924; // int[65]
	}

	public static final class CPlasma { // DT_Plasma
		public static final long m_flStartScale = 0xf68; // float
		public static final long m_flScale = 0xf6c; // float
		public static final long m_flScaleTime = 0xf70; // float
		public static final long m_nFlags = 0xf74; // int
		public static final long m_nPlasmaModelIndex = 0xf78; // int
		public static final long m_nPlasmaModelIndex2 = 0xf7c; // int
		public static final long m_nGlowModelIndex = 0xf80; // int
	}

	public static final class CPhysMagnet { // DT_PhysMagnet
	}

	public static final class CPhysicsProp { // DT_PhysicsProp
		public static final long m_bAwake = 0x3035; // int
		public static final long m_spawnflags = 0x314; // int
	}

	public static final class CStatueProp { // DT_StatueProp
		public static final long m_hInitBaseAnimating = 0x3050; // int
		public static final long m_bShatter = 0x3054; // int
		public static final long m_nShatterFlags = 0x3058; // int
		public static final long m_vShatterPosition = 0x305c; // Vector
		public static final long m_vShatterForce = 0x3068; // Vector
	}

	public static final class CPhysBox { // DT_PhysBox
		public static final long m_mass = 0xf68; // float
	}

	public static final class CParticleSystem { // DT_ParticleSystem
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long m_fEffects = 0x128; // int
		public static final long m_hOwnerEntity = 0x184; // int
		public static final long moveparent = 0x180; // int
		public static final long m_iParentAttachment = 0x340; // int
		public static final long m_angRotation = 0x164; // Vector
		public static final long m_iEffectIndex = 0xf68; // int
		public static final long m_bActive = 0xf70; // int
		public static final long m_nStopType = 0xf6c; // int
		public static final long m_flStartTime = 0xf74; // float
		public static final long m_szSnapshotFileName = 0xf78; // const char *
		public static final long m_vServerControlPoints = 0x107c; // Vector[4]
		public static final long m_iServerControlPointAssignments = 0x10ac; // int[4]
		public static final long m_hControlPointEnts = 0x10d0; // int[63]
		public static final long m_iControlPointParents = 0x11cc; // int[63]
	}

	public static final class CMovieDisplay { // DT_MovieDisplay
		public static final long m_bEnabled = 0xf65; // int
		public static final long m_bLooping = 0xf66; // int
		public static final long m_szMovieFilename = 0xf67; // const char *
		public static final long m_szGroupName = 0xfe7; // const char *
		public static final long m_bStretchToFill = 0x1067; // int
		public static final long m_bForcedSlave = 0x1068; // int
		public static final long m_bUseCustomUVs = 0x1069; // int
		public static final long m_flUMin = 0x106c; // float
		public static final long m_flUMax = 0x1070; // float
		public static final long m_flVMin = 0x1074; // float
		public static final long m_flVMax = 0x1078; // float
	}

	public static final class CMaterialModifyControl { // DT_MaterialModifyControl
		public static final long m_szMaterialName = 0xf65; // const char *
		public static final long m_szMaterialVar = 0x1064; // const char *
		public static final long m_szMaterialVarValue = 0x1163; // const char *
		public static final long m_iFrameStart = 0x1274; // int
		public static final long m_iFrameEnd = 0x1278; // int
		public static final long m_bWrap = 0x127c; // int
		public static final long m_flFramerate = 0x1280; // float
		public static final long m_bNewAnimCommandsSemaphore = 0x1284; // int
		public static final long m_flFloatLerpStartValue = 0x1288; // float
		public static final long m_flFloatLerpEndValue = 0x128c; // float
		public static final long m_flFloatLerpTransitionTime = 0x1290; // float
		public static final long m_bFloatLerpWrap = 0x1294; // int
		public static final long m_nModifyMode = 0x129c; // int
	}

	public static final class CLightGlow { // DT_LightGlow
		public static final long m_clrRender = 0xa8; // int
		public static final long m_nHorizontalSize = 0xf68; // int
		public static final long m_nVerticalSize = 0xf6c; // int
		public static final long m_nMinDist = 0xf70; // int
		public static final long m_nMaxDist = 0xf74; // int
		public static final long m_nOuterMaxDist = 0xf78; // int
		public static final long m_spawnflags = 0xf7c; // int
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long m_angRotation = 0x164; // Vector
		public static final long moveparent = 0x180; // int
		public static final long m_flGlowProxySize = 0x1080; // float
		public static final long HDRColorScale = 0x0; // float
	}

	public static final class CItemAssaultSuitUseable { // DT_ItemAssaultSuitUseable
		public static final long m_nArmorValue = 0x3ebc; // int
		public static final long m_bIsHeavyAssaultSuit = 0x3ec0; // int
	}

	public static final class CItem { // DT_Item
		public static final long m_bShouldGlow = 0x3ab8; // int
	}

	public static final class CInfoOverlayAccessor { // DT_InfoOverlayAccessor
		public static final long m_iTextureFrameIndex = 0xf1c; // int
		public static final long m_iOverlayID = 0xf68; // int
	}

	public static final class CFuncTrackTrain { // DT_FuncTrackTrain
	}

	public static final class CFuncSmokeVolume { // DT_FuncSmokeVolume
		public static final long m_Color1 = 0x10a0; // int
		public static final long m_Color2 = 0x10a4; // int
		public static final long m_MaterialName = 0x10a8; // const char *
		public static final long m_ParticleDrawWidth = 0x11a8; // float
		public static final long m_ParticleSpacingDistance = 0x11ac; // float
		public static final long m_DensityRampSpeed = 0x11b0; // float
		public static final long m_RotationSpeed = 0x11b4; // float
		public static final long m_MovementSpeed = 0x11b8; // float
		public static final long m_Density = 0x11bc; // float
		public static final long m_maxDrawDistance = 0x11c0; // float
		public static final long m_spawnflags = 0x11c4; // int
		public static final class m_Collision { // DT_CollisionProperty
			public static final long BASE_OFFSET = 0x378;
			public static final long m_vecMins = 0x10; // Vector
			public static final long m_vecMaxs = 0x1c; // Vector
			public static final long m_nSolidType = 0x2a; // int
			public static final long m_usSolidFlags = 0x28; // int
			public static final long m_nSurroundType = 0x32; // int
			public static final long m_triggerBloat = 0x2b; // int
			public static final long m_vecSpecifiedSurroundingMins = 0x34; // Vector
			public static final long m_vecSpecifiedSurroundingMaxs = 0x40; // Vector
		}
	}

	public static final class CFuncRotating { // DT_FuncRotating
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long m_angRotation_0 = 0x164; // float
		public static final long m_angRotation_1 = 0x168; // float
		public static final long m_angRotation_2 = 0x16c; // float
		public static final long m_flSimulationTime = 0x2a0; // int
	}

	public static final class CFuncReflectiveGlass { // DT_FuncReflectiveGlass
	}

	public static final class CFuncOccluder { // DT_FuncOccluder
		public static final long m_bActive = 0xf6c; // int
		public static final long m_nOccluderIndex = 0xf68; // int
	}

	public static final class CFuncMoveLinear { // DT_FuncMoveLinear
		public static final long m_vecVelocity = 0x14c; // Vector
		public static final long m_fFlags = 0x13c; // int
	}

	public static final class CFunc_LOD { // DT_Func_LOD
		public static final long m_nDisappearMinDist = 0xf68; // int
		public static final long m_nDisappearMaxDist = 0xf6c; // int
	}

	public static final class CTEDust { // DT_TEDust
		public static final long m_flSize = 0x2c; // float
		public static final long m_flSpeed = 0x30; // float
		public static final long m_vecDirection = 0x34; // Vector
	}

	public static final class CFunc_Dust { // DT_Func_Dust
		public static final long m_Color = 0xf65; // int
		public static final long m_SpawnRate = 0xf6c; // int
		public static final long m_flSizeMin = 0xf70; // float
		public static final long m_flSizeMax = 0xf74; // float
		public static final long m_LifetimeMin = 0xf7c; // int
		public static final long m_LifetimeMax = 0xf80; // int
		public static final long m_DustFlags = 0xf90; // int
		public static final long m_SpeedMax = 0xf78; // int
		public static final long m_DistMax = 0xf84; // int
		public static final long m_nModelIndex = 0x290; // int
		public static final long m_FallSpeed = 0xf88; // float
		public static final long m_bAffectedByWind = 0xf8c; // int
		public static final class m_Collision { // DT_CollisionProperty
			public static final long BASE_OFFSET = 0x378;
			public static final long m_vecMins = 0x10; // Vector
			public static final long m_vecMaxs = 0x1c; // Vector
			public static final long m_nSolidType = 0x2a; // int
			public static final long m_usSolidFlags = 0x28; // int
			public static final long m_nSurroundType = 0x32; // int
			public static final long m_triggerBloat = 0x2b; // int
			public static final long m_vecSpecifiedSurroundingMins = 0x34; // Vector
			public static final long m_vecSpecifiedSurroundingMaxs = 0x40; // Vector
		}
	}

	public static final class CFuncConveyor { // DT_FuncConveyor
		public static final long m_flConveyorSpeed = 0xf68; // float
	}

	public static final class CFuncBrush { // DT_FuncBrush
	}

	public static final class CBreakableSurface { // DT_BreakableSurface
		public static final long m_nNumWide = 0xf70; // int
		public static final long m_nNumHigh = 0xf74; // int
		public static final long m_flPanelWidth = 0xf78; // float
		public static final long m_flPanelHeight = 0xf7c; // float
		public static final long m_vNormal = 0xf80; // Vector
		public static final long m_vCorner = 0xf8c; // Vector
		public static final long m_bIsBroken = 0xf98; // int
		public static final long m_nSurfaceType = 0xf9c; // int
		public static final long m_RawPanelBitVec = 0xfd0; // int[256]
	}

	public static final class CFuncAreaPortalWindow { // DT_FuncAreaPortalWindow
		public static final long m_flFadeStartDist = 0xf68; // float
		public static final long m_flFadeDist = 0xf6c; // float
		public static final long m_flTranslucencyLimit = 0xf70; // float
		public static final long m_iBackgroundModelIndex = 0xf74; // int
	}

	public static final class CFish { // DT_CFish
		public static final long m_poolOrigin = 0x3088; // Vector
		public static final long m_x = 0x3070; // float
		public static final long m_y = 0x3074; // float
		public static final long m_z = 0x3078; // float
		public static final long m_angle = 0x3080; // float
		public static final long m_nModelIndex = 0x290; // int
		public static final long m_lifeState = 0x297; // int
		public static final long m_waterLevel = 0x3094; // float
	}

	public static final class CFireSmoke { // DT_FireSmoke
		public static final long m_flStartScale = 0xf68; // float
		public static final long m_flScale = 0xf6c; // float
		public static final long m_flScaleTime = 0xf70; // float
		public static final long m_nFlags = 0xf74; // int
		public static final long m_nFlameModelIndex = 0xf78; // int
		public static final long m_nFlameFromAboveModelIndex = 0xf7c; // int
	}

	public static final class CEntityFlame { // DT_EntityFlame
		public static final long m_hEntAttached = 0xf68; // int
		public static final long m_bCheapEffect = 0xf8c; // int
	}

	public static final class CEnvDOFController { // DT_EnvDOFController
		public static final long m_bDOFEnabled = 0xf65; // int
		public static final long m_flNearBlurDepth = 0xf68; // float
		public static final long m_flNearFocusDepth = 0xf6c; // float
		public static final long m_flFarFocusDepth = 0xf70; // float
		public static final long m_flFarBlurDepth = 0xf74; // float
		public static final long m_flNearBlurRadius = 0xf78; // float
		public static final long m_flFarBlurRadius = 0xf7c; // float
	}

	public static final class CEnvTonemapController { // DT_EnvTonemapController
		public static final long m_bUseCustomAutoExposureMin = 0xf65; // int
		public static final long m_bUseCustomAutoExposureMax = 0xf66; // int
		public static final long m_bUseCustomBloomScale = 0xf67; // int
		public static final long m_flCustomAutoExposureMin = 0xf68; // float
		public static final long m_flCustomAutoExposureMax = 0xf6c; // float
		public static final long m_flCustomBloomScale = 0xf70; // float
		public static final long m_flCustomBloomScaleMinimum = 0xf74; // float
		public static final long m_flBloomExponent = 0xf78; // float
		public static final long m_flBloomSaturation = 0xf7c; // float
		public static final long m_flTonemapPercentTarget = 0xf80; // float
		public static final long m_flTonemapPercentBrightPixels = 0xf84; // float
		public static final long m_flTonemapMinAvgLum = 0xf88; // float
		public static final long m_flTonemapRate = 0xf8c; // float
	}

	public static final class CEnvScreenEffect { // DT_EnvScreenEffect
		public static final long m_flDuration = 0xf68; // float
		public static final long m_nType = 0xf6c; // int
	}

	public static final class CEnvScreenOverlay { // DT_EnvScreenOverlay
		public static final long m_iszOverlayNames_0 = 0xf65; // const char *
		public static final long m_iszOverlayNames = 0x0; // array elements: 10
		public static final long m_flOverlayTimes_0 = 0x195c; // float
		public static final long m_flOverlayTimes = 0x0; // array elements: 10
		public static final long m_flStartTime = 0x1984; // float
		public static final long m_iDesiredOverlay = 0x1988; // int
		public static final long m_bIsActive = 0x198c; // int
	}

	public static final class CEnvProjectedTexture { // DT_EnvProjectedTexture
		public static final long m_hTargetEntity = 0xf6c; // int
		public static final long m_bState = 0xf70; // int
		public static final long m_bAlwaysUpdate = 0xf71; // int
		public static final long m_flLightFOV = 0xf74; // float
		public static final long m_bEnableShadows = 0xf78; // int
		public static final long m_bSimpleProjection = 0xf79; // int
		public static final long m_bLightOnlyTarget = 0xf7a; // int
		public static final long m_bLightWorld = 0xf7b; // int
		public static final long m_bCameraSpace = 0xf7c; // int
		public static final long m_flBrightnessScale = 0xf80; // float
		public static final long m_LightColor = 0xf84; // int
		public static final long m_flColorTransitionTime = 0xf98; // float
		public static final long m_flAmbient = 0xf9c; // float
		public static final long m_SpotlightTextureName = 0xfa8; // const char *
		public static final long m_nSpotlightTextureFrame = 0x10c0; // int
		public static final long m_flNearZ = 0xfa0; // float
		public static final long m_flFarZ = 0xfa4; // float
		public static final long m_nShadowQuality = 0x10c4; // int
		public static final long m_flProjectionSize = 0x10d8; // float
		public static final long m_flRotation = 0x10dc; // float
		public static final long m_iStyle = 0x10c8; // int
	}

	public static final class CEnvParticleScript { // DT_EnvParticleScript
		public static final long m_flSequenceScale = 0x3150; // float
	}

	public static final class CFogController { // DT_FogController
		public static final long m_fog_enable = 0xfb0; // int
		public static final long m_fog_blend = 0xfb1; // int
		public static final long m_fog_dirPrimary = 0xf70; // Vector
		public static final long m_fog_colorPrimary = 0xf7c; // int
		public static final long m_fog_colorSecondary = 0xf80; // int
		public static final long m_fog_start = 0xf8c; // float
		public static final long m_fog_end = 0xf90; // float
		public static final long m_fog_farz = 0xf94; // float
		public static final long m_fog_maxdensity = 0xf98; // float
		public static final long m_fog_colorPrimaryLerpTo = 0xf84; // int
		public static final long m_fog_colorSecondaryLerpTo = 0xf88; // int
		public static final long m_fog_startLerpTo = 0xf9c; // float
		public static final long m_fog_endLerpTo = 0xfa0; // float
		public static final long m_fog_maxdensityLerpTo = 0xfa4; // float
		public static final long m_fog_lerptime = 0xfa8; // float
		public static final long m_fog_duration = 0xfac; // float
		public static final long m_fog_HDRColorScale = 0xfb8; // float
		public static final long m_fog_ZoomFogScale = 0xfb4; // float
	}

	public static final class CCascadeLight { // DT_CascadeLight
		public static final long m_shadowDirection = 0xf68; // Vector
		public static final long m_envLightShadowDirection = 0xf74; // Vector
		public static final long m_bEnabled = 0xf80; // int
		public static final long m_bUseLightEnvAngles = 0xf81; // int
		public static final long m_LightColor = 0xf82; // int
		public static final long m_LightColorScale = 0xf88; // int
		public static final long m_flMaxShadowDist = 0xf8c; // float
	}

	public static final class CEnvAmbientLight { // DT_EnvAmbientLight
		public static final long m_vecColor = 0x1090; // Vector
	}

	public static final class CEntityParticleTrail { // DT_EntityParticleTrail
		public static final long m_iMaterialName = 0x1098; // int
		public static final class m_Info { // DT_EntityParticleTrailInfo
			public static final long BASE_OFFSET = 0x10a0;
			public static final long m_flLifetime = 0x10; // float
			public static final long m_flStartSize = 0x14; // float
			public static final long m_flEndSize = 0x18; // float
		}
		public static final long m_hConstraintEntity = 0x10c0; // int
	}

	public static final class CEntityFreezing { // DT_EntityFreezing
		public static final long m_vFreezingOrigin = 0xf68; // Vector
		public static final long m_flFrozenPerHitbox = 0xf74; // float[50]
		public static final long m_flFrozen = 0x103c; // float
		public static final long m_bFinishFreezing = 0x1040; // int
	}

	public static final class CEntityDissolve { // DT_EntityDissolve
		public static final long m_flStartTime = 0xf70; // float
		public static final long m_flFadeOutStart = 0xf74; // float
		public static final long m_flFadeOutLength = 0xf78; // float
		public static final long m_flFadeOutModelStart = 0xf7c; // float
		public static final long m_flFadeOutModelLength = 0xf80; // float
		public static final long m_flFadeInStart = 0xf84; // float
		public static final long m_flFadeInLength = 0xf88; // float
		public static final long m_nDissolveType = 0xf8c; // int
		public static final long m_vDissolverOrigin = 0xf94; // Vector
		public static final long m_nMagnitude = 0xfa0; // int
	}

	public static final class CDynamicLight { // DT_DynamicLight
		public static final long m_Flags = 0xf65; // int
		public static final long m_LightStyle = 0xf66; // int
		public static final long m_Radius = 0xf68; // float
		public static final long m_Exponent = 0xf6c; // int
		public static final long m_InnerAngle = 0xf70; // float
		public static final long m_OuterAngle = 0xf74; // float
		public static final long m_SpotRadius = 0xf78; // float
	}

	public static final class CPropCounter { // DT_PropCounter
		public static final long m_flDisplayValue = 0x3020; // float
	}

	public static final class CGrassBurn { // DT_GrassBurn
		public static final long m_flGrassBurnClearTime = 0xf68; // float
	}

	public static final class CDangerZone { // DT_DangerZone
		public static final long m_vecDangerZoneOriginStartedAt = 0xf68; // Vector
		public static final long m_flBombLaunchTime = 0xf74; // float
		public static final long m_flExtraRadius = 0xf78; // float
		public static final long m_flExtraRadiusStartTime = 0xf7c; // float
		public static final long m_flExtraRadiusTotalLerpTime = 0xf80; // float
		public static final long m_nDropOrder = 0xf84; // int
		public static final long m_iWave = 0xf88; // int
	}

	public static final class CDangerZoneController { // DT_DangerZoneController
		public static final long m_bDangerZoneControllerEnabled = 0xf65; // int
		public static final long m_bMissionControlledExplosions = 0xf66; // int
		public static final long m_flStartTime = 0xf80; // float
		public static final long m_flFinalExpansionTime = 0xf84; // float
		public static final long m_vecEndGameCircleStart = 0xf68; // Vector
		public static final long m_vecEndGameCircleEnd = 0xf74; // Vector
		public static final long m_DangerZones = 0xf88; // int[42]
		public static final long m_flWaveEndTimes = 0x1030; // float[5]
		public static final long m_hTheFinalZone = 0x1044; // int
	}

	public static final class CColorCorrectionVolume { // DT_ColorCorrectionVolume
		public static final long m_bEnabled = 0xfa4; // int
		public static final long m_MaxWeight = 0xfa8; // float
		public static final long m_FadeDuration = 0xfac; // float
		public static final long m_Weight = 0xfb0; // float
		public static final long m_lookupFilename = 0xfb4; // const char *
	}

	public static final class CColorCorrection { // DT_ColorCorrection
		public static final long m_vecOrigin = 0xf68; // Vector
		public static final long m_minFalloff = 0xf74; // float
		public static final long m_maxFalloff = 0xf78; // float
		public static final long m_flCurWeight = 0xf88; // float
		public static final long m_flMaxWeight = 0xf84; // float
		public static final long m_flFadeInDuration = 0xf7c; // float
		public static final long m_flFadeOutDuration = 0xf80; // float
		public static final long m_netLookupFilename = 0xf8c; // const char *
		public static final long m_bEnabled = 0x1090; // int
		public static final long m_bMaster = 0x1091; // int
		public static final long m_bClientSide = 0x1092; // int
		public static final long m_bExclusive = 0x1093; // int
	}

	public static final class CBreakableProp { // DT_BreakableProp
		public static final long m_qPreferredPlayerCarryAngles = 0x3028; // Vector
		public static final long m_bClientPhysics = 0x3034; // int
	}

	public static final class CBeamSpotlight { // DT_BeamSpotlight
		public static final long m_nHaloIndex = 0xf68; // int
		public static final long m_bSpotlightOn = 0xf74; // int
		public static final long m_bHasDynamicLight = 0xf75; // int
		public static final long m_flSpotlightMaxLength = 0xf78; // float
		public static final long m_flSpotlightGoalWidth = 0xf7c; // float
		public static final long m_flHDRColorScale = 0xf80; // float
		public static final long m_nRotationAxis = 0xf6c; // int
		public static final long m_flRotationSpeed = 0xf70; // float
	}

	public static final class CBaseButton { // DT_BaseButton
		public static final long m_usable = 0xf91; // int
	}

	public static final class CBaseToggle { // DT_BaseToggle
		public static final long m_vecFinalDest = 0xf7c; // Vector
		public static final long m_movementType = 0xf88; // int
		public static final long m_flMoveTargetTime = 0xf8c; // float
	}

	public static final class CBasePlayer { // DT_BasePlayer
		public static final class localdata { // DT_LocalPlayerExclusive
			public static final long BASE_OFFSET = 0x0;
			public static final class m_Local { // DT_Local
				public static final long BASE_OFFSET = 0x3708;
				public static final long m_chAreaBits = 0x8; // int[32]
				public static final long m_chAreaPortalBits = 0x28; // int[24]
				public static final long m_iHideHUD = 0x4c; // int
				public static final long m_flFOVRate = 0x48; // float
				public static final long m_bDucked = 0x8c; // int
				public static final long m_bDucking = 0x8d; // int
				public static final long m_flLastDuckTime = 0x90; // float
				public static final long m_bInDuckJump = 0x94; // int
				public static final long m_nDuckTimeMsecs = 0x50; // int
				public static final long m_nDuckJumpTimeMsecs = 0x54; // int
				public static final long m_nJumpTimeMsecs = 0x58; // int
				public static final long m_flFallVelocity = 0x5c; // float
				public static final long m_nOldButtons = 0x44; // int
				public static final long m_viewPunchAngle = 0x68; // Vector
				public static final long m_aimPunchAngle = 0x74; // Vector
				public static final long m_aimPunchAngleVel = 0x80; // Vector
				public static final long m_bDrawViewmodel = 0x95; // int
				public static final long m_bWearingSuit = 0x96; // int
				public static final long m_bPoisoned = 0x97; // int
				public static final long m_flStepSize = 0x64; // float
				public static final long m_bAllowAutoMovement = 0x98; // int
				public static final long m_skybox3d_scale = 0x198; // int
				public static final long m_skybox3d_origin = 0x19c; // Vector
				public static final long m_skybox3d_area = 0x1a8; // int
				public static final long m_skybox3d_fog_enable = 0x1f8; // int
				public static final long m_skybox3d_fog_blend = 0x1f9; // int
				public static final long m_skybox3d_fog_dirPrimary = 0x1b8; // Vector
				public static final long m_skybox3d_fog_colorPrimary = 0x1c4; // int
				public static final long m_skybox3d_fog_colorSecondary = 0x1c8; // int
				public static final long m_skybox3d_fog_start = 0x1d4; // float
				public static final long m_skybox3d_fog_end = 0x1d8; // float
				public static final long m_skybox3d_fog_maxdensity = 0x1e0; // float
				public static final long m_skybox3d_fog_HDRColorScale = 0x200; // float
				public static final long m_audio_localSound_0 = 0x210; // Vector
				public static final long m_audio_localSound_1 = 0x21c; // Vector
				public static final long m_audio_localSound_2 = 0x228; // Vector
				public static final long m_audio_localSound_3 = 0x234; // Vector
				public static final long m_audio_localSound_4 = 0x240; // Vector
				public static final long m_audio_localSound_5 = 0x24c; // Vector
				public static final long m_audio_localSound_6 = 0x258; // Vector
				public static final long m_audio_localSound_7 = 0x264; // Vector
				public static final long m_audio_soundscapeIndex = 0x270; // int
				public static final long m_audio_localBits = 0x274; // int
				public static final long m_audio_entIndex = 0x278; // int
			}
			public static final long m_vecViewOffset_0 = 0x140; // float
			public static final long m_vecViewOffset_1 = 0x144; // float
			public static final long m_vecViewOffset_2 = 0x148; // float
			public static final long m_flFriction = 0x17c; // float
			public static final long m_fOnTarget = 0x3b14; // int
			public static final long m_nTickBase = 0x3c58; // int
			public static final long m_nNextThinkTick = 0x134; // int
			public static final long m_hLastWeapon = 0x3ad8; // int
			public static final long m_vecVelocity_0 = 0x14c; // float
			public static final long m_vecVelocity_1 = 0x150; // float
			public static final long m_vecVelocity_2 = 0x154; // float
			public static final long m_vecBaseVelocity = 0x158; // Vector
			public static final long m_hConstraintEntity = 0x3b38; // int
			public static final long m_vecConstraintCenter = 0x3b3c; // Vector
			public static final long m_flConstraintRadius = 0x3b48; // float
			public static final long m_flConstraintWidth = 0x3b4c; // float
			public static final long m_flConstraintSpeedFactor = 0x3b50; // float
			public static final long m_bConstraintPastRadius = 0x3b54; // int
			public static final long m_flDeathTime = 0x3bc4; // float
			public static final long m_flNextDecalTime = 0x3bc8; // float
			public static final long m_fForceTeam = 0x3bcc; // float
			public static final long m_flLaggedMovementValue = 0x3dd8; // float
			public static final long m_hTonemapController = 0x3990; // int
		}
		public static final class pl { // DT_PlayerState
			public static final long BASE_OFFSET = 0x3998;
			public static final long deadflag = 0x8; // int
		}
		public static final long m_iFOV = 0x39b0; // int
		public static final long m_iFOVStart = 0x39b4; // int
		public static final long m_flFOVTime = 0x39d4; // float
		public static final long m_iDefaultFOV = 0x3b1c; // int
		public static final long m_hZoomOwner = 0x3a18; // int
		public static final long m_afPhysicsFlags = 0x3ad0; // int
		public static final long m_hVehicle = 0x3ad4; // int
		public static final long m_hUseEntity = 0x3b18; // int
		public static final long m_hGroundEntity = 0x188; // int
		public static final long m_iHealth = 0x138; // int
		public static final long m_lifeState = 0x297; // int
		public static final long m_iAmmo = 0x34c0; // int[32]
		public static final long m_iBonusProgress = 0x3a0c; // int
		public static final long m_iBonusChallenge = 0x3a10; // int
		public static final long m_flMaxspeed = 0x3a14; // float
		public static final long m_fFlags = 0x13c; // int
		public static final long m_iObserverMode = 0x3b78; // int
		public static final long m_bActiveCameraMan = 0x3b7c; // int
		public static final long m_bCameraManXRay = 0x3b7d; // int
		public static final long m_bCameraManOverview = 0x3b7e; // int
		public static final long m_bCameraManScoreBoard = 0x3b7f; // int
		public static final long m_uCameraManGraphs = 0x3b80; // int
		public static final long m_iDeathPostEffect = 0x3b74; // int
		public static final long m_hObserverTarget = 0x3b8c; // int
		public static final long m_hViewModel_0 = 0x3adc; // int
		public static final long m_hViewModel = 0x0; // array elements: 3
		public static final long m_iCoachingTeam = 0x3694; // int
		public static final long m_szLastPlaceName = 0x3df8; // const char *
		public static final long m_vecLadderNormal = 0x39fc; // Vector
		public static final long m_ladderSurfaceProps = 0x39cc; // int
		public static final long m_ubEFNoInterpParity = 0x3e20; // int
		public static final long m_hPostProcessCtrl = 0x4038; // int
		public static final long m_hColorCorrectionCtrl = 0x403c; // int
		public static final long m_PlayerFog_m_hCtrl = 0x4048; // int
		public static final long m_vphysicsCollisionState = 0x3a38; // int
		public static final long m_hViewEntity = 0x3b30; // int
		public static final long m_bShouldDrawPlayerWhileUsingViewEntity = 0x3b34; // int
		public static final long m_flDuckAmount = 0x36f8; // float
		public static final long m_flDuckSpeed = 0x36fc; // float
		public static final long m_nWaterLevel = 0x296; // int
	}

	public static final class CBaseFlex { // DT_BaseFlex
		public static final long m_flexWeight = 0x30f0; // float[96]
		public static final long m_blinktoggle = 0x32b8; // int
		public static final long m_viewtarget = 0x309c; // Vector
	}

	public static final class CBaseEntity { // DT_BaseEntity
		public static final class AnimTimeMustBeFirst { // DT_AnimTimeMustBeFirst
			public static final long BASE_OFFSET = 0x0;
			public static final long m_flAnimTime = 0x298; // int
		}
		public static final long m_flSimulationTime = 0x2a0; // int
		public static final long m_cellbits = 0xac; // int
		public static final long m_cellX = 0xb4; // int
		public static final long m_cellY = 0xb8; // int
		public static final long m_cellZ = 0xbc; // int
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long m_angRotation = 0x164; // Vector
		public static final long m_nModelIndex = 0x290; // int
		public static final long m_fEffects = 0x128; // int
		public static final long m_nRenderMode = 0x293; // int
		public static final long m_nRenderFX = 0x292; // int
		public static final long m_clrRender = 0xa8; // int
		public static final long m_iTeamNum = 0x12c; // int
		public static final long m_iPendingTeamNum = 0x130; // int
		public static final long m_CollisionGroup = 0x548; // int
		public static final long m_flElasticity = 0x354; // float
		public static final long m_flShadowCastDistance = 0x420; // float
		public static final long m_hOwnerEntity = 0x184; // int
		public static final long m_hEffectEntity = 0xf28; // int
		public static final long moveparent = 0x180; // int
		public static final long m_iParentAttachment = 0x340; // int
		public static final long m_iName = 0x18c; // const char *
		public static final long movetype = 0x0; // int
		public static final long movecollide = 0x0; // int
		public static final class m_Collision { // DT_CollisionProperty
			public static final long BASE_OFFSET = 0x378;
			public static final long m_vecMins = 0x10; // Vector
			public static final long m_vecMaxs = 0x1c; // Vector
			public static final long m_nSolidType = 0x2a; // int
			public static final long m_usSolidFlags = 0x28; // int
			public static final long m_nSurroundType = 0x32; // int
			public static final long m_triggerBloat = 0x2b; // int
			public static final long m_vecSpecifiedSurroundingMins = 0x34; // Vector
			public static final long m_vecSpecifiedSurroundingMaxs = 0x40; // Vector
		}
		public static final long m_iTextureFrameIndex = 0xf1c; // int
		public static final long m_bSimulatedEveryTick = 0xeca; // int
		public static final long m_bAnimatedEveryTick = 0xecb; // int
		public static final long m_bAlternateSorting = 0xecc; // int
		public static final long m_bSpotted = 0xecd; // int
		public static final long m_bSpottedBy = 0xece; // int[65]
		public static final long m_bSpottedByMask = 0xf10; // int[2]
		public static final long m_bIsAutoaimTarget = 0x90; // int
		public static final long m_fadeMinDist = 0x348; // float
		public static final long m_fadeMaxDist = 0x34c; // float
		public static final long m_flFadeScale = 0x350; // float
		public static final long m_nMinCPULevel = 0xf18; // int
		public static final long m_nMaxCPULevel = 0xf19; // int
		public static final long m_nMinGPULevel = 0xf1a; // int
		public static final long m_nMaxGPULevel = 0xf1b; // int
		public static final long m_flUseLookAtAngle = 0x318; // float
		public static final long m_flLastMadeNoiseTime = 0x40; // float
		public static final long m_flMaxFallVelocity = 0x114; // float
		public static final long m_bEligibleForScreenHighlight = 0xf49; // int
	}

	public static final class CBaseDoor { // DT_BaseDoor
		public static final long m_flWaveHeight = 0xf94; // float
	}

	public static final class CBaseCombatCharacter { // DT_BaseCombatCharacter
		public static final class bcc_localdata { // DT_BCCLocalPlayerExclusive
			public static final long BASE_OFFSET = 0x0;
			public static final long m_flNextAttack = 0x34b8; // float
		}
		public static final class bcc_nonlocaldata { // DT_BCCNonLocalPlayerExclusive
			public static final long BASE_OFFSET = 0x0;
			public static final long m_hMyWeapons = 0x3540; // int[64]
		}
		public static final long m_LastHitGroup = 0x34bc; // int
		public static final long m_hActiveWeapon = 0x3640; // int
		public static final long m_flTimeOfLastInjury = 0x3644; // float
		public static final long m_nRelativeDirectionOfLastInjury = 0x3648; // int
		public static final long m_hMyWeapons = 0x3540; // int[64]
		public static final long m_hMyWearables = 0x364c; // int[1]
	}

	public static final class CBaseAnimatingOverlay { // DT_BaseAnimatingOverlay
		public static final class overlay_vars { // DT_OverlayVars
			public static final long BASE_OFFSET = 0x0;
			public static final class m_AnimOverlay { // _ST_m_AnimOverlay_15
				public static final long BASE_OFFSET = 0x0;
				public static final class lengthproxy { // _LPT_m_AnimOverlay_15
					public static final long BASE_OFFSET = 0x0;
					public static final long lengthprop15 = 0x0; // int
				}
			}
		}
	}

	public static final class CBoneFollower { // DT_BoneFollower
		public static final long m_modelIndex = 0xf68; // int
		public static final long m_solidIndex = 0xf6c; // int
	}

	public static final class CBaseAnimating { // DT_BaseAnimating
		public static final long m_nSequence = 0x2f00; // int
		public static final long m_nForceBone = 0x2c54; // int
		public static final long m_vecForce = 0x2c48; // Vector
		public static final long m_nSkin = 0xfd8; // int
		public static final long m_nBody = 0xfdc; // int
		public static final long m_nHitboxSet = 0xfa8; // int
		public static final long m_flModelScale = 0x2d48; // float
		public static final long m_flPoseParameter = 0x2d80; // float[24]
		public static final long m_flPlaybackRate = 0xfd4; // float
		public static final long m_flEncodedController = 0x1010; // float[4]
		public static final long m_bClientSideAnimation = 0x2ee0; // int
		public static final long m_bClientSideFrameReset = 0x2ca4; // int
		public static final long m_bClientSideRagdoll = 0x2b1; // int
		public static final long m_nNewSequenceParity = 0x1000; // int
		public static final long m_nResetEventsParity = 0x1004; // int
		public static final long m_nMuzzleFlashParity = 0x1020; // int
		public static final long m_hLightingOrigin = 0x2fd0; // int
		public static final class serveranimdata { // DT_ServerAnimationData
			public static final long BASE_OFFSET = 0x0;
			public static final long m_flCycle = 0xfd0; // float
		}
		public static final long m_flFrozen = 0x2ce8; // float
		public static final long m_ScaleType = 0x2d4c; // int
		public static final long m_bSuppressAnimSounds = 0x2fd6; // int
		public static final long m_nHighlightColorR = 0xff4; // int
		public static final long m_nHighlightColorG = 0xff8; // int
		public static final long m_nHighlightColorB = 0xffc; // int
	}

	public static final class CAI_BaseNPC { // DT_AI_BaseNPC
		public static final long m_lifeState = 0x297; // int
		public static final long m_bPerformAvoidance = 0x3668; // int
		public static final long m_bIsMoving = 0x3669; // int
		public static final long m_bFadeCorpse = 0x366a; // int
		public static final long m_iDeathPose = 0x3658; // int
		public static final long m_iDeathFrame = 0x365c; // int
		public static final long m_iSpeedModRadius = 0x3660; // int
		public static final long m_iSpeedModSpeed = 0x3664; // int
		public static final long m_bSpeedModActive = 0x366b; // int
		public static final long m_bImportanRagdoll = 0x366c; // int
		public static final long m_flTimePingEffect = 0x3654; // float
	}

	public static final class CBeam { // DT_Beam
		public static final long m_nBeamType = 0xf84; // int
		public static final long m_nBeamFlags = 0xf88; // int
		public static final long m_nNumBeamEnts = 0xf78; // int
		public static final long m_hAttachEntity = 0xf8c; // int[10]
		public static final long m_nAttachIndex = 0xfb4; // int[10]
		public static final long m_nHaloIndex = 0xf80; // int
		public static final long m_fHaloScale = 0xfe8; // float
		public static final long m_fWidth = 0xfdc; // float
		public static final long m_fEndWidth = 0xfe0; // float
		public static final long m_fFadeLength = 0xfe4; // float
		public static final long m_fAmplitude = 0xfec; // float
		public static final long m_fStartFrame = 0xff0; // float
		public static final long m_fSpeed = 0xff4; // float
		public static final long m_flFrameRate = 0xf68; // float
		public static final long m_flHDRColorScale = 0xf6c; // float
		public static final long m_clrRender = 0xa8; // int
		public static final long m_nRenderFX = 0x292; // int
		public static final long m_nRenderMode = 0x293; // int
		public static final long m_flFrame = 0xff8; // float
		public static final long m_nClipStyle = 0xffc; // int
		public static final long m_vecEndPos = 0x1000; // Vector
		public static final long m_nModelIndex = 0x290; // int
		public static final long m_vecOrigin = 0x170; // Vector
		public static final long moveparent = 0x180; // int
	}

	public static final class CBaseViewModel { // DT_BaseViewModel
		public static final long m_nModelIndex = 0x290; // int
		public static final long m_hWeapon = 0x3078; // int
		public static final long m_nSkin = 0xfd8; // int
		public static final long m_nBody = 0xfdc; // int
		public static final long m_nSequence = 0x2f00; // int
		public static final long m_nViewModelIndex = 0x3070; // int
		public static final long m_flPlaybackRate = 0xfd4; // float
		public static final long m_fEffects = 0x128; // int
		public static final long m_nAnimationParity = 0x3074; // int
		public static final long m_hOwner = 0x307c; // int
		public static final long m_nNewSequenceParity = 0x1000; // int
		public static final long m_nResetEventsParity = 0x1004; // int
		public static final long m_nMuzzleFlashParity = 0x1020; // int
		public static final long m_bShouldIgnoreOffsetAndAccuracy = 0x3034; // int
	}

	public static final class CBaseParticleEntity { // DT_BaseParticleEntity
	}

	public static final class CBaseGrenade { // DT_BaseGrenade
		public static final long m_flDamage = 0x3038; // float
		public static final long m_DmgRadius = 0x3024; // float
		public static final long m_bIsLive = 0x3021; // int
		public static final long m_hThrower = 0x3048; // int
		public static final long m_vecVelocity = 0x14c; // Vector
		public static final long m_fFlags = 0x13c; // int
	}

	public static final class CBaseCombatWeapon { // DT_BaseCombatWeapon
		public static final class LocalWeaponData { // DT_LocalWeaponData
			public static final long BASE_OFFSET = 0x0;
			public static final long m_iPrimaryAmmoType = 0x3ae4; // int
			public static final long m_iSecondaryAmmoType = 0x3ae8; // int
			public static final long m_nViewModelIndex = 0x3abc; // int
			public static final long m_bFlipViewModel = 0x3b50; // int
			public static final long m_iWeaponOrigin = 0x3b54; // int
			public static final long m_iWeaponModule = 0x3ad4; // int
		}
		public static final class LocalActiveWeaponData { // DT_LocalActiveWeaponData
			public static final long BASE_OFFSET = 0x0;
			public static final long m_flNextPrimaryAttack = 0x3ac0; // float
			public static final long m_flNextSecondaryAttack = 0x3ac4; // float
			public static final long m_nNextThinkTick = 0x134; // int
			public static final long m_flTimeWeaponIdle = 0x3afc; // float
		}
		public static final long m_iViewModelIndex = 0x3ac8; // int
		public static final long m_iWorldModelIndex = 0x3acc; // int
		public static final long m_iWorldDroppedModelIndex = 0x3ad0; // int
		public static final long m_iState = 0x3ae0; // int
		public static final long m_hOwner = 0x3ab8; // int
		public static final long m_iClip1 = 0x3aec; // int
		public static final long m_iClip2 = 0x3af0; // int
		public static final long m_iPrimaryReserveAmmoCount = 0x3af4; // int
		public static final long m_iSecondaryReserveAmmoCount = 0x3af8; // int
		public static final long m_hWeaponWorldModel = 0x3adc; // int
		public static final long m_iNumEmptyAttacks = 0x3ad8; // int
	}

	public static final class CBaseWeaponWorldModel { // DT_BaseWeaponWorldModel
		public static final long m_nModelIndex = 0x290; // int
		public static final long m_nBody = 0xfdc; // int
		public static final long m_fEffects = 0x128; // int
		public static final long moveparent = 0x180; // int
		public static final long m_hCombatWeaponParent = 0x309c; // int
	}

	public static final class CFuncMonitor { // DT_FuncMonitor
	}

}
