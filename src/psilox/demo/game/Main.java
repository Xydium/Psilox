package psilox.demo.game;

import psilox.core.Config;
import psilox.core.Psilox;

public class Main {

	public static void main(String[] args) {
		Psilox.start(new Config("Food Truck", 1280, 720, false), new SplashScreen());
	}
	
}
