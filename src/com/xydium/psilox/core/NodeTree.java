package com.xydium.psilox.core;

import java.util.HashMap;
import java.util.Map;

import com.xydium.psilox.node.Node;
import com.xydium.psilox.rendering.Draw;

public class NodeTree {

	private boolean iterating; 
	
	private Map<Node, Node> queuedAdditions;
	private Map<Node, Node> queuedRemovals;
	
	private Node root;
	private Psilox psilox;
	
	public NodeTree(Psilox psilox) {
		this.queuedAdditions = new HashMap<Node, Node>();
		this.queuedRemovals = new HashMap<Node, Node>();
		this.psilox = psilox;
		this.root = new Node("root");
		root.setTree(this);
	}
	
	public void update() {
		iterating = true;
		root.updateChildren();
		iterating = false;
		applyChanges();
	}
	
	public void render() {
		iterating = true;
		root.renderChildren();
		iterating = false;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public Node getNode(String nodePath) {
		String[] childrenSequence = nodePath.split("\\.");
		Node n = root;
		for(String s : childrenSequence) {
			n = n.getChild(s);
			if(n == null) break;
		}
		return n;
	}
	
	public void queueAddition(Node parent, Node child) {
		queuedAdditions.put(parent, child);
	}
	
	public void queueRemoval(Node parent, Node child) {
		queuedRemovals.put(parent, child);
	}
	
	private void applyChanges() {
		for(Node parent : queuedAdditions.keySet()) {
			parent.addChild(queuedAdditions.get(parent));
		}
		for(Node parent : queuedRemovals.keySet()) {
			parent.removeChild(queuedAdditions.get(parent));
		}
		queuedAdditions.clear();
		queuedRemovals.clear();
	}
	
	public boolean isIterating() {
		return iterating;
	}
	
	public Psilox psilox() {
		return psilox;
	}
	
	public Window window() {
		return psilox.window();
	}
	
	public Draw draw() {
		return psilox.draw();
	}
	
	/*
	public Audio audio() {
		return psilox.audio();
	}
	
	public Input input() {
		return psilox.input();
	}
	*/
	
}
