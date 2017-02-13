import psilox.core.Node;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.math.Mat4;
import psilox.math.Vec;

public class Sky extends Node {

	private Shader sky;
	
	public void added() {
		sky = new Shader("shaders/sky.shd");
		sky.enable();
		sky.setUniform4f("color", new Color(2, 2, 10));
		sky.setUniform1f("threshold", .96f);
		sky.setUniformMat4f("projection", Draw.projection);
		sky.disable();
		transform.translate(new Vec(0, 0, -.1));
	}
	
	public void render() {
		sky.enable();
		sky.setUniform1f("time", psilox().ticks() / 20.0f);
		sky.setUniformMat4f("transform", Draw.currentTransform());
		Draw.quad(Color.WHITE, Vec.ZERO, viewSize());
		sky.disable();
	}
	
}
