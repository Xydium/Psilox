package psilox.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import psilox.utility.Buffers;

public class Mesh {

	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	
	private int vaoID;
	private int vertCount;
	
	public Mesh(int vaoID, int vertCount) {
		this.vaoID = vaoID;
		this.vertCount = vertCount;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertCount() {
		return vertCount;
	}
	
	public static Mesh loadToVao(float[] positions, float[] tcoords) {
		int vaoID = createVao();
		storeDataInAttribList(Shader.VERTEX_ATTRIB, positions, 3);
		storeDataInAttribList(Shader.TCOORD_ATTRIB, tcoords, 2);
		unbindVao();
		return new Mesh(vaoID, positions.length / 3);
	}
	
	private static int createVao() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private static void storeDataInAttribList(int attrib, float[] data, int size) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = Buffers.createFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attrib, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private static void unbindVao() {
		glBindVertexArray(0);
	}
	
	public void cleanUp() {
		for(int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		
		for(int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
	}
	
}
