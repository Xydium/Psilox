package psilox.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataResource extends Data {

	public DataResource(String path) {
		super(path);
	}

	public void save() {
		Log.warning("Attempted to save DataResource file, request ignored.");
	}
	
	public void load() {
		if(path != null) {
			Scanner in = null;
			try {
				in = new Scanner(new File(getClass().getClassLoader().getResource(path).getFile()));
				ArrayList<String> lines = new ArrayList<String>();
				String s;
				while((s = in.nextLine()) != null) {
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
	
}
