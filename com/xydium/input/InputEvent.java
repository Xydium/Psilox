package com.xydium.input;

import java.awt.event.KeyEvent;

public class InputEvent {
	
	public InputType type;
	public int key;
	public InputState state;
	public KeyEvent keyEvent;
	private boolean halted;
	
	public InputEvent(InputType type, int key, InputState state, KeyEvent keyEvent) {
		this.type = type;
		this.key = key;
		this.state = state;
		this.keyEvent = keyEvent;
	}
	
	public enum InputType {
		KEYBOARD, MOUSE;
	}
	
	public enum InputState {
		PRESSED, RELEASED;
	}
	
	public void halt() {
		this.halted = true;
	}
	
	public void unhalt() {
		this.halted = false;
	}
	
	public boolean isHalted() {
		return halted;
	}
	
	public boolean matches(InputType type, int key, InputState state) {
		return this.type == type && this.key == key && this.state == state;
	}
	
	public String toString() {
		return String.format("Key Event: Type=%s, Key=%s, State=%s", type.name(), key, state.name());
	}
	
}
