package com.xydium.psilox.input;

import com.xydium.psilox.input.InputEvent.InputState;
import com.xydium.psilox.input.InputEvent.InputType;
import com.xydium.psilox.utilities.Log;

public class KeySequence implements InputListener {

	private InputEvent[] sequence;
	private int position;
	private Function function;
	private Input input;
	private boolean ignoreRelease = true;
	private boolean asCombination = false;
	
	public KeySequence(Input input, Function function, InputEvent...sequence) {
		assert(sequence.length > 0);
		this.function = function;
		this.sequence = sequence;
		this.input = input;
		input.addListener(this);
	}
	
	public KeySequence(Input input, Function function, int... keys) {
		assert(keys.length > 0);
		InputEvent[] sequence = new InputEvent[keys.length];
		for(int i = 0; i < sequence.length; i++) {
			sequence[i] = new InputEvent(InputType.KEYBOARD, keys[i], InputState.PRESSED, null);
		}
		this.function = function;
		this.sequence = sequence;
		this.input = input;
		input.addListener(this);
	}
	
	public void setIgnoreRelease(boolean process) {
		ignoreRelease = process;
	}
	
	public KeySequence asCombination() {
		asCombination = true;
		return this;
	}
	
	protected void finalize() throws Throwable {
		super.finalize();
		input.removeListener(this);
	}
	
	public void receiveInput(InputEvent ev) {
		if(ev.state == InputState.MOVED || ev.state == InputState.DRAGGED) return;
		
		if(asCombination) {
			boolean combined = true;
			for(InputEvent ie : sequence) {
				if(ie.type == InputType.KEYBOARD) {
					combined &= input.keyDown(ie.key); 
				} else {
					combined &= input.buttonDown(ie.key);
				}
			}
			if(combined) {
				function.execute();
			}
			return;
		}
		
		if(ignoreRelease && ev.state == InputState.RELEASED && position > 0 && ev.key == sequence[position - 1].key) {
			return;
		}
		
		if(ev.matches(sequence[position])) {
			position++;
			if(position == sequence.length) {
				function.execute();
				position = 0;
			}
		} else {
			position = 0;
		}
	}
	
	public static int[] keysFromNames(String[] names) {
		int[] keys = new int[names.length];
		for(int i = 0; i < keys.length; i++) {
			String s = names[i];
			try {
				keys[i] = (int) Key.class.getField(s).get(null);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				Log.error("Termination Sequence contains Invalid Key");
			}
		}
		return keys;
	}
	
}
