package psilox.demo.ui;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;
import psilox.node.ui.Container;

public class UIDemo extends Node {

	private Container container;
	private Point[] points;
	
	public void enteredTree() {
		addChild(container = new Container("container", viewSize(), new Vec(5)));
		
		points = new Point[container.anchors.length];
		
		for(int i = 0; i < points.length; i++) {
			container.anchors[i].addChild(points[i] = new Point(Color.RED, 10));
			points[i].anchorPoint(Anchor.values()[i], new Vec(10));
		}
	}
	
	public static void main(String[] args) {
		Config c = new Config();
		new Psilox(c).start(new UIDemo());
	}
	
}

class Point extends Node {
	
	private Color c;
	private Vec size;
	
	public Point(Color c, int size) {
		super();
		this.c = c;
		this.size = new Vec(size);
	}
	
	public void render() {
		Draw.quad(c, Vec.ZERO, size);
	}
	
}