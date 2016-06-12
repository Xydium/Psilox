package com.xydium.core;

import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.xydium.geometry.Vec2i;
import com.xydium.input.Input;

public class Window {

	private int width;
	private int height;
	private double scale;
	private String title;
	
	private JFrame frame;

	public Window(int width, int height, double scale, String title) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.title = title;
		this.frame = new JFrame(title);
		configureFrame();
	}
	
	public void drawFrame(Image image) {
		frame.getGraphics().drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), null);
	}
	
	public void destroy() {
		frame.dispose();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public double getScale() {
		return scale;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void rescale(double scale) {
		this.scale = scale;
		frame.setSize((int) (width * scale), (int) (height * scale));
	}
	
	public Vec2i getDimensions() {
		return new Vec2i(frame.getWidth(), frame.getHeight());
	}
	
	private void configureFrame() {
		rescale(scale);
		frame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) { Psilox.stop(); }
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.addKeyListener(Input.init());
	}
	
	public String toString() {
		return String.format("Window: Width: %s, Height: %s, Scale: %s, Title: %s", width, height, scale, title);
	}
	
}
