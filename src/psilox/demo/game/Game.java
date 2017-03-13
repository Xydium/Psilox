package psilox.demo.game;

import java.awt.Font;

import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;
import psilox.node.ui.Panel;
import psilox.utils.Pointer.FloatPointer;
import psilox.utils.Pointer.StringPointer;

public class Game extends Node {
	
	private Font UIFontLarge;
	private Font UIFontSmall;
	private Level level;
	private TileMap map;
	private Node entities;
	private Player player;
	private Container UI;
	private float timeElapsed;
	private StringPointer timeLabelText;
	private FloatPointer speedLabelText; 
	private StringPointer playerMapPosText;
	private StringPointer playerTilePosText;
	
	public Game(Level level) {
		this.level = level;
	}
	
	public void enteredTree() {
		Tile.loadAllTiles();
		UIFontLarge = new Font("Verdana", Font.PLAIN, 24);
		UIFontSmall = new Font("Verdana", Font.PLAIN, 12);
		
		map = new TileMap(level.loadMap());
		entities = new Node();
		
		player = new Player(this, map, entities);
		player.position.set(viewSize().quo(2));
		player.mapPosition.set(level.spawnPoint);
		
		entities.position.z = 1;
		player.position.z = 2;
		
		Panel UIBacking = new Panel(new Vec(viewSize().x, 50));
		UIBacking.setColor(Color.DARK_GRAY);
		UIBacking.position.z = 3;
		
		UI = new Container(viewSize(), new Vec(10));
		UI.position.z = 5;

		timeLabelText = new StringPointer("00:00:00");
		Label timeElapsedLabel = new Label(Color.WHITE, UIFontLarge, "%s", timeLabelText);
		timeElapsedLabel.setAnchor(Anchor.BR);
		UI.bottomRight.addChild(timeElapsedLabel);
		
		speedLabelText = new FloatPointer(0);
		Label speedLabel = new Label(Color.WHITE, UIFontLarge, "Speed: %.1f m/h", speedLabelText);
		speedLabel.setAnchor(Anchor.BL);
		UI.bottomLeft.addChild(speedLabel);
		
		Label randomLabel = new Label(Color.WHITE, UIFontLarge, "Level " + (level.ordinal() + 1) + ": " + level.name);
		randomLabel.setAnchor(Anchor.BM);
		UI.bottomMiddle.addChild(randomLabel);

		playerMapPosText = new StringPointer("");
		Label playerMapPosLabel = new Label(Color.RED, UIFontSmall, "Player Pos: %s", playerMapPosText);
		playerMapPosLabel.setAnchor(Anchor.TL);
		UI.topLeft.addChild(playerMapPosLabel);
		
		playerTilePosText = new StringPointer("");
		Label playerTilePosLabel = new Label(Color.RED, UIFontSmall, "Player Tile: %s", playerTilePosText);
		playerTilePosLabel.setAnchor(Anchor.TL);
		playerTilePosLabel.position.y -= 20;
		UI.topLeft.addChild(playerTilePosLabel);
		
		map.addChild(entities);
		addChildren(map, player, UIBacking, UI);
	}
	
	public void update() {
		timeElapsed += Psilox.deltaTime;
		int minutes = (int) timeElapsed / 60;
		int seconds = (int) timeElapsed % 60;
		int millis = (int) (timeElapsed * 100) % 100;
		timeLabelText.set(String.format("%02d:%02d:%02d", minutes, seconds, millis));
	}
	
	public void render() {
		speedLabelText.set(feetSecToMPH(pxTickToFeetSec(player.speed)));
		playerMapPosText.set(player.mapPosition.toString());
		playerTilePosText.set(player.mapPosition.quo(Tile.SIZE.x).ic().toString());
		map.position.set(player.mapPosition.scl(-1).sum(viewSize().quo(2)));
	}
	
	public float pxTickToFeetSec(float pixPerTick) {
		return pixPerTick * 60 * 0.15f;
	}
	
	public float feetSecToMPH(float fps) {
		return fps * 60 * 60 / 5280;
	}
	
}
