package com.HomeGrownProgramming.aemo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Æmo {

	Point.Double pos, vel;
	Shape[] shapes;
	Rectangle.Double boundingRect;
	boolean drawOutline = false, onSomething = false, jumpPressed = false;
	BufferedImage overlay;
	int jump = 0, movementDir = NEITHER, dir = RIGHT, acc = 3;
	
	public static final int maxVel = 9, gravityAcc = 2;
	public static final int LEFT = -1, NEITHER = 0, RIGHT = 1;
	
	Æmo(Point.Double pos) {
		this.pos = pos;
		vel = new Point.Double(0, 0);
		shapes = new Shape[6];
		// Pink parts
		shapes[0] = new Ellipse2D.Double(pos.x+7, pos.y, 45, 40);
		shapes[1] = new Ellipse2D.Double(pos.x+3, pos.y+15, 53, 40);
		shapes[2] = new Ellipse2D.Double(pos.x, pos.y+25, 60, 30);
		shapes[3] = new RoundRectangle2D.Double(pos.x+5, pos.y+35, 50, 20, 15, 15);
		// Cyan parts now
		shapes[4] = new Ellipse2D.Double(pos.x+15, pos.y+10, 30, 30);
		shapes[5] = new Ellipse2D.Double(pos.x+10, pos.y+20, 40, 30);
		boundingRect = new Rectangle.Double(pos.x, pos.y, 60, 55);
		try {
			overlay =  ImageIO.read(getClass().getClassLoader().getResourceAsStream("aemoEyes.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		AffineTransform af = new AffineTransform();
		af.translate(-TheThread.view.x, -TheThread.view.y);
		Point.Double drawPos = new Point.Double(pos.x-TheThread.view.x, pos.y-TheThread.view.y);
		g.setColor(Color.PINK);
		for(int i = 0; i < shapes.length; i++) {
			if(i == 4)
				g.setColor(Color.CYAN);
				g.fill(af.createTransformedShape(shapes[i]));
			if(i == shapes.length-1 && drawOutline) {
				g.setColor(Color.BLACK);
				g.draw(af.createTransformedShape(boundingRect));
			}
		}
		g.setColor(Color.RED);
		if(dir == RIGHT) {
			g.drawImage(overlay, (int)drawPos.x, (int)drawPos.y, (int)drawPos.x+60, (int)drawPos.y+55, 0, 0, overlay.getWidth(), overlay.getHeight(), Starter.frame);
		} else if(dir == LEFT) {
			g.drawImage(overlay, (int)drawPos.x, (int)drawPos.y, (int)drawPos.x+60,	(int)drawPos.y+55, overlay.getWidth(), 0, 0, overlay.getHeight(), Starter.frame);
		}
	}
	
	public void update() {
		onSomething = onSomething();
		if(!onSomething)
			vel.y += gravityAcc;
		else
			vel.y = 0;
		move(movementDir);
		if(jumpPressed) {
			jump();
		}
		jumpPressed = false;
		translate(vel.x, vel.y);
		for(Platform p : TheThread.currentStage.platforms) {
			Line2D intersectingLine = intersects(p);
			if(intersectingLine != null) {
				handleCollision(p);
				changeVel(intersectingLine);
			}
		}
	}
	
	private boolean onSomething() {
		Rectangle.Double belowRect = new Rectangle.Double(boundingRect.x+1, boundingRect.y+boundingRect.height-2, boundingRect.width-2, 4);
		for(Platform p : TheThread.currentStage.platforms) {
			if(intersects(belowRect.getPathIterator(null), p) != null) {
				jump = 0;
				return true;
			}
		}
		return false;
	}
	
	private Line2D intersects(Platform p) {
		PathIterator pi = shapes[0].getPathIterator(null);
		for(int i = 0; i < 2; i++) {
			Line2D intersect = intersects(pi, p);
			if(intersect != null) {
				return intersect;
			}
			pi = shapes[3].getPathIterator(null);
		}
		return null;
	}
	
	private Line2D intersects(PathIterator pi, Platform p) {
		double[] coords = new double[6];
		double prevX = 0, prevY = 0, startX = 0, startY = 0;
		while(!pi.isDone()) {
			int type = pi.currentSegment(coords);
			if(type == PathIterator.SEG_MOVETO) {
				startX = coords[0];
				startY = coords[1];
			} else {
				for(Line2D d : p.edges) {
					if (type != PathIterator.SEG_CLOSE) {
						if(d.intersectsLine(prevX, prevY, coords[0], coords[1])) {
							return d;
						}
					} else {
						if(d.intersectsLine(prevX, prevY, startX, startY)) {
							return d;
						}
					}
				}
			}
			prevX = coords[0];
			prevY = coords[1];
			pi.next();
		}
		return null;
	}
	
	private void handleCollision(Platform p) {
		translate(-vel.x, -vel.y);
		Point.Double calcVel = new Point.Double(vel.x/2, vel.y/2);
		for(int i = 0; i < 3; i++) {
			if(intersects(p) != null) {
				translate(-calcVel.x, -calcVel.y);
			} else{
				translate(calcVel.x, calcVel.y);
			}
			calcVel = new Point.Double(calcVel.x/2, calcVel.y/2);
		}
		for(int i = 0; i < 6 && intersects(p) != null; i++) {
			translate(-calcVel.x, -calcVel.y);
		}
	}
	
	private void changeVel(Line2D l) {
		Point.Double v = makeVector(l);
		vel = new Point.Double(v.x*(v.x*vel.x+v.y*vel.y)/(v.x*v.x+v.y*v.y), v.y*(v.x*vel.x+v.y*vel.y)/(v.x*v.x+v.y*v.y));
	}
	
	private Point.Double makeVector(Line2D l) {
		return new Point.Double(l.getX2()-l.getX1(), l.getY2()-l.getY1());
	}
	
	public void move(int dir) {
		if(dir == LEFT) {
			if(onSomething) {
				vel.x = vel.x <= -maxVel ? -((Math.abs(vel.x)-maxVel)*.6+maxVel) : vel.x-acc; 
			} else {
				vel.x -= acc*.4;
			}
		} else if(dir == RIGHT) {
			if(onSomething) {
				vel.x = vel.x >= maxVel ? (Math.abs(vel.x)-maxVel)*.6+maxVel : vel.x+acc; 
			} else {
				vel.x += acc*.4;
			}
		}
		if(dir == NEITHER && onSomething) {
			vel.x = (int)(Math.abs(vel.x) <= 2 ? 0 : vel.x*.8);
		}
		if(dir != NEITHER && this.dir != dir)
			this.dir = dir;
	}
	
	private void jump() {
		if(jump < 2) {
			vel.y -= 16;
			jump++;
		}
	}
	
	public void translate(double x, double y) {
		AffineTransform af = new AffineTransform();
		af.translate(x, y);
		for(int i = 0; i < shapes.length; i++) {
			shapes[i] = af.createTransformedShape(shapes[i]);
		}
		boundingRect.x += x;
		boundingRect.y += y;
		pos.x += x;
		pos.y += y;
		if(pos.x <= TheThread.view.x+200) { 
			TheThread.view.x = (int)pos.x-200;
		} else if(pos.x+boundingRect.width >= TheThread.view.x+TheThread.view.width-200) {
			TheThread.view.x = (int)(pos.x+boundingRect.width-TheThread.view.width+200);
		}
		if(pos.y <= TheThread.view.y+200) { 
			TheThread.view.y = (int)pos.y-200;
		} else if(pos.y+boundingRect.height >= TheThread.view.y+TheThread.view.height-200) {
			TheThread.view.y = (int)(pos.y+boundingRect.height-TheThread.view.height+200);
		}
	}
}
