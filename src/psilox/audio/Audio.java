package psilox.audio;

import java.util.HashMap;
import java.util.Map;

import psilox.utils.Log;

public class Audio {

	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static Map<String, Music> music = new HashMap<String, Music>();
	
	public static void init() { 
		TinySound.init();
	}
	
	public static void shutdown() {
		TinySound.shutdown();
	}
	
	public static void addSound(String name, String path) {
		if(!sounds.containsKey(name))
			sounds.put(name, TinySound.loadSound(path));
		Log.internal("Added audio with name: " + name + " and path: " + path);
	}
	
	public static void addMusic(String name, String path) {
		if(!music.containsKey(name)) {
			music.put(name, TinySound.loadMusic(path));
			Log.internal("Added Music with name: " + name + " and path:" + path);
		}
	}
	
	public static void removeSound(String name) {
		sounds.remove(name);
		Log.internal("Removed sound with name: " + name);
	}
	
	public static void removeMusic(String name) {
		music.remove(name);
	}
	
	public static void playSound(String name, double vol) {
		sounds.get(name).play(vol);
	}
	
	public static void playSound(String name) {
		playSound(name, 1.0);
	}
	
	public static void stopSound(String name) {
		sounds.get(name).stop();
	}
	
	private static void playMusic(String name, double vol, boolean loop) {
		Music m = music.get(name);
		if(!m.playing()) m.play(loop, vol);
	}

	public static void playMusic(String name) {
		playMusic(name, 1.0, false);
	}
	
	public static void playMusic(String name, double volume) {
		playMusic(name, volume, false);
	}
	
	public static void loopMusic(String name) {
		playMusic(name, 1.0, true);
	}
	
	public static void loopMusic(String name, double vol) {
		playMusic(name, vol, true);
	}
	
	public static void stopMusic(String name) {
		music.get(name).stop();
	}
	
}
