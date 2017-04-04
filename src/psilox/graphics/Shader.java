package psilox.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import psilox.math.Mat4;
import psilox.math.Vec;
import psilox.utils.ShaderUtils;

public class Shader {

	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;
	public static final int VCOLOR_ATTRIB = 2;
	
	private boolean enabled = false;
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	private boolean ignoreTrn, ignoreProj, ignoreTrnProj;
	
	/**
	 * Constructs a new Shader object from the specified
	 * resource path, which will have added to it the transform,
	 * projection, and projection_transform shader uniforms.
	 * 
	 * @param shader
	 */
	public Shader(String shader) {
		ID = ShaderUtils.load(shader);
		enable();
		ignoreTrn = getUniform("transform") == -1;
		ignoreProj = getUniform("projection") == -1;
		ignoreTrnProj = getUniform("projection_transform") == -1;
	}
	
	/**
	 * Returns the shader address if the name exists,
	 * and stores it to prevent repeated GL calls. If the
	 * uniform name does not exist, returns -1.
	 * 
	 * @param name
	 * @return
	 */
	public int getUniform(String name) {
		if (locationCache.containsKey(name))
			return locationCache.get(name);
		
		int result = glGetUniformLocation(ID, name);
		if(result != -1)
			locationCache.put(name, result);
		return result;
	}
	
	/**
	 * Enables the shader and then sets an integer
	 * uniform by the given name to the passed value.
	 * 
	 * @param name
	 * @param value
	 */
	public void setUniform1i(String name, int value) {
		if (!enabled) enable();
		glUniform1i(getUniform(name), value);
	}
	
	/**
	 * Enables the shader and then sets a float
	 * uniform by the given name to the passed value.
	 * 
	 * @param name
	 * @param value
	 */
	public void setUniform1f(String name, float value) {
		if (!enabled) enable();
		glUniform1f(getUniform(name), value);
	}
	
	/**
	 * Enables the shader and then sets a vec2
	 * uniform by the given name to the passed values.
	 * 
	 * @param name
	 * @param value
	 */
	public void setUniform2f(String name, float x, float y) {
		if (!enabled) enable();
		glUniform2f(getUniform(name), x, y);
	}
	
	/**
	 * Enables the shader and then sets a vec3
	 * uniform by the given name to the passed value.
	 * 
	 * @param name
	 * @param value
	 */
	public void setUniform3f(String name, Vec vector) {
		if (!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	/**
	 * Enables the shader and then sets a vec4/color
	 * uniform by the given name to the passed value.
	 * 
	 * @param name
	 * @param value
	 */
	public void setUniform4f(String name, Color c) {
		if (!enabled) enable();
		glUniform4f(getUniform(name), c.r, c.g, c.b, c.a);
	}
	
	
	/**
	 * Enables the shader and then sets a mat4
	 * uniform by the given name to the passed value.
	 * 
	 * @param name
	 * @param value
	 */
	public void setUniformMat4f(String name, Mat4 matrix) {
		if (!enabled) enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}
	
	/**
	 * Enables the shader, meaning that it will be used for
	 * all rendering that occurs before it is disabled.
	 */
	public void enable() {
		glUseProgram(ID);
		enabled = true;
		if(!ignoreTrn) {
			setUniformMat4f("transform", Draw.currentTransform());
		}
		
		if(!ignoreProj) {
			setUniformMat4f("projection", Draw.projection);
		}
		
		if(!ignoreTrnProj) {
			setUniformMat4f("projection_transform", Draw.projection.multiply(Draw.currentTransform()));
		}
	}
	
	/**
	 * Disables the shader and allows draw calls
	 * to pass through the default GL program.
	 */
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}
	
}
