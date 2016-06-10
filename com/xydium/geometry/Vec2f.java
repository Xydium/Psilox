package com.xydium.geometry;

public class Vec2f extends Vec2<Float> {

	public Vec2f(Float x, Float y) {
		super(x, y);
	}
	
	public Vec2f(Float n) {
		this(n, n);
	}

	public Vec2<Float> add(Vec2<?> other) {
		return new Vec2f(getX() + other.getX().floatValue(), getY() + other.getY().floatValue());
	}

	public Vec2<Float> sub(Vec2<?> other) {
		return new Vec2f(getX() - other.getX().floatValue(), getY() - other.getY().floatValue());
	}

	public Vec2<Float> mul(Vec2<?> other) {
		return new Vec2f(getX() * other.getX().floatValue(), getY() * other.getY().floatValue());
	}

	public Vec2<Float> div(Vec2<?> other) {
		return new Vec2f(getX() / other.getX().floatValue(), getY() / other.getY().floatValue());
	}

	public void addR(Vec2<?> other) {
		setX(getX().floatValue() + other.getX().floatValue());
		setY(getY().floatValue() + other.getY().floatValue());
	}

	public void subR(Vec2<?> other) {
		setX(getX().floatValue() - other.getX().floatValue());
		setY(getY().floatValue() - other.getY().floatValue());
	}

	public void mulR(Vec2<?> other) {
		setX(getX().floatValue() * other.getX().floatValue());
		setY(getY().floatValue() * other.getY().floatValue());
	}

	public void divR(Vec2<?> other) {
		setX(getX().floatValue() / other.getX().floatValue());
		setY(getY().floatValue() / other.getY().floatValue());
	}
	
}
