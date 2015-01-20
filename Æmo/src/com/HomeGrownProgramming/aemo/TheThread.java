package com.HomeGrownProgramming.aemo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class TheThread extends Thread {

	long beginTime, timeDiff, sleeptime = 0; //To get the FPS correct. It helps with skipping and the timing.
	private int framesSkipped = 0, maxFPSskips = 5, FPS = 30, skipTicks = 1000/FPS; // same as above
	private static int Mode;
	public static Rectangle view = new Rectangle(0, 0, Starter.screenSize.width, Starter.screenSize.height);
	public static Stage currentStage;
	public static final int GAME = 0, MAP = 1, PAUSE = 2, START_GAME = 3;
	
	TheThread() {
		setMode(START_GAME);
	}
	
	public static void setMode(int mode) {
		if(mode == START_GAME) {
			currentStage = new Stage(0);
			mode = GAME;
		}
		Mode = mode;
	}
	
	public static int getMode() {
		return Mode;
	}
	
	private void render() {
		Graphics2D g = (Graphics2D)Starter.bufferStrategy.getDrawGraphics(); // IDK what g is, but we need it because it connects us to our window
		if (!Starter.bufferStrategy.contentsLost()) { // idk, but put all the drawing functions inside of this if statement
			BufferedImage image = generateScreen();
			g.drawImage(image, 0, 0, Starter.screenSize.width, Starter.screenSize.height, 0, 0, image.getWidth(), image.getHeight(), Starter.frame);
			Starter.bufferStrategy.show(); // make stuff appear
        	g.dispose(); // release the resources that we used to make the screen for this frame
        	Toolkit.getDefaultToolkit().sync(); // idk, but without it things look glitchy
		}
	}
	
	private BufferedImage generateScreen() {
		BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Starter.screenSize.width, Starter.screenSize.height);
		if(Mode == GAME) {
			g.setColor(Color.BLACK);
			for(Platform p : currentStage.platforms) {
				p.draw(g);
			}
			currentStage.aemo.draw(g);
			for(ShapedNPC s : currentStage.npcs) {
				s.draw(g);
			}
		}
		g.dispose();
		return image;
	}
	
	private void update() {
		if(Mode == GAME) {
			currentStage.aemo.update();
			for(int i = 0; i < currentStage.npcs.length; i++) {
				currentStage.npcs[i].update();
			}
		}
	}
	
	@Override
	public void run() {
		while(true) {
			beginTime = System.currentTimeMillis(); //time of the start of the cycle
			if(Mode != PAUSE) {
				update();
			}
			render();
			timeDiff = System.currentTimeMillis()-beginTime; // time at the end of the cycle
			sleeptime = skipTicks-timeDiff; // time the computer should wait
			if(sleeptime > 0) { // if we should wait
				try {
					Thread.sleep(sleeptime); // then try to wait
				} catch (InterruptedException e) { // if shit gets fucked, don't let it crash
					e.printStackTrace();
				}
			} 
			while(sleeptime < 0 && framesSkipped < maxFPSskips) { // catch up if we are behind
				sleeptime += skipTicks;
				framesSkipped++;
				update();
			}
		}
	}
}
