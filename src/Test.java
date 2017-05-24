import psilox.core.Psilox;
import psilox.core.Window;
import psilox.graphics.Color;
import psilox.graphics.Texture;
import psilox.math.Anchor;
import psilox.math.Rect;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.utility.Time;

public class Test extends Node {

	static final int WIDTH = 1280;
	static final int HEIGHT = 720;
	
	public Test(String tag) {
		super(tag);
	}
	
	public void enteredTree() {
		if(RUID < 20) {
			addChild(new Test("test"));
			position = new Vec(15, 0, .1f);
		}
		if(RUID == 0) {
			position = new Vec(WIDTH / 2, HEIGHT / 2);
			scale = new Vec(3);
		} else {
			scale = new Vec(0.99f);
		}
		dimensions = new Vec(10);
		anchor = Anchor.MIDDLE;
		texture = new Texture(3, 3);
		texture.setData(new int[]{0x8EFF0000, 0x8E00FF00, 0x8E0000FF, 0x8EFFFF00, 0x8EFF00FF, 0x8E00FFFF,
					0x8EFFFFFF, 0x8E8E8E8E, 0x8E252525}, 3, 3);
		textureRegion = new Rect(0, 0, 3, 3);
	}
	
	public void update() {
		rotation += 1;
	}

	public static void main(String[] args) {
		Psilox.start(new Window("Test", WIDTH, HEIGHT, false, false, Color.BLACK), new Test("test"));
	}
	
}
