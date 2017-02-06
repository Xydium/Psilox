package tests;

import com.xydium.psilox.core.Psilox;
import com.xydium.psilox.core.PsiloxConfig;
import com.xydium.psilox.math.Trig;
import com.xydium.psilox.math.Trig.TrigFunctionType;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.node.Polygon;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;
import com.xydium.psilox.utilities.Data;

public class Test4 extends Node {

	private Trig sin, cos;
	private Polygon p;
	
	public Test4(String tag) {
		super(tag);
	}
	
	public void added() {
		transform.setPosition(viewSize().scl(.5f));
		transform.setScale(new Vec(250, 250, 0));
		
		sin = new Trig(TrigFunctionType.SIN);
		sin.setThetaIncrease(Math.PI / 180);
		cos = new Trig(TrigFunctionType.COS);
		cos.setThetaIncrease(Math.PI / 180);
	}
	
	public void render() {
		float vsin = (float) sin.next(), vcos = (float) cos.next();
		draw().fixedFunction(Primitives.LINE, Color.RED, Vec.ZERO, new Vec(vcos, vsin));
		draw().fixedFunction(Primitives.LINE, Color.BLUE, Vec.ZERO, new Vec(vcos, 0));
		draw().fixedFunction(Primitives.LINE, Color.GREEN, Vec.ZERO, new Vec(0, vsin));
		draw().fixedFunction(Primitives.LINE, Color.YELLOW, new Vec(vcos, 0), new Vec(vcos, vsin));
		draw().fixedFunction(Primitives.LINE, Color.CYAN, new Vec(0, vsin), new Vec(vcos, vsin));
		draw().fixedFunction(Primitives.LINE, Color.MAGENTA, new Vec(0, vsin), new Vec(vcos, 0));
	}

	public static void main(String[] args) {
		Psilox.createRuntime(new PsiloxConfig()).start(new Test4("Test"));
	}
			
}
