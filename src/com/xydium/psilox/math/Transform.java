package com.xydium.psilox.math;

public class Transform {

	private Transform parent;
	private Vec3 position;
	private Vec3 scale;
	private float rotation;
	
	public Transform(Transform parent, Vec3 position, Vec3 scale, float rotation) {
		this.parent = parent;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	public Transform(Vec3 position, Vec3 scale, float rotation) {
		this(null, position, scale, rotation);
	}
	
	public Transform(Vec3 position, Vec3 scale) {
		this(position, scale, 0);
	}
	
	public Transform(Vec3 position, float rotation) {
		this(position, new Vec3(1f), rotation);
	}
	
	public Transform(Vec3 position) {
		this(position, 0);
	}
	
	public Transform() {
		this(new Vec3(0f));
	}
	
	public Vec3 position() {
		return position;
	}
	
	public void setPosition(Vec3 position) {
		this.position = position;
	}
	
	public Vec3 positionGlobal() {
		if(parent == null) {
			return position;
		}
		
		return position.sum(parent.positionGlobal());
	}
	
	public Vec3 scale() {
		return scale;
	}
	
	public void setScale(Vec3 scale) {
		this.scale = scale;
	}
	
	public Vec3 scaleGlobal() {
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
	
	public void translate(Vec3 delta) {
		position = position.sum(delta);
	}
	
	public void rescale(Vec3 delta) {
		scale = scale.sum(delta);
	}
	
	public void rotate(float delta) {
		rotation += delta;
	}
	
	public String toString() {
		return String.format("Transform: %s, Position=%s, Rotation=%.2f, Scale=%s", parent == null ? "NoParent" : "HasParent", position.toString(), rotation, scale.toString());
	}
	
}
