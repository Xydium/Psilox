package com.xydium.psilox.rendering;

public class Primitives {

	public static final Vertex[] SQUARE = {
			new Vertex(0, 0),
			new Vertex(1, 0),
			new Vertex(1, 1),
			new Vertex(0, 1)
	};
		
	public static final Vertex[] C_SQUARE = {
			new Vertex(-.5f, -.5f),
			new Vertex(.5f, -.5f),
			new Vertex(.5f, .5f),
			new Vertex(-.5f, .5f),
	};
	
}
