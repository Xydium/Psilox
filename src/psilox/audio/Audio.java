package psilox.audio;

import java.util.HashMap;
import java.util.Map;

import psilox.utils.Log;

/**
 * Audio utility class employing the TinySound library
 * to statically load and play sound and music files.
 * 
 * @author Xydium
 */
public class Audio {

	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static Map<String, Music> music = new HashMap<String, Music>();
	
	public static void init() { 
		TinySound.init();
	}
	
	public static void shutdown() {
		TinySound.shutdown();
	}
	
	/**
	 * Loads a sound file from the given path and stores it
	 * under the name provided.
	 * 
	 * @param name
	 * @param path
	 */
	public static void addSound(String name, String path) {
		if(!sounds.containsKey(name))
			sounds.put(name, TinySound.loadSound(path));
		Log.internal("Added audio with name: " + name + " and path: " + path);
	}
	
	/**
	 * Loads a music file from the given path and stores it
	 * under the name provided.
	 * 		
	 * @param name
	 * @param path
	 */
	public static void addMusic(String name, String path) {
		if(!music.containsKey(name)) {
			music.put(name, TinySound.loadMusic(path));
			Log.internal("Added Music with name: " + name + " and path:" + path);
		}
	}
	
	/**
	 * Removes a sound that was previously loaded under
	 * the given name.
	 * 
	 * @param name
	 */
	public static void removeSound(String name) {
		sounds.remove(name);
		Log.internal("Removed sound with name: " + name);
	}
	
	/**
	 * Removes a sound that was previously loaded under
	 * the given name.
	 * 
	 * @param name
	 */
	public static void removeMusic(String name) {
		music.remove(name);
		Log.internal("Removed music with name: " + name);
	}
	
	/**
	 * Plays a sound that was previously loaded under
	 * the given name with the volume specified as a
	 * decimal between zero and one.
	 * 
	 * @param name
	 * @param vol
	 */
	public static void playSound(String name, double vol) {
		sounds.get(name).play(vol);
	}
	
	/**
	 * Plays a sound that was previously loaded under
	 * the given name at full volume (1.0).
	 * 
	 * @param name
	 */
	public static void playSound(String name) {
		playSound(name, 1.0);
	}
	
	/**
	 * Stops a sound that is currently playing.
	 * 
	 * @param name
	 */
	public static void stopSound(String name) {
		sounds.get(name).stop();
	}
	
	/**
	 * Plays music that was previously loaded under
	 * the given name with the volume specified as a
	 * decimal between zero and one, and loops if requested.
	 * 
	 * @param name
	 * @param vol
	 * @param loop
	 */
	private static void playMusic(String name, double vol, boolean loop) {
		Music m = music.get(name);
		if(!m.playing()) m.play(loop, vol);
	}

	/**
	 * Plays music that was previously loaded under
	 * the given name with full volume and without looping.
	 * 
	 * @param name
	 */
	public static void playMusic(String name) {
		playMusic(name, 1.0, false);
	}
	
	/**
	 * Plays music that was previously loaded under
	 * the given name with specified volume and without looping.
	 * 
	 * @param name
	 * @param volume
	 */
	public static void playMusic(String name, double volume) {
		playMusic(name, volume, false);
	}
	
	/**
	 * Plays music that was previously loaded under
	 * the given name with full volume and looping.
	 * 
	 * @param name
	 */
	public static void loopMusic(String name) {
		playMusic(name, 1.0, true);
	}
	
	/**
	 * Plays music that was previously loaded under
	 * the given name with specified volume and looping.
	 * 
	 * @param name
	 * @param vol
	 */
	public static void loopMusic(String name, double vol) {
		playMusic(name, vol, true);
	}
	
	/**
	 * Stops the music previously loaded with the
	 * given name if it is playing.
	 * 
	 * @param name
	 */
	public static void stopMusic(String name) {
		music.get(name).stop();
	}
	
	/**
	 * Returns whether a previously loaded music object
	 * with the given name is currently playing.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isMusicPlaying(String name) {
		return music.get(name).playing();
	}
	
}
