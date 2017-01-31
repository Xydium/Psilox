package com.xydium.psilox.core;

import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.utilities.Log;

public class PsiloxConfig {

	public static final int UNLIMITED = 0;
	public static final int MANUAL = -1;
	
	public int width = 500, height = 500, ups = 60, fps = 60;
	public double scale = 1.0;
	public boolean fullscreen = false, clearscreen = true, doubleBuffer = true, console = true;
	public String title = "Psilox", logLevel = "DEBUG";
	public Color clearColor = new Color(0.0f, 0.0f, 0.0f);
	
	public void logConfig(Psilox psilox) {
		Log.debug("###### Psilox Started (Runtime %d), Reviewing Configuration", psilox.id());
		Log.debug("## Width: %d", width);
		Log.debug("## Height: %d", height);
		Log.debug("## Title: %s", title);
		Log.debug("## Update Rate: %d/sec", ups);
		Log.debug("## Frame Rate: %d/sec", fps);
		Log.debug("## Scale: %.2f", scale);
		Log.debug("## Fullscreen: %b", fullscreen);
		Log.debug("## Clearscreen: %b", clearscreen);
		Log.debug("## Clearcolor: %.2f, %.2f, %.2f, %.2f", clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Log.debug("## Doublebuffering: %b", doubleBuffer);
		Log.debug("## Using System Printstream: %b", console);
		Log.debug("## Log Level of Detail: %s", logLevel);
		Log.debug("######");
	}
	
}
