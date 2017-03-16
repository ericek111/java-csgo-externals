package me.lixko.csgoexternals.themes;

import java.awt.Font;

import com.jogamp.opengl.util.awt.TextRenderer;

public class BasicTheme {
	// ==================== COLORS ==================== //
	public final int stringBackgroundColor = 0x000000C0;
	public final int textColor = 0x00FFFFFF;

	// ================== DIMENSIONS ================== //
	public final int[] stringBackgroundPadding = new int[] { 2 };
	
	// Liberation Mono
	public TextRenderer textRenderer = new TextRenderer(new Font("Lucida Console", Font.BOLD, 13));

}
