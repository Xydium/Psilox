package com.xydium.geometry;

public class Transform {

	private Transform parent;
	
	private Vec2f position;
	private Vec2f dimensions;
	private double rotation;
	
	public Transform(Transform parent, Vec2f position, Vec2f dimensions, double rotation) {
		this.parent = parent;
		this.position = position;
		this.dimensions = dimensions;
		this.rotation = rotation;
	}
	
	public Transform(Vec2f position, Vec2f dimensions, double rotation) {
		this(null, position, dimensions, rotation);
	}
	
	public Transform(Vec2f position, Vec2f dimensions) {
		this(position, dimensions, 0);
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
	
	public Vec2f positionGlobal() {
		if(parent == null) {
			return position;
		}
		
		Vec2f result = new Vec2f(0f);
		result.addR(position);
		result.addR(parent.positionGlobal());
				
		return result;
	}
	
	public Vec2f dimensions() {
		return dimensions;
	}
	
	public Vec2f dimensionsGlobal() {
		if(parent == null) {
			return dimensions;
		}
		
		Vec2f result = new Vec2f(1f);
		result.mulR(dimensions);
		result.mulR(parent.dimensionsGlobal());
				
		return result;
	}
	
	public double rotation() {
		return rotation;
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
		dimensions.addR(delta);
	}
	
	public void rotate(double delta) {
		rotation += delta;
	}
	
}
