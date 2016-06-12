package com.xydium.core;

import com.xydium.input.Input;
import com.xydium.input.InputEvent;
import com.xydium.input.InputListener;
import com.xydium.node.NodeTree;

public abstract class Scene implements InputListener {

	private NodeTree sceneTree; 
	
	public Scene() {
		this.sceneTree = new NodeTree();
		load();
	}
	
	public void load() {}
	
	public void activate() {
		Input.addListener(this);
	}
	
	public void deactivate() {
		Input.removeListener(this);
	}
	
	public void update() {
		sceneTree.update();
	}
	public void render() {
		sceneTree.render();
	}
	
	public void receiveInput(InputEvent ev) {}
	
	public NodeTree getTree() {
		return sceneTree;
	}
	
}
