package me.lixko.csgoexternals.util;

public class StringFormat {
	public static String stringprefix = ChatColor.GOLD + "[LixkoPack] " + ChatColor.RESET;
	public static MsgPriority priority = MsgPriority.ALL;

	public StringFormat() {
	}

	public static boolean canSend(MsgPriority msgprior) {
		return msgprior.value <= priority.value;
	}

	public static String formatMessage(String msg) {
		return stringprefix + msg;
	}

	public static void sendComponent(String str) {
		System.out.println(str);
	}

	public static void sendmsg(String msg) {
		if (canSend(MsgPriority.EXPLICIT))
			sendComponent(stringprefix + msg);
	}

	public static void dirmsg(String msg) {
		if (canSend(MsgPriority.EXPLICIT))
			sendComponent(msg);
	}

	public static void msg(String msg) {
		if (canSend(MsgPriority.GENERAL))
			sendmsg(msg);
	}

	public static void critical(String msg) {
		if (canSend(MsgPriority.MUTE))
			msg(msg);
	}

	public static void info(String msg) {
		if (canSend(MsgPriority.INFO))
			sendmsg(msg);
	}

	public static void warn(String msg) {
		if (canSend(MsgPriority.WARNINGS))
			msg(msg);
	}

	public static void syntaxerror(String msg) {
		if (canSend(MsgPriority.SYNTAXERRORS))
			msg(msg);
	}

	public static void error(String msg) {
		if (canSend(MsgPriority.ERRORS))
			msg(msg);
	}

	public static void confchange(String msg) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.GREEN + msg);
	}

	public static void confnochange(String msg) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.GREEN + msg);
	}

	/*
	 * public static void status(String msg) {
	 * Minecraft.getMinecraft().thePlayer.sendStatusMessage(new
	 * TextComponentString(msg), false); }
	 * 
	 * public static void status(String msg, boolean actionbar) {
	 * Minecraft.getMinecraft().thePlayer.sendStatusMessage(new
	 * TextComponentString(msg), actionbar); }
	 */

	public static void custom(String msg, MsgPriority priority) {
		if (canSend(MsgPriority.GENERAL))
			msg(msg);
	}

	public static void out(String msg) {
		System.out.println(msg);
	}

	public static void unknownCommand(String command, String hint) {
		if (command.trim() != "")
			syntaxerror(ChatColor.RED + "Unknown command " + ChatColor.GRAY + ChatColor.ITALIC + command + ChatColor.RED + "!" + hint.trim() == "" ? "" : ("Hint: " + ChatColor.GRAY + ChatColor.ITALIC + hint));
		else
			syntaxerror(ChatColor.RED + "Empty command!");
	}

	public static void unknownModuleCommand(String modulename, String command, String hint) {
		if (command.trim() != "")
			syntaxerror(ChatColor.RED + "Unknown command " + ChatColor.GRAY + ChatColor.ITALIC + command + ChatColor.RED + "!" + hint.trim() == "" ? "" : ("Hint: " + ChatColor.GRAY + ChatColor.ITALIC + hint));
		else
			syntaxerror(ChatColor.RED + "Empty command!");
	}

	public static void notEnoughArguments() {
		syntaxerror(ChatColor.RED + ChatColor.RED + "Not enough arguments!");
	}

	public static void notEnoughArguments(int provided, int required) {
		syntaxerror(ChatColor.RED + ChatColor.RED + "Not enough arguments! " + ChatColor.GRAY + "[" + ChatColor.RED + provided + ChatColor.GRAY + " / " + ChatColor.YELLOW + required + ChatColor.GRAY + "]");
	}

	public static void notEnoughArguments(String format) {
		syntaxerror(ChatColor.RED + ChatColor.RED + "Not enough arguments! Hint:" + ChatColor.GRAY + format);
	}

	public static void notEnoughArguments(String cmd, String format) {
		syntaxerror(ChatColor.RED + ChatColor.RED + "Not enough arguments: " + ChatColor.GRAY + ChatColor.ITALIC + cmd + ChatColor.RED + " " + ChatColor.GOLD + format);
	}

	public static void readconf(String what, String value) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.YELLOW + what + ": " + ChatColor.GOLD + ChatColor.ITALIC + value);
	}

	public static void modreadconf(String modulename, String what, String value) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.GRAY + "[" + modulename + "] " + ChatColor.YELLOW + what + ": " + ChatColor.GOLD + ChatColor.ITALIC + value);
	}

	public static void confchange(String what, String from, String to) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.YELLOW + what + ": " + ChatColor.GOLD + ChatColor.ITALIC + to + ChatColor.GREEN + "!");
	}

	public static void confchange(String what, String to) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.YELLOW + what + ChatColor.GREEN + " was changed to " + ChatColor.GOLD + ChatColor.ITALIC + to + ChatColor.GREEN + "!");
	}

	public static void modmsg(String modulename, String msg) {
		if (canSend(MsgPriority.MODULESGENERAL))
			msg(ChatColor.GRAY + "[" + modulename + "] " + msg);
	}

	public static void moderror(String modulename, String msg) {
		if (canSend(MsgPriority.ERRORS))
			msg(ChatColor.GRAY + "[" + modulename + "] " + msg);
	}

	public static void modwarn(String modulename, String msg) {
		if (canSend(MsgPriority.MODULESWARNINGS))
			msg(ChatColor.GRAY + "[" + modulename + "] " + msg);
	}

	public static void modsyntaxerror(String modulename, String msg) {
		if (canSend(MsgPriority.MODULESWARNINGS))
			msg(ChatColor.GRAY + "[" + modulename + "] " + msg);
	}

	public static void moduleAction(String modulename, String msg) {
		if (canSend(MsgPriority.MODULESEXPLICIT))
			msg(ChatColor.GRAY + "[" + modulename + "] " + msg);
	}

	public static void moduleAction(String modulename, String msg, boolean brackets) {
		if (canSend(MsgPriority.MODULESEXPLICIT))
			if (brackets)
				msg(ChatColor.GRAY + "[" + modulename + "]" + ChatColor.RESET + " " + msg);
			else
				msg(ChatColor.YELLOW + ChatColor.ITALIC + modulename + ChatColor.RESET + " " + msg);
	}

	public static void modconfchange(String modulename, String what, String from, String to) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.GRAY + "[" + modulename + "] " + ChatColor.YELLOW + what + ChatColor.GRAY + "(" + from + ")" + ChatColor.GREEN + " was changed to " + ChatColor.GOLD + ChatColor.ITALIC + to + ChatColor.GREEN + "!");
	}

	public static void modconfchange(String modulename, String what, String to) {
		if (canSend(MsgPriority.CONFCHANGE))
			msg(ChatColor.GRAY + "[" + modulename + "] " + ChatColor.YELLOW + what + ChatColor.GREEN + " was changed to " + ChatColor.GOLD + ChatColor.ITALIC + to + ChatColor.GREEN + "!");
	}

	public static void modconfnochange(String modulename, String what, String value) {
		if (canSend(MsgPriority.CONFNOCHANGE))
			msg(ChatColor.GRAY + "[" + modulename + "] " + ChatColor.YELLOW + what + ChatColor.GREEN + " is already " + ChatColor.GOLD + ChatColor.ITALIC + value + ChatColor.GREEN + "!");
	}

	public static void modtoggle(String modulename, int state) {
		if (!canSend(MsgPriority.TOGGLESTATUS))
			return;
		if (state == 1)
			msg(ChatColor.YELLOW + modulename + " " + ChatColor.GREEN + "has been enabled!");
		else if (state == 2)
			msg(ChatColor.YELLOW + modulename + " " + ChatColor.RED + "has been disabled!");
		else if (state == 3)
			msg(ChatColor.YELLOW + modulename + " " + ChatColor.GREEN + "is already enabled!");
		else if (state == 4)
			msg(ChatColor.YELLOW + modulename + " " + ChatColor.RED + "is already disabled!");
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	public static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	public static boolean containsColor(String s) {
		return (s.contains(ChatColor.AQUA) || s.contains(ChatColor.AQUA));
	}

	// convert from UTF-8 -> internal Java String format
	public static String convertFromUTF8(String s) {
		String out = null;

		try {
			out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}

		return out;
	}

	// convert from internal Java String format -> UTF-8
	public static String convertToUTF8(String s) {
		String out = null;

		try {
			out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}

		return out;
	}

}