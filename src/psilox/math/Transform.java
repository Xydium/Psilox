package psilox.math;

public class Transform {

private Transform parent;
	
	private Vec position;
	private float rotation;
	
	public Transform(Transform parent, Vec position, float rotation) {
		this.parent = parent;
		this.position = position;
		this.rotation = rotation;
	}
	
	public Transform(Vec position, float rotation) {
		this(null, position, rotation);
	}
	
	public Transform(Vec position) {
		this(position, 0);
	}
	
	public Transform() {
		this(new Vec(0f));
	}
	
	public Vec position() {
		return position;
	}
	
	public void setPosition(Vec position) {
		this.position = position;
	}
	
	public Vec positionGlobal() {
		if(parent == null) {
			return position;
		}
		
		return position.rot(parent.rotationGlobal()).sum(parent.positionGlobal());
	}
	
	public float rotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public float rotationGlobal() {
		if(parent == null) {
			return rotation;
		}
		
		return parent.rotationGlobal() + rotation;
	}
	
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	public void translate(Vec delta) {
		position.add(delta);
	}
	
	public void rotate(float delta) {
		rotation += delta;
	}
	
	public Mat4 toMatrix() {
		if(position.equals(Vec.ZERO)) {
			return Mat4.identity();
		} else if(rotation == 0) {
			return Mat4.translate(position);
		} else {
			return Mat4.translate(position).multiply(Mat4.rotate(rotation, position));
		}
	}
	
}
