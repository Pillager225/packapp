package com.HomeGrownProgramming;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Shape3 {
	
	protected Point3[] points;
	private Plane[] sides = new Plane[0];
	public Point3 vel = new Point3(0, 0, 0);
	protected Color color;
	// mass is in kilograms, elasticity is between 0 and 1.
	public double mass, elasticity, maxX = -Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
	protected double minX = Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
	public LinkedList<Shape3> excludes = new LinkedList<Shape3>();
	public boolean moveable, gravAffect, invisible = false, isShadow = false;
	protected boolean applyFrictionX = true, applyFrictionZ = true, hasLeft = false, hasRight = false, hasUp = false, hasDown = false, hasIn = false, hasOut = false;
	protected Shape3 onTopOf = null, collided = null;
	public int topPlaneIndex = -1, idnum;
	public LinkedList<Shape3> shadows = new LinkedList<Shape3>();
	public static int lastIdnum = -1;
	
	public static final int OPPOSITE_POINT = 0, DIMENSIONS = 1;
	public static final double accDueToGrav = 3, MAX_DEPTH = 100;
	
	public Shape3() {
		idnum = ++lastIdnum;
		points = null;
		color = Color.BLACK;
		mass = 0;
		elasticity = 1;
		moveable = true;
		gravAffect = true;
	}
	
	public Shape3(Point3[] points, Color color, double mass, double elasticity, boolean moveable) {
		construct(points, color, mass, elasticity, moveable);
	}
	
	protected void construct(Point3[] points, Color color, double mass, double elasticity, boolean moveable) {
		idnum = ++lastIdnum;
		this.points = points;
		this.color = color;
		this.mass = mass;
		this.elasticity = elasticity;
		this.moveable = moveable;
		gravAffect = true;
		setPlanes();
		setMinsAndMaxes();
	}
	
	/* type refers to if the second point is the dimensions of the rectangular prism, or is the opposite point of p1
	 * p1 should have a smaller x, y, z value than p2 if(type == OPPOSITE_POINT)
	*/
	public Shape3(Point3 p1, Point3 p2, int type, Color color, double mass, double elasticity, boolean moveable) {
		idnum = ++lastIdnum;
		points = new Point3[8];
		if(type == OPPOSITE_POINT) {
			points[0] = p1;
			points[1] = new Point3(p1.x, p1.y, p2.z);
			points[2] = new Point3(p2.x, p1.y, p1.z);
			points[3] = new Point3(p2.x, p1.y, p2.z);
			points[4] = new Point3(p2.x, p2.y, p1.z);
			points[5] = p2;
			points[6] = new Point3(p1.x, p2.y, p1.z);
			points[7] = new Point3(p1.x, p2.y, p2.z);
			maxX = p2.x;
			maxY = p2.y;
			maxZ = p2.z;
		} else {
			points[0] = p1;
			points[1] = new Point3(p1.x, p1.y, p1.z+p2.depth);
			points[2] = new Point3(p1.x+p2.width, p1.y, p1.z);
			points[3] = new Point3(p1.x+p2.width, p1.y, p1.z+p2.depth);
			points[4] = new Point3(p1.x+p2.width, p1.y+p2.height, p1.z);
			points[5] = new Point3(p1.x+p2.width, p1.y+p2.height, p1.z+p2.depth);
			points[6] = new Point3(p1.x, p1.y+p2.height, p1.z);
			points[7] = new Point3(p1.x, p1.y+p2.height, p1.z+p2.depth);
			maxX = p1.x+p2.width;
			maxY = p1.y+p2.height;
			maxZ = p1.z+p2.depth;
		}
		minX = p1.x;
		minY = p1.y;
		minZ = p1.z;
		this.color = color;
		this.mass = mass;
		this.elasticity = elasticity;
		this.moveable = moveable;
		gravAffect = true;
		setPlanes();
	}
	
	public Shape3(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, boolean isShadow) {
		idnum = ++lastIdnum;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		points = new Point3[8];
		points[0] = new Point3(minX, minY, minZ);
		points[1] = new Point3(minX, minY, maxZ);
		points[2] = new Point3(maxX, minY, minZ);
		points[3] = new Point3(maxX, minY, maxZ);
		points[4] = new Point3(maxX, maxY, minZ);
		points[5] = new Point3(maxX, maxY, maxZ);
		points[6] = new Point3(minX, maxY, minZ);
		points[7] = new Point3(minX, maxY, maxZ);
		this.isShadow = isShadow;
		gravAffect = false;
		moveable = false;
		mass = 0;
		color = Color.BLACK;
		elasticity = 1;
		setPlanes();
	}
	
	public boolean contains(Point3 point) {
		return sides[sides.length-1].fakeContains(point) && point.z >= points[0].z && point.z <= points[1].z;
	}
	
	public boolean intersects(Shape3 s) {
		for(Point3 p : points) {
			if(s.contains(p)) {
				return true;
			}
		}
		return false;
	}
	
	public Point3 getIntersectingPoint(Shape3 s) {
		for(Point3 p : points) {
			if(s.contains(p)) {
				return p;
			}
		}
		return null;
	}
	
	public void draw(Graphics2D g) {
		if(invisible || isShadow) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
		}
		if(points != null && inView()) {
			for(int i = 0; i < sides.length; i++) {
				sides[i].draw(g);
			}
		}
		if(invisible || isShadow) {
			g.setComposite(TheThread.composite);
		}
	}
	
	public void update() {
		hasLeft = hasLeft();
		hasRight = hasRight();
		hasUp = hasUp();
		hasDown = hasDown();
		hasIn = hasIn();
		hasOut = hasOut();
		if(moveable) {
			if(gravAffect && !hasDown) {
				vel.y += accDueToGrav;
			}
			onTopOf = null;
			if(hasDown) {
				applyFriction();
			}
			translate(vel.x, vel.y, vel.z);
			collided = null;
			for(Shape3 s : TheThread.objects) {
				if(!s.equals(this) && !excludes.contains(s) && !s.equals(onTopOf)) {
					Point3 interPoint = getIntersectingPoint(s);
					if(interPoint != null) {
						handleCollision(s);
						changeVel(s, interPoint);
						s.excludes.add(this);
						collided = s;
					}
				}
			}
		}
		excludes = new LinkedList<Shape3>();
		castShadow();
	}
	
	protected void applyFriction() {
		if(applyFrictionX) {
			vel.x *= .8;
		}
		if(applyFrictionZ) {
			vel.z *= .8;
		}
		if(Math.abs(vel.x) <= 1) {
			vel.x = 0;
		}
		if(Math.abs(vel.z) <= 1) {
			vel.z = 0;
		}
	}
	
	private void handleCollision(Shape3 s) {
		Point3 calcVel = new Point3(vel.x/2, vel.y/2, vel.z/2);
		for(int i = 0; i < 3; i++) {
			if(intersects(s)) {
				translate(-calcVel.x, -calcVel.y, -calcVel.z);
			} else{
				translate(calcVel.x, calcVel.y, calcVel.z);
			}
			calcVel = new Point3(calcVel.x/2, calcVel.y/2, calcVel.z/2);
		}
		for(int i = 0; i < 6 && intersects(s); i++) {
			translate(-calcVel.x, -calcVel.y, -calcVel.z);
		}
	}
	
	/* Finds the closest plane to interPoint in the opposite direction of vel
	* then it finds the projection of the vel onto the normal of the plane
	* it takes that projection and subtracts it from vel to make the new vel
	*/
	public void changeVel(Shape3 s, Point3 interPoint) {
		double minT = Double.MAX_VALUE;
		Plane keyPlane = null;
		for(Plane p : sides) {
			// t is the distance from the intersecting point to the p
			double t = (p.constant-p.normal.x*interPoint.x-p.normal.y*interPoint.y-p.normal.z*interPoint.z)/(p.normal.x*vel.x+p.normal.y*vel.y+p.normal.z*vel.z);
			double planeX = interPoint.x+vel.x*t, planeY = interPoint.y+vel.y*t, planeZ = interPoint.z+vel.z*t;
			if(p.contains(new Point3(planeX, planeY, planeZ))) {
				if(Math.abs(t) < Math.abs(minT)) {
					minT = t;
					keyPlane = p;
				}
			}
		}
		// if we are inside on an object, keyPlane will get set. This will find the anti-direction vector of this Shape3 and change the velocity of the colliding objects along that vector
		if(keyPlane != null) {
			double scalar = (keyPlane.normal.x*vel.x+keyPlane.normal.y*vel.y+keyPlane.normal.z*vel.z);
			Point3 v = Point3.getScalarMultiple(scalar, keyPlane.normal);
			Point3 v1 = Point3.getScalarMultiple(s.mass/mass, v);
			Point3 v2 = Point3.getScalarMultiple(mass/s.mass, v);
			vel = Point3.getScalarMultiple(elasticity, new Point3(vel.x-v1.x, vel.y-v1.y, vel.z-v1.z));
			vel = new Point3(Math.abs(vel.x) <= 1 ? 0 : vel.x, Math.abs(vel.y) <= 1 ? 0 : vel.y, Math.abs(vel.z) <= 1 ? 0 : vel.z);
			s.vel = Point3.getScalarMultiple(s.elasticity, new Point3(s.vel.x-v2.x, s.vel.y-v2.y, s.vel.z-v2.z));
			s.vel = new Point3(Math.abs(s.vel.x) <= 1 ? 0 : s.vel.x, Math.abs(s.vel.y) <= 1 ? 0 : s.vel.y, Math.abs(s.vel.z) <= 1 ? 0 : s.vel.z);
			if(keyPlane.normal.y < 0) {
				onTopOf = s;
			}
		}
	}
	
	private void castShadow() {
		shadows.clear();
		LinkedList<Plane> possiblePlanes = new LinkedList<Plane>();
		for(Shape3 s : TheThread.objects) {
			if(!s.equals(this) 
					&& (s.sides[s.topPlaneIndex].minX <= maxX && s.sides[s.topPlaneIndex].maxX >= minX) 
					&& (s.sides[s.topPlaneIndex].minZ <= maxZ && s.sides[s.topPlaneIndex].maxZ >= minZ)
					&& s.sides[s.topPlaneIndex].minY-maxY > 0) {
				possiblePlanes.add(s.sides[s.topPlaneIndex]);
			}
		}
		if(!possiblePlanes.isEmpty()) {
			Plane[] shadowedPlane = new Plane[2];
			for(Plane p : possiblePlanes) {
				if(p.minY-maxY <= 2000) {
					if(shadowedPlane[0] == null || shadowedPlane[0].minY-maxY > p.minY-maxY) {
						shadowedPlane[0] = p;
					} else if(shadowedPlane[1] == null || shadowedPlane[1].minY-maxY > p.minY-maxY) {
						shadowedPlane[1] = p;
					}
				}
			}
			if(shadowedPlane[0] != null) {
				double shadowMinX0 = minX > shadowedPlane[0].minX ? minX : shadowedPlane[0].minX;
				double shadowMaxX0 = maxX < shadowedPlane[0].maxX ? maxX : shadowedPlane[0].maxX;
				double shadowMinZ0 = minZ > shadowedPlane[0].minZ ? minZ : shadowedPlane[0].minZ;
				double shadowMaxZ0 = maxZ < shadowedPlane[0].maxZ ? maxZ : shadowedPlane[0].maxZ;
				Shape3 newShadow = new Shape3(shadowMinX0, shadowedPlane[0].minY, shadowMinZ0, shadowMaxX0, shadowedPlane[0].maxY, shadowMaxZ0, true);
				if(shadowedPlane[0].minY-maxY > 3){
					TheThread.addObject(newShadow);
					shadows.add(newShadow);
				}
				if(shadowedPlane[1] != null) {
					double shadowMinX1 = minX > shadowedPlane[1].minX ? minX : shadowedPlane[1].minX;
					double shadowMaxX1 = maxX < shadowedPlane[1].maxX ? maxX : shadowedPlane[1].maxX;
					double shadowMinZ1 = minZ > shadowedPlane[1].minZ ? minZ : shadowedPlane[1].minZ;
					double shadowMaxZ1 = maxZ < shadowedPlane[1].maxZ ? maxZ : shadowedPlane[1].maxZ;
					newShadow = new Shape3(shadowMinX1, shadowedPlane[1].minY, shadowMinZ1, shadowMaxX1, shadowedPlane[1].maxY, shadowMaxZ1, true);
					if(shadowedPlane[1].minY-maxY > 3) {
						TheThread.addObject(newShadow);
						shadows.add(newShadow);
					}
				}
			}
		}
	}
	
	private boolean hasDown() {
		Shape3 extShape = new Shape3(new Point3(minX+1, maxY, minZ+1), new Point3(maxX-minX-2, 2, maxZ-minZ-2), DIMENSIONS, Color.WHITE, 0, 0, false);
		for(Shape3 s : TheThread.objects) {
			if(!this.equals(s) && extShape.intersects(s)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasUp() {
		Shape3 extShape = new Shape3(new Point3(minX+1, minY-2, minZ+1), new Point3(maxX-minX-2, 2, maxZ-minZ-2), DIMENSIONS, Color.WHITE, 0, 0, false);
		for(Shape3 s : TheThread.objects) {
			if(!this.equals(s) && extShape.intersects(s)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasLeft() {
		Shape3 extShape = new Shape3(new Point3(minX-2, minY+1, minZ+1), new Point3(2, maxY-minY-2, maxZ-minZ-2), DIMENSIONS, Color.WHITE, 0, 0, false);
		for(Shape3 s : TheThread.objects) {
			if(!this.equals(s) && extShape.intersects(s) && !invisible && !s.invisible) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasRight() {
		Shape3 extShape = new Shape3(new Point3(maxX, minY+1, minZ+1), new Point3(2, maxY-minY-2, maxZ-minZ-2), DIMENSIONS, Color.WHITE, 0, 0, false);
		for(Shape3 s : TheThread.objects) {
			if(!this.equals(s) && extShape.intersects(s) && !invisible && !s.invisible) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasIn() {
		Shape3 extShape = new Shape3(new Point3(minX+1, minY+1, maxZ), new Point3(maxX-minX-2, maxY-minY-2, 2), DIMENSIONS, Color.WHITE, 0, 0, false);
		for(Shape3 s : TheThread.objects) {
			if(!this.equals(s) && extShape.intersects(s) && !invisible && !s.invisible) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasOut() {
		Shape3 extShape = new Shape3(new Point3(minX+1, minY+1, minZ-2), new Point3(maxX-minX-2, maxY-minY-2, 2), DIMENSIONS, Color.WHITE, 0, 0, false);
		for(Shape3 s : TheThread.objects) {
			if(!this.equals(s) && extShape.intersects(s) && !invisible && !s.invisible) {
				return true;
			}
		}
		return false;
	}
	
	public void translate(double dx, double dy, double dz) {
		for(int i = 0; i < points.length; i++) {
			points[i].translate(dx, dy, dz);
		}
		for(Plane p : sides) {
			p.translate(dx, dy, dz);
		}
		minX += dx;
		maxX += dx;
		minY += dy;
		maxY += dy;
		minZ += dz;
		maxZ += dz;
	}
	
	public Shape3 returnTranslated(double dx, double dy, double dz) {
		Point3[] temp = new Point3[points.length];
		for(int i = 0; i < temp.length; i++) {
			temp[i] = points[i].returnTranslated(dx, dy, dz);
		}
		return new Shape3(temp, color, mass, elasticity, moveable);
	}
	
	public void transformTo(Point3[] points) {
		this.points = points;
		setPlanes();
		setMinsAndMaxes();
	}
	
	public void transformTo(Point3 p1, Point3 p2, int type) {
		points = new Point3[8];
		if(type == OPPOSITE_POINT) {
			points[0] = p1;
			points[1] = new Point3(p1.x, p1.y, p2.z);
			points[2] = new Point3(p2.x, p1.y, p1.z);
			points[3] = new Point3(p2.x, p1.y, p2.z);
			points[4] = new Point3(p2.x, p2.y, p1.z);
			points[5] = p2;
			points[6] = new Point3(p1.x, p2.y, p1.z);
			points[7] = new Point3(p1.x, p2.y, p2.z);
			maxX = p2.x;
			maxY = p2.y;
			maxZ = p2.z;
		} else {
			points[0] = p1;
			points[1] = new Point3(p1.x, p1.y, p1.z+p2.depth);
			points[2] = new Point3(p1.x+p2.width, p1.y, p1.z);
			points[3] = new Point3(p1.x+p2.width, p1.y, p1.z+p2.depth);
			points[4] = new Point3(p1.x+p2.width, p1.y+p2.height, p1.z);
			points[5] = new Point3(p1.x+p2.width, p1.y+p2.height, p1.z+p2.depth);
			points[6] = new Point3(p1.x, p1.y+p2.height, p1.z);
			points[7] = new Point3(p1.x, p1.y+p2.height, p1.z+p2.depth);
			maxX = p1.x+p2.width;
			maxY = p1.y+p2.height;
			maxZ = p1.z+p2.depth;
		}
		minX = p1.x;
		minY = p1.y;
		minZ = p1.z;
		setPlanes();
	}
	
	public Point3 getCenter() {
		return new Point3((maxX+minX)/2, (maxY+minY)/2, (maxZ+minZ)/2);
	}
	
	private boolean inView() {
		for(Point3 p : points) {
			if(p.x+p.z >= TheThread.view.x && p.x+p.z <= TheThread.view.x+TheThread.view.width && p.y-p.z >= TheThread.view.y && p.y-p.z <= TheThread.view.y+TheThread.view.height) {
				return true;
			}
		}
		return false;
	}
	
	private void setPlanes() {
		LinkedList<Plane> ll = new LinkedList<Plane>();
		int j = 0;
		for(int i = 0; i < points.length; i +=2) {
			j = i+2;
			if(j >= points.length) {
				j = 0;
			}
			Point3[] temp1 = {points[i], points[i+1], points[j+1], points[j]};
			ll.add(new Plane(temp1, color));
		}
		Point3[] temp = new Point3[points.length/2];
		j = 0;
		for(int i = 0; i < points.length; i += 2) {
			temp[j++] = points[i];
		}
		ll.add(new Plane(temp, color));
		sides = ll.toArray(sides);
		sortSides();
	}
	
	private void sortSides() {
		sortX();
		sortY();
		sortZ();
		Plane topPlane = null;
		for(int i = 0; i < sides.length; i++) {
			if(topPlane == null || (sides[i].minY == sides[i].maxY && sides[i].minY < topPlane.minY)) {
				topPlane = sides[i];
				topPlaneIndex = i;
			}
		}
	}
	
	private void sortX() {
		for(int i = 1; i < sides.length; i++) {
			Plane temp = sides[i];
			int j;
			for(j = i-1; j >= 0 && temp.maxX < sides[j].maxX; j--) {
				sides[j+1] = sides[j];
			}
			sides[j+1] = temp;
		}
	}
	
	private void sortY() {
		for(int i = 1; i < sides.length; i++) {
			Plane temp = sides[i];
			int j;
			for(j = i-1; j >= 0 && temp.minY > sides[j].minY; j--) {
				sides[j+1] = sides[j];
			}
			sides[j+1] = temp;
		}
	}
	
	private void sortZ() {
		for(int i = 1; i < sides.length; i++) {
			Plane temp = sides[i];
			int j;
			for(j = i-1; j >= 0 && temp.minZ > sides[j].minZ; j--) {
				sides[j+1] = sides[j];
			}
			sides[j+1] = temp;
		}
	}

	private void setMinsAndMaxes() {
		setMinX();
		setMaxX();
		setMinY();
		setMaxY();
		setMinZ();
		setMaxZ();
	}
	
	private void setMinX() {
		for(Plane p : sides) {
			if(p.minX > minX) {
				minX = p.minX;
			}
		}
	}
	
	private void setMaxX() {
		for(Plane p : sides) {
			if(p.maxX > maxX) {
				maxX = p.maxX;
			}
		}
	}
	
	private void setMinY() {
		for(Plane p : sides) {
			if(p.minY < minY) {
				minY = p.minY;
			}
		}
	}
	
	private void setMaxY() {
		for(Plane p : sides) {
			if(p.maxY < maxY) {
				maxY = p.maxY;
			}
		}
	}
	
	private void setMinZ() {
		for(Plane p : sides) {
			if(p.minZ < minZ) {
				minZ = p.minZ;
			}
		}
	}
	
	private void setMaxZ() {
		for(Plane p : sides) {
			if(p.maxZ < maxZ) {
				maxZ = p.maxZ;
			}
		}
	}

	public boolean equals(Shape3 s) {
		return s != null && idnum == s.idnum;
	}
}
