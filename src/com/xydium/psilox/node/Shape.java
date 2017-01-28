package com.xydium.psilox.node;

import com.xydium.psilox.math.Vec3;
import com.xydium.psilox.rendering.GLShape;

public class Shape extends Node {

	private int mode;
	private GLShape shape;
	
	public Shape(String tag, Vec3 dimensions, int mode, GLShape shape) {
		super(tag);
		transform.setScale(dimensions);
		this.mode = mode;
		this.shape = shape;
	}
	
	public void render() {
		draw().combinedBuffers(mode, shape.vertices, shape.colors);
	}
	
}
