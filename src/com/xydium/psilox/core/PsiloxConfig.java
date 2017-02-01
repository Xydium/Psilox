package com.xydium.psilox.core;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.xydium.psilox.math.Vec;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.utilities.Log;

public class PsiloxConfig {

	public static final int UNLIMITED = 0;
	public static final int MANUAL = -1;
	
	public int width = 500, height = 500, ups = 60, fps = 60;
	public boolean fullscreen = false, clearscreen = true, doubleBuffer = true, undecorated = false, console = true;
	public String title = "Psilox", logLevel = "DEBUG";
	public String[] terminationSequence = {"CONTROL", "SHIFT", "Z"};
	public Color clearColor = new Color(0.0f, 0.0f, 0.0f);
	
	public void logConfig(Psilox psilox) {
		Log.debug("###### Psilox Started (Runtime %d), Reviewing Configuration...###", psilox.id());
		Log.debug("## Width: %d", width);
		Log.debug("## Height: %d", height);
		Log.debug("## Title: %s", title);
		Log.debug("## Update Rate: %d/sec", ups);
		Log.debug("## Frame Rate: %d/sec", fps);
		Log.debug("## Full Screen: %b", fullscreen);
		Log.debug("## Clear Screen: %b", clearscreen);
		Log.debug("## Clear Color: %.2f, %.2f, %.2f, %.2f", clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Log.debug("## Double-Buffering: %b", doubleBuffer);
		Log.debug("## Undecorated: %b", undecorated);
		Log.debug("## Using System Printstream: %b", console);
		Log.debug("## Log Level-of-Detail: %s", logLevel);
		Log.debug("## Termination Sequence: %s", Stream.of(terminationSequence).collect(Collectors.joining(",","[","]")));
		Log.debug("################################################################");
	}
	
	public Vec monitorSize() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return new Vec(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
	}
	
}
