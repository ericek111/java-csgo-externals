package me.lixko.csgoexternals.modules;

import java.util.HashMap;
import java.util.Map;

import com.github.jonatino.misc.MemoryBuffer;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.structs.CSPlayerResource;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;
import me.lixko.csgoexternals.util.XKeySym;

public class RankReveal extends Module {
	boolean needsDataUpdate = false;
	CSPlayerResource res = new CSPlayerResource();
	MemoryBuffer resbuf = new MemoryBuffer(res.size());
	private Map<Integer, Integer> tbs_scoreboardPlayersT = new HashMap<Integer, Integer>(); // to // sorted
	private Map<Integer, Integer> tbs_scoreboardPlayersCT = new HashMap<Integer, Integer>(); // to
	private Map<Integer, Integer> scoreboardPlayersT = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> scoreboardPlayersCT = new HashMap<Integer, Integer>();
	private int lpteamnum;
	boolean shouldDraw = false;
	boolean isCompetitive = false;

	private final int SCOREBOARD_PLAYER_HEIGHT = 29;

	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(3);
					shouldDraw = XKeySym.isPressed(XKeySym.XK_Tab);
					if (!needsDataUpdate || Offsets.m_dwPlayerResourcesPointer == 0 || Offsets.m_dwPlayerResources == 0 || Offsets.m_dwLocalPlayer == 0)
						continue;

					Engine.clientModule().read(Offsets.m_dwPlayerResources, resbuf.size(), resbuf);

					byte[] m_bConnected = res.m_bConnected.getByteArray();
					int[] m_iTeam = res.m_iTeam.getIntArray();
					int[] m_iScore = res.m_iScore.getIntArray();
					int[] m_iCompetitiveRanking = res.m_iCompetitiveRanking.getIntArray();

					tbs_scoreboardPlayersT.clear();
					tbs_scoreboardPlayersCT.clear();
					isCompetitive = false;
					for (int i = 0; i < 63; i++) {
						if (m_bConnected[i] == 0)
							continue;
						if (m_iTeam[i] == 2)
							tbs_scoreboardPlayersT.put(i, m_iScore[i]);
						if (m_iTeam[i] == 3)
							tbs_scoreboardPlayersCT.put(i, m_iScore[i]);
						if (m_iCompetitiveRanking[i] > 0)
							isCompetitive = true;
					}

					scoreboardPlayersT = StringFormat.sortByValueReverse(tbs_scoreboardPlayersT, true);
					scoreboardPlayersCT = StringFormat.sortByValueReverse(tbs_scoreboardPlayersCT, true);

					lpteamnum = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Offsets.m_iTeamNum);

					needsDataUpdate = false;
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
					}
				}
			}
		}
	});

	@Override
	public void onUIRender() {
		if (!Client.theClient.isRunning || this.needsDataUpdate || !shouldDraw)
			return;

		DrawUtils.fontRenderer = DrawUtils.theme.fontRendererLarge;
		DrawUtils.enableStringBackground();
		DrawUtils.setTextColor(0x00FFFFFF);
		DrawUtils.setAlign(TextAlign.LEFT);

		int ctyoffset = 145;
		int tyoffset = 603;
		if (Math.max(scoreboardPlayersCT.size(), scoreboardPlayersT.size()) < 6) {
			ctyoffset = 377;
			tyoffset = 633;
		} else if (Math.max(scoreboardPlayersCT.size(), scoreboardPlayersT.size()) < 9) {
			ctyoffset = 288;
			tyoffset = 633;
		}

		int iter = 0;
		drawHeader(DrawUtils.getScreenHeight() - ctyoffset + SCOREBOARD_PLAYER_HEIGHT);

		for (Map.Entry<Integer, Integer> entry : scoreboardPlayersCT.entrySet()) {
			drawEntity(entry.getKey(), DrawUtils.getScreenHeight() - ctyoffset - iter * SCOREBOARD_PLAYER_HEIGHT, 3);
			iter++;
		}

		iter = 0;
		drawHeader(DrawUtils.getScreenHeight() - tyoffset + SCOREBOARD_PLAYER_HEIGHT);

		for (Map.Entry<Integer, Integer> entry : scoreboardPlayersT.entrySet()) {
			int ypos = DrawUtils.getScreenHeight() - tyoffset - iter * SCOREBOARD_PLAYER_HEIGHT;
			drawEntity(entry.getKey(), ypos, 2);
			iter++;
		}

		DrawUtils.fontRenderer = DrawUtils.theme.fontRenderer;
		this.needsDataUpdate = true;

	}

	@Override
	public void onEngineLoaded() {
		res.setSource(resbuf);
		updateLoop.start();
	}

	private void drawEntity(int resid, int y, int team) {
		DrawUtils.setAlign(TextAlign.RIGHT);
		DrawUtils.fontRenderer = DrawUtils.theme.fontRendererLarge;

		if (res.m_iHealth.getInt(resid * Integer.BYTES) > 0) {
			DrawUtils.setTextColor(0.9f, 0.42f, 0.4f, 1.0f);
			DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 + 8, y, res.m_iHealth.getInt(resid * Integer.BYTES) + "");
		}

		if (res.m_iArmor.getInt(resid * Integer.BYTES) > 0) {
			if (team == 2)
				DrawUtils.setTextColor(0.878f, 0.686f, 0.337f);
			else
				DrawUtils.setTextColor(0.54f, 0.72f, 1.0f);
			String str = res.m_iArmor.getInt(resid * Integer.BYTES) + "";
			DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 - 40, y, str);
			if (res.m_bHasHelmet.getBoolean(resid)) {
				DrawUtils.setLineWidth(3.0f);
				DrawUtils.setColor(0.6f, 0.6f, 0.6f, 0.8f);
				DrawUtils.drawRectangleAroundString(str, DrawUtils.getScreenWidth() / 4 - 40, y);
			}
		}

		if (res.m_iPlayerC4.getInt() == resid && lpteamnum != 2)
			DrawUtils.drawTexture("bomb", DrawUtils.getScreenWidth() / 4 + 60, y - 8, 25, -1);

		if (res.m_bHasDefuser.getBoolean(resid) && lpteamnum != 3)
			DrawUtils.drawTexture("defuser", DrawUtils.getScreenWidth() / 4 + 63, y - 4, 22, -1);

		DrawUtils.fontRenderer = DrawUtils.theme.fontRenderer;
		DrawUtils.setTextColor(0.9f, 0.9f, 0.9f);
		DrawUtils.setAlign(TextAlign.LEFT);

		int com_t = res.m_nPersonaDataPublicCommendsTeacher.getInt(resid * Integer.BYTES);
		int com_l = res.m_nPersonaDataPublicCommendsLeader.getInt(resid * Integer.BYTES);
		int com_f = res.m_nPersonaDataPublicCommendsFriendly.getInt(resid * Integer.BYTES);

		if (isCompetitive) {
			int rank = res.m_iCompetitiveRanking.getInt(resid * Integer.BYTES);
			int wins = res.m_iCompetitiveWins.getInt(resid * Integer.BYTES);
			if (rank > 0 && rank <= DrawUtils.csgoranks.length)
				DrawUtils.drawTexture(DrawUtils.csgoranks[rank - 1], DrawUtils.getScreenWidth() / 4 * 3 - 10, y - 6, 51, -1);
			if (wins > 1)
				DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 * 3 + 43, y + 2, wins + "");
		}

		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 * 3 + (isCompetitive ? 90 : -8), y + 2, (com_t > 0 ? com_t + "" : ""));
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 * 3 + (isCompetitive ? 90 : -8) + 30, y + 2, (com_l > 0 ? com_l + "" : ""));
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 * 3 + (isCompetitive ? 90 : -8) + 60, y + 2, (com_f > 0 ? com_f + "" : ""));

		int cashspent = res.m_iCashSpentThisRound.getInt(resid * Integer.BYTES);
		DrawUtils.setAlign(TextAlign.RIGHT);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 - 93, y + 2, "$" + cashspent);
	}

	private void drawHeader(int y) {
		DrawUtils.fontRenderer = DrawUtils.theme.fontRenderer;
		DrawUtils.setTextColor(1.0f, 0.4f, 0.4f);
		DrawUtils.setAlign(TextAlign.RIGHT);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 + 8, y, "+");
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 - 40, y, "Arm");
		DrawUtils.setAlign(TextAlign.LEFT);

		DrawUtils.setTextColor(0.6f, 0.6f, 0.6f);
		DrawUtils.setColor(0f, 0f, 0f, 0.1f);

		if (isCompetitive)
			DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 * 3 - 8, y, "Rank  Wins");

		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 * 3 + (isCompetitive ? 90 : -8), y, "Tea Lea Fri");

	}

}
