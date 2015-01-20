package com.HomeGrownProgramming.aemo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.LinkedList;

public class Platform {
	
	Shape shape;
	Color color;
	LinkedList<Line2D> edges = new LinkedList<Line2D>();
	boolean outline = false;
	
	Platform(Shape shape, Color color, boolean outline) {
		this.shape = shape;
		this.color = color;
		this.outline = outline;
		PathIterator pi = shape.getPathIterator(null);
		double[] coords = new double[6];
		double prevX = Double.NaN, prevY = Double.NaN;
		while(!pi.isDone()) {
			int type = pi.currentSegment(coords);
			if(type != PathIterator.SEG_MOVETO && type != PathIterator.SEG_CLOSE) {
				edges.add(new Line2D.Double(prevX, prevY, coords[0], coords[1]));
			}
			prevX = coords[0];
			prevY = coords[1];
			pi.next();
		}
	}
	
	public void draw(Graphics2D g) {
		if(inView()) {
			AffineTransform af = new AffineTransform();
			af.translate(-TheThread.view.x, -TheThread.view.y);
			g.setColor(color);
			g.fill(af.createTransformedShape(shape));
			if(outline)
				g.draw(af.createTransformedShape(shape));
		}
	}
	
	public boolean inView() {
		return shape.intersects(TheThread.view);
	}
}
