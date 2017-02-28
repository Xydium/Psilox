package psilox.utils;

public class Pointer<Type> {

	private Type data;
	private PointerUpdateListener listener;
	
	public Pointer(Type data) {
		this.data = data;
	}
	
	public Type get() {
		return data;
	}
	
	public void set(Type data) {
		this.data = data;
		if(listener != null) listener.changed(this);
	}

	public void subscribe(PointerUpdateListener listener) {
		this.listener = listener;
	}
	
	public void unsubscribe(PointerUpdateListener listener) {
		listener = null;
	}
	
	public static interface PointerUpdateListener {
		default public void changed(Pointer p) {};	
	}
	
	public String toString() {
		return get().toString();
	}
	
	public static class IntPointer extends Pointer<Integer> {
		
		public IntPointer(int data) {
			super(data);
		}
		
		public void add(int i) {
			set(get() + i);
		}
		
		public void sub(int i) {
			set(get() - i);
		}
		
		public void mul(int i) {
			set(get() * i);
		}
		
		public void div(int i) {
			set(get() / i);
		}
		
		public void inc() {
			add(1);
		}
		
		public void dec() {
			sub(1);
		}
		
	}
	
	public static class FloatPointer extends Pointer<Float> {
		
		public FloatPointer(float data) {
			super(data);
		}
		
		public void add(float i) {
			set(get() + i);
		}
		
		public void sub(float i) {
			add(-i);
		}
		
		public void mul(float i) {
			set(get() * i);
		}
		
		public void div(float i) {
			set(get() / i);
		}
		
	}
	
	public static class StringPointer extends Pointer<String> {
		
		public StringPointer(String data) {
			super(data);
		}
		
		public void concat(String other) {
			set(get() + other);
		}
		
	}
	
}
