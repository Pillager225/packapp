package com.HomeGrownProgramming;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.LinkedList;

public class Stage {

	public Shape3[] objects;
	public Unit player;
	public Rectangle view;
	
	public Stage() {}
	
	public void loadStage0() {
		objects = new Shape3[0];
		LinkedList<Shape3> objs = new LinkedList<Shape3>();
		objs.add(new Shape3(new Point3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE), new Point3(1, 1, 1), Shape3.DIMENSIONS, Color.WHITE, 100, 0, false));
		objs.getLast().gravAffect = false;
		objs.add(new Shape3(new Point3(-100, 100, 0), new Point3(0, 500, Shape3.MAX_DEPTH), Shape3.OPPOSITE_POINT, Color.GRAY, 100, 0, false));
		objs.getLast().gravAffect = false;
		objs.add(new Shape3(new Point3(0, 400, 0), new Point3(2000, 100, Shape3.MAX_DEPTH), Shape3.DIMENSIONS, Color.GRAY, 100, 0, false));
		objs.getLast().gravAffect = false;
		objs.add(new Unit(new Point3(1000, 295, 0), Color.GREEN));
		player = (Unit) objs.getLast();
		Point3 pCenter = player.getCenter();
		view = new Rectangle((int) (pCenter.x+pCenter.z-Starter.screenSize.width/2), (int)(pCenter.y-pCenter.z-Starter.screenSize.height/2), Starter.screenSize.width, Starter.screenSize.height);
		objs.add(new Unit(new Point3(100, 0, 0), Color.RED));
		objects = objs.toArray(objects);
	}
}
