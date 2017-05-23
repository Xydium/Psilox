package psilox.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Data {

	protected String path;
	protected String[] lines;
	
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
	
	public int find(String contents) {
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].contains(contents)) return i;
		}
		return -1;
	}
	
	public int find(String contents, int start) {
		for(int i = start; i < lines.length; i++) {
			if(lines[i].contains(contents)) return i;
		}
		return -1;
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
	
	public String concatenated(char sep) {
		String res = "";
		for(String s : lines) {
			res += s + sep;
		}
		return res;
	}
	
	public String concatenated(char sep, int start, int end) {
		String res = "";
		for(int i = start; i < end; i++) {
			res += lines[i] + sep;
		}
		return res;
	}
	
}
	
