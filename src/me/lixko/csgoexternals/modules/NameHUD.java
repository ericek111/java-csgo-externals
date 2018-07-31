package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Netvars;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.MemoryUtils;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;

public class NameHUD extends Module {

	RankReveal rankreveal;

	long lastspottedtime = 0;
	String lastspottedname = "";
	int lastspottedenti = 0;
	long lastspottedentptr = 0;
	int lastspottedteam = 0;
	int lastspottedhealth = 0;

	@Override
	public void onUIRender() {
		if (!this.isToggled() || Offsets.m_dwLocalPlayer == 0)
			return;
		//System.out.println(DrawUtils.getScreenWidth() / 6);
		if (lastspottedtime + 1500 < System.currentTimeMillis())
			return;
		if (lastspottedenti == 0)
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
		
		rankreveal.drawWeapons(lastspottedentptr, lastspottedenti, DrawUtils.getScreenWidth() / 2 + 100, DrawUtils.getScreenHeight() / 2 - 110, alpha);
	}

	@Override
	public void onLoop() {
		if (!this.isToggled())
			return;
		//if(true) return;
		int inCross = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Offsets.m_iCrosshairIndex);
		if (inCross == lastspottedenti && lastspottedtime + 100 > System.currentTimeMillis()) {
			lastspottedtime = System.currentTimeMillis();
			return;
		}

		if (inCross > 0) {
			long cEnt = MemoryUtils.getEntity(inCross);
			lastspottedentptr = cEnt;
			if (cEnt > 0) {
				int cHealth = Engine.clientModule().readInt(cEnt + Netvars.CBasePlayer.m_iHealth);
				if (cHealth > 0) {
					lastspottedenti = inCross;
					lastspottedname = rankreveal.names[inCross];
					lastspottedtime = System.currentTimeMillis();
					lastspottedteam = Engine.clientModule().readInt(cEnt + Netvars.CBaseEntity.m_iTeamNum);
					lastspottedhealth = Engine.clientModule().readInt(cEnt + Netvars.CBasePlayer.m_iHealth);
				}
			}
		}
	}

	@Override
	public void onEngineLoaded() {
		rankreveal = (RankReveal) Client.theClient.moduleManager.getModule("RankReveal");
	}

}
