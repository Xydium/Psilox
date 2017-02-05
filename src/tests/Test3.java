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
	
	private Polygon triangle;
	private Rect other;
	
	public Test3(String tag) {
		super(tag);
	}
	
	public void added() {
		transform.translate(viewSize().quo(new Vec(2)));
		transform.setScale(new Vec(50, 50));
		
		addChild(other = new Rect("Rect", new Vec(3), true, new Color(255, 0, 255, 127)));
		other.transform().translate(new Vec(0, 0, 0.1f));
		
		other.addChild(triangle = new Polygon("Triangle", Primitives.C_EQTRI, new Color(255, 255, 0, 200)));
		triangle.transform().translate(new Vec(90, 0, 0.2f));
	}
	
	public void update() {
		other.transform().rotate(1);
		triangle.transform().rotate(1);
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.ups = 60;
		Psilox.createRuntime(cfg).start(new Test3("Main"));
	}
	
}
