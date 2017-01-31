package tests;

import com.xydium.psilox.core.*;
import com.xydium.psilox.input.Key;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Rect;
import com.xydium.psilox.rendering.Color;

public class Test2 extends Rect {
	
	public Test2() {
		super("shape", new Vec(1920, 1080), false, new Color(55, 55, 255));
	}
	
	public void update() {
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.fullscreen = true;
		Psilox.createRuntime(cfg).start(new Test2());
	}
	
}