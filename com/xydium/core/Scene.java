package com.xydium.core;

public abstract class Scene {

	public Scene() {
		load();
	}
	
	public void load() {}
	public void activate() {}
	public void deactivate() {}
	public void update() {}
	public void render() {}
	
}
