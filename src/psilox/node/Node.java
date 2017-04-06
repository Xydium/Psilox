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
	
	/**
	 * Constructs a basic node with the next ID, a
	 * relative position of (0,0,0), anchored in the
	 * bottom-left, with a rotation of zero, no children,
	 * updatable, and visible.
	 */
	public Node() {
		UID = nextID++;
		position = new Vec(0);
		anchor = Anchor.BL;
		rotation = 0;
		children = new ArrayList<Node>();
		updatable = true;
		visible = true;
	}
	
	/**
	 * Called by Psilox when a Node enters the tree,
	 * meaning is added to a parent.
	 */
	public void enteredTree() {}
	
	/**
	 * Called by Psilox when a Node exits the tree,
	 * meaning loses its parent.
	 */
	public void exitedTree() {}
	
	/**
	 * Called by Psilox if the node is updatable
	 * and if it is in the tree. Use this for logic changes.
	 */
	public void update() {}
	
	/**
	 * Called by Psilox if the node is visible and
	 * if it is in the tree. Use this for render calls.
	 */
	public void render() {}
	
	/**
	 * Called by Input if the node has been set as inputListening
	 * for every input event.
	 */
	public void receiveInput(InputEvent ev) {}
	
	/**
	 * Updates all of the children below this node in the tree.
	 */
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
	
	/**
	 * Renders all of the children below this node in the tree.
	 */
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

	/**
	 * Returns the world position of this node based
	 * on its position and all nodes above it.
	 * 
	 * @return
	 */
	public Vec globalPosition() {
		if(parent == null) {
			return position;
		}
		
		return position.rot(parent.globalRotation()).sum(parent.globalPosition());
	}
	
	/**
	 * Returns the world rotation of this node summed
	 * with all above it.
	 * 
	 * @return
	 */
	public float globalRotation() {
		if(parent == null) {
			return rotation;
		}
		
		return rotation + parent.globalRotation();
	}
	
	/**
	 * Returns this node's relative position adjusted for 
	 * its current anchor and dimensions.
	 * 
	 * @return
	 */
	public Vec anchoredPosition() {
		return position.dif(anchor.calculate(getDimensions(), Vec.ZERO));
	}
	
	/**
	 * Returns this node's global position adjusted for
	 * its current anchor and dimensions.
	 * 
	 * @return
	 */
	public Vec globalAnchoredPosition() {
		return globalPosition().dif(anchor.calculate(getDimensions(), Vec.ZERO));
	}
	
	/**
	 * Returns the dimensions of the node. Default is (0,0).
	 * Should be overriden by Nodes with actual dimensions.
	 * 
	 * @return
	 */
	public Vec getDimensions() {
		return new Vec(0);
	}
	
	/**
	 * Sets the anchor of the Node.
	 * 
	 * @param anchor
	 */
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}
	
	/**
	 * Returns the Node's parent.
	 * 
	 * @return
	 */
	public Node getParent() {
		return parent;
	}
	
	/**
	 * Sets the node's parent. Used by Psilox and will
	 * cause inconsistent behavior if used elsewhere.
	 * 
	 * @param parent
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	/**
	 * Adds the specified Node as a child of this,
	 * delayed until the end of the current update
	 * if necessary.
	 * 
	 * @param child
	 */
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
	
	/**
	 * Swaps the nth child of this node with the new one.
	 * 
	 * @param child
	 * @param index
	 */
	public void swapChild(Node child, int index) {
		if(locked || index >= children.size() || iterating) return;
		Node current = children.get(index);
		current.setParent(null);
		current.refreshRoot(null);
		current.exitedTree();
		
		child.setParent(this);
		child.refreshRoot(null);
		children.set(index, child);
		child.enteredTree();
	}
	
	/**
	 * Calls addChild on multiple nodes.
	 * 
	 * @param children
	 */
	public void addChildren(Node...children) {
		for(Node n : children) {
			addChild(n);
		}
	}
	
	/**
	 * Removes a node from this.
	 * 
	 * @param child
	 */
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
		child.setInputListening(false);
	}
	
	/**
	 * Calls removeChild on multiple nodes.
	 * 
	 * @param children
	 */
	public void removeChildren(Node...children) {
		for(Node n : children) {
			removeChild(n);
		}
	}
	
	/**
	 * Removes all children of the type given which extends Node.
	 * 
	 * @param type
	 */
	public void removeChildren(Class<? extends Node> type) {
		for(Node n : children) {
			if(n.getClass().equals(type)) {
				removeChild(n);
			}
		}
	}
	
	/**
	 * Removes all children.
	 */
	public void removeAllChildren() {
		iterating = true;
		for(Node n : children) {
			removeChild(n);
		}
		iterating = false;
	}
	
	/**
	 * Calls removeChild on this for this's parent.
	 */
	public void freeSelf() {
		if(parent != null) parent.removeChild(this);
	}
	
	/**
	 * Returns the nth Node that is a child of this node.
	 * 
	 * @param index
	 * @return
	 */
	public Node getChild(int index) {
		if(index < 0 || index >= children.size()) return null;
		return children.get(index);
	}
	
	/**
	 * Returns the nth Node cast as T.
	 * 
	 * @param index
	 * @param t
	 * @return
	 */
	public <T extends Node> T getChild(int index, Class<T> t) {
		return (T) getChild(index);
	}
	
	/**
	 * Gets the first child whose tag matches the one specified.
	 * 
	 * @param tag
	 * @return
	 */
	public Node getChild(String tag) {
		for(Node n : children) {
			if(n.tag.equals(tag)) {
				return n;
			}
		}
		return null;
	}
	
	/**
	 * Gets the first child whose tag matches the one specified
	 * cast as T.
	 * 
	 * @param tag
	 * @param t
	 * @return
	 */
	public <T extends Node> T getChild(String tag, Class<T> t) {
		return (T) getChild(tag);
	}
	
	/**
	 * Gets all children of type T.
	 * 
	 * @param type
	 * @return
	 */
	public <T extends Node> List<T> getChildren(Class<T> type) {
		List<T> nodes = new ArrayList<T>();
		for(Node n : children) {
			if(n.getClass().equals(type)) {
				nodes.add((T) n);
			}
		}
		return nodes;
	}
	
	/**
	 * Returns a new list of all the children of this node.
	 * This list may be safely modified externally.
	 * 
	 * @return
	 */
	public List<Node> getChildren() {
		return new ArrayList<Node>(children);
	}
	
	/**
	 * Returns the number of children this node has.
	 * 
	 * @return
	 */
	public int getChildCount() {
		return children.size();
	}
	
	/**
	 * Returns the same array used internally for storing children.
	 * THIS SHOULD BE USED AS READ-ONLY.
	 * 
	 * @return
	 */
	public List<Node> getChildrenUnsafe() {
		return children;
	}
	
	/**
	 * Returns a Node at the specified path.
	 * If the first character is a '/' then
	 * the path is absolute and starts at Psilox.root.
	 * 
	 * Otherwise, the path begins at this and works down
	 * the tree.
	 * 
	 * Paths are formed using Node tags, if they have one.
	 * Paths cannot be used with tagless nodes.
	 * 
	 * Absolute: "/node1/node2/node3"
	 * Relative (on root): "node1/node2/node3"
	 * 
	 * @param path
	 * @return
	 */
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
	
	/**
	 * Returns a call to nodePath cast as T.
	 * 
	 * @param path
	 * @param t
	 * @return
	 */
	public <T extends Node> T nodePath(String path, Class<T> t) { 
		return (T) nodePath(path);
	}
	
	/**
	 * Returns if the node is currently configured to receive
	 * input events.
	 * 
	 * @return
	 */
	public boolean isInputListening() {
		return inputListening;
	}
	
	/**
	 * Configures the node to receive input events.
	 * 
	 * @param inputListening
	 */
	public void setInputListening(boolean inputListening) {
		if(this.inputListening && !inputListening) {
			Input.removeListener(this);
		} else if(!this.inputListening && inputListening) {
			Input.addListener(this);
		}
		this.inputListening = inputListening;
	}
	
	/**
	 * Returns the root according to this node. This will only
	 * be Psilox.root if all parents of this node are also
	 * in the Psilox.root tree.
	 * 
	 * @return
	 */
	public Node getRoot() {
		return root;
	}
	
	/**
	 * Refreshes the root when this node's parentage
	 * status has been changed.
	 * 
	 * @param parentRoot
	 */
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
	
	/**
	 * Shorthand function for Log.info.
	 * 
	 * @param msg
	 */
	public void print(Object msg) {
		Log.info(msg.toString());
	}
	
	/**
	 * Shorthand function for Log.info with string formatting.
	 * 
	 * @param msg
	 * @param objects
	 */
	public void print(String msg, Object...objects) {
		Log.info(msg, objects);
	}
	
	/**
	 * Shorthand function for Log.error.
	 * 
	 * @param msg
	 */
	public void error(Object msg) {
		Log.error(msg.toString());
	}
	
	/**
	 * Shorthand function for Log.error with string formatting.
	 * 
	 * @param msg
	 * @param objects
	 */
	public void error(String msg, Object...objects) {
		Log.error(msg, objects);
	}
	
	/**
	 * Shorthand function for Log.error and printing the exception.
	 * 
	 * @param msg
	 * @param e
	 */
	public void error(String msg, Exception e) {
		Log.error(msg);
		Log.error(e);
	}
	
	/**
	 * Returns the string-representation of this Node in the form:
	 * {TAG} ({TYPE}), UID={UID}
	 */
	public String toString() {
		return String.format("%s (%s), UID=%s", tag.equals("") ? "{tagless}" : tag, getClass().getSimpleName(), UID);
	}
	
	/**
	 * Returns the pixel dimensions of the window of this Psilox runtime.
	 * 
	 * @return
	 */
	public Vec viewSize() {
		return new Vec(Psilox.config().width, Psilox.config().height);
	}
	
	/**
	 * Prints the node tree starting from the specified node.
	 * Pass Psilox.root to print the whole tree.
	 * 
	 * @param node
	 */
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
	
	/**
	 * Run by Psilox to refresh the node tree for any changes made
	 * while iterating. Not for end-user calling.
	 */
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
