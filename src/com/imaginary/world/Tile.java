package com.imaginary.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.imaginary.main.Game;
import com.imaginary.utils.TileType;

public class Tile {

	public static Map<TileType, BufferedImage> AVAILABLE_TILE = new HashMap<TileType, BufferedImage>();
	private static boolean lockTiles;

	private BufferedImage sprite;
	private int x, y;
	private TileType type;

	public Tile(int x, int y, TileType type, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.type = type;

		if (!Tile.lockTiles) {
			loadTiles();
		}
	}

	public void render(Graphics g) {
		g.drawImage(sprite, x, y, null);
	}

	public TileType getType() {
		return this.type;
	}

	private void loadTiles() {
		AVAILABLE_TILE.put(TileType.FLOOR, Game.spritesheet.getSprite(0, 0, 16, 16));
		AVAILABLE_TILE.put(TileType.WALL, Game.spritesheet.getSprite(16, 0, 16, 16));
		lockTiles = true;
	}
}
