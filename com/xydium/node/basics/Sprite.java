package com.xydium.node.basics;

import com.xydium.node.Node;
import com.xydium.rendering.Draw;
import com.xydium.resources.Texture;

public class Sprite extends Node {

	private Texture texture;
	
	public Sprite(String tag, Texture texture) {
		super(tag);
		this.texture = texture;
		setUpdating(false);
	}
	
	public Sprite(String tag, String texturePath) {
		this(tag, new Texture(texturePath));
	}
	
	public void render() {
		Draw.texture(texture, getTransform());
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
}
