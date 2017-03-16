package me.lixko.csgoexternals;

import java.util.ArrayList;
import java.util.HashMap;

import me.lixko.csgoexternals.modules.*;

public class ModuleManager {
	public ArrayList<Module> activeModules = new ArrayList<Module>();
	public HashMap<String, Boolean> toggledModules = new HashMap<String, Boolean>();

	public ModuleManager() {
		registerModules();
	}

	public void registerModules() {
		/*
		 * registerModule(new ClickGui(Keyboard.KEY_Y)); registerModule(new
		 * Console(Keyboard.KEY_C)); registerModule(new
		 * FastPlace(Keyboard.KEY_PERIOD)); registerModule(new
		 * Flight(Keyboard.KEY_R)); registerModule(new FreeCam(Keyboard.KEY_M));
		 * registerModule(new FullBright(Keyboard.KEY_F)); registerModule(new
		 * Reach(Keyboard.KEY_H)); //registerModule(new
		 * EntityRadar(Keyboard.KEY_I)); registerModule(new
		 * Sneak(Keyboard.KEY_Z)); registerModule(new Sprint(Keyboard.KEY_K));
		 * registerModule(new Step(Keyboard.KEY_P));
		 */
		registerModule(new RecoilCross());
		registerModule(new Bunnyhop());
		registerModule(new DisablePP());
		registerModule(new Glow());
		registerModule(new Module());
	}

	public void reloadManager() {
		this.activeModules.clear();
		registerModules();
		System.out.println("[LixkoPack] ModuleManager reloaded!");
	}

	public void registerModule(Module module) {
		this.activeModules.add(module);
		// System.out.println("[LixkoPack] Registering module " +
		// module.getName());
		module.onRegister();
	}

	public boolean isLoaded(String moduleName) {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			if (eventModule.getName().equals(moduleName)) {
				return true;
			}
		}
		return false;
	}

	public int getKeybind(String moduleName) {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			if (eventModule.getName().equals(moduleName)) {
				return eventModule.getBind();
			}
		}

		return 0;
	}

	public boolean isToggled(String moduleName) {
		return (toggledModules.containsKey(moduleName) ? toggledModules.get(moduleName) : false);
	}

	public void setToggle(String moduleName, Boolean toggled) {
		this.toggledModules.put(moduleName, toggled);
	}

	public Module getModuleByKeybind(int key) {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			if (eventModule.getBind() == key) {
				return eventModule;
			}
		}

		return null;
	}

	public Module getModule(String moduleName) {
		for (Module eventModule : Client.theClient.moduleManager.activeModules) {
			if (eventModule.getName().equals(moduleName)) {
				return eventModule;
			}
		}

		return null;
	}

	public Module getModule(int moduleID) {
		if (moduleID > Client.theClient.moduleManager.activeModules.size())
			return null;
		else
			return Client.theClient.moduleManager.activeModules.get(moduleID);
	}

	public int moduleSize() {
		return Client.theClient.moduleManager.activeModules.size();
	}
}