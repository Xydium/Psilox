package com.xydium.utility;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input extends KeyAdapter {

	private static final int NUM_KEYS = 1024;
	private static final short[] KEYS = new short[NUM_KEYS];
	
	public static Input init() {
		return new Input();
	}
	
	/**
	 * Returns if a key is currently being held down.
	 * 
	 * @param keyCode
	 * @return
	 */
	public static boolean keyDown(int keyCode) {
		if(KEYS[keyCode] > 0) {
			KEYS[keyCode]++;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true only the first time a key event is
	 * sent without a release event AND the first time 
	 * that event is accessed.
	 * 
	 * @param keyCode
	 * @return
	 */
	public static boolean keyTap(int keyCode) {
		if(KEYS[keyCode] == 1) {
			KEYS[keyCode]++;
			return true;
		}
		return false;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		KEYS[key]++;
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		KEYS[key] = 0;
	}
	
}
