package psilox.node.ui;

import psilox.math.Vec;
import psilox.node.Node;

public class Container extends Node {

	private Vec dimensions;
	private Vec margins;
	
	public final Node bottomLeft = new Node();
	public final Node middleLeft = new Node();
	public final Node topLeft = new Node();
	public final Node bottomMiddle = new Node();
	public final Node center = new Node();
	public final Node topMiddle = new Node();
	public final Node bottomRight = new Node();
	public final Node middleRight = new Node();
	public final Node topRight = new Node();
	
	{
		bottomLeft.tag = "BottomLeft";
		middleLeft.tag = "MiddleLeft";
		topLeft.tag = "TopLeft";
		bottomMiddle.tag = "BottomMiddle";
		center.tag = "Center";
		topMiddle.tag = "TopMiddle";
		bottomRight.tag = "BottomRight";
		middleRight.tag = "MiddleRight";
		topRight.tag = "TopRight";
	}
	
	public final Node[] anchors = {
		bottomLeft, middleLeft, topLeft,
		bottomMiddle, center, topMiddle,
		bottomRight, middleRight, topRight
	};
		
	public final Node[][] anchorsMat = {
		{topLeft, topMiddle, topRight},
		{middleLeft, center, middleRight},
		{bottomLeft, bottomMiddle, bottomRight}
	};
	
	public Container(Vec dimensions, Vec margins) {
		super();
		this.dimensions = dimensions;
		this.margins = margins;
	}
	
	public void enteredTree() {
		addChildren(anchors);
		locked = true;
		positionAnchors();
	}
	
	private void positionAnchors() {
		for(int i = 0; i < anchors.length; i++) {
			anchors[i].position.set(Anchor.values()[i].calculate(dimensions, margins));
		}
	}
	
	public Vec getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Vec dimensions) {
		this.dimensions = dimensions;
		positionAnchors();
	}
	
	public Vec getMargins() {
		return margins;
	}
	
	public void setMargins(Vec margins) {
		this.margins = margins;
		positionAnchors();
	}
	
}
