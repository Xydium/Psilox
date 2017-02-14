package psilox.core;

import psilox.graphics.Color;
import psilox.input.Input;
import psilox.utils.Log;

public class Config {

	public static final int UNLIMITED = 0;
	public static final int MANUAL = -1;
	
	public int width = 500, height = 500, ups = 60, fps = 60;
	public boolean fullscreen = false, clearscreen = true, doubleBuffer = true, undecorated = false, console = true, immediateMode = true;
	public String title = "Psilox", logLevel = "DEBUG";
	public int[] terminationSequence = {Input.LEFT_CONTROL, Input.LEFT_SHIFT, Input.Z};
	public Color clearColor = new Color(0.0f, 0.0f, 0.0f);
	
	public void logConfig(Psilox psilox) {
		Log.debug("###### Psilox Started, Reviewing Configuration...###");
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
		Log.debug("## Immediate-Mode GL: %b", immediateMode);
		Log.debug("## Using System Printstream: %b", console);
		Log.debug("## Log Level-of-Detail: %s", logLevel);
		String termSeq = "" + terminationSequence[0];
		for(int i = 1; i < terminationSequence.length; i++) {
			termSeq += "," + terminationSequence[i];
		}
		Log.debug("## Termination Sequence: %s", termSeq);
		Log.debug("####################################################");
	}
	
}
