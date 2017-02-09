package psilox.utils;

public class Time {

	public static final long SECOND = 1000000000;
	public static final long MILLIS = 1000000;
	public static final long MICROS = 1000;
	
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;
	
	private static long profile;
	
	public static void profile() {
		profile = Time.now();
	}
	
	public static long now() {
		return System.nanoTime();
	}
	
	public static long since() {
		return since(profile);
	}
	
	public static long since(long time) {
		return System.nanoTime() - time;
	}

	public static double flipFlop(long offset, long duration) {
		double now = (now() + offset) % duration;
		if(now < duration / 2) {
			return now / (duration / 2);
		} else {
			return 1 - (now - duration / 2) / (duration / 2);
		}
	}
	
	public static int flipFlop(int max, long offset, long duration) {
		return (int) (flipFlop(offset, duration) * max);
	}
	
}
