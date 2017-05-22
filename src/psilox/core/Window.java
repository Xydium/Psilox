package psilox.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import psilox.utility.Log;

public class Window {
	
	private String title;
	private int width;
	private int height;
	private boolean fullscreen;
	private boolean undecorated;
	private long handle;
	
	public Window() {
		this("Psilox", 500, 500, false, false);
	}
	
	public Window(String title, int width, int height, boolean fullscreen, boolean undecorated/*, Color clearColor*/) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.undecorated = undecorated;
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
		
		if(fullscreen) {
			glViewport(0, 0, m.width(), m.height());
		} else {
			glViewport(0, 0, width, height);
		}
	}
	
	void terminate() {
		glfwDestroyWindow(handle);
		glfwTerminate();
	}
	
	boolean shouldClose() {
		return glfwWindowShouldClose(handle);
	}
	
}
