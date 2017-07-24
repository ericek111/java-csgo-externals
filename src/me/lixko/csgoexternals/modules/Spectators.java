package me.lixko.csgoexternals.modules;

import java.util.ArrayList;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.Engine;
import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.TextAlign;

public class Spectators extends Module {
	RankReveal rankreveal;

	ArrayList<String> spectators = new ArrayList<String>();
	int loopc = 0;

	@Override
	public void onUIRender() {
		if (!this.isToggled())
			return;
		int i = 0;
		DrawUtils.setTextColor(0xBBBBBBFF);
		DrawUtils.setAlign(TextAlign.LEFT);
		for (String name : spectators) {
			DrawUtils.drawString(290, DrawUtils.getScreenHeight() - 50 - i * 15, ChatColor.SMALL + name);
			i++;
		}
	}

	@Override
	public void onLoop() {
		if (!this.isToggled())
			return;
		loopc++;
		if (loopc < 90)
			return;
		loopc = 0;
		spectators.clear();
		int lpobstarget = Engine.clientModule().readInt(Offsets.m_dwLocalPlayer + Offsets.m_hObserverTarget) & 0xFFF - 1;
		for (int i = 1; i < 64; i++) {
			long entptr = Engine.clientModule().readLong(Offsets.m_dwEntityList + Offsets.m_dwEntityLoopDistance * i);
			if (rankreveal.res.m_bAlive.getBoolean(i))
				continue;
			if (entptr == 0 || entptr == Offsets.m_dwLocalPlayer)
				continue;
			int obs = Engine.clientModule().readInt(entptr + Offsets.m_hObserverTarget) & 0xFFF - 1;
			if (obs == 0xFFE)
				continue;
			long targetptr = Engine.clientModule().readLong(Offsets.m_dwEntityList + Offsets.m_dwEntityLoopDistance * obs);
			if (lpobstarget == 0xFFE && targetptr != Offsets.m_dwLocalPlayer)
				continue;
			else if (lpobstarget != obs)
				continue;

			long nameptr = rankreveal.resbuf.getLong(0xF78 + i * 8);
			if (nameptr == 0)
				continue;
			String name = Engine.clientModule().readString(nameptr, 64);
			spectators.add(name);
		}

	}

	@Override
	public void onEngineLoaded() {
		rankreveal = (RankReveal) Client.theClient.moduleManager.getModule("RankReveal");
	}
}
