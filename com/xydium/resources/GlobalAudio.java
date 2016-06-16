package com.xydium.resources;

import java.util.HashMap;
import java.util.Map;

import com.xydium.utility.Log;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 * A utility class for loading and playing audio files
 * 
 * @author Tim Hornick
 *
 */
public final class GlobalAudio 
{
	private static Map<String, Sound> sounds;
	private static Map<String, Music> music;

	/**
	 * Initializes the GlobalAudio object to properly
	 * load, store, and play sound
	 */
	public static void initAudio()
	{
		sounds = new HashMap<String, Sound>();
		music = new HashMap<String, Music>();
		TinySound.init();
	}
	
	public static void shutdown()
	{
		TinySound.shutdown();
	}
	
	/**
	 * Loads a sound from the given path
	 * 
	 * @param name the name of the sound to bind it to
	 * @param path the path where the sound file is
	 */
	public static void addSound(String name, String path)
	{
		if(!sounds.containsKey(name))
			sounds.put(name, TinySound.loadSound(path));
		Log.internal("Added audio with name: " + name + " and path: " + path);
	}
	
	/**
	 * Loads audio from the given path and adds it as a
	 * music loop sound
	 * 
	 * @param name the name of the sound to bind it to
	 * @param path the path where the sound file is
	 */
	public static void addMusic(String name, String path)
	{
		if(!music.containsKey(name))
		{
			music.put(name, TinySound.loadMusic(path));
			Log.internal("Added Music with name: " + name + " and path:" + path);
		}
	}
	
	/**
	 * Removes the sound from the list of sounds
	 * 
	 * @param name the name of the sound to remove
	 */
	public static void removeSound(String name)
	{
		sounds.remove(name);
		Log.internal("Removed sound with name: " + name);
	}
	
	/**
	 * Removes the music from the list of music
	 * 
	 * @param name the name of the music to remove
	 */
	public static void removeMusic(String name)
	{
		music.remove(name);
	}
	
	/**
	 * Plays the sound with the given name and volume
	 * 
	 * @param name the name of the sound to play
	 * @param vol the volume of the sound
	 */
	public static void playSound(String name, double vol)
	{
		sounds.get(name).play(vol);
	}
	
	/**
	 * Plays the sound with the given name
	 * 
	 * @param name the name of the sound to play
	 */
	public static void playSound(String name)
	{
		playSound(name, 1.0);
	}
	
	/**
	 * Stops the sound with the given name
	 * 
	 * @param name the name of the sound to stop
	 */
	public static void stopSound(String name)
	{
		sounds.get(name).stop();
	}
	
	/**
	 * Plays the music with the given name and volume
	 * 
	 * @param name the name of the music to play
	 * @param vol the volume of the music
	 * @param loop whether or not to loop the music
	 */
	private static void playMusic(String name, double vol, boolean loop)
	{
		Music m = music.get(name);
		if(!m.playing()) m.play(loop, vol);
	}
	
	/**
	 * Plays the music with the given name, and does not loop it
	 * 
	 * @param name the name of the music to play
	 */
	public static void playMusic(String name)
	{
		playMusic(name, 1.0, false);
	}
	
	/**
	 * Plays the music with the given name with the given volume
	 * 
	 * @param name the name of the music to play
	 * @param volume the volume of the music
	 */
	public static void playMusic(String name, double volume)
	{
		playMusic(name, volume, false);
	}
	
	/**
	 * Loops the music with the given name
	 * 
	 * @param name the name of the music to loop
	 */
	public static void loopMusic(String name)
	{
		playMusic(name, 1.0, true);
	}
	
	/**
	 * Loops the music with the given name with the given volume
	 * 
	 * @param name the name of the music to loop
	 * @param vol the volume of the music
	 */
	public static void loopMusic(String name, double vol)
	{
		playMusic(name, vol, true);
	}
	
	public static void stopMusic(String name)
	{
		music.get(name).stop();
	}
	
}
