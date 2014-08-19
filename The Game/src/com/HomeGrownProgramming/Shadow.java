package com.HomeGrownProgramming;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

public class Shadow {

	private Polygon poly;
	public boolean draw = true;
	
	public Shadow(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		int[] xpoints = {(int)(minX+minZ), (int)(minX+maxZ), (int)(maxX+maxZ), (int)(maxX+minZ)};
		int[] ypoints = {(int)(minY-minZ), (int)(minY-maxZ), (int)(maxY-maxZ), (int)(maxY-minZ)};
		poly = new Polygon(xpoints, ypoints, 4);
	}
	
	public void draw(Graphics2D g) {
		if(draw) {
			AffineTransform af = new AffineTransform();
			af.translate(-TheThread.view.x, -TheThread.view.y);
			Shape drawShape = af.createTransformedShape(poly);
			if(inView(drawShape)) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
				g.setColor(Color.BLACK);
				g.fill(drawShape);
				g.setComposite(TheThread.composite);
			}
		}
	}
	
	private boolean inView(Shape s) {
		PathIterator i = s.getPathIterator(null);
		while(!i.isDone()) {
			double[] coords = new double[6];
			i.currentSegment(coords);
			if(TheThread.view.contains(new Point.Double(coords[0], coords[1]))) {
				return true;
			}
			i.next();
		}
		return false;
	}
}
