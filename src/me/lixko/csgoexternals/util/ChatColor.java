package me.lixko.csgoexternals.util;

import java.util.HashMap;

public enum ChatColor {
	BLACK('0', 0x000000FF),
	DARK_BLUE('1', 0x0000AAFF),
	DARK_GREEN('2', 0x00AA00FF),
	DARK_AQUA('3', 0x00AAAAFF),
	DARK_RED('4', 0xAA0000FF),
	DARK_PURPLE('5', 0xAA00AAFF),
	GOLD('6', 0xFFAA00FF),
	GRAY('7', 0xAAAAAAFF),
	DARK_GRAY('8', 0x555555FF),
	BLUE('9', 0x5555FFFF),
	GREEN('a', 0x55FF55FF),
	AQUA('b', 0x55FFFFFF),
	RED('c', 0xFF5555FF),
	LIGHT_PURPLE('d', 0xFF55FFFF),
	YELLOW('e', 0xFFFF55FF),
	WHITE('f', 0xFFFFFFFF),
	CTCHAT('f', 0.878f, 0.686f, 0.337f, 1f),
	TCHAT('f', 0.54f, 0.72f, 1.0f, 1f),
	BOLD('l', -1),
	STRIKETHROUGH('m', -1),
	UNDERLINE('u', -1),
	PLAIN('n', -1),
	ITALIC('o', -1),
	SMALL('p', -1),
	MEDIUM('s', -1),
	LARGE('t', -1),
	RESET('r', -1);

	public char code;
	public float[] color = new float[4];
	public static HashMap<Short, ChatColor> cmap = new HashMap<Short, ChatColor>();
	public static char colorChar = '\247';

	private ChatColor(char value, int color) {
		this.code = value;
		if (color == -1) {
			this.color = new float[] { -1, -1, -1, -1 };
			return;
		}
		this.color[0] = (float) ((color >> 24) & 0xFF) / 255f;
		this.color[1] = (float) ((color >> 16) & 0xFF) / 255f;
		this.color[2] = (float) ((color >> 8) & 0xFF) / 255f;
		this.color[3] = (float) ((color >> 0) & 0xFF) / 255f;
	}

	private ChatColor(char value, float r, float g, float b, float a) {
		this.code = value;
		this.color[0] = r;
		this.color[1] = g;
		this.color[2] = b;
		this.color[3] = a;
	}

	static {
		for (ChatColor col : values()) {
			cmap.put((short) col.code, col);
		}
	}

	public static String stripColors(String str) {
		String res = "";
		boolean wascolor = false;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == colorChar) {
				if (wascolor)
					res += colorChar;
				else
					wascolor = true;
			} else if (wascolor) {
				wascolor = false;
			} else {
				res += str.charAt(i);
			}
		}
		return res;
	}

	public static ChatColor getChatColor(char code) {
		for (ChatColor e : ChatColor.values()) {
			if (e.code == code)
				return e;
		}
		return null;
		// return cmap.get(color);
	}

	public String toString() {
		return ChatColor.colorChar + "" + this.code;
	}

	public float[] getColor() {
		if (this == ChatColor.RESET)
			return convertColor(DrawUtils.theme.textColor);
		return this.color;
	}

	public static float[] convertColor(int color) {
		return new float[] { ((color >> 24) & 0xFF) / 255f, ((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color >> 0) & 0xFF) / 255f };
	}

	public static float[] convertColor(float[] color) {
		return color;
	}

}
