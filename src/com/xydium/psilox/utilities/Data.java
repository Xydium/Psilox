package com.xydium.psilox.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Data {
	
	private String path;
	private String[] lines;
	
	public Data(String path) {
		this.path = path;
		this.lines = new String[0];
	}
	
	public void save() {
		if(path != null) {
			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileWriter(path));
				for(String s : lines) {
					out.println(s);
				}
				out.close();
			} catch (IOException e) {
				Log.error(e);
			}
		} else {
			Log.error("Cannot save file with null path.");
		}
	}
	
	public void load() {
		if(path != null) {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(path));
				ArrayList<String> lines = new ArrayList<String>();
				String s;
				while((s = in.readLine()) != null) {
					lines.add(s);
				}
				setLines(lines.toArray(new String[lines.size()]));
				in.close();
			} catch (IOException e) {
				Log.error(e);
			}
		} else {
			Log.error("Cannot load file with null path.");
		}
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getLine(int idx) {
		return lines[idx];
	}
	
	public void setLine(int idx, String line) {
		lines[idx] = line;
	}
	
	public String[] getLines() {
		return lines;
	}
	
	public void setLines(String[] lines) {
		this.lines = lines;
	}
	
	public int getLineCount() {
		return lines.length;
	}
	
}
