package psilox.demo.game;

import java.awt.Font;

import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.Sprite;
import psilox.node.ui.Button;
import psilox.node.ui.Container;
import psilox.node.ui.Label;

public class Menus extends Node {
	
	public MainMenu mainMenu;
	public HelpMenu helpMenu;
	public LevelMenu levelMenu;
	
	public void enteredTree() {
		mainMenu = new MainMenu();
		helpMenu = new HelpMenu();
		helpMenu.position.set(viewSize().x, 0);
		levelMenu = new LevelMenu();
		levelMenu.position.set(viewSize().x * 2, 0);
		addChildren(mainMenu, helpMenu, levelMenu);
	}
	
	public void show(Node subMenu) {
		position.set(subMenu.position.scl(-1));
	}
	
}

class MainMenu extends Node {
	
	public Menus menu;
	public Container container;
	public Font titleFont;
	
	public void enteredTree() {
		menu = (Menus) getParent();
		container = new Container(viewSize(), new Vec(20));
		titleFont = new Font("Verdana", Font.BOLD, 48);
		
		Label title = new Label(Color.ORANGE, titleFont, "Food Truck");
		title.setAnchor(Anchor.TL);
		container.topLeft.addChild(title);
		
		Button play = new Button(new Vec(200, 25), "Play", () -> menu.show(menu.levelMenu));
		play.position.set(0, 60);
		Button help = new Button(new Vec(200, 25), "Help", () -> menu.show(menu.helpMenu));
		help.position.set(0, 30);
		Button quit = new Button(new Vec(200, 25), "Quit", Psilox::stop);
		
		container.bottomLeft.addChildren(play, help, quit);
		
		addChildren(container);
	}
	
}

class HelpMenu extends Node {
	
	public Menus menu;
	public Container container;
	public Font titleFont;
	
	public void enteredTree() {
		menu = (Menus) getParent();
		container = new Container(viewSize(), new Vec(20));
		titleFont = menu.mainMenu.titleFont;
		
		Label title = new Label(Color.ORANGE, titleFont, "Help");
		title.setAnchor(Anchor.TL);
		container.topLeft.addChild(title);
		
		Sprite help = new Sprite("psilox/demo/game/help.jpg");
		help.setAnchor(Anchor.MM);
		container.center.addChild(help);
		
		Button toMenu = new Button(new Vec(200, 25), "Return to Menu", () -> menu.show(menu.mainMenu));
		container.bottomLeft.addChild(toMenu);
		
		addChildren(container);
	}
	
}

class LevelMenu extends Node {
	
	public Menus menu;
	public Container container;
	public Font titleFont;
	
	public void enteredTree() {
		menu = (Menus) getParent();
		container = new Container(viewSize(), new Vec(20));
		titleFont = menu.mainMenu.titleFont;
		
		Label title = new Label(Color.ORANGE, titleFont, "Level Select");
		title.setAnchor(Anchor.TL);
		container.topLeft.addChild(title);
		
		Button toMenu = new Button(new Vec(200, 25), "Return to Menu", () -> menu.show(menu.mainMenu));
		container.bottomLeft.addChild(toMenu);
		
		Button testLevel = new Button(new Vec(200, 25), "Test Level", () -> print("Enter test level!"));
		testLevel.setAnchor(Anchor.MM);
		container.center.addChild(testLevel);
		
		addChildren(container);
	}
	
}