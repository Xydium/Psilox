package com.xydium.psilox.utilities;

import java.util.HashMap;
import java.util.Map;

import com.kuusisto.tinysound.Music;
import com.kuusisto.tinysound.Sound;
import com.kuusisto.tinysound.TinySound;
import com.xydium.psilox.utilities.Log;

public final class Audio 
{
	private Map<String, Sound> sounds;
	private Map<String, Music> music;

	public Audio() {
		sounds = new HashMap<String, Sound>();
		music = new HashMap<String, Music>();
		TinySound.init();
	}
	
	public void destroy() {
		TinySound.shutdown();
	}
	
	public void addSound(String name, String path) {
		if(!sounds.containsKey(name))
			sounds.put(name, TinySound.loadSound(path));
		Log.internal("Added audio with name: " + name + " and path: " + path);
	}
	
	public void addMusic(String name, String path) {
		if(!music.containsKey(name)) {
			music.put(name, TinySound.loadMusic(path));
			Log.internal("Added Music with name: " + name + " and path:" + path);
		}
	}
	
	public void removeSound(String name) {
		sounds.remove(name);
		Log.internal("Removed sound with name: " + name);
	}
	
	public void removeMusic(String name) {
		music.remove(name);
	}
	
	public void playSound(String name, double vol) {
		sounds.get(name).play(vol);
	}
	
	public void playSound(String name) {
		playSound(name, 1.0);
	}
	
	public void stopSound(String name) {
		sounds.get(name).stop();
	}
	
	private void playMusic(String name, double vol, boolean loop) {
		Music m = music.get(name);
		if(!m.playing()) m.play(loop, vol);
	}

	public void playMusic(String name) {
		playMusic(name, 1.0, false);
	}
	
	public void playMusic(String name, double volume) {
		playMusic(name, volume, false);
	}
	
	public void loopMusic(String name) {
		playMusic(name, 1.0, true);
	}
	
	public void loopMusic(String name, double vol) {
		playMusic(name, vol, true);
	}
	
	public void stopMusic(String name) {
		music.get(name).stop();
	}
	
}
