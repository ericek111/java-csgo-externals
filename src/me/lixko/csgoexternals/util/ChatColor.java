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
	public int color;
	public static HashMap<Short, ChatColor> cmap = new HashMap<Short, ChatColor>();
	public static char colorChar = '\247';

	private ChatColor(char value, int color) {
		this.code = value;
		this.color = color;
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

	public int getColor() {
		if (this == ChatColor.RESET)
			return DrawUtils.theme.textColor;
		return this.color;
	}

}
