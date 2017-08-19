package me.lixko.csgoexternals;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import me.lixko.csgoexternals.util.StringFormat;

public class Client {
	public boolean isInitialized = false;
	public boolean isRunning = false;
	public ModuleManager moduleManager;
	public EventHandler eventHandler;
	public static final Client theClient = new Client();
	public StringFormat stringFormat;
	public CommandManager commandManager;
	public ConfigManager configManager;
	public KeyboardHandler keyboardHandler;

	public Gson gson;
	public Gson nicegson;
	public JsonParser jsonParser;
	public ScriptEngine jsengine;
	public Invocable jsinvocable;
	public boolean jsinitialized;

	public Client startClient() {
		this.moduleManager = new ModuleManager();
		this.eventHandler = new EventHandler();
		this.stringFormat = new StringFormat();
		this.commandManager = new CommandManager();
		this.configManager = new ConfigManager();
		this.keyboardHandler = new KeyboardHandler();

		this.nicegson = new GsonBuilder().setPrettyPrinting().create();
		this.gson = new Gson();

		this.isInitialized = true;
		this.isRunning = true;
		return this;
	}

	public void restartClient() {
		this.nicegson = null;
		this.gson = null;
		this.stringFormat = null;
		this.configManager = null;
		this.moduleManager = null;
		this.eventHandler = null;
		this.commandManager = null;
		this.jsengine = null;
		this.jsinvocable = null;
		this.keyboardHandler = null;
		startClient();
	}

	public void startJS() {
		this.jsengine = new ScriptEngineManager().getEngineByName("nashorn");
		this.jsinvocable = (Invocable) Client.theClient.jsengine;
		this.jsengine.put("Client", this);
		this.jsengine.put("console", this.stringFormat);
		this.jsinitialized = false;
		try {
			this.jsengine.eval(new FileReader(new File(ConfigManager.formatPath("mainjs/main.js"))));
			// this.jsengine.eval(new FileReader(new
			// File(this.configManager.formatPath("mainjs/eventhandler.js"))));
			// this.jsengine.eval(new FileReader(new
			// File(this.configManager.formatPath("mainjs/moduledef.js"))));
			for (File file : (new File(ConfigManager.formatPath("mods"))).listFiles())
				if (!file.isDirectory())
					this.jsengine.eval(new FileReader(file));
			// TODO: JavaScript engine support
			this.jsinitialized = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void restartJS() {
		this.jsengine = null;
		this.jsinvocable = null;
		startJS();
	}

	public void shutdownClient() {
		Client.theClient.eventHandler.onClientShutdown();
		this.isRunning = false;
	}

}
