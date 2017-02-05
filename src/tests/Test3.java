package tests;

import com.xydium.psilox.core.Psilox;
import com.xydium.psilox.core.PsiloxConfig;
import com.xydium.psilox.input.Key;
import com.xydium.psilox.input.KeySequence;
import com.xydium.psilox.math.Random;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.node.Polygon;
import com.xydium.psilox.node.Rect;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;

public class Test3 extends Node {
	
	private Rect one;
	private Rect two;
	
	public Test3(String tag) {
		super(tag);
	}
	
	public void added() {
		transform.translate(viewSize().quo(new Vec(2)));
		transform.setScale(new Vec(50, 50));
		
		//addChild(two = new Rect("one", Vec.ONE.scl(3), true, new Color(255, 0, 255, 127)));
		//two.transform().translate(new Vec(0, 0, 0.1f));
		
		//two.addChild(one = new Rect("two", Vec.ONE, true, new Color(255, 255, 0, 200)));
		//one.transform().translate(new Vec(1, 0, 0.2f));
		
		print(transform);
		//print(two.transform());
		//print(one.transform());
	}
	
	public void update() {
		//two.transform().rotate(1);
		//one.transform().rotate(1);
	}
	
	public void render() {
		draw().fixedFunction(Primitives.LINE, Color.RED, Vec.ZERO, Vec.TWO_D);
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		Psilox.createRuntime(cfg).start(new Test3("Main"));
	}
	
}
