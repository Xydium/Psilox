package psilox.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.util.Stack;

import psilox.math.Mat4;
import psilox.math.Transform;
import psilox.math.Vec;
import psilox.utils.Log;

public class Draw {

	private static Shader circle;
	private static int[] viewport = new int[4];
	private static float[] clearColor = new float[4];
	private static Stack<Mat4> transforms = new Stack<Mat4>();
	public static boolean immediateMode = false;
	
	public static void loadDrawShaders() {
		if(circle == null) {
			circle = new Shader("shaders/circle.shd");
		}
	}
	
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
	
	public static void restoreFrameBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);
		clearColor(new Color(clearColor[0], clearColor[1], clearColor[2], clearColor[3]));
	}
	
	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public static void clearColor(Color c) {
		glClearColor(c.r, c.g, c.b, c.a);
	}
	
	public static void clearTransform() {
		glLoadIdentity();
		transforms.clear();
		transforms.push(Mat4.identity());
	}
	
	public static void pushTransform(Mat4 transform) {
		if(transforms.size() == 0) {
			transforms.push(Mat4.identity());
		}
		transforms.push(currentTransform().multiply(transform));
		if(immediateMode) {
			glLoadMatrixf(currentTransform().elements);
		}
	}
	
	public static void pushTransform(Transform transform) {
		pushTransform(transform.toMatrix());
	}
	
	public static Mat4 currentTransform() {
		return transforms.peek();
	}
	
	public static void popTransform() {
		if(transforms.size() > 1) {
			transforms.pop();
			if(immediateMode) {
				glLoadMatrixf(currentTransform().elements);
			}
		}
	}
	
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
	
	public static void translate(Vec p) {
		if(!immediateEnabled()) return;
		glTranslatef(p.x, p.y, p.z);
	}
	
	public static void rotate(float theta) {
		if(!immediateEnabled()) return;
		glRotatef(theta, 0, 0, 1);
	}
	
	public static void scale(Vec s) {
		if(!immediateEnabled()) return;
		glScalef(s.x, s.y, s.z);
	}
	
	public static void transform(Vec p, float theta, Vec s) {
		if(!immediateEnabled()) return;
		translate(p);
		rotate(theta);
		scale(s);
	}
	
	public static void setTransform(Vec p, float theta, Vec s) {
		if(!immediateEnabled()) return;
		clearTransform();
		transform(p, theta, s);
	}
	
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
	
	public static void point(Color c, Vec i) {
		immediate(GL_POINTS, c, i);
	}
	
	public static void line(Color c, Vec i, Vec j) {
		immediate(GL_LINES, c, i, j);
	}
	
	public static void tri(Color c, Vec i, Vec j, Vec k) {
		immediate(GL_TRIANGLES, c, i, j, k);
	}
	
	public static void quad(Color c, Vec i, Vec j, Vec k, Vec l) {
		immediate(GL_QUADS, c, i, j, k, l);
	}
	
	public static void quad(Color c, Vec p, Vec s) {
		quad(c, p, new Vec(p.x + s.x, p.y), new Vec(p.x + s.x, p.y + s.y), new Vec(p.x, p.y + s.y));
	}
	
	public static void cquad(Color c, Vec p, Vec s) {
		quad(c, p.dif(s.scl(.5f)), s);
	}
	
	public static void poly(Color c, Vec... verts) {
		immediate(GL_TRIANGLE_FAN, c, verts);
	}
	
	public static void outline(Color c, Vec...verts) {
		immediate(GL_LINE_LOOP, c, verts);
	}
	
	public static void strip(Color c, Vec...verts) {
		immediate(GL_TRIANGLE_STRIP, c, verts);
	}
	
	public static void texture(Texture tex, Vec i) {
		if(!immediateEnabled()) return;
		tex.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex3f(i.x, i.y, i.z);
		i.x += tex.getWidth();
		glTexCoord2f(1, 0);
		glVertex3f(i.x, i.y, i.z);
		i.y -= tex.getHeight();
		glTexCoord2f(1, 1);
		glVertex3f(i.x, i.y, i.z);
		i.x -= tex.getWidth();
		glTexCoord2f(0, 1);
		glVertex3f(i.x, i.y, i.z);
		glEnd();
		tex.unbind();
	}
	
	public static void ctexture(Texture tex, Vec i) {
		texture(tex, i.dif(new Vec(tex.getWidth(), -tex.getHeight()).scl(.5f)));
	}
	
	public static void ellipsef(Color c, Vec origin, float r) {
		circle.enable();
		circle.setUniform3f("u_origin", origin);
		circle.setUniform1f("u_radius", r);
		circle.setUniform1i("u_filled", GL_TRUE);
		quad(c, origin.sum(new Vec(-r)), origin.sum(new Vec(r, -r)), origin.sum(new Vec(r, r)), origin.sum(new Vec(-r, r)));
		circle.disable();
	}
	
	public static void ellipse(Color c, Vec origin, float r) {
		circle.enable();
		circle.setUniform3f("u_origin", origin);
		circle.setUniform1f("u_radius", r);
		circle.setUniform1i("u_filled", GL_FALSE);
		quad(c, origin.sum(new Vec(-r)), origin.sum(new Vec(r, -r)), origin.sum(new Vec(r, r)), origin.sum(new Vec(-r, r)));
		circle.disable();
	}
	
	public static void ellipsef(Color c, Vec origin, float r, int segs) {
		arc(c, origin, r, r, 0, 360, segs, GL_TRIANGLE_FAN);
	}
	
	public static void ellipse(Color c, Vec origin, float r, int segs) {
		arc(c, origin, r, r, 0, 360, segs, GL_LINE_LOOP);
	}
	
	public static void arcf(Color c, Vec origin, float r, float ti, float tf, int segs) {
		arc(c, origin, r, r, ti % 360, tf % 360, segs, GL_TRIANGLE_FAN);
	}
	
	public static void arc(Color c, Vec origin, float r, float ti, float tf, int segs) {
		arc(c, origin, r, r, ti % 360, tf % 360, segs, GL_LINE_STRIP);
	}
	
	public static void ellipsef(Color c, Vec origin, float hr, float vr, int segs) {
		arc(c, origin, hr, vr, 0, 360, segs, GL_TRIANGLE_FAN);
	}
	
	public static void ellipse(Color c, Vec origin, float hr, float vr, int segs) {
		arc(c, origin, hr, vr, 0, 360, segs, GL_LINE_LOOP);
	}
	
	public static void arcf(Color c, Vec origin, float hr, float vr, float ti, float tf, int segs) {
		arc(c, origin, hr, vr, ti % 360, tf % 360, segs, GL_TRIANGLE_FAN);
	}
	
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
