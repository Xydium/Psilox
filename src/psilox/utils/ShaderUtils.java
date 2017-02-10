package psilox.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {
	
	private ShaderUtils() {
	}
	
	public static int load(String path) {
		String src = FileUtils.loadAsString(path);
		String vert = src.substring(src.indexOf("VERTEX:") + 7, src.indexOf("FRAGMENT:"));
		String frag = src.substring(src.indexOf("FRAGMENT:") + 9, src.length());
		return create(vert, frag);
	}
	
	public static int create(String vert, String frag) {
		int program = glCreateProgram(); 
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertID, vert);
		glShaderSource(fragID, frag);
		
		glCompileShader(vertID);
		if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(glGetShaderInfoLog(vertID));
			return -1;
		}
		
		glCompileShader(fragID);
		if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(glGetShaderInfoLog(fragID));
			return -1;
		}
		
		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		if(glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Failed to link program!");
			System.err.println(glGetProgramInfoLog(program));
			return -1;
		}
		glValidateProgram(program);
		
		glDeleteShader(vertID);
		glDeleteShader(fragID);
		
		return program;
	}

}
