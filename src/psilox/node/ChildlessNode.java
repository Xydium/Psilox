package psilox.node;

public class ChildlessNode extends Node {

	public ChildlessNode() {
		this(null);
	}
	
	public ChildlessNode(String tag) {
		super(tag);
		children = null;
		lock();
	}
	
}
