package com.xydium.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xydium.geometry.Transform;
import com.xydium.input.Input;
import com.xydium.input.InputEvent;
import com.xydium.input.InputListener;
import com.xydium.rendering.Draw;

public class Node implements InputListener {

	private static long nextID = 0;
	
	private Transform transform;
	private Node parent;
	private Map<String, Node> children;
	private String tag;
	private boolean updating;
	private boolean visible;
	private boolean inputListening;
	private NodeTree tree;
	private long UUID;
	private String layer;
	
	public Node(String tag) {
		this.transform = new Transform();
		this.children = new HashMap<String, Node>();
		this.tag = tag;
		this.UUID = nextID++;
		this.layer = "default";
		setUpdating(true);
		setVisible(true);
		setInputListening(false);
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
				Draw.setDrawingLayer(child.getLayer());
				child.render();
			}
		}
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public void setTransform(Transform transform) {
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
	
	public void setChildren(Map<String, Node> children) {
		this.children = children;
	}
	
	public void addChild(Node child) {
		if(getTree().isIterating()) {
			getTree().queueAddition(this, child);
			return;
		}
		child.setParent(this);
		child.getTransform().setParent(getTransform());
		child.setTree(getTree());
		children.put(child.getTag(), child);
		child.added();
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
		child.getTransform().setParent(null);
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
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
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
	
	public void setTree(NodeTree tree) {
		this.tree = tree;
	}
	
	public long getUUID() {
		return UUID;
	}
	
	public String getLayer() {
		return layer;
	}
	
	public void setLayer(String layer) {
		this.layer = layer;
	}
	
}
