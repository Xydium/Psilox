package psilox.input;

import org.lwjgl.glfw.GLFW;

public class InputEvent {

	public final InputType type;
	public final int key;
	public final InputState state;
	private boolean halted;
	
	public InputEvent(InputType type, int key, InputState state) {
		this.type = type;
		this.key = key;
		this.state = state;
	}
	
	public enum InputType {
		KEYBOARD, MOUSE;
	}
	
	public enum InputState {
		PRESSED, RELEASED, MOVED;
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
	
	public boolean matches(InputEvent event) {
		return matches(event.type, event.key, event.state);
	}
	
	public String toString() {
		return String.format("InputEvent: Type=%s, Key=%s, State=%s", type.name(), "" + key, state.name());
	}
	
}
