package com.HomeGrownProgramming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Button {
	
	Rectangle2D.Double r;
	Color c;
	BufferedImage image;
	
	public Button(Rectangle2D.Double r, Color c) {
		this.r = r;
		this.c = c;
	}
	
	public Button(Rectangle2D.Double r, BufferedImage image) {
		this.r = r;
		this.image = image;
	}
	
	public void draw(Graphics2D g) {
		if(image == null) {
			g.setColor(c);
			g.fill(r);
		} else {
			g.drawImage(image, (int)r.x, (int)r.y, (int)r.width, (int)r.height, 0, 0, image.getWidth(), image.getHeight(), Starter.frame);
		}
	}

	public boolean contains(Point p) {
		return r.contains(p);
	}

	// You will want to override this when you create a new button. Otherwise, the button will do nothing when it is clicked on
	public void doAction() { }
}
