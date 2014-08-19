package com.HomeGrownProgramming;

/* This class can represent a point in 3D space, a vector, and dimensions. 
 * x is always equal to width, y is always equal to height, and z is always equal to depth
 */

public class Point3 {
	
	double x, y, z, width, height, depth;
	
	public Point3() {
		x = width = Double.NaN;
		y = height = Double.NaN;
		z = depth = Double.NaN;
	}
	
	public Point3(double x, double y, double z) {
		this.x = width = x;
		this.y = height = y;
		this.z = depth = z;
	}
	
	public void translate(double dx, double dy, double dz) {
		x = width += dx;
		y = height += dy;
		z = depth += dz;
	}
	
	public Point3 returnTranslated(double dx, double dy, double dz) {
		return new Point3(x+dx, y+dy, z+dz);
	}
	
	public static Point3 getScalarMultiple(double scalar, Point3 p) {
		return new Point3(p.x*scalar, p.y*scalar, p.z*scalar);
	}
	
	public static double getMagnitude(Point3 p) {
		return Math.pow(p.x*p.x+p.y*p.y+p.z*p.z, .5);
	}
	
	public static Point3 getUnitVector(Point3 p) {
		return getScalarMultiple(1/getMagnitude(p), p);
	}
	
	public static double getDistanceBetween(Point3 p1, Point3 p2) {
		return Math.pow((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y)+(p1.z-p2.z)*(p1.z-p2.z), .5);
	}
}
