package me.lixko.csgoexternals.modules;

import me.lixko.csgoexternals.offsets.Offsets;
import me.lixko.csgoexternals.util.DrawUtils;

public class CrosshairDot extends Module {

	private final int sx = (int) (DrawUtils.getScreenWidth() * 0.5f);
	private final int sy = (int) (DrawUtils.getScreenHeight() * 0.5f);

	@Override
	public void onUIRender() {
		if (Offsets.m_dwLocalPlayer < 1)
			return;
		DrawUtils.setColor(0, 0, 0, 150);
		DrawUtils.fillRectanglew(sx - 2, sy - 2, 4, 4);
		DrawUtils.setColor(0x00FFFFFF);
		DrawUtils.fillRectanglew(sx - 1, sy - 1, 2, 2);
	}

}
