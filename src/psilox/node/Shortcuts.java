package psilox.node;

import psilox.core.Psilox;
import psilox.math.Transform;
import psilox.math.Vec;
import psilox.utils.Log;

public interface Shortcuts {

	public Transform transform();
	
	default public Vec viewSize() {
		return new Vec(Psilox.config().width, Psilox.config().height);
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
	
	default public void anchorAt(Anchor anchor, Vec dimension, Vec margin) {
		transform().setPosition(new Vec(0));
		Vec p = pos();
		switch(anchor) {
		case BOTTOM_LEFT:
			p.add(new Vec(margin.x, margin.y));
			break;
		case BOTTOM_MIDDLE:
			p.add(new Vec(dimension.x / 2, margin.y));
			break;
		case BOTTOM_RIGHT:
			p.add(new Vec(dimension.x - margin.x, margin.y));
			break;
		case CENTER:
			p.add(new Vec(dimension.x / 2, dimension.y / 2));
			break;
		case MIDDLE_LEFT:
			p.add(new Vec(margin.x, dimension.y / 2));
			break;
		case MIDDLE_RIGHT:
			p.add(new Vec(dimension.x - margin.x, dimension.y / 2));
			break;
		case TOP_LEFT:
			p.add(new Vec(margin.x, dimension.y - margin.y));
			break;
		case TOP_MIDDLE:
			p.add(new Vec(dimension.x / 2, dimension.y - margin.y));
			break;
		case TOP_RIGHT:
			p.add(new Vec(dimension.x - margin.x, dimension.y - margin.y));
			break;
		default:
			break;
		}
	}
	
	default public void anchorPoint(Anchor anchor, Vec dimensions) {
		anchorAt(anchor, dimensions.scl(-1), Vec.ZERO);
	}
	
}
