package com.xydium.geometry;

public class Transform {

	private Vec2f position;
	private Vec2f dimensions;
	private double rotation;
	
	public Transform(Vec2f position, Vec2f dimensions, double rotation) {
		this.position = position;
		this.dimensions = dimensions;
		this.rotation = rotation;
	}
	
	public Transform(Vec2f position, Vec2f dimensions) {
		this(position, dimensions, 0);
	}
	
	public Transform(Vec2f position) {
		this(position, new Vec2f(0f));
	}
	
	public Transform() {
		this(new Vec2f(0f));
	}
	
	public Vec2f position() {
		return position;
	}
	
	public Vec2f dimensions() {
		return dimensions;
	}
	
	public double rotation() {
		return rotation;
	}
	
	public void translate(Vec2f delta) {
		position.setX(position.getX() + delta.getX());
		position.setY(position.getY() + delta.getY());
	}
	
	public void stretch(Vec2f delta) {
		dimensions.setX(dimensions.getX() + delta.getX());
		dimensions.setY(dimensions.getY() + delta.getY());
	}
	
	public void rotate(double delta) {
		rotation += delta;
	}
	
}
