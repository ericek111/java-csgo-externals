package me.lixko.csgoexternals.util;

import java.awt.font.LineMetrics;

import com.jogamp.opengl.util.awt.TextRenderer;

public class FontRenderer {

	public TextRenderer textRenderer;
	private float[] charWidth = new float[Character.MAX_VALUE];
	private float[] charHeight = new float[Character.MAX_VALUE];
	private float[] charDescender = new float[Character.MAX_VALUE];

	public FontRenderer(TextRenderer textRenderer) {
		this.textRenderer = textRenderer;
		this.textRenderer.setSmoothing(true);
	}

	public float getStringWidth(String str) {
		char chari;
		float strw = 0;
		for (int i = 0; i < str.length(); i++) {
			chari = str.charAt(i);
			if (chari < 32)
				continue;
			if (charWidth[chari] == 0) {
				try {
					charWidth[chari] = textRenderer.getCharWidth(chari);
				} catch (Throwable e) {
					System.out.println("Can't find width of " + chari);
				}

				if (charWidth[chari] == 0)
					charWidth[chari] = -1;
			}
			strw += Math.max(0f, charWidth[chari]);
		}
		return strw;
	}

	public float getStringHeight(String str) {
		char chari;
		float strh = 0;
		for (int i = 0; i < str.length(); i++) {
			chari = str.charAt(i);

			if (charHeight[chari] == 0) {
				charHeight[chari] = textRenderer.getFont().getLineMetrics(String.valueOf(chari), textRenderer.getFontRenderContext()).getHeight();
				if (charHeight[chari] == 0)
					charHeight[chari] = -1;

			}
			strh = Math.max(strh, charHeight[chari]);
		}
		return strh;
	}

	public float getStringMinDescend(String str) {
		char chari;
		float strh = 0;
		for (int i = 0; i < str.length(); i++) {
			chari = str.charAt(i);

			if (charDescender[chari] == 0) {
				charDescender[chari] = textRenderer.getFont().getLineMetrics(String.valueOf(chari), textRenderer.getFontRenderContext()).getDescent();
			}
			strh = Math.max(strh, charDescender[chari]);
		}
		return strh;
	}

	public LineMetrics getLineMetrics(String str) {
		return textRenderer.getFont().getLineMetrics(str, textRenderer.getFontRenderContext());
	}
}
