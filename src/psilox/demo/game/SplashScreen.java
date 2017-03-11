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
		font = new Font("Verdana", Font.ITALIC, 72);
		
		Label madeBy = new Label(Color.WHITE, font, "Made by Fizzion Studios");
		madeBy.setAnchor(Anchor.MM);
		madeBy.position.set(viewSize().quo(2));
		
		Interpolator alpha = new Interpolator(v -> {
			madeBy.setColor(madeBy.getColor().aAdj(v));
		});
		alpha.addKeyFrames(0, 0, 3, 0, 4, 1, 8, 1, 9, 0);
		alpha.setOnEnd(() -> Psilox.changeScene(new Menus()));
		alpha.start();
		
		addChildren(alpha, madeBy);
	}
	
	public void update() {
		if(Input.keyTap(Input.ESCAPE)) {
			Psilox.changeScene(new Menus());
		}
	}
	
}
