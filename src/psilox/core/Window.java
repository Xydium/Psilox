package psilox.core;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import psilox.graphics.Color;
import psilox.graphics.Mesh;
import psilox.graphics.Shader;
import psilox.graphics.Texture;
import psilox.math.Mat4;
import psilox.math.Vec;
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
				0, 1, 0f, //Top Left
				0, 0, 0f, //Bottom Left
				1, 0, 0f, //Bottom Right
				1, 0, 0f, //Bottom Right 
				1, 1, 0f, //Top Right
				0, 1, 0f //Top Left
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
			tex.setData(new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF,
					0xFFFFFFFF, 0xFF8E8E8E, 0xFF252525}, 3, 3);
		};
		
		debugShader.enable();
		glBindVertexArray(debugMesh.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		debugShader.setUniformMat4f("u_projection", Mat4.orthographic(0, width, 0, height, 10, -10));
		debugShader.setUniformMat4f("u_transform", Mat4.transform(new Vec(width / 2, height / 2), Psilox.ticks, new Vec(90)));
		debugShader.setUniform2f("u_dimensions", 50, 50);
		debugShader.setUniform2f("u_tex_dimensions", 3, 3);
		debugShader.setUniform2f("u_tex_botleft", .5f, .5f);
		debugShader.setUniform2f("u_tex_topright", 2.5f, 2.5f);
		debugShader.setUniform2f("u_anchor", .5f, .5f);
		debugShader.setUniform4f("u_modulate", Color.WHITE);
		glDrawArrays(GL_TRIANGLES, 0, debugMesh.getVertCount());
		tex.unbind();
		glDisableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glBindVertexArray(0);
		debugShader.disable();
	}
	
}
