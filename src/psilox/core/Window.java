package psilox.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.Stack;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import psilox.graphics.Color;
import psilox.graphics.Mesh;
import psilox.graphics.Shader;
import psilox.input.Input;
import psilox.math.Mat4;
import psilox.node.Node;
import psilox.utility.Log;

public class Window {
	
	private String title;
	private int width;
	private int height;
	private boolean fullscreen;
	private boolean undecorated;
	private boolean antiAlias;
	private Color clearColor;
	private long handle;
	
	private Mesh defaultMesh;
	private Shader defaultShader;
	private Mat4 projection;
	private Stack<Mat4> transforms;
	
	public Window() {
		this("Psilox", 500, 500, false, false, false, Color.BLACK);
	}
	
	public Window(String title, int width, int height, boolean fullscreen, boolean undecorated, boolean antiAlias, Color clearColor) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.undecorated = undecorated;
		this.antiAlias = antiAlias;
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
		if(antiAlias) glfwWindowHint(GLFW_SAMPLES, 8);
		
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
		
		glfwSetKeyCallback(handle, Input.keyCallback);
		glfwSetMouseButtonCallback(handle, Input.mouseCallback);
		glfwSetCursorPosCallback(handle, Input.cursorCallback);
		glfwSetCharCallback(handle, Input.charCallback);
		
		glfwMakeContextCurrent(handle);
		glfwShowWindow(handle);
		GL.createCapabilities();

		if(antiAlias) glEnable(GL_MULTISAMPLE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		
		if(fullscreen) {
			glViewport(0, 0, m.width(), m.height());
		} else {
			glViewport(0, 0, width, height);
		}
		
		Log.internal("Created Window using GL: %s", getGLVersion());
		
		defaultMesh = Mesh.unitSquare();
		defaultShader = new Shader("shaders/psilox.shd");
		projection = Mat4.orthographic(0, width, 0, height, -100, 100);
		transforms = new Stack<Mat4>();
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
	
	void renderTree(Node root) {
		glBindVertexArray(defaultMesh.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		
		if(root.visible)
			renderNode(root);
		
		glDisableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glBindVertexArray(0);
	}
	
	private void renderNode(Node node) {
		if(transforms.isEmpty()) {
			transforms.push(Mat4.transform(node.position, node.rotation, node.scale));
		} else {
			transforms.push(Mat4.transform(transforms.peek(), node.position, node.rotation, node.scale));
		}
	
		for(Node n : node.getChildrenUnsafe()) {
			if(n.visible) {
				renderNode(n);
			}
		}
		
		Shader s = node.shader == null ? defaultShader : node.shader;
		
		s.enable();
		s.setUniformMat4f("u_projection", projection);
		s.setUniformMat4f("u_transform", transforms.peek());
		s.setUniform2f("u_dimensions", node.dimensions.x, node.dimensions.y);
		
		if(node.texture != null) {
			node.texture.bind();
			s.setUniform1i("u_tex_valid", 1);
			final int width = node.texture.getWidth();
			final int height = node.texture.getHeight();
			s.setUniform2f("u_tex_dimensions", width, height);
			if(node.textureRegion != null) {
				s.setUniform4f("u_tex_region", node.textureRegion);
				s.setUniform1i("u_region_valid", 1);
			} else {
				s.setUniform1i("u_region_valid", 0);
			}
		} else {
			s.setUniform1i("u_tex_valid", 0);
		}
				
		s.setUniform2f("u_anchor", node.anchor.x, node.anchor.y);
		s.setUniform4f("u_modulate", node.modulate);
		
		node.setUniforms(s);
		
		glDrawArrays(GL_TRIANGLES, 0, defaultMesh.getVertCount());
		
		if(node.texture != null) {
			node.texture.unbind();
		}
		
		s.disable();
		
		transforms.pop();
	}
	
}
