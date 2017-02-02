package tests;

import com.xydium.psilox.core.Psilox;
import com.xydium.psilox.core.PsiloxConfig;
import com.xydium.psilox.input.Key;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Rect;
import com.xydium.psilox.rendering.Color;

public class Test2 extends Rect {
	
	public Test2() {
		super("shape", new Vec(100, 100), false, new Color(55, 55, 255));
		transform.translate(new Vec(50, 50));
	}
	
	public void update() {
		if(input().keyTap(Key.ENTER)) {
			PsiloxConfig cfg = psilox().config();
			cfg.width += 10;
			cfg.height += 10;
			psilox().reconfigure(cfg);
		}
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.ups = 60;
		Psilox.createRuntime(cfg).start(new Test2());
	}
	
}