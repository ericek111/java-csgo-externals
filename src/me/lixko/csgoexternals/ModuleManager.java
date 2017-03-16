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
		registerModule(new Bunnyhop());
		registerModule(new CrosshairDot());
		registerModule(new DisablePP());
		registerModule(new Glow());
		registerModule(new Module());
		registerModule(new RecoilCross());
	}

	public void reloadManager() {
		this.activeModules.clear();
		registerModules();
		System.out.println("[LixkoPack] ModuleManager reloaded!");
	}

	public void registerModule(Module module) {
		this.activeModules.add(module);
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