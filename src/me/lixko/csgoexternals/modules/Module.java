package me.lixko.csgoexternals.modules;

import java.util.TreeMap;

import com.sun.jna.platform.unix.X11.KeySym;

import me.lixko.csgoexternals.Client;
import me.lixko.csgoexternals.util.StringFormat;

public class Module {

	private String moduleName;
	private int moduleBind;
	private boolean isToggled;
	private boolean isToggleable = true;

	public Module() {
		// Module name = class name
		this.moduleName = this.getClass().getSimpleName();
	}

	public String getName() {
		return this.moduleName;
	}

	public int getBind() {
		return this.moduleBind;
	}

	public boolean isToggled() {
		return this.isToggled;
	}

	public boolean isToggleable() {
		return this.isToggleable;
	}

	public void setToggle(boolean shouldToggle) {
		if (this.isToggleable) {

			if (shouldToggle) {
				if (this.isToggled) {
					StringFormat.modtoggle(this.getName(), 3);
				} else {
					this.onEnable();
					Client.theClient.moduleManager.setToggle(this.getName(), true);
					this.isToggled = true;
					StringFormat.modtoggle(this.getName(), 1);
				}
			} else {
				if (!this.isToggled) {
					StringFormat.modtoggle(this.getName(), 4);
				} else {
					this.onDisable();
					Client.theClient.moduleManager.setToggle(this.getName(), false);
					this.isToggled = false;
					StringFormat.modtoggle(this.getName(), 2);
				}
			}
		} else {
			this.onToggle();
		}
	}

	public void toggleModule() {
		this.setToggle(!this.isToggled());
	}

	public void setToggleable(boolean newValue) {
		this.isToggleable = newValue;
	}

	public void onToggle() {
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	public boolean onCommand(String command) {
		System.out.println("Executing " + this.getName() + " command: " + command);
		return false;
	}

	public void onRegister() {
	}

	public void onLoop() {
	}

	public void onUIRender() {
	}

	public void onPlayerRightClick() {
	}

	public void onModuleReset(String argstring) {
	}

	public void onDisconnect() {
	}

	public void onKeyPress(KeySym key) {
	}

	public void onEngineLoaded() {
	}

	public void onWorldRender() {
	}
	
	public void setStatusLabel(TreeMap<Integer, String> map) {
	}

}
