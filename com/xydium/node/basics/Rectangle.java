package com.xydium.node.basics;

import java.awt.Color;

import com.xydium.geometry.Vec2f;
import com.xydium.node.Node;
import com.xydium.rendering.Draw;

public class Rectangle extends Node {
	
	private Vec2f dimensions;
	private Color color;
	private boolean centered;
	private boolean filled;
	
	public Rectangle(String tag, Vec2f dimensions, Color color, boolean filled) {
		super(tag);
		this.dimensions = dimensions;
		this.color = color;
		this.centered = true;
		this.filled = true;
		setUpdating(false);
	}
	
	public void render() {
		int x = getTransform().position().getX().intValue();
		int y = getTransform().position().getY().intValue();
		int width = (int) (getTransform().scale().getX().floatValue() * dimensions.getX().floatValue());
		int height = (int) (getTransform().scale().getY().floatValue() * dimensions.getY().floatValue());
		if(centered) {
			if(filled) {
				Draw.fillCenteredRect(x, y, width, height, color);
			} else {
				Draw.outlineCenteredRect(x, y, width, height, color);
			}
		} else {
			if(filled) {
				Draw.fillRect(x, y, width, height, color);
			} else {
				Draw.outlineRect(x, y, width, height, color);
			}
		}
	}
	
	public Vec2f getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Vec2f dimensions) {
		this.dimensions = dimensions;
	}
	
	public boolean isCentered() {
		return centered;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
}
