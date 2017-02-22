package psilox.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import psilox.graphics.Draw;
import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputListener;
import psilox.math.Transform;
import psilox.utils.Log;

public class Node implements InputListener, Shortcuts {
	
	private static long nextID = 0;
	
	protected Transform transform;
	private Node parent;
	private Node root;
	Map<String, Node> children;
	private String tag;
	private boolean updatable;
	private boolean visible;
	private boolean inputListening;
	private long UID;
	private boolean locked;
	private boolean iterating;
	
	public Node() {
		this(null);
	}
	
	public Node(String tag) {
		if(tag == null) {
			tag = getClass().getSimpleName() + (nextID + 1);
		}
		this.transform = new Transform();
		this.children = new HashMap<String, Node>();
		this.tag = tag;
		this.UID = nextID++;
		refreshRoot();
		setUpdatable(true);
		setVisible(true);
	}
	
	public void enteredTree() {}
	public void exitedTree() {}
	public void update() {}
	public void render() {}
	public void receiveInput(InputEvent ev) {}
	
	public void updateChildren() {
		iterating = true;
		for(Node child : getChildList()) {	
			if(child.isUpdatable()) {
				child.updateChildren();
				child.update();
			}
		}
		iterating = false;
	}
	
	public void renderChildren() {
		iterating = true;
		for(Node child : getChildList()) {	
			if(child.isVisible()) {
				Draw.pushTransform(child.transform);
				child.renderChildren();
				child.render();
				Draw.popTransform();
			}
		}
		iterating = false;
	}
	
	public Transform transform() {
		return transform;
	}
	
	public void setTransform(Transform transform) {
		if(parent != null) {
			transform.setParent(parent.transform);
		}
		this.transform = transform;
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
		child.transform().setParent(transform());
		child.refreshRoot();
		if(children.putIfAbsent(child.getTag(), child) == null) {
			child.enteredTree();
		}
		else 
			Log.error(String.format("Failed to add duplicate tagged node %s to %s", child.getTag(), tag));
	}
	
	public void addChildren(Node... children) {
		for(Node n : children) {
			addChild(n);
		}
	}
	
	public void removeChild(String tag) {
		if(locked) return;
		if(iterating) {
			queueRemoval(this, children.get(tag));
			return;
		}
		Node child = children.get(tag);
		child.setParent(null);
		child.refreshRoot();
		child.transform().setParent(null);
		children.remove(tag);
		child.exitedTree();
	}
	
	public void removeChildren(String... tags) {
		for(String t : tags) {
			removeChild(t);
		}
	}
	
	public void removeChild(Node child) {
		removeChild(child.getTag());
	}
	
	public void removeChildren(Node... children) {
		for(Node n : children) {
			removeChild(n);
		}
	}
	
	public void removeChildren(Class<? extends Node> type) {
		for(Node n : getChildList()) { 
			if(n.getClass().equals(type)) {
				removeChild(n);
			}
		}
	}
	
	public Node getChild(String tag) {
		return children.get(tag);
	}
	
	public void freeSelf() {
		getParent().removeChild(this.getTag());
	}
	
	public List<Node> getChildren(String tagPart) {
		List<Node> nodes = new ArrayList<Node>();
		for(String tag : children.keySet()) {
			if(tag.contains(tagPart)) {
				nodes.add(children.get(tag));
			}
		}
		return nodes;
	}
	
	public <T extends Node> List<T> getChildren(Class<T> type) {
		List<T> nodes = new ArrayList<T>();
		for(String tag : children.keySet()) {
			Node n = children.get(tag);
			if(n.getClass().equals(type)) {
				nodes.add((T) n);
			}
		}
		return nodes;
	}
	
	public List<Node> getChildList() {
		List<Node> nodes = new ArrayList<Node>();
		for(String tag : children.keySet()) {
			nodes.add(children.get(tag));
		}
		return nodes;
	}
	
	public Node getNode(String nodePath) {
		String[] childrenSequence = nodePath.split("\\.");
		Node n = this;
		for(String s : childrenSequence) {
			n = n.getChild(s);
			if(n == null) break;
		}
		return n;
	}
	
	public String getTag() {
		return tag;
	}
	
	public boolean isUpdatable() {
		return updatable;
	}
	
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isInputListening() {
		return inputListening;
	}
	
	public void setInputListening(boolean inputListening) {
		this.inputListening = inputListening;
		if(inputListening) {
			Input.addListener(this);
		} else {
			Input.removeListener(this);
		}
	}
	
	public void lock() {
		locked = true;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void refreshRoot() {
		if(parent == null) {
			root = this;
		} else {
			root = parent;
			while(root.getParent() != null) {
				root = root.getParent();
			}
		}
		
		for(Node n : getChildList()) {
			n.refreshRoot();
		}
	}
	
	public long getUID() {
		return UID;
	}
	
	public String toString() {
		return String.format("Node %s: Type=%s, Parent=%s, UID=%s", tag, getClass().getSimpleName(), parent == null ? "null" : parent.getTag(), UID);
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
				Log.warning("Error applying queued change parent=%s, child=%s, adding=%b", p.parent.getTag(), p.child.getTag(), p.adding);
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
