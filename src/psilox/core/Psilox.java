package psilox.core;

import psilox.audio.Audio;
import psilox.node.Node;
import psilox.utility.Time;

public class Psilox {
	
	private static final long DEFAULT_INTERVAL = Time.SECOND / 60;
	
	private static Window window;
	
	private static boolean running;
	
	private static long tickInterval = DEFAULT_INTERVAL;
	private static long frameInterval = DEFAULT_INTERVAL;
	public static float dt, du, df;
	public static long ticks;
	
	private static Thread psiloxThread;
	
	public static final Node root = new Node("root");
	private static Node mainNode;
	private static Node nextScene;
	
	public static void start(Window window, Node mainNode) {
		if(running) return;
		Psilox.window = window;
		Psilox.mainNode = mainNode;
		initThread();
	}
	
	public static void quit() {
		running = false;
	}
	
	private static void update() {
		window.pollEvents();
		root.updateChildren();
		Node.applyChanges();
	}
	
	private static void render() {
		window.clear();
		
		window.renderTree(root);
		
		window.logLastError();
		window.swapBuffers();
	}
	
	private static void loop() {
		long lastTick = Time.now() - tickInterval;
		long lastFrame = Time.now() - frameInterval;
		
		window.initialize();
		Audio.init();
		root.addChild(mainNode);
		
		while(running) {
			if(nextScene != null) {
				root.swapChild(nextScene, 0);
				nextScene = null;
			}
			if(Time.since(lastTick) >= tickInterval) {
				dt = Time.since(lastTick) / (float) Time.SECOND;
				lastTick = Time.now();
				update();
				ticks++;
				du = (Time.now() - lastTick) / (float) Time.SECOND;
			}
			
			if(Time.since(lastFrame) >= frameInterval) {
				lastFrame = Time.now();
				render();
				df = (Time.now() - lastFrame) / (float) Time.SECOND;
			}
			
			running &= !window.shouldClose();
		}
		
		Audio.shutdown();
		window.terminate();
	}
	
	public static void changeScene(Node nextScene) {
		Psilox.nextScene = nextScene;
	}
	
	private static void initThread() {
		running = true;
		if(System.getProperty("os.name").contains("Mac")) {
			System.setProperty("java.awt.headless", "true");
			loop();
		} else {
			(psiloxThread = new Thread(Psilox::loop, "psilox")).start();
		}
	}
	
}
