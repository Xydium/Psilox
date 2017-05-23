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

	static final int WIDTH = 500;
	static final int HEIGHT = 500;
	
	public Test(String tag) {
		super(tag);
	}
	
	public void enteredTree() {
		if(getParent().RUID == Psilox.root.RUID) {
			addChild(new Test("test2"));
			position = new Vec(WIDTH / 2, HEIGHT / 2);
		} else {
			position = new Vec(50, 0);
		}
		dimensions = new Vec(50);
		anchor = Anchor.MIDDLE_RIGHT;
		texture = new Texture(3, 3);
		texture.setData(new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF,
					0xFFFFFFFF, 0xFF8E8E8E, 0xFF252525}, 3, 3);
		textureRegion = new Rect(.5f, .5f, 2f, 2f);
	}
	
	public void update() {
		rotation += 2;
		
		if(tag.equals("test3")) {
			position.x = (WIDTH - 200) * (float) Time.flipFlop(0, Time.SECOND * 13) + 100;
			position.y = (HEIGHT - 200) * (float) Time.flipFlop(0, Time.SECOND * 5) + 100;
			scale = new Vec(2).scl((float) Time.flipFlop(0, Time.SECOND * 7));
		}		
	}

	public static void main(String[] args) {
		Psilox.start(new Window("Test", WIDTH, HEIGHT, false, false, Color.BLACK), new Test("test"));
	}
	
}
