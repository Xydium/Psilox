package tests;

import com.xydium.psilox.core.Psilox;
import com.xydium.psilox.core.PsiloxConfig;
import com.xydium.psilox.input.KeySequence;
import com.xydium.psilox.input.Key;
import com.xydium.psilox.math.Random;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.node.Polygon;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.utilities.Time;

public class Test3 extends Node {
	
	private Polygon circle;
	private int sides = 3;
	private KeySequence s;
	
	public Test3(String tag) {
		super(tag);
	}
	
	public void added() {
		transform.translate(viewSize().quo(new Vec(2)));
		transform.setScale(new Vec(50, 50));
		addChild(circle = new Polygon("Circle", new Vec[] { new Vec(0, 0) }, new Color(1.0f, 1.0f, 0)));
		s = new KeySequence(input(), () -> { sides = Random.intVal(10) + 3; }, Key.CONTROL, Key.ENTER).asCombination();
	}
	
	public void update() {
		float inc = (float) (2 * Math.PI) / sides;
		Vec[] shape = new Vec[sides];
		
		for(int i = 0; i < sides; i++) {
			shape[i] = new Vec(
					(float) Math.cos(i * inc + Math.PI / 2), 
					(float) Math.sin(i * inc + Math.PI / 2)
			);
		}
		
		transform.rotate(1);
		circle.setShape(shape);
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.ups = 60;
		cfg.clearColor = new Color(150, 50, 250);
		Psilox.createRuntime(cfg).start(new Test3("Main"));
	}
	
}
