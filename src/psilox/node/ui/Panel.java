package psilox.node.ui;

import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputEvent.InputState;
import psilox.input.InputEvent.InputType;
import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;

public class Panel extends Node {

	private Vec dimensions;
	private Anchor anchor;
	private boolean mouseWasInside;
	private boolean pressed;
	
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

	public void mouseEntered() {}
	public void mouseExited() {}
	public void pressed() {}
	public void released() {}

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
				pressed();
			} else if(ev.state == InputState.RELEASED && mouseWasInside) {
				if(pressed) {
					pressed = false;
					released();
				}
			}
		}
	}
	
}
