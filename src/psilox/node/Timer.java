package psilox.node;

import psilox.input.Function;

public class Timer extends Node {

	private float duration;
	private float elapsed;
	private boolean oneshot;
	private Function connection;
	
	public Timer(String tag, float duration, boolean oneshot, Function connection) {
		super(tag);
		this.duration = duration;
		this.oneshot = oneshot;
		this.connection = connection;
		setUpdating(false);
		setVisible(false);
	}
	
	public void update() {
		elapsed += psilox().deltaTime();
		
		if(elapsed >= duration) {
			connection.execute();
			
			if(oneshot) {
				stop();
			} else {
				elapsed = 0;
			}
		}
	}
	
	public Timer start() {
		elapsed = 0;
		setUpdating(true);
		return this;
	}
	
	public void stop() {
		setUpdating(false);
	}
	
}
