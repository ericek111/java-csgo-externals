package me.lixko.csgoexternals.structs;

public class CSPlayerResource extends MemStruct {
	public final StructField m_iPing = new StructField(this, 64 * Integer.BYTES, 0x1180);
	public final StructField m_iKills = new StructField(this, 64 * Integer.BYTES, 0x1284);
	public final StructField m_iAssists = new StructField(this, 64 * Integer.BYTES, 0x1388);
	public final StructField m_iDeaths = new StructField(this, 64 * Integer.BYTES, 0x148C);
	public final StructField m_bConnected = new StructField(this, 64, 0x1590);
	public final StructField m_iTeam = new StructField(this, 64 * Integer.BYTES, 0x15D4);
	public final StructField m_iPendingTeam = new StructField(this, 64 * Integer.BYTES, 0x16D8);
	public final StructField m_bAlive = new StructField(this, 64, 0x17DC);
	public final StructField m_iHealth = new StructField(this, 64 * Integer.BYTES, 0x1820);
	public final StructField m_iCoachingTeam = new StructField(this, 64 * Integer.BYTES, 0x19A4);

	public final StructField m_iPlayerC4 = new StructField(this, Integer.BYTES, 0x1CB4);
	public final StructField m_iPlayerVIP = new StructField(this, Integer.BYTES, 0x1CB8);
	public final StructField m_bombsiteCenterA = new StructField(this, 2 * Float.BYTES, 0x1CBC);
	public final StructField m_bombsiteCenterB = new StructField(this, 2 * Float.BYTES, 0x1CC8);
	public final StructField m_bHostageAlive = new StructField(this, 64, 0x1CD4);
	public final StructField m_isHostageFollowingSomeone = new StructField(this, 64 * Integer.BYTES, 0x1CE0);
	public final StructField m_iHostageEntityIDs = new StructField(this, 64 * Integer.BYTES, 0x1CEC);
	public final StructField m_hostageRescueX = new StructField(this, 64 * Integer.BYTES, 0x1D1C);
	public final StructField m_hostageRescueY = new StructField(this, 64 * Integer.BYTES, 0x1D2C);
	public final StructField m_hostageRescueZ = new StructField(this, 64 * Integer.BYTES, 0x1D3C);
	public final StructField m_iMVPs = new StructField(this, 64 * Integer.BYTES, 0x1D4C);
	public final StructField m_bHasDefuser = new StructField(this, 64, 0x1E50);
	public final StructField m_bHasHelmet = new StructField(this, 64, 0x1E91);
	public final StructField m_iArmor = new StructField(this, 64 * Integer.BYTES, 0x1ED4);
	public final StructField m_iScore = new StructField(this, 64 * Integer.BYTES, 0x1FD8);
	public final StructField m_iCompetitiveRanking = new StructField(this, 64 * Integer.BYTES, 0x20DC);
	public final StructField m_iCompetitiveWins = new StructField(this, 64 * Integer.BYTES, 0x21E0);
	public final StructField m_iCompTeammateColor = new StructField(this, 64 * Integer.BYTES, 0x22E4);
	public final StructField m_bControllingBot = new StructField(this, 64 * Integer.BYTES, 0x23E8);
	public final StructField m_iControlledPlayer = new StructField(this, 64 * Integer.BYTES, 0x242C);
	public final StructField m_iControlledByPlayer = new StructField(this, 64 * Integer.BYTES, 0x2530);
	public final StructField m_iBotDifficulty = new StructField(this, 64 * Integer.BYTES, 0x46B4);
	public final StructField m_szClan = new StructField(this, 64 * Integer.BYTES, 0x47B8);
	public final StructField m_iTotalCashSpent = new StructField(this, 64 * Integer.BYTES, 0x4BC8);
	public final StructField m_iCashSpentThisRound = new StructField(this, 64 * Integer.BYTES, 0x4CCC);
	public final StructField m_nEndMatchNextMapVotes = new StructField(this, 64 * Integer.BYTES, 0x4DD0);
	public final StructField m_bEndMatchNextMapAllVoted = new StructField(this, Integer.BYTES, 0x4ED4);
	public final StructField m_nActiveCoinRank = new StructField(this, 64 * Integer.BYTES, 0x4ED8);
	public final StructField m_nMusicID = new StructField(this, 64 * Integer.BYTES, 0x4FDC);
	public final StructField m_nPersonaDataPublicLevel = new StructField(this, 64 * Integer.BYTES, 0x5124);
	public final StructField m_nPersonaDataPublicCommendsLeader = new StructField(this, 64 * Integer.BYTES, 0x5228);
	public final StructField m_nPersonaDataPublicCommendsTeacher = new StructField(this, 64 * Integer.BYTES, 0x532C);
	public final StructField m_nPersonaDataPublicCommendsFriendly = new StructField(this, 64 * Integer.BYTES, 0x5430);

}
