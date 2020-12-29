package com.imaginary.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.imaginary.utils.TileType;

public class World {

	private Map<String, TileType> objectDictionary = new HashMap<String, TileType>();

	private Tile[] tiles;
	private int WIDTH, HEIGHT;

	public World(String path) {
		try {
			AssociateColorObject();

			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();

			int[] mapPixels = new int[WIDTH * HEIGHT];

			tiles = new Tile[WIDTH * HEIGHT];
			map.getRGB(0, 0, WIDTH, HEIGHT, mapPixels, 0, map.getWidth());

			for (int axisX = 0; axisX < WIDTH; axisX++) {
				for (int axisY = 0; axisY < HEIGHT; axisY++) {

					String hexColor = Integer.toHexString(mapPixels[axisX + (axisY * WIDTH)]).toUpperCase();
					TileType type = objectDictionary.get(hexColor);
					
					if(type != null) {
						tiles[axisX + (axisY * WIDTH)] = new Tile(axisX * 16, axisY * 16, type,Tile.AVAILABLE_TILE.get(type));						
					} else {
						tiles[axisX + (axisY * WIDTH)] = new Tile(axisX * 16, axisY * 16, type,Tile.AVAILABLE_TILE.get(TileType.FLOOR));
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		for (int axisX = 0; axisX < WIDTH; axisX++) {
			for (int axisY = 0; axisY < HEIGHT; axisY++) {
				Tile t = tiles[axisX + (axisY * WIDTH)];
				t.render(g);
			}
		}
	}

	public int getWidth() {
		return this.WIDTH;
	}

	public int getHeight() {
		return this.HEIGHT;
	}

	private void AssociateColorObject() {
		objectDictionary.put("FFFFFFFF", TileType.WALL);
		objectDictionary.put("FF000000", TileType.FLOOR);
		objectDictionary.put("FF0006FF", TileType.FLOOR);
	}
}
