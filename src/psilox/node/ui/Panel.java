package psilox.node.ui;

import psilox.input.Function;
import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputEvent.InputState;
import psilox.input.InputEvent.InputType;
import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;

public class Panel extends Node {

	private static Panel focusedPanel;
	
	private Vec dimensions;
	private Anchor anchor;
	private boolean mouseWasInside;
	private boolean pressed;
	
	private Function onMouseEnter = () -> {};
	private Function onMouseExit = () -> {};
	private Function onPressed = () -> {};
	private Function onReleased = () -> {};
	
	public Panel(String tag, Vec dimensions, Anchor anchor) {
		super(tag);
		this.dimensions = dimensions;
		this.anchor = anchor;
		anchorPoint(anchor);
		setInputListening(true);
	}
	
	public Vec getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Vec dimensions) {
		this.dimensions = dimensions;
		anchorPoint(anchor);
	}
	
	public Anchor getAnchor() {
		return anchor;
	}
	
	public void anchorPoint(Anchor anchor) {
		this.anchor = anchor;
		anchorPoint(anchor, dimensions);
	}

	public void mouseEntered() { onMouseEnter.execute(); }
	public void mouseExited() { onMouseExit.execute(); }
	public void pressed() { onPressed.execute(); }
	public void released() { onReleased.execute(); }
	public void lostFocus() {}
	
	public boolean isMouseInside() {
		return Input.position.btn(transform().positionGlobal(), transform.positionGlobal().sum(dimensions));
	}
	
	public boolean getIsMouseInside() {
		return mouseWasInside;
	}
	
	public boolean isPressed() {
		return pressed;
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
