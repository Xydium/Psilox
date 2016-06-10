package com.xydium.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class InputMap 
{

	private HashMap<String, ArrayList<Integer>> inputMap;
	
	public InputMap()
	{
		inputMap = new HashMap<String, ArrayList<Integer>>();
	}
	
	public InputMap(String mapPath) {
		this();
		loadMap(mapPath);
	}
	
	public void addKey(String name, int... keys) 
	{
		ArrayList<Integer> keyConns = new ArrayList<Integer>();
		for(int k : keys) 
		{
			keyConns.add(k);
		}
		inputMap.put(name, keyConns);
	}
	
	public boolean actionDown(String key) 
	{
		ArrayList<Integer> keys = inputMap.get(key);
		for(int k : keys) 
		{
			if(Input.keyDown(k)) 
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean actionTap(String key)
	{
		ArrayList<Integer> keys = inputMap.get(key);
		for(int k : keys)
		{
			if(Input.keyTap(k)) 
			{
				return true;
			}
		}
		return false;
	}
	
	private void loadMap(String mapPath) {
		try {
			Scanner sc = new Scanner(new File(getClass().getClassLoader().getResource(mapPath).getFile()));
			String action;
			String[] keys;
			int[] keyCodes;
			int splitIndex;
			while(sc.hasNextLine()) {
				action = sc.nextLine();
				if(action.equals("") || action.charAt(0) == '#') continue;
				splitIndex = action.indexOf('=');
				keys = action.substring(splitIndex + 1).split(",");
				keyCodes = new int[keys.length];
				for(int k = 0; k < keys.length; k++) {
					try {
						keyCodes[k] = Key.class.getField(keys[k]).getInt(null);
					} catch(Exception e) { 
						Log.error(String.format("Input map %s is formatted improperly.", mapPath));
						Log.error(e);
					}
				}
				addKey(action.substring(0, splitIndex), keyCodes);
			}
		} catch(Exception e) { Log.error("No such input map: " + mapPath);}
	}
	
}
