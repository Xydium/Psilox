package psilox.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import psilox.core.Config;
import psilox.core.NodeTree;
import psilox.core.Psilox;
import psilox.graphics.Draw;
import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputListener;
import psilox.math.Random;
import psilox.math.Transform;
import psilox.math.Vec;
import psilox.utils.Log;

public class Node implements InputListener {
		
	private static long nextID = 0;
	
	protected Transform transform;
	private Node parent;
	private NodeTree tree;
	private Map<String, Node> children;
	private String tag;
	private boolean updating;
	private boolean visible;
	private boolean inputListening;
	private long UID;
	
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
		setUpdating(true);
		setVisible(true);
	}
	
	public void added() {}
	public void removed() {}
	public void update() {}
	public void render() {}
	public void receiveInput(InputEvent ev) {}
	
	public void updateChildren() {
		for(Node child : getChildList()) {	
			if(child.isUpdating()) {
				child.updateChildren();
				child.update();
			}
		}
	}
	
	public void renderChildren() {
		for(Node child : getChildList()) {	
			if(child.isVisible()) {
				Draw.pushTransform(child.transform);
				child.renderChildren();
				child.render();
				Draw.popTransform();
			}
		}
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
	
	public Map<String, Node> getChildren() {
		return children;
	}
	
	public void addChild(Node child) {
		if(tree.isIterating()) {
			tree.queueAddition(this, child);
			return;
		}
		child.setParent(this);
		child.transform().setParent(transform());
		child.setTree(tree);
		if(children.putIfAbsent(child.getTag(), child) == null) {
			child.added();
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
		if(getTree().isIterating()) {
			getTree().queueRemoval(this, children.get(tag));
			return;
		}
		Node child = children.get(tag);
		child.setParent(null);
		child.transform().setParent(null);
		child.setTree(null);
		children.remove(tag);
		child.removed();
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
	
	public List<Node> getChildren(Class<? extends Node> type) {
		List<Node> nodes = new ArrayList<Node>();
		for(String tag : children.keySet()) {
			Node n = children.get(tag);
			if(n.getClass().equals(type)) {
				nodes.add(n);
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
	
	public boolean isUpdating() {
		return updating;
	}
	
	public void setUpdating(boolean updating) {
		this.updating = updating;
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
	
	public NodeTree getTree() {
		return tree;
	}
	
	public Node getRoot() {
		return tree.getRoot();
	}
	
	public void setTree(NodeTree tree) {
		this.tree = tree;
	}
	
	public long getUID() {
		return UID;
	}
	
	public String toString() {
		return String.format("Node %s: Type=%s, Parent=%s, UID=%s", tag, getClass().getSimpleName(), parent == null ? "null" : parent.getTag(), UID);
	}
	
	public Psilox psilox() {
		return getTree().psilox();
	}
	
	public Config config() {
		return getTree().config();
	}
	
	public Vec viewSize() {
		return new Vec(config().width, config().height);
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
	
	public Vec pos() {
		return transform.position();
	}
	
	public float rtn() {
		return transform.rotation();
	}
	
}
