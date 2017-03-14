package psilox.demo.game;

import java.awt.Font;

import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.input.Input;
import psilox.node.Node;
import psilox.node.ui.Label;
import psilox.node.utility.Interpolator;

public class SplashScreen extends Node {

	private Font font;
	
	public void enteredTree() {
		font = new Font("Verdana", Font.ITALIC, 48);

		Color navy = new Color(0x1D59B1FF);
		Color gray = new Color(0xE0E0E0FF);
		
		Label madeBy = new Label(gray, font, "SJP Game Design and Development Club");
		madeBy.setAnchor(Anchor.MM);
		madeBy.position.set(viewSize().half());
		madeBy.position.y += 30;
		
		Label presents = new Label(navy, font, "Presents...");
		presents.setAnchor(Anchor.MM);
		presents.position.set(viewSize().half());
		presents.position.y -= 30;
		
		Interpolator alpha = new Interpolator(v -> {
			madeBy.setColor(madeBy.getColor().aAdj(v));
			presents.setColor(presents.getColor().aAdj(v));
		});
		alpha.addKeyFrames(0, 0, 3, 0, 4, 1, 8, 1, 9, 0);
		alpha.setOnEnd(() -> Psilox.changeScene(new Menus()));
		alpha.start();
		
		addChildren(alpha, madeBy, presents);
	}
	
	public void update() {
		if(Input.keyTap(Input.ESCAPE)) {
			Psilox.changeScene(new Menus());
		}
	}
	
}
