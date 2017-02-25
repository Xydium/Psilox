package psilox.node.ui;

import java.awt.Font;

import psilox.graphics.Color;
import psilox.input.Function;
import psilox.math.Vec;
import psilox.node.Node;

public class Button extends Node {

	private static final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 18);
	
	private Panel buttonBase;
	private Node centerPoint;
	private Label buttonText;
	
	public Button(Vec dimensions, String label, Function action) {
		buttonBase = new Panel(dimensions);
		buttonBase.setColor(Color.DARK_GRAY);
		buttonBase.setOnReleased(action);
		buttonText = new Label(Color.WHITE, DEFAULT_FONT, label);
		buttonText.setAnchor(Anchor.MM);
		buttonText.position.z = .1f;
		centerPoint = new Node();
	}
	
	public void setAnchor(Anchor anchor) {
		super.setAnchor(anchor);
		buttonBase.setAnchor(anchor);
		centerPoint.position.set(anchor.calculate(buttonBase.getDimensions().scl(-1), Vec.ZERO));
		centerPoint.position.add(buttonBase.getDimensions().half());
	}
	
	public void enteredTree() {
		addChildren(buttonBase, centerPoint);
		centerPoint.addChild(buttonText);
	}
	
}
