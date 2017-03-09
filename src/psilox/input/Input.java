package psilox.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import psilox.input.InputEvent.InputState;
import psilox.input.InputEvent.InputType;
import psilox.math.Vec;

public class Input {

	private static List<InputListener> listeners = new ArrayList<InputListener>();
	
	private static final int NUM_KEYS = 1000;
	private static final int NUM_BUTTONS = 10;
	private static final byte[] KEYS = new byte[NUM_KEYS];
	private static final byte[] BUTTONS = new byte[NUM_BUTTONS];
	public static int WINDOW_HEIGHT;
	public static Vec position = new Vec(0);
	
	public static void addListener(InputListener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(InputListener listener) {
		listeners.remove(listener);
	}
	
	public static void dumpListeners() {
		listeners.clear();
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
	
	private static void dispatchEvent(InputType type, int code, InputState state) {
		InputEvent ev = new InputEvent(type, code, state);
		for(InputListener l : listeners) {
			if(ev.isHalted()) break;
			l.receiveInput(ev);
		}
	}
	
	public static final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if(key == UNKNOWN) return;
			KEYS[key] = (byte) action;
			if(action == GLFW_REPEAT) {
				dispatchEvent(InputType.KEYBOARD, key, InputState.REPEAT);
			} else {
				dispatchEvent(InputType.KEYBOARD, key, action == GLFW_RELEASE? InputState.RELEASED : InputState.PRESSED);
			}
		}
	};
	
	public static final GLFWMouseButtonCallback mouseCallback = new GLFWMouseButtonCallback() {
		public void invoke(long window, int button, int action, int mods) {
			if(button == UNKNOWN) return;
			BUTTONS[button] = (byte) action;
			if(action == GLFW_REPEAT) return;
			dispatchEvent(InputType.MOUSE, button, action == GLFW_RELEASE? InputState.RELEASED : InputState.PRESSED);
		}
	};
	
	public static final GLFWCursorPosCallback cursorCallback = new GLFWCursorPosCallback() {
		public void invoke(long window, double x, double y) {
			position.x = (float) x;
			position.y = WINDOW_HEIGHT - (float) y;
			dispatchEvent(InputType.MOUSE, 0, InputState.MOVED);
		}
	};
	
	public static final GLFWCharCallback charCallback = new GLFWCharCallback() {

		public void invoke(long window, int character) {
			dispatchEvent(InputType.KEYBOARD, character, InputState.CHARACTER);
		}
		
	};
	
	public static final int UNKNOWN         =   -1                 ;
	public static final int SPACE           =   32                 ;
	public static final int APOSTROPHE      =   39                 ;
	public static final int COMMA           =   44                 ;
	public static final int MINUS           =   45                 ;
	public static final int PERIOD          =   46                 ;
	public static final int SLASH           =   47                 ;
	public static final int N0               =   48                 ;
	public static final int N1               =   49                 ;
	public static final int N2               =   50                 ;
	public static final int N3               =   51                 ;
	public static final int N4               =   52                 ;
	public static final int N5               =   53                 ;
	public static final int N6               =   54                 ;
	public static final int N7               =   55                 ;
	public static final int N8               =   56                 ;
	public static final int N9               =   57                 ;
	public static final int SEMICOLON       =   59                 ;
	public static final int EQUAL           =   61                 ;
	public static final int A               =   65                 ;
	public static final int B               =   66                 ;
	public static final int C               =   67                 ;
	public static final int D               =   68                 ;
	public static final int E               =   69                 ;
	public static final int F               =   70                 ;
	public static final int G               =   71                 ;
	public static final int H               =   72                 ;
	public static final int I               =   73                 ;
	public static final int J               =   74                 ;
	public static final int K               =   75                 ;
	public static final int L               =   76                 ;
	public static final int M               =   77                 ;
	public static final int N               =   78                 ;
	public static final int O               =   79                 ;
	public static final int P               =   80                 ;
	public static final int Q               =   81                 ;
	public static final int R               =   82                 ;
	public static final int S               =   83                 ;
	public static final int T               =   84                 ;
	public static final int U               =   85                 ;
	public static final int V               =   86                 ;
	public static final int W               =   87                 ;
	public static final int X               =   88                 ;
	public static final int Y               =   89                 ;
	public static final int Z               =   90                 ;
	public static final int LEFT_BRACKET    =   91                 ;
	public static final int BACKSLASH       =   92                 ;
	public static final int RIGHT_BRACKET   =   93                 ;
	public static final int GRAVE_ACCENT    =   96                 ;
	public static final int ESCAPE          =   256                ;
	public static final int ENTER           =   257                ;
	public static final int TAB             =   258                ;
	public static final int BACKSPACE       =   259                ;
	public static final int INSERT          =   260                ;
	public static final int DELETE          =   261                ;
	public static final int RIGHT           =   262                ;
	public static final int LEFT            =   263                ;
	public static final int DOWN            =   264                ;
	public static final int UP              =   265                ;
	public static final int PAGE_UP         =   266                ;
	public static final int PAGE_DOWN       =   267                ;
	public static final int HOME            =   268                ;
	public static final int END             =   269                ;
	public static final int CAPS_LOCK       =   280                ;
	public static final int SCROLL_LOCK     =   281                ;
	public static final int NUM_LOCK        =   282                ;
	public static final int PRINT_SCREEN    =   283                ;
	public static final int PAUSE           =   284                ;
	public static final int F1              =   290                ;
	public static final int F2              =   291                ;
	public static final int F3              =   292                ;
	public static final int F4              =   293                ;
	public static final int F5              =   294                ;
	public static final int F6              =   295                ;
	public static final int F7              =   296                ;
	public static final int F8              =   297                ;
	public static final int F9              =   298                ;
	public static final int F10             =   299                ;
	public static final int F11             =   300                ;
	public static final int F12             =   301                ;
	public static final int F13             =   302                ;
	public static final int F14             =   303                ;
	public static final int F15             =   304                ;
	public static final int F16             =   305                ;
	public static final int F17             =   306                ;
	public static final int F18             =   307                ;
	public static final int F19             =   308                ;
	public static final int F20             =   309                ;
	public static final int F21             =   310                ;
	public static final int F22             =   311                ;
	public static final int F23             =   312                ;
	public static final int F24             =   313                ;
	public static final int F25             =   314                ;
	public static final int KP_0            =   320                ;
	public static final int KP_1            =   321                ;
	public static final int KP_2            =   322                ;
	public static final int KP_3            =   323                ;
	public static final int KP_4            =   324                ;
	public static final int KP_5            =   325                ;
	public static final int KP_6            =   326                ;
	public static final int KP_7            =   327                ;
	public static final int KP_8            =   328                ;
	public static final int KP_9            =   329                ;
	public static final int KP_DECIMAL      =   330                ;
	public static final int KP_DIVIDE       =   331                ;
	public static final int KP_MULTIPLY     =   332                ;
	public static final int KP_SUBTRACT     =   333                ;
	public static final int KP_ADD          =   334                ;
	public static final int KP_ENTER        =   335                ;
	public static final int KP_EQUAL        =   336                ;
	public static final int LEFT_SHIFT      =   340                ;
	public static final int LEFT_CONTROL    =   341                ;
	public static final int LEFT_ALT        =   342                ;
	public static final int LEFT_SUPER      =   343                ;
	public static final int RIGHT_SHIFT     =   344                ;
	public static final int RIGHT_CONTROL   =   345                ;
	public static final int RIGHT_ALT       =   346                ;
	public static final int RIGHT_SUPER     =   347                ;
	public static final int MENU            =   348                ;
	public static final int BUTTON_1      =   0                  ;
	public static final int BUTTON_2      =   1                  ;
	public static final int BUTTON_3      =   2                  ;
	public static final int BUTTON_4      =   3                  ;
	public static final int BUTTON_5      =   4                  ;
	public static final int BUTTON_6      =   5                  ;
	public static final int BUTTON_7      =   6                  ;
	public static final int BUTTON_8      =   7                  ;
	public static final int BUTTON_LEFT   =   BUTTON_1;
	public static final int BUTTON_RIGHT  =   BUTTON_2;
	public static final int BUTTON_MIDDLE =   BUTTON_3;
	
}
