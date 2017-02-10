package psilox.graphics;

import static org.lwjgl.opengl.GL11.*;

import psilox.math.Vec;

public class Draw {

	private static Shader circle;
	
	public static void loadDrawShaders() {
		if(circle == null) {
			circle = new Shader("shaders/circle.shd");
		}
	}
	
	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public static void clearColor(Color c) {
		glClearColor(c.r, c.g, c.b, c.a);
	}
	
	public static void clearTransform() {
		glLoadIdentity();
	}
	
	public static void translate(Vec p) {
		glTranslatef(p.x, p.y, p.z);
	}
	
	public static void rotate(float theta) {
		glRotatef(theta, 0, 0, 1);
	}
	
	public static void scale(Vec s) {
		glScalef(s.x, s.y, s.z);
	}
	
	public static void transform(Vec p, float theta, Vec s) {
		translate(p);
		rotate(theta);
		scale(s);
	}
	
	public static void setTransform(Vec p, float theta, Vec s) {
		clearTransform();
		transform(p, theta, s);
	}
	
	public static void immediate(int mode, Color c, Vec... verts) {
		glBegin(mode);
		glColor4f(c.r, c.g, c.b, c.a);
		Vec idv;
		for(int i = 0; i < verts.length; i++) {
			idv = verts[i];
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
	
	public static void poly(Color c, Vec... verts) {
		immediate(GL_TRIANGLE_FAN, c, verts);
	}
	
	public static void outline(Color c, Vec...verts) {
		immediate(GL_LINE_LOOP, c, verts);
	}
	
	public static void strip(Color c, Vec...verts) {
		immediate(GL_TRIANGLE_STRIP, c, verts);
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
