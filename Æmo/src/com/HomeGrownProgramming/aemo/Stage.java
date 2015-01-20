package com.HomeGrownProgramming.aemo;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class Stage {
	
	int swidth = Starter.screenSize.width, sheight = Starter.screenSize.height; 
	public Platform[] platforms;
	public ShapedNPC[] npcs;
	public Æmo aemo;
	
	public Stage(int stage) {
		switch (stage) {
		case 0:
			loadStage0();
			break;
		}
		postStuff();
	}
	
	private void loadStage0() {
		platforms = new Platform[5];
		platforms[0] = new Platform(new Rectangle.Double(0, sheight*.7, swidth*.2, sheight*.1), Color.BLACK, false);
		platforms[1] = new Platform(new Rectangle.Double(0, sheight*.8, swidth*.4, sheight*.2), Color.BLACK, false);
		platforms[2] = new Platform(new Rectangle.Double(swidth*.6, sheight*.8, swidth*.4, sheight*.2), Color.BLACK, false);
		platforms[3] = new Platform(new Rectangle.Double(swidth*.8, sheight*.7, swidth*.2, sheight*.1), Color.BLACK, false);
		platforms[4] = new Platform(new Rectangle.Double(swidth*.9, sheight*.6, swidth*.1, sheight*.1), Color.BLACK, false);
		npcs = new ShapedNPC[0];
		aemo = new Æmo(new Point.Double(0, sheight*.7-70));
	}
	
	private void postStuff() {
		TheThread.view = new Rectangle((int)(aemo.boundingRect.getCenterX()-swidth/2), (int)(aemo.boundingRect.getCenterY()-sheight/2), swidth, sheight);
	}
}
