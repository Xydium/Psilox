package psilox.demo.game;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Texture;
import psilox.math.Vec;

public enum Tile {

	TEST_RED(true, null, Color.RED),
	TEST_BLUE(false, null, Color.BLUE),
	TEST_GRAY(true, null, Color.LIGHT_GRAY),
	TEST_GREEN(false, null, new Color(55, 155, 55));
	
	public static final Vec SIZE = new Vec(80);
	
	private static boolean loaded = false;
	
	public final boolean passable;
	public String texturePath;
	public Texture texture;
	public Color modulate;
	
	private Tile(boolean passable, String texturePath, Color modulate) {
		this.passable = passable;
		this.texturePath = texturePath;
		this.modulate = modulate;
	}
	
	private Tile(boolean passable, String texturePath) {
		this(passable, texturePath, null);
	}
	
	private Tile(boolean passable, Color modulate) {
		this(passable, null, modulate);
	}
	
	public void render(Vec coordinate) {
		if(texture == null) {
			if(modulate != null) {
				Draw.quad(modulate, coordinate.pro(SIZE), SIZE);
			}
		} else {
			Draw.texture(texture, coordinate.pro(SIZE), modulate == null ? Color.WHITE : modulate);
		}
	}
	
	public static void loadAllTiles() {
		if(loaded) return;
		for(Tile t : Tile.values()) {
			if(t.texturePath != null) {
				t.texture = new Texture(t.texturePath);
			}
		}
		loaded = true;
	}
	
}
