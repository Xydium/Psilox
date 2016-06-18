package com.xydium.core;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
		frame.getContentPane().getGraphics().drawImage(image, 0, 0, (int) (width * scale), (int) (height * scale), null);
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
		frame.getContentPane().setMinimumSize(new Dimension((int)(width * scale), (int)(height * scale)));
		frame.getContentPane().setPreferredSize(new Dimension((int)(width * scale), (int)(height * scale)));
		frame.getContentPane().setMaximumSize(new Dimension((int)(width * scale), (int)(height * scale)));
		frame.pack();
		frame.setSize(frame.getWidth() - 10, frame.getHeight() - 10);
	}
	
	public Vec2i getDimensions() {
		return new Vec2i(frame.getWidth(), frame.getHeight());
	}
	
	private void configureFrame() {
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
		frame.setContentPane(new JPanel());
		rescale(scale);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addKeyListener(Input.init());
	}
	
	public void setFullscreen() {
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public String toString() {
		return String.format("Window: Width: %s, Height: %s, Scale: %s, Title: %s", width, height, scale, title);
	}
	
}
