package psilox.node;

import java.util.ArrayList;
import java.util.List;

import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Shader;
import psilox.graphics.Texture;
import psilox.math.Anchor;
import psilox.math.Rect;
import psilox.math.Vec;
import psilox.utility.Log;

public class Node {

	private static long nextID = 0;
	
	public final long RUID;
	public final String tag;
	
	public Vec position;
	public Vec anchor;
	public float rotation;
	public Vec scale;
	public Vec dimensions;
	public Texture texture;
	public Shader shader;
	public Rect textureRegion;
	public Color modulate;
	
	private Node parent;
	private Node root;
	
	private final ArrayList<Node> children;
	
	public boolean updatable;
	public boolean visible;
	public boolean locked;
	private boolean iterating;
	
	public Node(String tag) {
		this.RUID = nextID++;
		this.tag = tag;
		this.position = new Vec(0);
		this.anchor = Anchor.MIDDLE;
		this.rotation = 0;
		this.scale = new Vec(1);
		this.dimensions = new Vec(1);
		this.texture = null;
		this.shader = null;
		this.textureRegion = null;
		this.modulate = Color.WHITE;
		this.children = new ArrayList<Node>();
		this.updatable = true;
		this.visible = true;
	}
	
	public void enteredTree() {}
	public void exitedTree() {}
	public void update() {}
	
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
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void addChild(Node child) {
		if(child == null) {
			Log.warning("Attempted to add null child to %s", toString());
			return;
		}
		if(locked) {
			Log.warning("Attempted to add child to locked %s", toString());
			return;
		}
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
		if(child == null) {
			Log.warning("Attempted to remove null child from %s", toString());
		}
		if(locked) {
			Log.warning("Attempted to remove child from locked %s", toString());
			return;
		}
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
	
	public void removeAllChildren() {
		iterating = true;
		for(Node n : children) {
			removeChild(n);
		}
		iterating = false;
	}
	
	public void freeSelf() {
		if(parent != null) parent.removeChild(this);
	}
	
	public Node getChild(int index) {
		if(index < 0 || index >= children.size()) return null;
		return children.get(index);
	}
	
	public <T extends Node> T getChild(int index, Class<T> t) {
		return (T) getChild(index);
	}
	
	public Node getChild(String tag) {
		for(Node n : children) {
			if(n.tag.equals(tag)) {
				return n;
			}
		}
		return null;
	}
	
	public <T extends Node> T getChild(String tag, Class<T> t) {
		return (T) getChild(tag);
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

	public List<Node> getChildrenUnsafe() {
		return children;
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	public Node nodePath(String path) {
		Node current;
		
		if(path.charAt(0) == '/') {
			current = Psilox.root;
			path = path.substring(1);
		} else {
			current = this;
		}
		
		String[] nodes = path.split("/");
		
		for(String n : nodes) {
			Node next = current.getChild(n);
			if(next != null) {
				current = next;
			} else {
				return null;
			}
		}
		
		return current;
	}
	
	public <T extends Node> T nodePath(String path, Class<T> t) { 
		return (T) nodePath(path);
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
	
	public void print(Object msg) { Log.info(msg.toString()); }
	public void print(String msg, Object...objects) { Log.info(msg, objects); }
	public void error(Object msg) { Log.error(msg.toString()); }
	public void error(String msg, Object...objects) { Log.error(msg, objects); }
	public void error(String msg, Exception e) { Log.error(msg); Log.error(e); }
	
	public String toString() {
		return String.format("%s (%s), UID=%s", tag, getClass().getSimpleName(), RUID);
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
				Log.warning("Error applying queued change parent=%s, child=%s, adding=%b", p.parent.RUID, p.child.RUID, p.adding);
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
	
}
