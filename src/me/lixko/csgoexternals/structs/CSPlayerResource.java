package me.lixko.csgoexternals.structs;

import me.lixko.csgoexternals.offsets.Netvars;

public class CSPlayerResource extends MemStruct {
	// TODO: Dynamic loading using netvars.
	public final StructField m_szName = new StructField(this, Long.BYTES * 64, 0xF78);
	public final StructField m_iPing = new StructField(this, 64 * Integer.BYTES, 0x1180);
	public final StructField m_iKills = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CPlayerResource.m_iKills);
	public final StructField m_iAssists = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CPlayerResource.m_iAssists);
	public final StructField m_iDeaths = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CPlayerResource.m_iDeaths);
	public final StructField m_bConnected = new StructField(this, 64, (int) Netvars.CPlayerResource.m_bConnected);
	public final StructField m_iTeam = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CPlayerResource.m_iTeam);
	public final StructField m_iPendingTeam = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CPlayerResource.m_iPendingTeam);
	public final StructField m_bAlive = new StructField(this, 64, (int) Netvars.CPlayerResource.m_bAlive);
	public final StructField m_iHealth = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CPlayerResource.m_iHealth);
	public final StructField m_iCoachingTeam = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CPlayerResource.m_iCoachingTeam);

	public final StructField m_iPlayerC4 = new StructField(this, Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iPlayerC4);
	public final StructField m_iPlayerVIP = new StructField(this, Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iPlayerVIP);
	public final StructField m_bombsiteCenterA = new StructField(this, 2 * Float.BYTES, (int) Netvars.CCSPlayerResource.m_bombsiteCenterA);
	public final StructField m_bombsiteCenterB = new StructField(this, 2 * Float.BYTES, (int) Netvars.CCSPlayerResource.m_bombsiteCenterB);
	public final StructField m_bHostageAlive = new StructField(this, 64, (int) Netvars.CCSPlayerResource.m_bHostageAlive);
	public final StructField m_isHostageFollowingSomeone = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_isHostageFollowingSomeone);
	public final StructField m_iHostageEntityIDs = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iHostageEntityIDs);
	public final StructField m_hostageRescueX = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_hostageRescueX);
	public final StructField m_hostageRescueY = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_hostageRescueY);
	public final StructField m_hostageRescueZ = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_hostageRescueZ);
	public final StructField m_iMVPs = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iMVPs);
	public final StructField m_bHasDefuser = new StructField(this, 64, (int) Netvars.CCSPlayerResource.m_bHasDefuser);
	public final StructField m_bHasHelmet = new StructField(this, 64, (int) Netvars.CCSPlayerResource.m_bHasHelmet);
	public final StructField m_iArmor = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iArmor);
	public final StructField m_iScore = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iScore);
	public final StructField m_iCompetitiveRanking = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iCompetitiveRanking);
	public final StructField m_iCompetitiveWins = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iCompetitiveWins);
	public final StructField m_iCompTeammateColor = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iCompTeammateColor);
	public final StructField m_bControllingBot = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_bControllingBot);
	public final StructField m_iControlledPlayer = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iControlledPlayer);
	public final StructField m_iControlledByPlayer = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iControlledByPlayer);
	public final StructField m_iBotDifficulty = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iBotDifficulty);
	public final StructField m_szClan = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_szClan);
	public final StructField m_iTotalCashSpent = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iTotalCashSpent);
	public final StructField m_iCashSpentThisRound = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_iCashSpentThisRound);
	public final StructField m_nEndMatchNextMapVotes = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_nEndMatchNextMapVotes);
	public final StructField m_bEndMatchNextMapAllVoted = new StructField(this, Integer.BYTES, (int) Netvars.CCSPlayerResource.m_bEndMatchNextMapAllVoted);
	public final StructField m_nActiveCoinRank = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_nActiveCoinRank);
	public final StructField m_nMusicID = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_nMusicID);
	public final StructField m_nPersonaDataPublicLevel = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_nPersonaDataPublicLevel);
	public final StructField m_nPersonaDataPublicCommendsLeader = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_nPersonaDataPublicCommendsLeader);
	public final StructField m_nPersonaDataPublicCommendsTeacher = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_nPersonaDataPublicCommendsTeacher);
	public final StructField m_nPersonaDataPublicCommendsFriendly = new StructField(this, 64 * Integer.BYTES, (int) Netvars.CCSPlayerResource.m_nPersonaDataPublicCommendsFriendly);

}
