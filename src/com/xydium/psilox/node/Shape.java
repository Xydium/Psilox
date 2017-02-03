package com.xydium.psilox.node;

import com.xydium.psilox.math.Vec;
import com.xydium.psilox.rendering.Color;

public class Shape extends Node {

	private int mode;
	private Vec[] shape;
	private Color[] colors;
	
	public Shape(String tag, Vec dimensions, int mode, Vec[] shape, Color... colors) {
		super(tag);
		transform.setScale(dimensions);
		this.mode = mode;
		this.shape = shape;
		this.colors = colors;
	}
	
	public void render() {
		draw().fixedFunction(mode, shape, colors);
	}
	
}
