package psilox.node.utility;

import java.util.ArrayList;
import java.util.List;

import psilox.core.Psilox;
import psilox.input.Function;
import psilox.math.Mathf;
import psilox.node.Node;

public class Interpolator extends Node {

	private List<KeyFrame> keyFrames;
	private InterpolatorCallback callback;
	private Function onEnd = () -> {};
	private int currentFrame;
	private float elapsedTime;
	private boolean discrete;
	private boolean oneShot;
	private boolean cosLerp;
	
	public Interpolator(InterpolatorCallback callback) {
		super();
		keyFrames = new ArrayList<KeyFrame>();
		this.callback = callback;
		currentFrame = 0;
		elapsedTime = 0;
		updatable = false;
		oneShot = true;
	}
	
	public void update() {
		elapsedTime += Psilox.deltaTime;
		
		boolean advanced = false;
		
		if(elapsedTime > keyFrames.get(currentFrame + 1).time) {
			currentFrame++;
			advanced = true;
			if(currentFrame == keyFrames.size() - 1) {
				callback.lerp(keyFrames.get(currentFrame).value);
				onEnd.execute();
				if(oneShot) {
					stop();
				} else {
					currentFrame = 0;
					elapsedTime = 0;
				}
				return;
			}
		}
		
		if(discrete || !updatable) {
			if(advanced) {
				callback.lerp(keyFrames.get(currentFrame).value);
			}
		} else {
			callback.lerp(calculateValue());
		}
	}
	
	public float calculateValue() {
		KeyFrame a = keyFrames.get(currentFrame);
		KeyFrame b = keyFrames.get(currentFrame + 1);
		if(a.value == b.value) return b.value;
		
		if(cosLerp) {
			float mu = (elapsedTime - a.time) / (b.time - a.time);
			float mu2 = (1 - Mathf.cos(mu * Math.PI)) / 2;
			return(a.value * (1 - mu2) + b.value * mu2);
		} else {
			float av = a.value * (1 - ((elapsedTime - a.time) / (b.time - a.time)));
			float bv = b.value * ((elapsedTime - a.time) / (b.time - a.time));
			
			return av + bv;
		}
	}
	
	public void start() {
		elapsedTime = 0;
		currentFrame = 0;
		updatable = true;
		callback.lerp(keyFrames.get(currentFrame).value);
	}
	
	public void stop() {
		updatable = false;
	}
	
	public boolean isOneShot() {
		return oneShot;
	}
	
	public void setOneShot(boolean oneShot) {
		this.oneShot = oneShot;
	}
	
	public boolean isCosineInterpolated() {
		return cosLerp;
	}
	
	public void setCosineInterpolated(boolean cosLerp) {
		this.cosLerp = cosLerp;
	}
	
	public void setOnEnd(Function onEnd) {
		this.onEnd = onEnd;
	}
	
	public void addKeyFrame(KeyFrame frame) {
		if(!keyFrames.isEmpty()) {
			assert(frame.time > keyFrames.get(keyFrames.size() - 1).time);
		}
		keyFrames.add(frame);
	}
	
	public void addKeyFrame(float time, float value) {
		addKeyFrame(new KeyFrame(time, value));
	}
	
	public void addKeyFrames(float... frames) {
		assert(frames.length % 2 == 0);
		for(int i = 0; i < frames.length; i+=2) {
			addKeyFrame(frames[i], frames[i + 1]);
		}
	}
	
	public static class KeyFrame {
		
		public final float time;
		public final float value;
		
		public KeyFrame(float time, float value) {
			this.time = time;
			this.value = value;
		}
		
	}
	
	public static interface InterpolatorCallback {
		public void lerp(float value);
	}
	
}
