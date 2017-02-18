package psilox.node.ui;

import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;

public class Container extends Node {

	private Vec dimension;
	private Vec margin;
	
	public final Node bottomLeft = new Node();
	public final Node middleLeft = new Node();
	public final Node topLeft = new Node();
	public final Node bottomMiddle = new Node();
	public final Node center = new Node();
	public final Node topMiddle = new Node();
	public final Node bottomRight = new Node();
	public final Node middleRight = new Node();
	public final Node topRight = new Node();
	
	public final Node[] anchors = {
		bottomLeft, middleLeft, topLeft,
		bottomMiddle, center, topMiddle,
		bottomRight, middleRight, topRight
	};
	
	public Container(String tag, Vec dimension, Vec margin) {
		super(tag);
		this.dimension = dimension;
		this.margin = margin;
	}
	
	public void enteredTree() {
		addChildren(bottomLeft, middleLeft, topLeft, bottomMiddle, center, topMiddle, bottomRight, middleRight, topRight);
		lock();
		positionAnchors();
	}
	
	private void positionAnchors() {
		bottomLeft.anchorAt(Anchor.BOTTOM_LEFT, dimension, margin);
		middleLeft.anchorAt(Anchor.MIDDLE_LEFT, dimension, margin);
		topLeft.anchorAt(Anchor.TOP_LEFT, dimension, margin);
		bottomMiddle.anchorAt(Anchor.BOTTOM_MIDDLE, dimension, margin);
		center.anchorAt(Anchor.CENTER, dimension, margin);
		topMiddle.anchorAt(Anchor.TOP_MIDDLE, dimension, margin);
		bottomRight.anchorAt(Anchor.BOTTOM_RIGHT, dimension, margin);
		middleRight.anchorAt(Anchor.MIDDLE_RIGHT, dimension, margin);
		topRight.anchorAt(Anchor.TOP_RIGHT, dimension, margin);
	}
	
}
