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
	private Map<Integer, Integer> tbs_scoreboardPlayersT = new HashMap<Integer, Integer>(); // to
																							// be
																							// sorted
	private Map<Integer, Integer> tbs_scoreboardPlayersCT = new HashMap<Integer, Integer>(); // to
																								// be
																								// sorted
	private Map<Integer, Integer> scoreboardPlayersT = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> scoreboardPlayersCT = new HashMap<Integer, Integer>();

	boolean shouldDraw = false;

	private final int SCOREBOARD_PLAYER_HEIGHT = 29;

	Thread updateLoop = new Thread(new Runnable() {
		@Override
		public void run() {
			while (Client.theClient.isRunning) {
				try {
					Thread.sleep(3);
					shouldDraw = XKeySym.isPressed(XKeySym.XK_Tab);
					if (!needsDataUpdate)
						continue;

					if (Offsets.m_dwPlayerResourcesPointer == 0)
						continue;
					if (Offsets.m_dwPlayerResources == 0)
						continue;
					Engine.clientModule().read(Offsets.m_dwPlayerResources, resbuf.size(), resbuf);

					byte[] m_bConnected = res.m_bConnected.getByteArray();
					int[] m_iTeam = res.m_iTeam.getIntArray();
					int[] m_iScore = res.m_iScore.getIntArray();

					tbs_scoreboardPlayersT.clear();
					tbs_scoreboardPlayersCT.clear();
					for (int i = 0; i < 63; i++) {
						if (m_bConnected[i] == 0)
							continue;
						if (m_iTeam[i] == 2)
							tbs_scoreboardPlayersT.put(i, m_iScore[i]);
						if (m_iTeam[i] == 3)
							tbs_scoreboardPlayersCT.put(i, m_iScore[i]);
					}

					scoreboardPlayersT = StringFormat.sortByValueReverse(tbs_scoreboardPlayersT, true);
					scoreboardPlayersCT = StringFormat.sortByValueReverse(tbs_scoreboardPlayersCT, true);

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

		DrawUtils.textRenderer = DrawUtils.theme.textRendererLarge;
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
		DrawUtils.setTextColor(0.54f, 0.72f, 1.0f);
		for (Map.Entry<Integer, Integer> entry : scoreboardPlayersCT.entrySet()) {
			drawEntity(entry.getKey(), DrawUtils.getScreenHeight() - ctyoffset - iter * SCOREBOARD_PLAYER_HEIGHT);
			iter++;
		}

		iter = 0;
		DrawUtils.setTextColor(0.878f, 0.686f, 0.337f);
		for (Map.Entry<Integer, Integer> entry : scoreboardPlayersT.entrySet()) {
			drawEntity(entry.getKey(), DrawUtils.getScreenHeight() - tyoffset - iter * SCOREBOARD_PLAYER_HEIGHT);
			iter++;
		}

		DrawUtils.textRenderer = DrawUtils.theme.textRenderer;
		this.needsDataUpdate = true;
	}

	@Override
	public void onEngineLoaded() {
		res.setSource(resbuf);
		updateLoop.start();
	}

	private void drawEntity(int resid, int y) {
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 4 * 3, y, resid + "");
	}

}
