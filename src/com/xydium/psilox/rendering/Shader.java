package com.xydium.psilox.rendering;

import com.jogamp.opengl.GL2;
import com.xydium.psilox.utilities.DataResource;

public class Shader {

	private int program;
	
	public Shader(Draw draw, String path) {
		GL2 gl = draw.gl();
		
		int vert = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		int frag = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		
		String vsrc = new DataResource(path + ".vert").concatenated('\n');
		String fsrc = new DataResource(path + ".frag").concatenated('\n');
		
		gl.glShaderSource(vert, 1, new String[] {vsrc}, (int[]) null, 0);
		gl.glCompileShader(vert);
		
		gl.glShaderSource(frag, 1, new String[] {fsrc}, (int[]) null, 0);
		gl.glCompileShader(frag);
		
		program = gl.glCreateProgram();
		gl.glAttachShader(program, vert);
		gl.glAttachShader(program, frag);
		gl.glLinkProgram(program);
		gl.glValidateProgram(program);
	}

	public int getProgram() {
		return program;
	}
	
}
