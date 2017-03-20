package me.lixko.csgoexternals.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class FileUtil {

	public static String mainpath = me.lixko.csgoexternals.Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

	public static void writeConfig(HashMap<String, String> map, String path) {
		File dir = new File(path);
		new File(dir.getParent()).mkdirs();
		Gson gson = new Gson();
		String json = gson.toJson(map);
		// System.out.println("JSON: " + json);
		writeToFile(json, new File(path));
	}

	public static void writeToFile(String content, File file) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			writer.write(content);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String readFile(String file) {
		String data = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}

			data = sb.toString();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	public JsonElement decodeJson(String jsonText) {
		JsonParser parser = new JsonParser();

		try {
			return parser.parse(jsonText);
		} catch (JsonParseException pe) {
			System.out.println("Error on: " + pe.getMessage());
			System.out.println(pe);
		}

		return null;
	}

	public static String formatPath(String s) {
		// String path = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() +
		// File.separator + Client.theClient.configManager.folderName + (s !=
		// ""? (File.separator + s) : "");
		String path = mainpath + "/" + (s != "" ? (File.separator + s) : "");
		// System.out.println(path);
		path.replace("/", File.separator);
		path.replace("//", File.separator);
		path.replace("\\", File.separator);
		return path;
	}

	public static String formatPath() {
		return formatPath("");
	}

	public static void appendToFile(String content, File file) {
		PrintWriter out = null;

		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}