package psilox.demo.game;

import psilox.utils.FileUtils;

public enum Level {

	DEBUG("DEBUG", "psilox/demo/game/debug.map");
	
	public String name;
	public String mapPath;
	
	private Level(String name, String mapPath) {
		this.name = name;
		this.mapPath = mapPath;
	}
	
	public Tile[][] loadMap() {
		String[] mapData = FileUtils.loadAsString(mapPath).split("\n");
		int width = Integer.parseInt(mapData[0].split("x")[0]);
		int height = Integer.parseInt(mapData[0].split("x")[1]);
		Tile[][] map = new Tile[width][height];
		for(int y = 0; y < height; y++) {
			String[] tiles = mapData[y + 1].split(" ");
			for(int x = 0; x < width; x++) {
				map[x][height - y - 1] = Tile.values()[Integer.parseInt(tiles[x])];
			}
		}
		return map;
	}
	
}
