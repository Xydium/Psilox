package com.xydium.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.xydium.geometry.Vec2i;
import com.xydium.input.InputEvent.InputState;
import com.xydium.input.InputEvent.InputType;

public class Input extends KeyAdapter implements MouseListener, MouseMotionListener {

	private static List<InputListener> listeners = new ArrayList<InputListener>();
	
	private static final int NUM_KEYS = 1024;
	private static final int NUM_BUTTONS = 3;
	private static final short[] KEYS = new short[NUM_KEYS];
	private static final short[] BUTTONS = new short[NUM_BUTTONS];
	
	public static Vec2i mousePosition = new Vec2i(0);
	
	public static Input init() {
		return new Input();
	}
	
	public static void addListener(InputListener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(InputListener listener) {
		listeners.remove(listener);
	}
	
	public static void dumpListeners() {
		listeners.clear();
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
		if(KEYS[key] == 1) dispatchEvent(new InputEvent(InputType.KEYBOARD, key, InputState.PRESSED, e));
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		KEYS[key] = 0;
		dispatchEvent(new InputEvent(InputType.KEYBOARD, key, InputState.RELEASED, e));
	}
	
	private static void dispatchEvent(InputEvent ev) {
		for(InputListener l : listeners) {
			if(ev.isHalted()) break;
			l.receiveInput(ev);
		}
	}

	private static void updateMousePosition(MouseEvent e) {
		mousePosition.setX(e.getX());
		mousePosition.setY(e.getY());
	}
	
	public void mouseDragged(MouseEvent e) {
		updateMousePosition(e);
		dispatchEvent(new InputEvent(InputType.MOUSE, 0, InputState.DRAGGED, e));
	}

	public void mouseMoved(MouseEvent e) {
		updateMousePosition(e);
		dispatchEvent(new InputEvent(InputType.MOUSE, 0, InputState.MOVED, e));
	}

	public void mouseClicked(MouseEvent e) {
		dispatchEvent(new InputEvent(InputType.MOUSE, e.getButton(), InputState.CLICKED, e));
	}

	public void mousePressed(MouseEvent e) {
		dispatchEvent(new InputEvent(InputType.MOUSE, e.getButton(), InputState.PRESSED, e));
		BUTTONS[e.getButton()]++;
	}

	public void mouseReleased(MouseEvent e) {
		dispatchEvent(new InputEvent(InputType.MOUSE, e.getButton(), InputState.RELEASED, e));
		BUTTONS[e.getButton()]++;
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}
