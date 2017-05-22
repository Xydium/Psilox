package psilox.core;

public class Psilox {
	
	private static final long DEFAULT_INTERVAL = Time.SECOND / 60;
	
	private static Window window;
	
	private static boolean running;
	
	private static long tickInterval = DEFAULT_INTERVAL;
	private static long frameInterval = DEFAULT_INTERVAL;
	public static float dt, du, df;
	public static long ticks;
	
	private static Thread psiloxThread;
	
	public static void start(Window window) {
		if(running) return;
		Psilox.window = window;
		initThread();
	}
	
	private static void loop() {
		long lastTick = Time.now() - tickInterval;
		long lastFrame = Time.now() - frameInterval;
		
		window.initialize();
		//root.addChild(mainNode);
		
		while(running) {
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
			
			running = !window.shouldClose();
		}
		
		window.terminate();
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
