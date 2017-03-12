package psilox.demo.game;

import psilox.math.Vec;
import psilox.utils.FileUtils;

public enum Level {

	DEBUG("DEBUG", "psilox/demo/game/debug.map");
	
	public String name;
	public String mapPath;
	public Vec spawnPoint;
	
	private Level(String name, String mapPath) {
		this.name = name;
		this.mapPath = mapPath;
	}
	
	public Tile[][] loadMap() {
		String[] mapData = FileUtils.loadAsString(mapPath).split("\n");
		String[] meta = mapData[0].split("[x:,]");
		int width = Integer.parseInt(meta[0]);
		int height = Integer.parseInt(meta[1]);
		int spawnX = Integer.parseInt(meta[2]);
		int spawnY = Integer.parseInt(meta[3]);
		Tile[][] map = new Tile[width][height];
		for(int y = 0; y < height; y++) {
			String[] tiles = mapData[y + 1].split(" ");
			for(int x = 0; x < width; x++) {
				map[x][height - y - 1] = Tile.values()[Integer.parseInt(tiles[x])];
			}
		}
		spawnPoint = new Vec(spawnX, spawnY);
		return map;
	}
	
}
