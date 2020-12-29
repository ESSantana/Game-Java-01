package com.imaginary.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.imaginary.graphics.Spritesheet;

public class Player extends Entity {

	public boolean up, down, left, right;
	public int speed = 3;

	private int frames = 0, maxFrames = 10, animationIndex = 0;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage lastDir;

	public Player() {
	}

	public Player(int x, int y, int width, int height, Spritesheet spritesheet) {
		super(x, y, width, height, spritesheet.getSprite(32, 0, 16, 16));

		rightPlayer = new BufferedImage[4];
		for (int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = spritesheet.getSprite((i + 2) * 16, 0, 16, 16);
		}

		leftPlayer = new BufferedImage[4];
		for (int i = 0; i < leftPlayer.length; i++) {
			leftPlayer[i] = spritesheet.getSprite((i + 2) * 16, 16, 16, 16);
		}
		
		lastDir = rightPlayer[0];
	}

	@Override
	public void tick() {
				
		if (up) {
			setY(getY() - speed);
		} else if (down) {
			setY(getY() + speed);
		}

		if (right) {
			handleAnimation();
			setX(getX() + speed);
		} else if (left) {
			handleAnimation();
			setX(getX() - speed);
		}
		
		handleAnimation();
	}

	@Override
	public void render(Graphics g) {
		if (right) {
			g.drawImage(rightPlayer[animationIndex], this.getX(), this.getY(), null);
			lastDir = rightPlayer[0];
		} else if (left) {
			g.drawImage(leftPlayer[animationIndex], this.getX(), this.getY(), null);
			lastDir = leftPlayer[3];
		} else {
			g.drawImage(lastDir, this.getX(), this.getY(), null);
		}
	}
	
	public void handleAnimation() {
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			animationIndex++;
			if(animationIndex == rightPlayer.length - 1) {
				animationIndex = 0;
			}
		}
	}

}
