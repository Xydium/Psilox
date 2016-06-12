package com.xydium.core;

import com.xydium.input.Input;
import com.xydium.input.InputEvent;
import com.xydium.input.InputListener;

public abstract class Scene implements InputListener {

	public Scene() {
		load();
	}
	
	public void load() {}
	
	public void activate() {
		Input.addListener(this);
	}
	
	public void deactivate() {
		Input.removeListener(this);
	}
	
	public void update() {}
	public void render() {}
	
	public void receiveInput(InputEvent ev) {}
	
}
