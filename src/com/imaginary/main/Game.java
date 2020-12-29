package com.imaginary.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.JFrame;

import com.imaginary.entities.Entity;
import com.imaginary.entities.Player;
import com.imaginary.graphics.Spritesheet;
import com.imaginary.world.World;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private BufferedImage buffImg;

	private boolean isRunning;
	private final int WIDTH = 240;
	private final int HEIGHT = 160;
	private final int SCALE = 3;

	public List<Entity> entities;
	public static Spritesheet spritesheet;
	
	public World world;

	public Game() {
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		world = new World("/map.png");

		Player player = new Player(0, 0, 16, 16, spritesheet);

		entities.add(player);
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	private void initFrame() {
		frame = new JFrame("Meu Jogo");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = buffImg.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g);
		}

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(buffImg, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		bs.show();
	}

	@Override
	public void run() {

		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000_000_000 / amountOfTicks;
		double delta = 0;

		int frameRate = 0;
		double timer = System.currentTimeMillis();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frameRate++;
				delta--;
			}

			// FPS monitor
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("Frame Rate: " + frameRate);
				frameRate = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		controlPlayer(e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		controlPlayer(e, false);
	}

	private void controlPlayer(KeyEvent e, boolean setValue) {
		Player p = new Player();

		for (Entity entity : entities) {
			if (entity instanceof Player) {
				p = (Player) entity;
			}
		}

		int[] up = new int[] { KeyEvent.VK_UP, KeyEvent.VK_W };
		int[] down = new int[] { KeyEvent.VK_DOWN, KeyEvent.VK_S };
		int[] left = new int[] { KeyEvent.VK_LEFT, KeyEvent.VK_A };
		int[] right = new int[] { KeyEvent.VK_RIGHT, KeyEvent.VK_D };

		if (matchKeys(e, up))
			p.up = setValue;
		else if (matchKeys(e, down))
			p.down = setValue;
		
		if (matchKeys(e, left))
			p.left = setValue;
		else if (matchKeys(e, right))
			p.right = setValue;
		
	}

	private boolean matchKeys(KeyEvent e, int[] matchingKeys) {

		boolean match = false;

		for (int key : matchingKeys) {
			if (key == e.getKeyCode()) {
				match = true;
				break;
			}
		}
		return match;
	}
}
