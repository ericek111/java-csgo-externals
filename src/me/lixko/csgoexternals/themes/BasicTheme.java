package me.lixko.csgoexternals.themes;

import java.awt.Font;

import com.jogamp.opengl.util.awt.TextRenderer;

import me.lixko.csgoexternals.util.FontRenderer;

public class BasicTheme {
	// ==================== COLORS ==================== //
	public static final int stringBackgroundColor = 0x000000C0;
	public static final int textColor = 0x00FFFFFF;

	// ================== DIMENSIONS ================== //
	public static final int[] stringBackgroundPadding = new int[] { 2 };

	public final FontRenderer fontRenderer = new FontRenderer(new TextRenderer(new Font("Verdana", Font.BOLD, 13)));
	public final FontRenderer fontRendererLarge = new FontRenderer(new TextRenderer(new Font("Verdana", Font.BOLD, 18)));
	
	/*static {
		fontRenderer.textRenderer.setSmoothing(true);
		fontRenderer.textRenderer.setSmoothing(true);
	}*/

}
