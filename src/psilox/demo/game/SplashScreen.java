package psilox.demo.game;

import java.awt.Font;

import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.input.Input;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;
import psilox.node.utility.Interpolator;

public class SplashScreen extends Node {

	private Font font;
	private Font smallFont;
	private Container UI;
	
	public void enteredTree() {
		font = new Font("Verdana", Font.ITALIC, 48);
		smallFont = new Font("Verdana", Font.ITALIC, 24);

		UI = new Container(new Vec(10, 110), new Vec(10));
		UI.setAnchor(Anchor.MM);
		UI.position.set(viewSize().half());	
		
		Color navy = new Color(0x1D59B1FF);
		Color gray = new Color(0xE0E0E0FF);
		
		Label madeBy = new Label(gray, font, "SJP Game Design and Development Club");
		madeBy.setAnchor(Anchor.TM);
		UI.topMiddle.addChild(madeBy);
		
		Label presents = new Label(navy, smallFont, "Presents...");
		presents.setAnchor(Anchor.BM);
		UI.bottomMiddle.addChild(presents);
		
		Interpolator alpha = new Interpolator(v -> {
			madeBy.setColor(madeBy.getColor().aAdj(v));
			presents.setColor(presents.getColor().aAdj(v));
		});
		alpha.addKeyFrames(0, 0, 3, 0, 4, 1, 8, 1, 9, 0);
		alpha.setOnEnd(() -> Psilox.changeScene(new Menus()));
		alpha.start();
		
		addChildren(alpha, UI);
	}
	
	public void update() {
		if(Input.keyTap(Input.ESCAPE)) {
			Psilox.changeScene(new Menus());
		}
	}
	
}
