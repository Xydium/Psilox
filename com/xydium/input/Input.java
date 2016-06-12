package com.xydium.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.xydium.input.InputEvent.InputState;
import com.xydium.input.InputEvent.InputType;

public class Input extends KeyAdapter {

	private static List<InputListener> listeners = new ArrayList<InputListener>();
	
	private static final int NUM_KEYS = 1024;
	private static final short[] KEYS = new short[NUM_KEYS];
	
	public static Input init() {
		return new Input();
	}
	
	public static void addListener(InputListener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(InputListener listener) {
		listeners.remove(listener);
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
		dispatchEvent(new InputEvent(InputType.KEYBOARD, key, InputState.PRESSED, e));
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		KEYS[key] = 0;
		dispatchEvent(new InputEvent(InputType.KEYBOARD, key, InputState.RELEASED, e));
	}
	
	public void keyTyped(KeyEvent e) {
		int key = e.getKeyCode();
		dispatchEvent(new InputEvent(InputType.KEYBOARD, key, InputState.TYPED, e));
	}
	
	private static void dispatchEvent(InputEvent ev) {
		for(InputListener l : listeners) {
			if(ev.isHalted()) break;
			l.receiveInput(ev);
		}
	}
	
}
