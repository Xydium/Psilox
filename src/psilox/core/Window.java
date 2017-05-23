package psilox.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import psilox.graphics.Color;
import psilox.graphics.Mesh;
import psilox.graphics.Shader;
import psilox.graphics.Texture;
import psilox.utility.Log;

public class Window {
	
	private String title;
	private int width;
	private int height;
	private boolean fullscreen;
	private boolean undecorated;
	private Color clearColor;
	private long handle;
	
	public Window() {
		this("Psilox", 500, 500, false, false, Color.BLACK);
	}
	
	public Window(String title, int width, int height, boolean fullscreen, boolean undecorated, Color clearColor) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.undecorated = undecorated;
		this.clearColor = clearColor;
	}
	
	void initialize() {
		if(!glfwInit()) {
			Log.error("Initializing GLFW failed.");
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, 0);
		glfwWindowHint(GLFW_DECORATED, undecorated ? 0 : 1);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		handle = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if(handle == NULL) {
			Log.error("Creating GLFW window failed.");
			return;
		}
		
		GLFWVidMode m = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(handle, (m.width() - width) / 2, (m.height() - height) / 2);
		
		if(fullscreen) {
			glfwSetWindowMonitor(handle, glfwGetPrimaryMonitor(), 0, 0, m.width(), m.height(), 60);
		}
		
		glfwMakeContextCurrent(handle);
		glfwShowWindow(handle);
		GL.createCapabilities();
		
		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		
		if(fullscreen) {
			glViewport(0, 0, m.width(), m.height());
		} else {
			glViewport(0, 0, width, height);
		}
		
		Log.internal("Created Window using GL: %s", getGLVersion());
	}
	
	void pollEvents() {
		glfwPollEvents();
	}
	
	void terminate() {
		glfwDestroyWindow(handle);
		glfwTerminate();
	}
	
	void swapBuffers() {
		glfwSwapBuffers(handle);
	}
	
	void logLastError() {
		int error = glGetError();
		if(error != 0) {
			Log.error("GL encountered error #%d", error);
		}
	}
	
	void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	String getGLVersion() {
		return glGetString(GL_VERSION);
	}
	
	boolean shouldClose() {
		return glfwWindowShouldClose(handle);
	}
	
	private Mesh debugMesh;
	private Shader debugShader;
	private Texture tex;
	void debugRender() {
		if(debugMesh == null) {
			float[] verts = {
				-.5f, .5f, 0f, //Top Left
				-.5f, -.5f, 0f, //Bottom Left
				.5f, -.5f, 0f, //Bottom Right
				.5f, -.5f, 0f, //Bottom Right 
				.5f, .5f, 0f, //Top Right
				-.5f, .5f, 0f //Top Left
			};
			
			float[] tcoords = {
				0f, 0f,
				0f, 1f,
				1f, 1f,
				1f, 1f,
				1f, 0f,
				0f, 0f
			};
			
			debugMesh = Mesh.loadToVao(verts, tcoords);
			debugShader = new Shader("shaders/psilox.shd");
			tex = new Texture(3, 2);
			tex.setData(new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF}, 3, 2);
		};
		
		debugShader.enable();
		glBindVertexArray(debugMesh.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		glDrawArrays(GL_TRIANGLES, 0, debugMesh.getVertCount());
		tex.unbind();
		glDisableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glBindVertexArray(0);
		debugShader.disable();
	}
	
}
