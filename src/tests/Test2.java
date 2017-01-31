package tests;

import com.xydium.psilox.core.*;
import com.xydium.psilox.input.Key;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Rect;
import com.xydium.psilox.rendering.Color;

public class Test2 extends Rect {
	
	public Test2() {
		super("shape", new Vec(50), true, new Color(55, 55, 255));
	}
	
	public void update() {
		Vec d = Vec.ZERO;
		if(input().keyDown(Key.W)) d = d.sum(Vec.UP);
		else if(input().keyDown(Key.S)) d = d.sum(Vec.DOWN);
		if(input().keyDown(Key.A)) d = d.sum(Vec.LEFT);
		else if(input().keyDown(Key.D)) d = d.sum(Vec.RIGHT);
		transform.translate(d.nrm().scl(10));
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.width = 1280;
		cfg.height = 720;
		Psilox.createRuntime(cfg).start(new Test2());
	}
	
}