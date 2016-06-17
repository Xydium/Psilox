package com.xydium.input;

import java.awt.AWTEvent;

public class InputEvent {
	
	public InputType type;
	public int key;
	public InputState state;
	public AWTEvent event;
	private boolean halted;
	
	public InputEvent(InputType type, int key, InputState state, AWTEvent event) {
		this.type = type;
		this.key = key;
		this.state = state;
		this.event = event;
	}
	
	public enum InputType {
		KEYBOARD, MOUSE;
	}
	
	public enum InputState {
		PRESSED, RELEASED, DRAGGED, CLICKED, MOVED;
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
		return String.format("Key Event: Type=%s, Key=%s, State=%s", type.name(), "" + key, state.name());
	}
	
}
