package psilox.node;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.math.Transform;
import psilox.math.Vec;
import psilox.utils.Log;

public interface Shortcuts {
	
	public Psilox psilox();
	public Transform transform();
	
	default public Config config() {
		return psilox().config();
	}
	
	default public Vec viewSize() {
		return new Vec(config().width, config().height);
	}
	
	default public void print(Object msg) {
		Log.info(msg.toString());
	}
	
	default public void print(String msg, Object...objects) {
		Log.info(msg, objects);
	}
	
	default public void error(String msg, Exception e) {
		Log.error(msg);
		Log.error(e);
	}
	
	default public Vec pos() {
		return transform().position();
	}
	
	default public float rtn() {
		return transform().rotation();
	}
	
	default public void setLayer(float layer) {
		pos().z = layer;
	}
	
}
