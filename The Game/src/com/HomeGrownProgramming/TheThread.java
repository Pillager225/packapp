package com.HomeGrownProgramming;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class TheThread extends Thread {

	long beginTime, timeDiff, sleeptime = 0; //To get the FPS correct. It helps with skipping and the timing.
	private int framesSkipped = 0, maxFPSskips = 5, FPS = 30, skipTicks = 1000/FPS; // same as above
	private static int Mode;
	public static final int PAUSE = 0, TITLE = 1, GAME = 2, START_GAME = 3;
	public static Rectangle view = new Rectangle(0, 0, Starter.screenSize.width, Starter.screenSize.height);
	public static Shape3[] objects = new Shape3[0];
	public static LinkedList<Button> buttons = new LinkedList<Button>();
	public static Unit player;
	public static Composite composite;
	
	public static boolean infiniMana = false, restartPending = false;
	
	public static final Dimension screen = new Dimension(1920, 1080);
	
	public TheThread() {
		setMode(TITLE);
	}

	public static void setMode(int mode) {
		if(mode == TITLE) {
			buttons.add(new Button(new Rectangle.Double(50, 50, 750, 250), Color.BLUE) {
				@Override
				public void doAction() {
					setMode(START_GAME);
				}
			});
		} else if(mode == START_GAME) {
			objects = new Shape3[0];
			LinkedList<Shape3> objs = new LinkedList<Shape3>();
			objs.add(new Shape3(new Point3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE), new Point3(1, 1, 1), Shape3.DIMENSIONS, Color.WHITE, 100, 0, false));
			objs.getLast().gravAffect = false;
			objs.add(new Shape3(new Point3(-100, 100, 0), new Point3(0, 500, Shape3.MAX_DEPTH), Shape3.OPPOSITE_POINT, Color.GRAY, 100, 0, false));
			objs.getLast().gravAffect = false;
			objs.add(new Shape3(new Point3(0, 400, 0), new Point3(2000, 100, Shape3.MAX_DEPTH), Shape3.DIMENSIONS, Color.GRAY, 100, 0, false));
			objs.getLast().gravAffect = false;
			objs.add(new Unit(new Point3(1000, 0, 0), Color.GREEN));
			player = (Unit) objs.getLast();
			Point3 pCenter = player.getCenter();
			view = new Rectangle((int) (pCenter.x+pCenter.z-Starter.screenSize.width/2), (int)(pCenter.y-pCenter.z-Starter.screenSize.height/2), Starter.screenSize.width, Starter.screenSize.height);
			objs.add(new Unit(new Point3(100, 0, 0), Color.RED));
			objects = objs.toArray(objects);
		}
		if(mode == START_GAME) {
			mode = GAME;
		}
		Mode = mode;
	}

	public static int getMode() {
		return Mode;
	}

	private void render() {
		Graphics2D g = (Graphics2D)Starter.bufferStrategy.getDrawGraphics(); // IDK what g is, but we need it because it connects us to our window
		if (!Starter.bufferStrategy.contentsLost()) { // idk, but put all the drawing functions inside of this if statement
			BufferedImage image = generateScreen();
			g.drawImage(image, 0, 0, Starter.screenSize.width, Starter.screenSize.height, 0, 0, image.getWidth(), image.getHeight(), Starter.frame);
			Starter.bufferStrategy.show(); // make stuff appear
        	g.dispose(); // release the resources that we used to make the screen for this frame
        	Toolkit.getDefaultToolkit().sync(); // idk, but without it things look glitchy
		}
	}

	private BufferedImage generateScreen() {
		BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		composite = g.getComposite();
		if(Mode == TITLE) { // if we are in the title mode
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, screen.width, screen.height); // background
			for(Button b : buttons) {
				b.draw(g);
			}
		} else if(Mode == GAME || Mode == PAUSE) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, screen.width, screen.height);
			for(Shape3 s : objects) {
				s.draw(g);
			}
			drawPlayerStuff(g);
			if(Mode == PAUSE) {
				g.setColor(Color.BLACK);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
				g.fillRect(0,  0, screen.width, screen.height);
				g.setComposite(composite);
			}
			for(int i = 0; i < objects.length; i++) {
				if(objects[i].isShadow) {
					deleteObject(objects[i]);
				}
			}
		}
		g.dispose();
		return image;
	}

	private void update() {
		if(Mode == GAME) {
			for(Shape3 s : objects) {
				s.update();
				if(s.getClass().equals(Unit.class)) {
				}
			}
			Point3 pCenter = player.getCenter();
			if((Math.abs(pCenter.x-view.x) < 400 && player.vel.x < 0) || (Math.abs(pCenter.x-(view.x+view.width)) < 400 && player.vel.x > 0)) {
				view.x += player.vel.x;
			}
			if((Math.abs(pCenter.y-view.y) < 400 && player.vel.y < 0) || (Math.abs(pCenter.y-(view.y+view.height)) < 400 && player.vel.y > 0)) {
				view.y += player.vel.y;
			}
//			objects = mergeSort(objects);
			objects = insertionSort(objects);
			if(infiniMana) {
				player.mana = player.maxMana;
			}
		}
	}
	
	private void drawPlayerStuff(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fill(new Rectangle.Double(10, 7, 20, 80));
		g.setColor(Color.WHITE);
		g.fill(new Rectangle.Double(34, 4, 5*player.maxHealth+12, 42));
		g.fill(new Rectangle.Double(34, 44, player.maxMana+12, 42));
		// Dark red
		g.setColor(new Color(180, 0, 0));
		g.fill(new Rectangle.Double(37, 7, 5*player.maxHealth+6, 36));
		g.setColor(Color.RED);
		g.fill(new Rectangle.Double(40, 10, 5*player.health, 30));
		g.setColor(Color.BLUE);
		g.fill(new Rectangle.Double(37, 47, player.maxMana+6, 36));
		// Light blue
		g.setColor(new Color(0, 110, 255));
		g.fill(new Rectangle.Double(40, 50, player.mana, 30));
	}
	
	private Shape3[] mergeSort(Shape3[] arr) {
		if(arr.length > 1) {
			Shape3[] left = new Shape3[arr.length/2], right = new Shape3[arr.length-arr.length/2];
			int i;
			for(i = 0; i < left.length; i++) {
				left[i] = arr[i];
			}
			for(int j = 0; j <right.length; j++) {
				right[j] = arr[i++];
			}
			left = mergeSort(left);
			right = mergeSort(right);
			Shape3[] temp = new Shape3[arr.length];
			Point3 viewPoint = new Point3(1920, 0, 0);
			int lloc = 0, rloc = 0;
			for(i = 0; i < temp.length; i++) {
				if(lloc >= left.length && rloc < right.length) {
					temp[i] = right[rloc++];
				} else if(rloc >= right.length && lloc < left.length) {
					temp[i] = left[lloc++];
				} else {
					Point3 leftPoint = new Point3(left[lloc].minX, left[lloc].maxY, left[lloc].maxZ), rightPoint = new Point3(right[rloc].minX, right[rloc].maxY, right[rloc].maxZ);
					double leftDist = Point3.getDistanceBetween(leftPoint, viewPoint), rightDist = Point3.getDistanceBetween(rightPoint, viewPoint);
					if(leftDist >= rightDist) {
						temp[i] = left[lloc++];
					} else {
						temp[i] = right[rloc++];
					}
				}
			}
			return temp;
		}
		return arr;
	}
	
	private Shape3[] insertionSort(Shape3[] a) {
		for(int i = 1; i < a.length; i++) {
			Shape3 temp = a[i];
			int j;
			boolean go = true;
			for(j = i-1; j >= 0 && go; j--) {
				int xVal = checkX(a[j+1], a[j]);
				if(xVal == s2First) {
					a[j+1] = a[j];
				} else if(xVal == s1First) {
					go = false;
				} else {
					int yVal = checkY(a[j+1], a[j]);
					if(yVal == s2First) {
						a[j+1] = a[j];
					} else if(xVal == s1First) {
						go = false;
					} else {
						int zVal = checkZ(a[j+1], a[j]);
						if(zVal == s2First) {
							a[j+1] = a[j];
						} else {
							go = false;
						}
					}
				}
			}
			a[j+2] = temp;
		}
		return a;
	}
	
	private static final int s1First = 0, s2First = 1, INTERSECTING = 2;
	
	private int checkX(Shape3 s1, Shape3 s2) {
		if(s1.minX >= s2.maxX) {
			return s1First;
		} else if(s2.minX >= s1.maxX) {
			return s2First;
		} else {
			return INTERSECTING;
		}
	}
	
	private int checkY(Shape3 s1, Shape3 s2) {
		if(s1.maxY <= s2.minY) {
			return s1First;
		} else if(s2.maxY <= s1.minY) {
			return s2First;
		} else {
			return INTERSECTING;
		}
	}
	
	private int checkZ(Shape3 s1, Shape3 s2) {
		if(s1.maxZ <= s2.minZ) {
			return s1First;
		} else if(s2.maxZ <= s1.minZ) {
			return s2First;
		} else {
			return INTERSECTING;
		}
	}
	
	public static void deleteObject(Shape3 o) {
		Shape3[] newObjects = new Shape3[objects.length-1];
		int j = 0;
		for(int i = 0; i < objects.length; i++) {
			if(!objects[i].equals(o)) {
				newObjects[j] = objects[i];
				j++;
			} else {
				System.out.print("");
			}
		}
		objects = newObjects;
	}
	
	public static void addObject(Shape3 o) {
		Shape3[] newObjects = new Shape3[objects.length+1];
		for(int i = 0; i < objects.length; i++) {
			newObjects[i] = objects[i];
		}
		newObjects[objects.length] = o;
		objects = newObjects;
	}
	
	@Override
	public void run() {
		while(true) {
			beginTime = System.currentTimeMillis(); //time of the start of the cycle
			if(Mode != PAUSE) {
				update();
			}
			render();
			timeDiff = System.currentTimeMillis()-beginTime; // time at the end of the cycle
			sleeptime = skipTicks-timeDiff; // time the computer should wait
			if(sleeptime > 0) { // if we should wait
				try {
					Thread.sleep(sleeptime); // then try to wait
				} catch (InterruptedException e) { // if shit gets fucked, don't let it crash
					e.printStackTrace();
				}
			} 
			while(sleeptime < 0 && framesSkipped < maxFPSskips) { // catch up if we are behind
				sleeptime += skipTicks;
				framesSkipped++;
				update();
			}
		}
	}
}
