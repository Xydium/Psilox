package com.xydium.geometry;

public abstract class Vec2<T extends Number> {

	private T x;
	private T y;
	
	public Vec2(T x, T y) {
		this.x = x;
		this.y = y;
	}
	
	public T getX() {
		return x;
	}
	
	public void setX(T x) {
		this.x = x;
	}
	
	public T getY() {
		return y;
	}
	
	public void setY(T y) {
		this.y = y;
	}
	
	public abstract Vec2<T> add(Vec2<?> other);
	public abstract Vec2<T> sub(Vec2<?> other);
	public abstract Vec2<T> mul(Vec2<?> other);
	public abstract Vec2<T> div(Vec2<?> other);
	
	public abstract void addR(Vec2<?> other);
	public abstract void subR(Vec2<?> other);
	public abstract void mulR(Vec2<?> other);
	public abstract void divR(Vec2<?> other);
	
	public String toString() {
		return String.format("Vec2<%s>(%s, %s)", x.getClass().getSimpleName(), x, y);
	}
	
}
