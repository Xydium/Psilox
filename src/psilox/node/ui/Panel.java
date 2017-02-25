package psilox.node.ui;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.input.Function;
import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputEvent.InputState;
import psilox.input.InputEvent.InputType;
import psilox.math.Vec;
import psilox.node.Node;

public class Panel extends Node {

	private static Panel focusedPanel;
	
	private Vec dimensions;
	private boolean mouseWasInside;
	private boolean pressed;
	private Color color;
	
	private Function onMouseEnter = () -> {};
	private Function onMouseExit = () -> {};
	private Function onPressed = () -> {};
	private Function onReleased = () -> {};
	
	public Panel(Vec dimensions) {
		super();
		this.dimensions = dimensions;
		setInputListening(true);
	}
	
	public Vec getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Vec dimensions) {
		this.dimensions = dimensions;
	}
	
	public void mouseEntered() { onMouseEnter.execute(); }
	public void mouseExited() { onMouseExit.execute(); }
	public void pressed() { onPressed.execute(); }
	public void released() { onReleased.execute(); }
	public void lostFocus() {}
	
	public boolean isMouseInside() {
		Vec gap = globalAnchoredPosition();
		return Input.position.btn(gap, gap.sum(dimensions));
	}
	
	public boolean getIsMouseInside() {
		return mouseWasInside;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public void render() {
		if(color != null) {
			Draw.quad(color, Vec.ZERO, dimensions);
		}
	}
	
	public void receiveInput(InputEvent ev) {
		if(ev.type == InputType.MOUSE) {
			if(ev.state == InputState.MOVED) {
				if(!mouseWasInside && (mouseWasInside = isMouseInside())) {
					mouseEntered();
				} else if(mouseWasInside && !(mouseWasInside = isMouseInside())) {
					mouseExited();
					pressed = false;
				}
			} else if(ev.state == InputState.PRESSED && mouseWasInside) {
				pressed = true;
				requestFocus();
				pressed();
			} else if(ev.state == InputState.RELEASED && mouseWasInside) {
				if(pressed) {
					pressed = false;
					released();
				}
			}
		}
	}
	
	public void setOnMouseEnter(Function onMouseEnter) {
		this.onMouseEnter = onMouseEnter;
	}
	
	public void setOnMouseExit(Function onMouseExit) {
		this.onMouseExit = onMouseExit;
	}

	public void setOnPressed(Function onPressed) {
		this.onPressed = onPressed;
	}

	public void setOnReleased(Function onReleased) {
		this.onReleased = onReleased;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void requestFocus() {
		if(focusedPanel != null) {
			focusedPanel.lostFocus();
		}
		focusedPanel = this;
	}
	
	public boolean hasFocus() {
		return focusedPanel == this;
	}
	
	public static Panel getFocused() {
		return focusedPanel;
	}
	
}
