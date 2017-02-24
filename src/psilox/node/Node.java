package psilox.node;

import java.util.ArrayList;
import java.util.List;

import psilox.core.Psilox;
import psilox.graphics.Draw;
import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputListener;
import psilox.math.Mat4;
import psilox.math.Vec;
import psilox.utils.Log;

public class Node implements InputListener {
	
	private static long nextID = 0;

	public final long UID;
	public String tag = "";
	
	public final Vec position;
	private Anchor anchor;
	public float rotation;
	
	private Node parent;
	private Node root;
	
	private final ArrayList<Node> children;
	
	public boolean updatable;
	public boolean visible;
	private boolean inputListening;
	public boolean locked;
	private boolean iterating;
	
	public Node() {
		UID = nextID++;
		position = new Vec(0);
		anchor = Anchor.BL;
		rotation = 0;
		children = new ArrayList<Node>();
		updatable = true;
		visible = true;
	}
	
	public void enteredTree() {}
	public void exitedTree() {}
	public void update() {}
	public void render() {}
	public void receiveInput(InputEvent ev) {}
	
	public void updateChildren() {
		iterating = true;
		for(Node c : children) {
			if(c.updatable) {
				c.update();
				c.updateChildren();
			}
		}
		iterating = false;
	}
	
	public void renderChildren() {
		iterating = true;
		for(Node c : children) {
			if(c.visible) {
				Draw.pushTransform(Mat4.transform(c.anchoredPosition(), c.rotation));
				c.render();
				c.renderChildren();
				Draw.popTransform();
			}
		}
		iterating = false;
	}
	
	public Vec globalPosition() {
		if(parent == null) {
			return position;
		}
		
		return position.rot(parent.globalRotation()).sum(parent.globalPosition());
	}
	
	public float globalRotation() {
		if(parent == null) {
			return rotation;
		}
		
		return rotation + parent.globalRotation();
	}
	
	public Vec anchoredPosition() {
		return position.dif(anchor.calculate(getDimensions(), Vec.ZERO));
	}
	
	public Vec getDimensions() {
		return new Vec(0);
	}
	
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void addChild(Node child) {
		if(locked) return;
		if(iterating) {
			queueAddition(this, child);
			return;
		}
		child.setParent(this);
		child.refreshRoot(null);
		children.add(child);
		child.enteredTree();
	}
	
	public void addChildren(Node...children) {
		for(Node n : children) {
			addChild(n);
		}
	}
	
	public void removeChild(Node child) {
		if(locked) return;
		if(iterating) {
			queueRemoval(this, child);
			return;
		}
		child.setParent(null);
		child.refreshRoot(null);
		children.remove(child);
		child.exitedTree();
	}
	
	public void removeChildren(Node...children) {
		for(Node n : children) {
			removeChild(n);
		}
	}
	
	public void removeChildren(Class<? extends Node> type) {
		for(Node n : children) {
			if(n.getClass().equals(type)) {
				removeChild(n);
			}
		}
	}
	
	public void freeSelf() {
		parent.removeChild(this);
	}
	
	public Node getChild(int index) {
		return children.get(index);
	}
	
	public <T extends Node> List<T> getChildren(Class<T> type) {
		List<T> nodes = new ArrayList<T>();
		for(Node n : children) {
			if(n.getClass().equals(type)) {
				nodes.add((T) n);
			}
		}
		return nodes;
	}
	
	public List<Node> getChildren() {
		return new ArrayList<Node>(children);
	}
	
	public boolean isInputListening() {
		return inputListening;
	}
	
	public void setInputListening(boolean inputListening) {
		if(this.inputListening && !inputListening) {
			Input.removeListener(this);
		} else if(!this.inputListening && inputListening) {
			Input.addListener(this);
		}
		this.inputListening = inputListening;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void refreshRoot(Node parentRoot) {
		if(parentRoot != null) {
			root = parentRoot;
			return;
		}
		
		if(parent == null) {
			root = this;
		} else {
			root = parent;
			while(root.parent != null) {
				root = root.parent;
			}
		}
		
		for(Node n : children) {
			n.refreshRoot(root);
		}
	}
	
	public void print(Object msg) {
		Log.info(msg.toString());
	}
	
	public void print(String msg, Object...objects) {
		Log.info(msg, objects);
	}
	
	public void error(String msg, Exception e) {
		Log.error(msg);
		Log.error(e);
	}
	
	public String toString() {
		return String.format("%s, UID=%s", tag.equals("") ? getClass().getSimpleName() : tag, UID);
	}
	
	public Vec viewSize() {
		return new Vec(Psilox.config().width, Psilox.config().height);
	}
	
	public static void printTree(Node node) {
		printTree(node, "");
	}
	
	private static void printTree(Node node, String indent) {
		Log.info(indent + node.toString());
		for(Node n : node.children) {
			printTree(n, indent + "    ");
		}
	}
	
	private static List<NodePair> queuedChanges = new ArrayList<NodePair>();
	
	private static void queueAddition(Node parent, Node child) {
		queuedChanges.add(new NodePair(parent, child, true));
	}
	
	private static void queueRemoval(Node parent, Node child) {
		queuedChanges.add(new NodePair(parent, child, false));
	}
	
	public static void applyChanges() {
		for(NodePair p : queuedChanges) {
			try {
				if(p.adding) {
					p.parent.addChild(p.child);
				} else {
					p.parent.removeChild(p.child);
				}
			} catch(Exception e) {
				Log.warning("Error applying queued change parent=%s, child=%s, adding=%b", p.parent.UID, p.child.UID, p.adding);
			}
		}
		queuedChanges.clear();
	}
	
	private static class NodePair {
		public Node parent;
		public Node child;
		public boolean adding;
		
		public NodePair(Node p, Node c, boolean a) {
			this.parent = p;
			this.child = c;
			this.adding = a;
		}
	}

	public static enum Anchor {
		BL(0, 0, 1, 1), 
		ML(0, .5, 1, 0), 
		TL(0, 1, 1, -1), 
		BM(.5, 0, 0, 1), 
		MM(.5, .5, 0, 0), 
		TM(.5, 1, 0, -1), 
		BR(1, 0, -1, 1), 
		MR(1, .5, -1, 0), 
		TR(1, 1, -1, -1);
		
		private Vec dimMult, margMult;
		
		private Anchor(double dx, double dy, double mx, double my) {
			dimMult = new Vec(dx, dy, 0);
			margMult = new Vec(mx, my, 0);
		}
		
		public Vec calculate(Vec dim, Vec marg) {
			return dim.pro(dimMult).sum(marg.pro(margMult)); 
		}
	}
	
}
