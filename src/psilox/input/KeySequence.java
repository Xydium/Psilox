package psilox.input;

import psilox.input.InputEvent.InputState;
import psilox.input.InputEvent.InputType;
import psilox.utility.Log;

public class KeySequence implements InputListener {

	private InputEvent[] sequence;
	private int position;
	private Function function;
	private boolean ignoreRelease = true;
	private boolean asCombination = false;
	
	public KeySequence(Function function, InputEvent... sequence) {
		assert(sequence.length > 0);
		this.function = function;
		this.sequence = sequence;
		Input.addListener(this);
	}
	
	public KeySequence(Function function, int... keys) {
		assert(keys.length > 0);
		InputEvent[] sequence = new InputEvent[keys.length];
		for(int i = 0; i < sequence.length; i++) {
			sequence[i] = new InputEvent(InputType.KEYBOARD, keys[i], InputState.PRESSED);
		}
		this.function = function;
		this.sequence = sequence;
		Input.addListener(this);
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
		Input.removeListener(this);
	}
	
	public void receiveInput(InputEvent ev) {
		if(ev.state == InputState.MOVED) return;
		
		if(asCombination) {
			boolean combined = true;
			for(InputEvent ie : sequence) {
				if(ie.type == InputType.KEYBOARD) {
					combined &= Input.keyDown(ie.key); 
				} else {
					combined &= Input.buttonDown(ie.key);
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
	
}
