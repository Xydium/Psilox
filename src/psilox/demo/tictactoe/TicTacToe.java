package psilox.demo.tictactoe;

import java.awt.Font;
import java.util.ArrayList;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;
import psilox.node.ui.Button;
import psilox.node.ui.Container;
import psilox.node.ui.Label;

public class TicTacToe extends Node {

	private Container container;
	private Tile[][] tiles;
	private Label nextMove;
	private VictoryScreen vs;
	
	public boolean playerTurn = true;
	
	public void enteredTree() {
		addChild(container = new Container("container", viewSize(), new Vec(100)));
		
		container.topMiddle.addChild(nextMove = new Label(null, Anchor.CENTER, Color.WHITE, new Font("Verdana", Font.PLAIN, 16), "Next Move: X"));
		nextMove.pos().y = 65;
		nextMove.setLayer(2);
		
		tiles = new Tile[3][3];
		
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				container.anchorsMat[y][x].addChild(tiles[y][x] = new Tile(this, new Vec(x, y), "", new Vec(100)));
			}
		}
	}
	
	public void moveMade() {
		ArrayList<Tile> xtiles = getTilesWithText("X");
		ArrayList<Tile> otiles = getTilesWithText("O");
		
		if(didWin(xtiles) || didWin(otiles)) {
			for(Tile t : xtiles) {
				t.getLabel().setText("");
			}
			for(Tile t : otiles) {
				t.getLabel().setText("");
			}
			addChild(new VictoryScreen());
		}
		
		nextMove.setText("Next Move: " + (playerTurn ? "X" : "O"));
		nextMove.pos().y = 65;
		nextMove.setLayer(2);
	}
	
	private boolean didWin(ArrayList<Tile> tiles) {
		for(int i = 0; i < tiles.size(); i++) {
			int sameRowCount = 0;
			int sameColCount = 0;
			int diagonalCount = 0;
			
			Tile it = tiles.get(i);
			
			for(int j = i + 1; j < tiles.size(); j++) {
				Tile jt = tiles.get(j);
				
				if(it.coord.x == jt.coord.x) sameColCount++;
				if(it.coord.y == jt.coord.y) sameRowCount++;
				 
				float ang = jt.coord.dif(it.coord).ang();
				if(ang % 90 == 45) diagonalCount++;
			}
			
			if(sameRowCount == 2 || sameColCount == 2 || diagonalCount == 2)
				return true;
		}
		
		return false;
	}
	
	private ArrayList<Tile> getTilesWithText(String text) {
		ArrayList<Tile> result = new ArrayList<Tile>();
		
		for(Tile[] ts : tiles) {
			for(Tile t : ts) {
				if(t.getLabel().getText().equals(text)) {
					result.add(t);
				}
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		new Psilox(new Config("Tic Tac Toe", 500, 500, false)).start(new TicTacToe());
	}
	
}

class Tile extends Button {
	
	private static final Font FONT = new Font("Verdana", Font.BOLD, 72);
	
	private TicTacToe game;
	public Vec coord;
	
	public Tile(TicTacToe game, Vec coord, String tag, Vec dimensions) {
		super(
				tag, 
				dimensions, 
				Anchor.CENTER, 
				"", 
				Color.DARK_GRAY, 
				Color.LIGHT_GRAY, 
				Color.WHITE, 
				Color.BLACK, 
				FONT, 
				() -> {}
		);
		this.game = game;
		this.coord = coord;
		setOnReleased(this::flipTile);
	}
	
	private void flipTile() {
		if(getLabel().getText() != "") return;
		if(game.playerTurn) {
			getLabel().setText("X");
			game.playerTurn = false;
		} else {
			getLabel().setText("O");
			game.playerTurn = true;
		}
		game.moveMade();
	}
	
}

class VictoryScreen extends Node {
	
	private float alpha = 1;
	
	public void enteredTree() {
		setLayer(10);
	}
	
	public void render() {
		Draw.quad(Color.GREEN.aAdj(alpha), Vec.ZERO, viewSize());
		alpha -= psilox().deltaTime();
		if(alpha < 0) {
			freeSelf();
		}
	}
	
}