package me.lixko.csgoexternals;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.modules.Module;
import me.lixko.csgoexternals.util.DrawUtils;
import me.lixko.csgoexternals.util.ProfilerUtil;
import me.lixko.csgoexternals.util.StringFormat;
import me.lixko.csgoexternals.util.TextAlign;

public class EventHandler {
	
	HashMap<Module, Long> uirendertimes = new HashMap<>();
	HashMap<Module, Long> worldrendertimes = new HashMap<>();
	long rendertime = 0;
	int rtindex = 0;
	public static boolean PROFILER = false;
	
	public EventHandler() {
	}

	public boolean onCommand(String command, String[] args, String argstring) {
		try {
			if (Client.theClient.jsinitialized) {
				Object o = Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onCommand", command, args, argstring);
				if (o instanceof Boolean)
					if ((Boolean) o)
						return true;
			}
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}

		for (Module module : Client.theClient.moduleManager.activeModules) {
			if (module.getName().equalsIgnoreCase(command)) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("toggle")) {
						module.toggleModule();
					} else if (args[0].equalsIgnoreCase("enable")) {
						module.setToggle(true);
					} else if (args[0].equalsIgnoreCase("disable")) {
						module.setToggle(false);
					} else if (args[0].equalsIgnoreCase("reset")) {
						module.onModuleReset(argstring);
					} else {
						if (!module.onCommand(argstring)) {
							StringFormat.unknownCommand(command, "toggle, enable, disable, reset");
						}
					}
				} else {
					StringFormat.notEnoughArguments(command, "[action]");
				}
				return true;
			}
		}
		return false;
	}

	// Gets executed regardless of inGame
	public void onPreLoop() {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			eventModule.onPreLoop();
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onPreLoop");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
	public void onLoop() {
		Client.theClient.moduleManager.cachedStatusText.clear();
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			eventModule.onLoop();
			eventModule.setStatusLabel(Client.theClient.moduleManager.cachedStatusText);
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onLoop");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}

	public void onUIRender() {
		if(PROFILER) rtindex++;
		//ProfilerUtil.start();
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			try {
				if(PROFILER) rendertime = System.nanoTime();
				eventModule.onUIRender();
				if(PROFILER) {
					long oldrt = 0l;
					if(rtindex == 1) oldrt = System.nanoTime() - rendertime;
					else oldrt = uirendertimes.get(eventModule)  + (System.nanoTime() - rendertime);
					uirendertimes.put(eventModule, oldrt);
					//System.out.println(uirendertimes.size());
					if(rtindex == 100) {
						long rt = (uirendertimes.put(eventModule, 0l) / rtindex);
						if(rt > 3000)
							System.out.println("U: " + eventModule.getName() + ": " + rt);
					}
					//ProfilerUtil.measure("UI render of " + eventModule.getName());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onUIRender");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
		/*String str = "";
		for (Map.Entry<Integer, String> entry : Client.theClient.moduleManager.cachedStatusText.entrySet())
			str += str == "" ? entry.getValue() : " + " + entry.getValue();

		DrawUtils.setTextColor(0.0f, 1.0f, 1.0f, 0.8f);
		DrawUtils.setAlign(TextAlign.CENTER);
		DrawUtils.enableStringBackground();
		DrawUtils.drawString(DrawUtils.drawable.getSurfaceWidth() / 2, 15, str);
		DrawUtils.setAlign(TextAlign.LEFT);*/
	}

	public void onWorldRender() {
		//ProfilerUtil.start();
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			try {
				if(PROFILER) rendertime = System.nanoTime();
				eventModule.onWorldRender();
				if(PROFILER) {
					long oldrt = 0;
					if(rtindex == 1) oldrt = System.nanoTime() - rendertime;
					else oldrt = worldrendertimes.get(eventModule) + (System.nanoTime() - rendertime);
					worldrendertimes.put(eventModule, oldrt);
					if(rtindex == 100) {
						long rt = (worldrendertimes.put(eventModule, 0l) / rtindex);
						if(rt > 3000)
							System.out.println("W: " + eventModule.getName() + ": " + rt);
					}
					//ProfilerUtil.measure("World render of " + eventModule.getName());
				}				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onWorldRender");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
		if(PROFILER && rtindex == 100) {
			rtindex = 0;
			System.out.println("================================");
		}
	}

	public void onKeyPress(KeySym key) {
		Client.theClient.configManager.executeCommandOnKey(key.intValue());
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			eventModule.onKeyPress(key);
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onKeyPress", key);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}

	public void onKeyRelease(KeySym key) {
		Client.theClient.configManager.executeCommandOnKeyRelease(key.intValue());
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			eventModule.onKeyRelease(key);
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onKeyRelease", key);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}

	public void onEngineLoaded() {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			eventModule.onEngineLoaded();
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onEngineLoaded");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}

	public void onClientShutdown() {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			eventModule.onClientShutdown();
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onClientShutdown");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}

	}

}