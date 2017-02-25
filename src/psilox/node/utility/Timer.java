package psilox.node.utility;

import psilox.core.Psilox;
import psilox.input.Function;
import psilox.node.Node;

public class Timer extends Node {

	private float duration;
	private float elapsed;
	private boolean oneshot;
	private Function connection;
	
	public Timer(float duration, boolean oneshot, Function connection) {
		super();
		this.duration = duration;
		this.oneshot = oneshot;
		this.connection = connection;
		updatable = false;
		visible = false;
	}
	
	public void update() {
		elapsed += Psilox.deltaTime();
		
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
		updatable = true;
		return this;
	}
	
	public void stop() {
		updatable = false;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getElapsed() {
		return elapsed;
	}

	public void setElapsed(float elapsed) {
		this.elapsed = elapsed;
	}

	public boolean isOneshot() {
		return oneshot;
	}

	public void setOneshot(boolean oneshot) {
		this.oneshot = oneshot;
	}

	public Function getConnection() {
		return connection;
	}

	public void setConnection(Function connection) {
		this.connection = connection;
	}
	
}
