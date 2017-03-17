package me.lixko.csgoexternals;

import javax.script.ScriptException;

import com.jogamp.opengl.GL2;
import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.modules.Module;
import me.lixko.csgoexternals.util.StringFormat;

public class EventHandler {

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

	public void onLoop() {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			eventModule.onLoop();
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onLoop");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}

	public void onUIRender() {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			try {
				eventModule.onUIRender();	
			} catch(Exception ex) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onUIRender");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
	public void onWorldRender(GL2 gl) {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			try {
				eventModule.onWorldRender(gl);	
			} catch(Exception ex) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			if (Client.theClient.jsinitialized)
				Client.theClient.jsinvocable.invokeMethod(Client.theClient.jsengine.eval("EventHandler"), "onWorldRender");
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
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

}