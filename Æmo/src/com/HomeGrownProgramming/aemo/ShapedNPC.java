package com.HomeGrownProgramming.aemo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class ShapedNPC {
	
	Shape shape;
	Point.Double pos;
	int health, maxMoveSpeed, type;
	Point vel = new Point(0, 0);
	boolean inView;
	
	private final double rectWidth = 60, rectHeight = 60;
	public static final int RECTANGLE = 0, TRIANGLE = 1, CIRCLE = 2, OTHER = 3; 
	
	ShapedNPC(Point.Double pos, int type) {
		this.pos = pos;
		this.type = type;
		if(type == RECTANGLE) {
			shape = new Rectangle.Double(pos.x, pos.y, rectWidth, rectHeight);
		}
		health = 1;
		maxMoveSpeed = 8;
	}
	
	public void translate(double x, double y) {
		AffineTransform af = new AffineTransform();
		af.translate(x, y);
		shape = af.createTransformedShape(shape);
	}
	
	public void draw(Graphics2D g) {
		AffineTransform af = new AffineTransform();
		af.translate(-TheThread.view.x, -TheThread.view.y);
		if(type == RECTANGLE)
			g.setColor(Color.RED);
		g.fill(af.createTransformedShape(shape));
	}
	
	public void update() {
		checkInView();
		if(inView) {
			translate(vel.x, vel.y);
			for(Platform p : TheThread.currentStage.platforms) {
				handleCollision(p);
				changeVel(p);
			}
		}
	}
	
	public boolean checkInView() {
		return shape.intersects(TheThread.view);
	}
	
	public void handleCollision(Platform p) {
		
	}
	
	public void changeVel(Platform p) {
		
	}
}
