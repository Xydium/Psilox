package com.xydium.psilox.math;

public class Transform {

	private Transform parent;
	private Vec position;
	private Vec scale;
	private float rotation;
	
	public Transform(Transform parent, Vec position, Vec scale, float rotation) {
		this.parent = parent;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	public Transform(Vec position, Vec scale, float rotation) {
		this(null, position, scale, rotation);
	}
	
	public Transform(Vec position, Vec scale) {
		this(position, scale, 0);
	}
	
	public Transform(Vec position, float rotation) {
		this(position, new Vec(1f), rotation);
	}
	
	public Transform(Vec position) {
		this(position, 0);
	}
	
	public Transform() {
		this(new Vec(0f));
	}
	
	public Vec position() {
		return position;
	}
	
	public void setPosition(Vec position) {
		this.position = position;
	}
	
	public Vec positionGlobal() {
		if(parent == null) {
			return position;
		}
		
		return position.sum(parent.positionGlobal());
	}
	
	public Vec scale() {
		return scale;
	}
	
	public void setScale(Vec scale) {
		this.scale = scale;
	}
	
	public Vec scaleGlobal() {
		if(parent == null) {
			return scale;
		}
		
		return scale.pro(parent.scaleGlobal());
	}
	
	public float rotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public float rotationGlobal() {
		if(parent == null) {
			return rotation;
		}
		
		return parent.rotationGlobal() + rotation;
	}
	
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	public void translate(Vec delta) {
		position = position.sum(delta);
	}
	
	public void rescale(Vec delta) {
		scale = scale.sum(delta);
	}
	
	public void rotate(float delta) {
		rotation += delta;
	}
	
	public String toString() {
		return String.format("Transform: %s, Position=%s, Rotation=%.2f, Scale=%s", parent == null ? "NoParent" : "HasParent", position.toString(), rotation, scale.toString());
	}
	
}
