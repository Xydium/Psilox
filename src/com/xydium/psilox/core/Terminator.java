package com.xydium.psilox.core;

import com.xydium.psilox.input.Input;
import com.xydium.psilox.input.Key;
import com.xydium.psilox.utilities.Log;

class Terminator {

	private int[] keys;
	
	public Terminator(String[] keyNames) {
		keys = new int[keyNames.length];
		for(int i = 0; i < keyNames.length; i++) {
			String s = keyNames[i];
			try {
				keys[i] = (int) Key.class.getField(s).get(null);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				Log.error("Termination Sequence contains Invalid Key");
			}
		}
	}
	
	public boolean shouldTerminate(Input input) {
		boolean res = true;
		for(int i : keys) {
			res = res && input.keyDown(i);
		}
		return res;
	}
	
}
