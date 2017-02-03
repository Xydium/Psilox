package com.xydium.psilox.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {

	private String[] lines;
	
	private Data(String[] lines) {
		this.lines = lines;
	}
	
	public String getLine(int line) {
		return lines[line];
	}
	
	public String[] getLines() {
		return lines;
	}
	
	public static Data load(String filepath) {
		try {
			URL loaded = Data.class.getClassLoader().getResource(filepath); 
			if(loaded == null) throw new FileNotFoundException();
			Scanner sc = new Scanner(new File(loaded.getFile()));
			ArrayList<String> lines = new ArrayList<String>();
			while(sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			Data data = new Data(lines.toArray(new String[lines.size()]));
			sc.close();
			return data;
		} catch (Exception e) { Log.error("Failure to load %s. No such resource.", filepath); }
		return null;
	}
	
}
