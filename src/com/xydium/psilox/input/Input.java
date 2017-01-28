package com.xydium.psilox.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.xydium.psilox.input.InputEvent.InputState;
import com.xydium.psilox.input.InputEvent.InputType;
import com.xydium.psilox.math.Vec3;

public class Input extends KeyAdapter implements MouseListener, MouseMotionListener {

	private List<InputListener> listeners = new ArrayList<InputListener>();
	
	private static final int NUM_KEYS = 1024;
	private static final int NUM_BUTTONS = 4;
	private final short[] KEYS = new short[NUM_KEYS];
	private final short[] BUTTONS = new short[NUM_BUTTONS];
	
	public static Vec3 mousePosition = new Vec3(0);
	
	public static Input init() {
		return new Input();
	}
	
	public void addListener(InputListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(InputListener listener) {
		listeners.remove(listener);
	}
	
	public void dumpListeners() {
		listeners.clear();
	}
	
	/**
	 * Returns if a key is currently being held down.
	 * 
	 * @param keyCode
	 * @return
	 */
	public boolean keyDown(int keyCode) {
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
	public boolean keyTap(int keyCode) {
		if(KEYS[keyCode] == 1) {
			KEYS[keyCode]++;
			return true;
		}
		return false;
	}
	
	public boolean comboDown(int... keyCodes) {
		for(int i : keyCodes) {
			if(!keyDown(i)) return false;
		}
		return true;
	}
	
	public boolean buttonDown(int buttonCode) {
		if(BUTTONS[buttonCode] > 0) {
			BUTTONS[buttonCode]++;
			return true;
		}
		return false;
	}
	
	public boolean buttonTap(int buttonCode) {
		if(BUTTONS[buttonCode] == 1) {
			BUTTONS[buttonCode]++;
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
	
	private void dispatchEvent(InputEvent ev) {
		for(InputListener l : listeners) {
			if(ev.isHalted()) break;
			l.receiveInput(ev);
		}
	}

	private void updateMousePosition(MouseEvent e) {
		mousePosition = new Vec3(e.getX(), e.getY());
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
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
		BUTTONS[e.getButton()] = 0;
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}
