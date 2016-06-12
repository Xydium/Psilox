package com.xydium.node.basics;

import java.awt.Color;
import java.awt.Font;

import com.xydium.node.Node;
import com.xydium.rendering.Draw;

public class Label extends Node {

	private String text;
	private Color color;
	private Font font;
	private boolean centered;
	
	public Label(String tag, String text, Color color, Font font) {
		super(tag);
		this.text = text;
		this.color = color;
		this.font = font;
		this.centered = true;
		setUpdating(false);
	}

	public void render() {
		if(centered) {
			Draw.centeredText(text, getTransform().position(), color, font);
		} else {
			Draw.text(text, getTransform().position(), color, font);
		}
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
	
	public boolean isCentered() {
		return centered;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
}
