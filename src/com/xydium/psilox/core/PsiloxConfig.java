package com.xydium.psilox.core;

import com.xydium.psilox.rendering.Color;

public class PsiloxConfig {

	public static final int UNLIMITED = 0;
	public static final int MANUAL = -1;
	
	public int width = 500, height = 500, ups = 60, fps = 60;
	public double scale = 1.0;
	public boolean fullscreen = false, clearscreen = true, console = true;
	public String title = "Psilox", logLevel = "DEBUG";
	public Color clearColor = new Color(0.0f, 0.0f, 0.0f);
	
}
