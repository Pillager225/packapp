/* Planes are described in this manner
 *        2*		3*
 *        
 *        1*		4*
 */

package com.HomeGrownProgramming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Plane {
	
	public Point3[] points;
//	normal is a unit vector
	public Point3 normal;
//	constant is the multiplier of the direction vector to make it the true vector
	public double constant;
	public Shape shape;
	public Color color = Color.BLACK;
	public double maxX = -Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE, minX = Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
	
	public Plane() {
		points = new Point3[0];
		normal = new Point3();
		shape = null;
	}
	
	public Plane(Point3[] points, Color color) {
		this.color = color;
		this.points = points;
		int[] xpoints = new int[points.length], ypoints = new int[points.length];
		for(int i = 0; i < points.length; i++) {
			if(minX > points[i].x) {
				minX = points[i].x;
			}
			if(maxX < points[i].x) {
				maxX = points[i].x;
			}
			if(minY > points[i].y) {
				minY = points[i].y;
			}
			if(maxY < points[i].y) {
				maxY = points[i].y;
			}
			if(minZ > points[i].z) {
				minZ = points[i].z;
			}
			if(maxZ < points[i].z) {
				maxZ = points[i].z;
			}
			xpoints[i] = (int)(points[i].x+points[i].z);
			ypoints[i] = (int)(points[i].y-points[i].z);
		}
		shape = new Polygon(xpoints, ypoints, xpoints.length);
		setNormal();
		setConstant();
	}
	
	private void setNormal() {
		Point3 v1 = new Point3(points[1].x-points[0].x, points[1].y-points[0].y, points[1].z-points[0].z);
		Point3 v2 = new Point3(points[points.length-1].x-points[0].x, points[points.length-1].y-points[0].y, points[points.length-1].z-points[0].z);
		normal = Point3.getUnitVector(Point3.crossProduct(v1, v2));
	}
	
	private void setConstant() {
		constant = normal.x*points[0].x+normal.y*points[0].y+normal.z*points[0].z;
	}
	
	public void draw(Graphics2D g) {
		AffineTransform af = new AffineTransform();
		af.translate(-TheThread.view.x, -TheThread.view.y);
		Shape drawShape = af.createTransformedShape(shape);
		g.setColor(color);
		g.fill(drawShape);
		g.setColor(Color.BLACK);
		g.draw(drawShape);
	}
	
	public boolean fakeContains(Point3 point) {
		return shape.contains(point.x, point.y);
	}
	
	public boolean contains(Point3 point) {
		return aboutEqual(constant, normal.x*point.x+normal.y*point.y+normal.z*point.z) && point.x >= minX && point.x <= maxX && point.y >= minY && point.y <= maxY && point.z >= minZ && point.z <= maxZ;
	}

	public boolean aboutEqual(double a, double b) {
		return Math.abs(b-a) <= .01;
	}
	 
	public void translate(double dx, double dy, double dz) {
		minX += dx;
		maxX += dx;
		minY += dy;
		maxY += dy;
		minZ += dz;
		maxZ += dz;
		AffineTransform af = new AffineTransform();
		af.translate(dx+dz, dy-dz);
		shape = af.createTransformedShape(shape);
		setConstant();
	}
}
