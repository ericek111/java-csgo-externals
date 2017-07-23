package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;

public class NameHUD extends Module {

	RankReveal rankreveal;

	long lastspottedtime = 0;
	String lastspottedname = "";
	int lastspottedent = 0;
	int lastspottedteam = 0;
	int lastspottedhealth = 0;

	@Override
	public void onUIRender() {
		if (!this.isToggled())
			return;
		if (lastspottedtime + 1500 < System.currentTimeMillis())
			return;
		DrawUtils.setAlign(TextAlign.CENTER);
		if (lastspottedteam == 2)
			DrawUtils.setTextColor(0.878f, 0.686f, 0.337f);
		else
			DrawUtils.setTextColor(0.54f, 0.72f, 1.0f);
		DrawUtils.setAlign(TextAlign.CENTER);
		long elapsed = System.currentTimeMillis() - lastspottedtime;
		elapsed -= 500;
		elapsed = Math.max(elapsed, 0);
		float alpha = 1f - Math.min(1, elapsed / 1000f);
		DrawUtils.disableTextBackgroundColor();
		DrawUtils.setTextBGColorToDefault(alpha * DrawUtils.getDefaultTextBGAlpha());
		DrawUtils.setTextAlpha(alpha);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, DrawUtils.getScreenHeight() / 2 - 50, ChatColor.LARGE + lastspottedname);
		DrawUtils.setTextBGColorToDefault(alpha * DrawUtils.getDefaultTextBGAlpha());
		DrawUtils.setTextColor(0.80f, 0.1f, 0.1f, alpha);
		DrawUtils.drawString(DrawUtils.getScreenWidth() / 2, DrawUtils.getScreenHeight() / 2 - 80, "" + ChatColor.LARGE + lastspottedhealth);
		DrawUtils.enableTextBackgroundColor();
	}

	@Override
	public void onLoop() {
		if (!this.isToggled())
			return;

		int inCross = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Offsets.m_iCrosshairIndex);
		if (inCross == lastspottedent) {
			lastspottedtime = System.currentTimeMillis();
			return;
		}
		if (inCross > 0) {
			long cEnt = Engine.clientModule().readLong(Offsets.m_dwEntityList + Offsets.m_dwEntityLoopDistance * inCross);
			if (cEnt > 0) {
				int cHealth = Engine.clientModule().readInt(cEnt + Offsets.m_iHealth);
				if (cHealth > 0) {
					long nameptr = rankreveal.resbuf.getLong(0xF78 + (inCross) * 8);
					if (nameptr > 0) {
						String name = Engine.clientModule().readString(nameptr, 64);
						lastspottedent = inCross;
						lastspottedname = name;
						lastspottedtime = System.currentTimeMillis();
						lastspottedteam = Engine.clientModule().readInt(cEnt + Offsets.m_iTeamNum);
						lastspottedhealth = Engine.clientModule().readInt(cEnt + Offsets.m_iHealth);
					}
				}

			}
		}
	}

	@Override
	public void onEngineLoaded() {
		rankreveal = (RankReveal) Client.theClient.moduleManager.getModule("RankReveal");
	}

}
