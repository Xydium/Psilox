package psilox.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Stack;

import psilox.math.Mat4;
import psilox.math.Vec;
import psilox.utils.BufferUtils;
import psilox.utils.Log;

public class Draw {

	private static int[] viewport = new int[4];
	private static float[] clearColor = new float[4];
	private static final Graphics2D gDefault = new BufferedImage(1, 1, 1).createGraphics();
	private static Stack<Mat4> transforms = new Stack<Mat4>();
	public static boolean immediateMode = false;
	public static Mat4 projection;
	
	private static void makeFrameBuffer(Texture tex) {
		int frameBuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		int depthBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, tex.getWidth(), tex.getHeight());
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex.getTexture(), 0);
		tex.setFrameBuffer(frameBuffer);
	}
	
	/**
	 * Makes the specified texture the render target
	 * for all GL draw calls.
	 * 
	 * @param tex
	 */
	public static void renderTarget(Texture tex) {
		glGetIntegerv(GL_VIEWPORT, viewport);
		glGetFloatv(GL_COLOR_CLEAR_VALUE, clearColor);
		restoreFrameBuffer();
		if(tex.getFrameBuffer() == 0) {
			makeFrameBuffer(tex);
		}
		glBindFramebuffer(GL_FRAMEBUFFER, tex.getFrameBuffer());
		glViewport(0, 0, tex.getWidth(), tex.getHeight());
	}
	
	/**
	 * Unbinds a texture as the render target, allowing
	 * rendering to again apply to the window framebuffer.
	 */
	public static void restoreFrameBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);
		clearColor(new Color(clearColor[0], clearColor[1], clearColor[2], clearColor[3]));
	}
	
	/**
	 * Applies Color and Depth buffer bit clearing.
	 */
	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Changes the clear color of the window.
	 * 
	 * @param c
	 */
	public static void clearColor(Color c) {
		glClearColor(c.r, c.g, c.b, c.a);
	}
	
	/**
	 * Restores the identity transform and clears 
	 * the client-side stack.
	 */
	public static void clearTransform() {
		glLoadIdentity();
		transforms.clear();
		transforms.push(Mat4.identity());
	}
	
	/**
	 * Pushes a new relative transformation to the stack
	 * and to GL Immediate.
	 * 
	 * @param transform
	 */
	public static void pushTransform(Mat4 transform) {
		if(transforms.size() == 0) {
			transforms.push(Mat4.identity());
		}
		transforms.push(currentTransform().multiply(transform));
		if(immediateMode) {
			glLoadMatrixf(currentTransform().elements);
		}
	}
	
	/**
	 * Returns the top of the transform stack,
	 * which is the product of all below.
	 * 
	 * @return
	 */
	public static Mat4 currentTransform() {
		if(transforms.size() == 0) transforms.push(Mat4.identity());
		return transforms.peek();
	}
	
	/**
	 * Removes the top transform.
	 */
	public static void popTransform() {
		if(transforms.size() > 1) {
			transforms.pop();
			if(immediateMode) {
				glLoadMatrixf(currentTransform().elements);
			}
		}
	}
	
	/**
	 * Returns the global origin point of the current
	 * transformation.
	 * 
	 * @return
	 */
	public static Vec getOrigin() {
		float[] t = transforms.peek().elements;
		return new Vec(t[0 + 3 * 4], t[1 + 3 * 4], t[2 + 3 * 4]);
	}
	
	private static boolean immediateEnabled() { 
		if(!immediateMode) {
			Log.error("Attempted to call an immediate-mode Draw function without enabling immediate-mode through the config.");
		}
		return immediateMode;
	}
	
	/**
	 * Directly applies a translation to GL Immediate.
	 * 
	 * @param p
	 */
	public static void translate(Vec p) {
		if(!immediateEnabled()) return;
		glTranslatef(p.x, p.y, p.z);
	}
	
	/**
	 * Directly rotates GL Immediate.
	 * 
	 * @param theta
	 */
	public static void rotate(float theta) {
		if(!immediateEnabled()) return;
		glRotatef(theta, 0, 0, 1);
	}
	
	/**
	 * Directly scales GL Immediate.
	 * 
	 * @param s
	 */
	public static void scale(Vec s) {
		if(!immediateEnabled()) return;
		glScalef(s.x, s.y, s.z);
	}
	
	/**
	 * Directly transforms GL Immediate.
	 * 
	 * @param p
	 * @param theta
	 * @param s
	 */
	public static void transform(Vec p, float theta, Vec s) {
		if(!immediateEnabled()) return;
		translate(p);
		rotate(theta);
		scale(s);
	}
	
	/**
	 * Clears and then directly transforms GL Immediate.
	 * 
	 * @param p
	 * @param theta
	 * @param s
	 */
	public static void setTransform(Vec p, float theta, Vec s) {
		if(!immediateEnabled()) return;
		clearTransform();
		transform(p, theta, s);
	}
	
	/**
	 * Runs a set of immediate mode verts with the specified
	 * mode and color.
	 * 
	 * @param mode
	 * @param c
	 * @param verts
	 */
	public static void immediate(int mode, Color c, Vec... verts) {
		if(!immediateEnabled()) return;
		glBegin(mode);
		glColor4f(c.r, c.g, c.b, c.a);
		Vec idv;
		for(int i = 0; i < verts.length; i++) {
			idv = verts[i];
			glVertex3f(idv.x, idv.y, idv.z);
		}
		glEnd();
	}
	
	/**
	 * Runs a set of immediate mode verts with the
	 * specified modes and colors.
	 * 
	 * @param mode
	 * @param colors
	 * @param verts
	 */
	public static void immediate(int mode, Color[] colors, Vec[] verts) {
		if(!immediateEnabled()) return;
		glBegin(mode);
		Color idc;
		Vec idv;
		for(int i = 0; i < verts.length; i++) {
			idv = verts[i];
			idc = colors[i % colors.length];
			glColor4f(idc.r, idc.g, idc.b, idc.a);
			glVertex3f(idv.x, idv.y, idv.z);
		}
		glEnd();
	}
	
	/**
	 * Renders a shape object.
	 * 
	 * @param shape
	 */
	public static void shape(Shape shape) {
		float[] verts = shape.getVerts();
		float[] colors = shape.getColors();
		byte[] inds = shape.getIndices();
		int mode = shape.getMode();
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glVertexPointer(3, GL_FLOAT, 0, verts);
		
		if(colors.length == 4) {
			glColor4f(colors[0], colors[1], colors[2], colors[3]);
		} else {
			glEnableClientState(GL_COLOR_ARRAY);
			glColorPointer(4, GL_FLOAT, 0, colors);
		}

		if(inds != null) {
			glDrawElements(mode, BufferUtils.createByteBuffer(inds));
		} else {
			glDrawArrays(mode, 0, verts.length / 3);
		}
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}
	
	/**
	 * Renders a point.
	 * 
	 * @param c
	 * @param i
	 */
	public static void point(Color c, Vec i) {
		immediate(GL_POINTS, c, i);
	}
	
	/**
	 * Renders a line.
	 * 
	 * @param c
	 * @param i
	 * @param j
	 */
	public static void line(Color c, Vec i, Vec j) {
		immediate(GL_LINES, c, i, j);
	}
	
	/**
	 * Renders a triangle.
	 * 
	 * @param c
	 * @param i
	 * @param j
	 * @param k
	 */
	public static void tri(Color c, Vec i, Vec j, Vec k) {
		immediate(GL_TRIANGLES, c, i, j, k);
	}
	
	/**
	 * Renders a rectangle.
	 * 
	 * @param c
	 * @param i
	 * @param j
	 * @param k
	 * @param l
	 */
	public static void quad(Color c, Vec i, Vec j, Vec k, Vec l) {
		immediate(GL_QUADS, c, i, j, k, l);
	}
	
	/**
	 * Renders a rectangle with origin p
	 * of size s.
	 * 
	 * @param c
	 * @param p
	 * @param s
	 */
	public static void quad(Color c, Vec p, Vec s) {
		quad(c, p, new Vec(p.x + s.x, p.y), new Vec(p.x + s.x, p.y + s.y), new Vec(p.x, p.y + s.y));
	}
	
	/**
	 * Renders a rectangle centered around
	 * origin p of size s.
	 * 
	 * @param c
	 * @param p
	 * @param s
	 */
	public static void cquad(Color c, Vec p, Vec s) {
		quad(c, p.dif(s.scl(.5f)), s);
	}
	
	/**
	 * Renders a polygon.
	 * 
	 * @param c
	 * @param verts
	 */
	public static void poly(Color c, Vec... verts) {
		immediate(GL_TRIANGLE_FAN, c, verts);
	}
	
	/**
	 * Renders an outline.
	 * 
	 * @param c
	 * @param verts
	 */
	public static void outline(Color c, Vec...verts) {
		immediate(GL_LINE_LOOP, c, verts);
	}
	
	/**
	 * Renders a strip of lines.
	 * 
	 * @param c
	 * @param verts
	 */
	public static void strip(Color c, Vec...verts) {
		immediate(GL_TRIANGLE_STRIP, c, verts);
	}

	/**
	 * Returns the default font metrics for
	 * a java.awt.Font object.
	 * 
	 * @param f
	 * @return
	 */
	public static FontMetrics getFontMetrics(Font f) {
		return gDefault.getFontMetrics(f);
	}
	
	/**
	 * Renders text with the given color and font
	 * into the specified texture object.
	 * 
	 * @param c
	 * @param font
	 * @param texture
	 * @param text
	 */
	public static void text(Color c, Font font, Texture texture, String text) {
		FontMetrics m = gDefault.getFontMetrics(font);
		BufferedImage image = new BufferedImage(m.stringWidth(text) + (text.isEmpty() ? 1 : 0), m.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setFont(font);
		g.setColor(new java.awt.Color(c.r, c.g, c.b, c.a));
		g.drawString(text, 0, image.getHeight() - m.getMaxDescent());
		texture.setData(image);
	}
	
	/**
	 * Renders a subsection of text using the specified dimensions
	 * for the texture passed.
	 * 
	 * @param c
	 * @param font
	 * @param texture
	 * @param dimensions
	 * @param text
	 */
	public static void text(Color c, Font font, Texture texture, Vec dimensions, String text) {
		FontMetrics m = gDefault.getFontMetrics(font);
		BufferedImage image = new BufferedImage((int) dimensions.x, (int) dimensions.y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setFont(font);
		g.setColor(new java.awt.Color(c.r, c.g, c.b, c.a));
		g.drawString(text, 0, image.getHeight() - m.getMaxDescent());
		texture.setData(image);
	}
	
	/**
	 * Renders multiple lines of text, left-justified,
	 * into the given texture.
	 * 
	 * @param c
	 * @param font
	 * @param texture
	 * @param text
	 */
	public static void multiLineText(Color c, Font font, Texture texture, String[] text) {
		FontMetrics m = gDefault.getFontMetrics(font);
		int lineHeight = m.getHeight();
		int maxDescent = m.getMaxDescent();
		int maxWidth = 1;
		for(String s : text) {
			int w = m.stringWidth(s); 
			if(w > maxWidth) {
				maxWidth = w;
			}
		}
		int maxHeight = lineHeight * text.length;
		if(maxHeight <= 0) maxHeight = 1;
		BufferedImage image = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setFont(font);
		g.setColor(new java.awt.Color(c.r, c.g, c.b, c.a));
		for(int i = 0; i < text.length; i++) {
			g.drawString(text[i], 0, (i + 1) * lineHeight - maxDescent);
		}
		texture.setData(image);
	}
	
	/**
	 * Renders a texture on screen with the bottom-left
	 * corner at point i, on a quad of size d, with a color
	 * modulation of mod.
	 * 
	 * @param tex
	 * @param i
	 * @param d
	 * @param mod
	 */
	public static void texture(Texture tex, Vec i, Vec d, Color mod) {
		if(!immediateEnabled()) return;
		i = new Vec(i);
		if(mod == null) { mod = Color.WHITE; }
		tex.bind();
		glBegin(GL_QUADS);
		glColor4f(mod.r, mod.g, mod.b, mod.a);
		glTexCoord2f(0, 1);
		glVertex3f(i.x, i.y, i.z);
		i.x += d.x;
		glTexCoord2f(1, 1);
		glVertex3f(i.x, i.y, i.z);
		i.y += d.y;
		glTexCoord2f(1, 0);
		glVertex3f(i.x, i.y, i.z);
		i.x -= d.x;
		glTexCoord2f(0, 0);
		glVertex3f(i.x, i.y, i.z);
		glEnd();
		tex.unbind();
	}
	
	/**
	 * Renders a texture with bottom-left
	 * corner at point i, with a color mod,
	 * using the dimensions of the texture.
	 * 
	 * @param tex
	 * @param i
	 * @param mod
	 */
	public static void texture(Texture tex, Vec i, Color mod) {
		texture(tex, i, new Vec(tex.getWidth(), tex.getHeight()), mod);
	}
	
	/**
	 * Renders a texture with bottom-left corner
	 * at point i using no color modulation.
	 * 
	 * @param tex
	 * @param i
	 */
	public static void texture(Texture tex, Vec i) {
		texture(tex, i, Color.WHITE);
	}
	
	/**
	 * Renders a texture centered around point i.
	 * 
	 * @param tex
	 * @param i
	 */
	public static void ctexture(Texture tex, Vec i) {
		texture(tex, i.dif(new Vec(tex.getWidth(), -tex.getHeight()).scl(.5f)));
	}
	
	/**
	 * Renders a filled ellipse.
	 * 
	 * @param c
	 * @param origin
	 * @param r
	 * @param segs
	 */
	public static void ellipsef(Color c, Vec origin, float r, int segs) {
		arc(c, origin, r, r, 0, 360, segs, GL_TRIANGLE_FAN);
	}
	
	/**
	 * Renders an outline ellipse.
	 * 
	 * @param c
	 * @param origin
	 * @param r
	 * @param segs
	 */
	public static void ellipse(Color c, Vec origin, float r, int segs) {
		arc(c, origin, r, r, 0, 360, segs, GL_LINE_LOOP);
	}
	
	/**
	 * Renders a filled arc.
	 * 
	 * @param c
	 * @param origin
	 * @param r
	 * @param ti
	 * @param tf
	 * @param segs
	 */
	public static void arcf(Color c, Vec origin, float r, float ti, float tf, int segs) {
		arc(c, origin, r, r, ti % 360, tf % 360, segs, GL_TRIANGLE_FAN);
	}
	
	/**
	 * Renders a line arc.
	 * 
	 * @param c
	 * @param origin
	 * @param r
	 * @param ti
	 * @param tf
	 * @param segs
	 */
	public static void arc(Color c, Vec origin, float r, float ti, float tf, int segs) {
		arc(c, origin, r, r, ti % 360, tf % 360, segs, GL_LINE_STRIP);
	}
	
	/**
	 * Renders a filled ellipse.
	 * 
	 * @param c
	 * @param origin
	 * @param hr
	 * @param vr
	 * @param segs
	 */
	public static void ellipsef(Color c, Vec origin, float hr, float vr, int segs) {
		arc(c, origin, hr, vr, 0, 360, segs, GL_TRIANGLE_FAN);
	}
	
	/**
	 * Renders an outline ellipse.
	 * 
	 * @param c
	 * @param origin
	 * @param hr
	 * @param vr
	 * @param segs
	 */
	public static void ellipse(Color c, Vec origin, float hr, float vr, int segs) {
		arc(c, origin, hr, vr, 0, 360, segs, GL_LINE_LOOP);
	}
	
	/**
	 * Renders a filled arc.
	 * 
	 * @param c
	 * @param origin
	 * @param hr
	 * @param vr
	 * @param ti
	 * @param tf
	 * @param segs
	 */
	public static void arcf(Color c, Vec origin, float hr, float vr, float ti, float tf, int segs) {
		arc(c, origin, hr, vr, ti % 360, tf % 360, segs, GL_TRIANGLE_FAN);
	}
	
	/**
	 * Renders an outline arc.
	 * 
	 * @param c
	 * @param origin
	 * @param hr
	 * @param vr
	 * @param ti
	 * @param tf
	 * @param segs
	 */
	public static void arc(Color c, Vec origin, float hr, float vr, float ti, float tf, int segs) {
		arc(c, origin, hr, vr, ti % 360, tf % 360, segs, GL_LINE_STRIP);
	}
	
	private static void arc(Color c, Vec origin, float hr, float vr, float ti, float tf, int segs, int mode) {
		if(!immediateEnabled()) return;
		ti = (float) Math.toRadians(ti);
		tf = (float) Math.toRadians(tf);
		float theta = (float) (tf / (segs - 1));
		float tanFac = (float) Math.tan(theta);
		float radFac = (float) Math.cos(theta);
		float x = hr * (float) Math.cos(ti);
		float y = vr * (float) Math.sin(ti);
		float rath = hr / vr;
		float ratv = vr / hr;
		glBegin(mode);
		glColor4f(c.r, c.g, c.b, c.a);
		if(mode == GL_TRIANGLE_FAN) {
			glVertex3f(origin.x, origin.y, origin.z);
		}
		for(int i = 0; i < segs; i++) {
			glVertex3f(x + origin.x, y + origin.y, origin.z);
			float tx = rath * -y;
			float ty = ratv * x;
			x += tx * tanFac;
			y += ty * tanFac;
			x *= radFac;
			y *= radFac;
		}
		glEnd();
	}
	
}
