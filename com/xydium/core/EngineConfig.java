package com.xydium.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class EngineConfig {
	
	private HashMap<String, String> configurations;
	
	public EngineConfig() {
		this.configurations = new HashMap<String, String>();
		loadConfig();
	}
	
	public int getInt(String config) {
		return Integer.parseInt(getString(config));
	}
	
	public float getFloat(String config) {
		return Float.parseFloat(getString(config));
	}
	
	public double getDouble(String config) {
		return Double.parseDouble(getString(config));
	}
	
	public boolean getBoolean(String config) {
		return Boolean.parseBoolean(getString(config));
	}
	
	public String getString(String config) { 
		return configurations.get(config);
	}
	
	private void loadConfig() {
		try {
			Scanner sc = new Scanner(new File(getClass().getClassLoader().getResource("psilox.cfg").getFile()));
			String line;
			int splitIndex;
			while(sc.hasNextLine()) {
				line = sc.nextLine();
				splitIndex = line.indexOf('=');
				configurations.put(line.substring(0, splitIndex), line.substring(splitIndex + 1));
			}
		} catch (FileNotFoundException e) { e.printStackTrace(); Psilox.stop(); }
	}
	
}
