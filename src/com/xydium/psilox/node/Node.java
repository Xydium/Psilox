package com.xydium.psilox.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xydium.psilox.core.NodeTree;
import com.xydium.psilox.core.Psilox;
import com.xydium.psilox.core.Window;
import com.xydium.psilox.input.Input;
import com.xydium.psilox.input.InputEvent;
import com.xydium.psilox.input.InputListener;
import com.xydium.psilox.math.Random;
import com.xydium.psilox.math.Transform;
import com.xydium.psilox.rendering.Draw;
import com.xydium.psilox.utilities.Log;

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
		this("" + Random.intVal());
	}
	
	public Node(String tag) {
		this.transform = new Transform();
		this.children = new HashMap<String, Node>();
		this.tag = tag;
		this.UID = nextID++;
		setUpdating(false);
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
				child.renderChildren();
				draw().setTransform(child.transform());
				child.render();
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
		if(getTree().isIterating()) {
			getTree().queueAddition(this, child);
			return;
		}
		child.setParent(this);
		child.transform().setParent(transform());
		child.setTree(getTree());
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
			input().addListener(this);
		} else {
			input().removeListener(this);
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
	
	public Window window() {
		return getTree().window();
	}
	
	public Draw draw() {
		return getTree().draw();
	}
	
	/*
	public Audio audio() {
		return getTree().audio();
	} */
	
	public Input input() {
		return getTree().input();
	}
	
}
