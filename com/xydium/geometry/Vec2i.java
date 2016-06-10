package com.xydium.geometry;

public class Vec2i extends Vec2<Integer> {
	
	public Vec2i(Integer x, Integer y) {
		super(x, y);
	}
	
	public Vec2i(Integer n) {
		this(n , n);
	}

	public Vec2<Integer> add(Vec2<?> other) {
		return new Vec2i(getX() + other.getX().intValue(), getY() + other.getY().intValue());
	}
	
	public Vec2<Integer> sub(Vec2<?> other) {
		return new Vec2i(getX() - other.getX().intValue(), getY() - other.getY().intValue());
	}

	public Vec2<Integer> mul(Vec2<?> other) {
		return new Vec2i(getX() * other.getX().intValue(), getY() * other.getY().intValue());
	}

	public Vec2<Integer> div(Vec2<?> other) {
		return new Vec2i(getX() / other.getX().intValue(), getY() / other.getY().intValue());
	}
	
	public void addR(Vec2<?> other) {
		setX(getX().intValue() + other.getX().intValue());
		setY(getY().intValue() + other.getY().intValue());
	}

	public void subR(Vec2<?> other) {
		setX(getX().intValue() - other.getX().intValue());
		setY(getY().intValue() - other.getY().intValue());
	}

	public void mulR(Vec2<?> other) {
		setX(getX().intValue() * other.getX().intValue());
		setY(getY().intValue() * other.getY().intValue());
	}

	public void divR(Vec2<?> other) {
		setX(getX().intValue() / other.getX().intValue());
		setY(getY().intValue() / other.getY().intValue());
	}
	
}
