import psilox.core.Config;
import psilox.core.Node;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Vec;
import static psilox.graphics.Draw.*;
import static psilox.input.Input.*;

public class Test extends Node {

	private Vec pos = pos(), velocity = new Vec(0);
	
	public void update() {
		if(keyDown(W)) velocity = velocity.sum(new Vec(0, .1f).rot(rtn())).clm(10);
		if(keyDown(A)) transform.rotate(6); else
		if(keyDown(D)) transform.rotate(-6);
		pos.x = (pos.x + velocity.x + ((pos.x < 0) ? viewSize().x : 0)) % viewSize().x;
		pos.y = (pos.y + velocity.y + ((pos.y < 0) ? viewSize().y : 0)) % viewSize().y;
	}
	
	public void render() { tri(Color.DARK_GRAY, new Vec(0, 20), new Vec(-10, -20), new Vec(10, -20)); }
	
	public static void main(String[] args) { new Psilox(new Config()).start(new Test()); }
	
}
