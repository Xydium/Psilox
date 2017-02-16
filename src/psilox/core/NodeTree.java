package psilox.core;

import java.util.ArrayList;
import java.util.List;

import psilox.node.Node;
import psilox.utils.Log;

public class NodeTree {

	private boolean iterating;
	
	private List<NodePair> queuedAdditions;
	private List<NodePair> queuedRemovals;
	
	private Node root;
	private Psilox psilox;
	
	public NodeTree(Psilox psilox) {
		this.queuedAdditions = new ArrayList<NodePair>();
		this.queuedRemovals = new ArrayList<NodePair>();
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
		queuedAdditions.add(new NodePair(parent, child));
	}
	
	public void queueRemoval(Node parent, Node child) {
		queuedRemovals.add(new NodePair(parent, child));
	}
	
	private void applyChanges() {
		for(NodePair pair : queuedAdditions) {
			if(pair.child.getTree() != null) continue;
			try {
				pair.parent.addChild(pair.child);
			} catch (Exception e) {
				Log.warning("Something went wrong when deferred adding %s to %s.", pair.child.getTag(), pair.parent.getTag());
			}
		}
		for(NodePair pair : queuedRemovals) {
			if(pair.child.getTree() == null) continue;
			try {
				pair.parent.removeChild(pair.child);
			} catch (Exception e) {
				Log.warning("Something went wrong when deferred removing %s from %s.", pair.child.getTag(), pair.parent.getTag());
			}
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
	
	public Config config() {
		return psilox.config();
	}
	
	private class NodePair {
		public Node parent;
		public Node child;
		
		public NodePair(Node p, Node c) {
			this.parent = p;
			this.child = c;
		}
	}
	
}
