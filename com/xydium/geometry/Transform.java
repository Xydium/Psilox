package com.xydium.geometry;

public class Transform {

	private Transform parent;
	
	private Vec2f position;
	private Vec2f scale;
	private double rotation;
	
	public Transform(Transform parent, Vec2f position, Vec2f scale, double rotation) {
		this.parent = parent;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	public Transform(Vec2f position, Vec2f scale, double rotation) {
		this(null, position, scale, rotation);
	}
	
	public Transform(Vec2f position, Vec2f scale) {
		this(position, scale, 0);
	}
	
	public Transform(Vec2f position) {
		this(position, new Vec2f(1f));
	}
	
	public Transform() {
		this(new Vec2f(0f));
	}
	
	public Vec2f position() {
		return position;
	}
	
	public void setPosition(Vec2f position) {
		this.position = position;
	}
	
	public Vec2f positionGlobal() {
		if(parent == null) {
			return position;
		}
		
		Vec2f result = new Vec2f(0f);
		result.addR(position);
		result.addR(parent.positionGlobal());
				
		return result;
	}
	
	public Vec2f scale() {
		return scale;
	}
	
	public void setScale(Vec2f scale) {
		this.scale = scale;
	}
	
	public Vec2f scaleGlobal() {
		if(parent == null) {
			return scale;
		}
		
		Vec2f result = new Vec2f(1f);
		result.mulR(scale);
		result.mulR(parent.scaleGlobal());
				
		return result;
	}
	
	public double rotation() {
		return rotation;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	public double rotationGlobal() {
		if(parent == null) {
			return rotation;
		}
		
		return parent.rotationGlobal() + rotation;
	}
	
	public void translate(Vec2f delta) {
		position.addR(delta);
	}
	
	public void stretch(Vec2f delta) {
		scale.addR(delta);
	}
	
	public void rotate(double delta) {
		rotation += delta;
	}
	
}
