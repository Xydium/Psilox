package psilox.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

import psilox.math.Vec;

public class Input {

	private static final int NUM_KEYS = 65535;
	private static final int NUM_BUTTONS = 255;
	private static final byte[] KEYS = new byte[NUM_KEYS];
	private static final byte[] BUTTONS = new byte[NUM_BUTTONS];
	public static int WINDOW_HEIGHT;
	public static Vec position = new Vec(0);
	
	static {
		for(int i = 0; i < KEYS.length; i++) {
			KEYS[i] = GLFW_RELEASE;
		}
		
		for(int i = 0; i < BUTTONS.length; i++) {
			BUTTONS[i] = GLFW_RELEASE;
		}
	}
	
	public static boolean keyDown(int keyCode) {
		return KEYS[keyCode] != GLFW_RELEASE;
	}
	
	public static boolean keyTap(int keyCode) {
		if(KEYS[keyCode] == GLFW_PRESS) {
			KEYS[keyCode] = GLFW_REPEAT;
			return true;
		}
		return false;
	}
	
	public static boolean buttonDown(int buttonCode) {
		return BUTTONS[buttonCode] != GLFW_RELEASE;
	}
	
	public static boolean buttonTap(int buttonCode) {
		if(BUTTONS[buttonCode] == GLFW_PRESS) {
			BUTTONS[buttonCode] = GLFW_REPEAT;
			return true;
		}
		return false;
	}
	
	public boolean comboDown(int... keys) {
		for(int i : keys) {
			if(!keyDown(i)) return false;
		}
		return true;
	}
	
	public static final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		public void invoke(long window, int key, int scancode, int action, int mods) {
			KEYS[key] = (byte) action;
		}
	};
	
	public static final GLFWMouseButtonCallback mouseCallback = new GLFWMouseButtonCallback() {
		public void invoke(long window, int button, int action, int mods) {
			BUTTONS[button] = (byte) action;
		}
	};
	
	public static final GLFWCursorPosCallback cursorCallback = new GLFWCursorPosCallback() {
		public void invoke(long window, double x, double y) {
			position.x = (float) x;
			position.y = WINDOW_HEIGHT - (float) y; 
		}
	};
	
}
