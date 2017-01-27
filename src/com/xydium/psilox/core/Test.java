package com.xydium.psilox.core;

public class Test {

	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.width = 1280;
		cfg.height = 720;
		cfg.ups = 60;
		Psilox.createRuntime(cfg).start();
	}
	
}
