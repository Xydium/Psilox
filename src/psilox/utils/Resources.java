package psilox.utils;

import java.util.HashMap;

import psilox.graphics.Shader;
import psilox.graphics.Texture;

public class Resources {

	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private static HashMap<String, Shader> shaders = new HashMap<String, Shader>();
	
	public static Texture getTexture(String path) {
		Texture t;
		if(textures.containsKey(path)) {
			t = textures.get(path);
		} else {
			t = new Texture(path);
			textures.put(path, t);
		}
		return t;
	}
	
	public static void addTexture(String name, Texture t) {
		textures.put(name, t);
	}
	
	public static Shader getShader(String path) {
		Shader s;
		if(shaders.containsKey(path)) {
			s = shaders.get(path);
		} else {
			s = new Shader(path);
			shaders.put(path, s);
		}
		return s;
	}
	
	public static void addShader(String name, Shader s) {
		shaders.put(name, s);
	}
	
}
