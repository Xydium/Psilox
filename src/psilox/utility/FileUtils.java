package psilox.utility;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

	private FileUtils() {
	}
	
	public static String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream("/" + file)));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static Font loadFont(String font) {
		try {
			InputStream is = FileUtils.class.getResourceAsStream("/" + font);
			return Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			Log.error("Failed to load font at %s. Using Verdana 24.", "/" + font);
			Log.error(e);
			return new Font("Verdana", Font.PLAIN, 24);
		}
	}
	
}
