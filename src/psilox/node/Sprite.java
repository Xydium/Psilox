package psilox.node;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.graphics.Texture;
import psilox.math.Vec;

public class Sprite extends Node {

	protected Texture texture;
	protected Color modulate;
	protected Shader shader;
	private Vec dimensions;
	private UniformConfig config = s -> {};
	
	public Sprite(Texture texture) {
		this.texture = texture;
	}
	
	public Sprite(String path) {
		this(new Texture(path));
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Color getModulate() {
		return modulate;
	}
	
	public void setModulate(Color modulate) {
		this.modulate = modulate;
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public void setUniformConfig(UniformConfig config) {
		this.config = config;
	}
	
	public Vec getDimensions() {
		if(dimensions == null) {
			return new Vec(texture.getWidth(), texture.getHeight());
		} else {
			return dimensions;
		}
	}
	
	public void setDimensions(Vec dimensions) {
		this.dimensions = dimensions;
	}
	
	public void render() {
		if(shader != null) {
			shader.enable();
			config.setUniforms(shader);
		}
		Draw.texture(texture, Vec.ZERO, getDimensions(), modulate);
		if(shader != null) shader.disable();
	}
	
	public static interface UniformConfig {
		public void setUniforms(Shader s);
	}
	
}
