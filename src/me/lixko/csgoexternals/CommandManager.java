package me.lixko.csgoexternals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.gson.Gson;

import me.lixko.csgoexternals.util.ChatColor;
import me.lixko.csgoexternals.util.FileUtil;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.XKeySym;

public class CommandManager {
	public ArrayList<String> sentCommands = new ArrayList<String>();

	// modulename, command
	public HashMap<String, String> registeredCommands = new HashMap<String, String>();
	// command, helpstring
	public HashMap<String, String> registeredCommandsHelp = new HashMap<String, String>();

	CommandManager() {
	}

	public String[] parseArgs(String[] commandarr) {
		return Arrays.copyOfRange(commandarr, 1, commandarr.length);
	}

	public void registerCommandHelp(String command, String helpstring) {
		registeredCommandsHelp.put(command, helpstring);
	}

	public void registerCommand(String modulename, String command) {
		registeredCommands.put(modulename, command);
	}

	public boolean isCommandDefined(String modulename, String command) {
		return registeredCommands.get(modulename).equals(command);
	}

	public boolean isCommandHelpDefined(String command) {
		return registeredCommandsHelp.containsKey(command);
	}

	public void processCommand(String command) {
		boolean executed = executeCommand(command);

		if (executed) {
			System.out.println("[LixkoPack] Executed command: " + command);
			// Minecraft.getMinecraft().thePlayer.addChatMessage(new
			// ChatComponentText(Client.theClient.stringFormat.stringprefix +
			// ChatColor.GREEN + "Egzekjutit!"));
		} else {
			StringFormat.unknownCommand(command, "");
		}
	}

	public boolean executeCommand(String command) {
		if (command.length() < 1) {
			return false;
		}

		String argstring = "";
		String[] args = {};

		if (command.contains(" ")) {
			String[] commandarr = command.split("\\s+");
			args = parseArgs(commandarr);
			argstring = command.replace(commandarr[0] + " ", "");
			command = commandarr[0];
			// System.out.println("comm: " + command + ", argstring: " +
			// argstring);
		}

		if (command.equalsIgnoreCase("ping")) {
			StringFormat.sendmsg("Pong!");
			return true;
		} else if (command.equalsIgnoreCase("chat")) {
			if (args.length > 0) {
				if (argstring != "") {
					// sendChatMessage(argstring);
				}
			} else {
				StringFormat.notEnoughArguments("chat", "[text]");
			}

			return true;
		} else if (command.equalsIgnoreCase("echo")) {
			if (args.length > 0) {
				if (argstring != "") {
					StringFormat.msg(argstring);
				}
			}
			return true;
		} else if (command.equalsIgnoreCase("execjs")) {
			if (args.length > 0) {
				if (argstring != "") {
					File f = new File(FileUtil.formatPath("scripts/" + args[0]));

					if (f.exists() && !f.isDirectory()) {
						ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
						try {
							engine.eval(new FileReader(f));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (ScriptException e) {
							e.printStackTrace();
						}

						Invocable invocable = (Invocable) engine;
						Object result = null;

						try {
							if (args.length > 1) {
								result = invocable.invokeFunction(args[1], Client.theClient, argstring, args);
							} else {
								result = invocable.invokeFunction("default", Client.theClient);
							}
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
							if (args.length > 1)
								StringFormat.error(ChatColor.RED + "Function " + ChatColor.GRAY + ChatColor.ITALIC + args[1] + ChatColor.RESET + ChatColor.RED + " was not found in " + ChatColor.YELLOW + ChatColor.ITALIC + args[0] + ChatColor.RESET + "!");
							else
								StringFormat.error(ChatColor.RED + "Function " + ChatColor.GRAY + ChatColor.ITALIC + "default" + ChatColor.RESET + ChatColor.RED + " was not found in " + ChatColor.YELLOW + ChatColor.ITALIC + args[0] + ChatColor.RESET + "!");
						} catch (ScriptException e) {
							StringFormat.msg(e.getMessage());
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
							StringFormat.msg(e.getMessage());
						}

						// Minecraft.getMinecraft().thePlayer.motionY = 1.0F;
						if (result != null) {
							StringFormat.msg(ChatColor.YELLOW + "Output type: " + ChatColor.GRAY + ChatColor.ITALIC + result.getClass().getSimpleName() + " - " + result.getClass().getName());

							if (result instanceof String) {
								StringFormat.msg((String) result);
							} else if (result instanceof Boolean) {
								Boolean out = (Boolean) result;

								if (out) {
									StringFormat.msg(ChatColor.GREEN + "true");
								} else {
									StringFormat.msg(ChatColor.DARK_RED + "false");
								}
							} else {
								try {
									Gson gson = new Gson();
									String json = gson.toJson(result);
									StringFormat.msg(json);
								} catch (Exception ex) {
									ex.printStackTrace();
									StringFormat.error(ChatColor.DARK_RED + "Failed to parse JS result!");
									StringFormat.msg(result.toString());
								}
							}
						}
					} else {
						StringFormat.error(ChatColor.RED + "Script file " + ChatColor.YELLOW + ChatColor.ITALIC + args[0] + ChatColor.RED + " was not found!");
					}
				}
			} else {
				StringFormat.notEnoughArguments("execjs", "(file) (function)");
			}

			return true;
		} else if (command.equalsIgnoreCase("exec")) {
			if (args.length == 1) {
				if (argstring != "") {
					File f = new File(FileUtil.formatPath("scripts/" + args[0]));

					if (f.exists() && !f.isDirectory()) {
						String line = "";
						BufferedReader reader;

						try {
							reader = new BufferedReader(new FileReader(f));

							while ((line = reader.readLine()) != null) {
								if (line.startsWith("exec")) {
									StringFormat.error(ChatColor.RED + "Detected loop in script " + ChatColor.YELLOW + ChatColor.ITALIC + args[0] + ChatColor.RESET + ChatColor.RED + "!");
								} else if (!line.startsWith("#")) {
									this.executeCommand(line);
								}
							}

							System.out.println("Executing script " + args[0] + "!");
						} catch (IOException e) {
							StringFormat.error(ChatColor.RED + "Error in script " + ChatColor.YELLOW + ChatColor.ITALIC + args[0] + ChatColor.RESET + ChatColor.RED + ": " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						StringFormat.error(StringFormat.stringprefix + ChatColor.RED + "Script file " + ChatColor.YELLOW + ChatColor.ITALIC + args[0] + ChatColor.RESET + ChatColor.RED + " was not found!");
						System.out.println("[LixkoPack] Script not found at " + f.getAbsolutePath());
					}
				}
			} else {
				StringFormat.notEnoughArguments(command, "(file)");
			}

			return true;
		} else if (command.equalsIgnoreCase("bind")) {
			/*
			 * Command: bind Args: action, key, command Example: bind add K
			 * sprint toggle
			 */
			if (args.length > 2) {
				String boundcommand = argstring.replace(args[0] + " " + args[1] + " ", "");
				System.out.println(args[1]);
				int bindkey = 0;
				try {
					// bindkey =
					// X11.INSTANCE.XStringToKeysym(args[1]).intValue();
					bindkey = XKeySym.find(args[1]);
				} catch (NullPointerException ex) {

				}

				if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {
					if (bindkey < 1) {
						StringFormat.error(ChatColor.RED + "Invalid key " + ChatColor.GRAY + ChatColor.ITALIC + args[1] + ChatColor.RED + "!");
					} else {
						Client.theClient.configManager.unregisterKeybind(bindkey);
						StringFormat.confchange("Bound command on key " + ChatColor.YELLOW + ChatColor.ITALIC + args[1] + ChatColor.RESET + ChatColor.GREEN + " was removed!");
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					if (bindkey < 1) {
						StringFormat.error(ChatColor.RED + "Invalid key " + ChatColor.GRAY + ChatColor.ITALIC + args[1] + ChatColor.RED + "!");
					} else {
						Client.theClient.configManager.registerKeybind(bindkey, boundcommand);
						StringFormat.confchange("Command (" + ChatColor.YELLOW + ChatColor.ITALIC + boundcommand + ChatColor.RESET + ChatColor.GREEN + ") was " + ChatColor.GREEN + "bound to key " + ChatColor.YELLOW + ChatColor.ITALIC + args[1] + ChatColor.RESET + ChatColor.GREEN + "!");
					}
				} else {
					StringFormat.notEnoughArguments(command, "(del|delete|set) (key) [command]");
				}
			} else if (args.length > 1) {
				if (args[0].equalsIgnoreCase("set")) {
					StringFormat.notEnoughArguments(command, "(set) (key) [command]");
				} else {
					StringFormat.notEnoughArguments(command, "(del|delete|set) (key) [command]");
				}
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("set")) {
					StringFormat.notEnoughArguments(command, "set (key) (command)");
				} else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {
					StringFormat.notEnoughArguments(command, "del|delete (key)");
				} else if (args[0].equalsIgnoreCase("list")) {
					StringFormat.msg(ChatColor.YELLOW + "Binds:");
					for (Map.Entry<String, String> entry : Client.theClient.configManager.config.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						if (key.startsWith("keybinds.key.")) {
							String bkey = key.substring("keybinds.key.".length());
							if (StringFormat.isInteger(bkey)) {
								StringFormat.dirmsg(ChatColor.GOLD + Client.theClient.xlib.XKeycodeToKeysym(Client.theClient.dpy, (byte) (Integer.parseInt(bkey)), 0) + ": " + ChatColor.GRAY + value);
							} else {
								StringFormat.warn(ChatColor.GOLD + "Possible config corruption! Key: " + key);
							}
						}
					}
				} else {
					StringFormat.notEnoughArguments(command, "(del|delete|set) (key) [command]");
				}
			} else {
				StringFormat.notEnoughArguments(command, "(del|delete|set|list) (key) [command]");
			}

			return true;
		} else if (command.equalsIgnoreCase("help")) {
			StringFormat.msg(ChatColor.YELLOW + "Available commands: ");
			return true;
		} else if (command.equalsIgnoreCase("reload")) {
			StringFormat.confchange("ModuleManager reloaded!");
			Client.theClient.moduleManager.reloadManager();
			return true;
		} else if (command.equalsIgnoreCase("restart")) {
			StringFormat.confchange("Client restarted!");
			Client.theClient.restartClient();
			return true;
		} else if (command.equalsIgnoreCase("restartjs")) {
			StringFormat.confchange("JavaScript Engine restarted!");
			Client.theClient.restartJS();
			return true;
		} else if (command.equalsIgnoreCase("reloadconfig")) {
			StringFormat.confchange("Config reloaded!");
			Client.theClient.configManager.reload();
			return true;
		} else if (command.equalsIgnoreCase("getcwd")) {
			// StringFormat.msg(Minecraft.getMinecraft().mcDataDir.getAbsolutePath());
			return true;
		} else if (command.equalsIgnoreCase("updateconfig")) {
			Client.theClient.configManager.updateConfig();
			StringFormat.confchange("Config updated!");
			return true;
		} else {
			return Client.theClient.eventHandler.onCommand(command, args, argstring);
		}
	}
}