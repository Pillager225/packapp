package com.HomeGrownProgramming;

import java.awt.Color;

public class Projectile extends Shape3 {

	public static final int FIRE = 0, ICE = 1;
	private final double fiWidth = 150, fiDiag = Math.sqrt(200), fiSpeed = 10, fiStrength = 5;
	private int type;
	Point3 direction;
	Unit parent;
	// if(!alive) { awaitDeletion(); }
	private boolean alive = true; 
	
	// origin should be considered to be in the center of the Projectile on the yz plane, starting at origin.
	public Projectile(Point3 origin, Point3 target, int type, Unit parent) {
		this.type = type;
		this.parent = parent;
		direction = Point3.getUnitVector(new Point3(target.x-origin.x, target.y-origin.y, target.z-origin.z));
		Point3 vertVect = new Point3(0, 1, 0);
		// d1 and d2 is perpendicular to direction 
		Point3 orth1, orth2, d1, d2;
		if(direction.isScalarOf(vertVect)) {
			orth1 = new Point3(1, 0, 0);
			orth2 = new Point3(0, 0, 1);
		} else {
			orth2 = Point3.crossProduct(direction, vertVect);
			orth1 = Point3.crossProduct(direction, orth2);
		}
		d1 = Point3.getUnitVector(new Point3(orth1.x+orth2.x, orth1.y+orth2.y, orth1.z+orth2.z));
		d2 = Point3.getUnitVector(new Point3(orth1.x-orth2.x, orth1.y-orth2.y, orth1.z-orth2.z));
		if(type == FIRE || type == ICE) {
			Point3[] points = new Point3[8];
			points[0] = new Point3(origin.x+d2.x*fiDiag, origin.y+d2.y*fiDiag, origin.z+d2.z*fiDiag);
			points[1] = new Point3(origin.x+d1.x*fiDiag, origin.y+d1.y*fiDiag, origin.z+d1.z*fiDiag);
			points[2] = new Point3(origin.x+direction.x*fiWidth+d2.x*fiDiag, origin.y+direction.y*fiWidth+d2.y*fiDiag, origin.z+direction.z*fiWidth+d2.z*fiDiag);
			points[3] = new Point3(origin.x+direction.x*fiWidth+d1.x*fiDiag, origin.y+direction.y*fiWidth+d1.y*fiDiag, origin.z+direction.z*fiWidth+d1.z*fiDiag);
			points[4] = new Point3(origin.x+direction.x*fiWidth+-d1.x*fiDiag, origin.y+direction.y*fiWidth+-d1.y*fiDiag, origin.z+direction.z*fiWidth+-d1.z*fiDiag);
			points[5] = new Point3(origin.x+direction.x*fiWidth+-d2.x*fiDiag, origin.y+direction.y*fiWidth+-d2.y*fiDiag, origin.z+direction.z*fiWidth+-d2.z*fiDiag);
			points[6] = new Point3(origin.x+-d1.x*fiDiag, origin.y+-d1.y*fiDiag, origin.z+-d1.z*fiDiag);
			points[7] = new Point3(origin.x+-d2.x*fiDiag, origin.y+-d2.y*fiDiag, origin.z+-d2.z*fiDiag);
			Color color;
			if(type == FIRE) {
				color = Color.RED;
			} else {
				color = Color.BLUE;
			}
			construct(points, color, (double)5, (double)0, true);
			gravAffect = false;
		}
	}
	
	@Override
	public void update() {
		if(alive) {
			if(type == FIRE || type == ICE) {
				vel = Point3.getScalarMultiple(fiSpeed, direction);
			}
			excludes.add(parent);
			super.update();
			if(collided != null) {
				parent.deleteProjectile(this);
				if(collided.getClass() == Unit.class) {
					Unit u = (Unit) collided;
					u.health -= fiStrength;
				}
				alive = false;
			}
		}
	}
}
