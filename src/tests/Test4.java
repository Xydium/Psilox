package tests;

import com.xydium.psilox.core.Psilox;
import com.xydium.psilox.core.PsiloxConfig;
import com.xydium.psilox.math.Trig;
import com.xydium.psilox.math.Trig.TrigFunctionType;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;

public class Test4 extends Node {

	private Trig sin, cos;
	
	public Test4(String tag) {
		super(tag);
	}
	
	public void added() {
		transform.setPosition(viewSize().scl(.5f));
		transform.setScale(new Vec(250, 250, 1));
		
		sin = new Trig(TrigFunctionType.SIN);
		sin.setThetaIncrease(Math.PI / 1000);
		cos = new Trig(TrigFunctionType.COS);
		cos.setThetaIncrease(Math.PI / 360);
	}
	
	public void render() {
		float vsin = (float) sin.next(), vcos = (float) cos.next();
		draw().scale(Vec.ONE.scl(2));
		draw().fixedFunction(Primitives.QUADS, new Color(0, 0, 0, 25), Primitives.C_RECT);
		draw().scale(Vec.ONE.scl(.5f));
		draw().fixedFunction(Primitives.LINE, Color.hsba((float) (sin.evaluate(sin.getTheta())), 1, 1, 255), Vec.ZERO, new Vec(vcos, vsin));
		draw().fixedFunction(Primitives.LINE, new Color(0xFFFF00FF), Vec.ZERO, new Vec(vcos, 0));
		draw().fixedFunction(Primitives.LINE, new Color(0xFFFFFFFF), Vec.ZERO, new Vec(0, vsin));
	}

	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.clearscreen = false;
		cfg.doubleBuffer = false;
		cfg.fps = 120;
		Psilox.createRuntime(cfg).start(new Test4("Test"));
	}
			
}
