package me.lixko.csgoexternals;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.lixko.csgoexternals.util.FileUtil;

public class ConfigManager extends FileUtil {
	public static String folderName = "lixkopack";
	public String configpath;
	public String packpath = FileUtil.formatPath();

	public HashMap<String, String> config = new HashMap<String, String>();

	ConfigManager() {
		configpath = FileUtil.formatPath("config.txt");

		/*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			Font r = Font.createFont(Font.TRUETYPE_FONT, new File(FileUtil.formatPath("LiberationMono-Regular.ttf")));
			Font b = Font.createFont(Font.TRUETYPE_FONT, new File(FileUtil.formatPath("LiberationMono-Bold.ttf")));
			Font bi = Font.createFont(Font.TRUETYPE_FONT, new File(FileUtil.formatPath("LiberationMono-BoldItalic.ttf")));
			Font i = Font.createFont(Font.TRUETYPE_FONT, new File(FileUtil.formatPath("LiberationMono-Italic.ttf")));
			ge.registerFont(r);
			ge.registerFont(b);
			ge.registerFont(bi);
			ge.registerFont(i);
			
			DrawUtils.textRenderer = new TextRenderer(b.deriveFont(Font.TRUETYPE_FONT, 16));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}*/
		

		// configfile = readFile(configpath);
		if (!isConfigured()) {
			System.out.println("[LixkoPack] Config not found! Creating one...");
			createDefaults();
			FileUtil.writeConfig(config, configpath);
		} else {
			updateConfig();
		}
	}

	public void setValue(String path, String value) {
		config.put(path, value);
		System.out.println("Setting " + path + " to " + value + "!");
		FileUtil.writeConfig(config, configpath);
	}

	public String getValue(String path) {
		updateConfig();
		return config.get(path);
	}

	public void delValue(String path) {
		config.remove(path);
		FileUtil.writeConfig(config, configpath);
	}

	public void updateConfig() {
		String configjson = FileUtil.readFile(configpath);
		Gson gson = new Gson();
		Type stringStringMap = new TypeToken<HashMap<String, String>>() {
		}.getType();
		// HashMap<String, String> featuresFromJson =
		// gson.fromJson(JSONFeatureSet, new TypeToken<Map<String, String>>()
		// {}.getType());
		HashMap<String, String> map = gson.fromJson(configjson, stringStringMap);
		config = map;
	}

	public void registerKeybind(int key, String action) {
		setValue("keybinds.key." + key, action);
	}

	public void unregisterKeybind(int key) {
		delValue("keybinds.key." + key);
	}

	public String getBoundCommand(int key) {
		if (pathExists("keybinds.key." + key)) {
			return getValue("keybinds.key." + key);
		} else {
			return "";
		}
	}

	public void executeCommandOnKey(int key) {
		String s = getBoundCommand(key);

		if (s != "") {
			Client.theClient.commandManager.processCommand(s);
		}
	}

	public void createDefaults() {
		new File(formatPath("")).mkdirs();
		new File(formatPath("scripts")).mkdirs();
		new File(formatPath("mods")).mkdirs();
	}

	public boolean isConfigured() {
		File f = new File(configpath);

		if (f.exists() && !f.isDirectory()) {
			return true;
		}

		return false;
	}

	public boolean pathExists(String key) {
		updateConfig();
		return config.containsKey(key);
	}

	public void reload() {
		if (!isConfigured()) {
			createDefaults();
			FileUtil.writeConfig(config, configpath);
		} else {
			updateConfig();
		}
	}
}