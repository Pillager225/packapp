package com.HomeGrownProgramming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class Unit extends Shape3 {

	private double acc = 3, jumpPower = 30;
	private int jumpNum = 0, dir = RIGHT, punchCounter = 0, kickCounter = 0, punchStrength = 1, kickStrength = 2;
	public int maxMana = 100, mana = maxMana, manaRegen = 1, maxHealth = 20, health = maxHealth;
	public boolean up = false, down = false, left = false, right = false, in = false, out = false, crouched = false, flying = false, speedy = false, kickReady = true, punchReady = true;
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, IN = 4, OUT = 5;
	public static final Point3 SIZE = new Point3(60, 100, 10);
	private final double flyMultiplyer = 2, speedMultiplyer = 3;
	private final int flyCost = 2, speedCost = 2, invisCost = 2;
	private double maxAxialSpeed = 10;
	public LinkedList<Shape3> allies = new LinkedList<Shape3>();
	public Shape3 punch = null, kick = null;
	
	public Unit() {
		super();
	}
	
	public Unit(Point3 pos, Color c) {
		super(pos, SIZE, DIMENSIONS, c, 60, 0, true);
	}

	public void update() {
		handleMovement();
		limitVel();
		handleMana();
		excludes.addAll(allies);
		super.update();
		handlePunch();
		handleKick();
		dealWithJumpNum();
		keepZWithinBounds();
		if(health <= 0) {
			TheThread.deleteObject(this);
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
	private void handleMovement() {
		if(left && !hasLeft) {
			move(LEFT);
		} else if(right && !hasRight) {
			move(RIGHT);
		}
		if(!left && !right) {
			applyFrictionX = true;
		}
		if(in && !hasIn) {
			move(IN);
		} else if(out && !hasOut) {
			move(OUT);
		}
		if(!in && !out) {
			applyFrictionZ = true;
		}
		if(up && !hasUp) {
			move(UP);
		} else if(down && !hasDown) {
			move(DOWN);
		}
	}
	
	private void limitVel() {
		if(Math.abs(vel.x) > maxAxialSpeed) {
			vel.x = maxAxialSpeed*vel.x/Math.abs(vel.x);
		}
		if(flying && Math.abs(vel.y) > maxAxialSpeed) {
			vel.y = maxAxialSpeed*vel.y/Math.abs(vel.y);
		}
		if(Math.abs(vel.z) > maxAxialSpeed) {
			vel.z = maxAxialSpeed*vel.z/Math.abs(vel.z);
		}
	}
	
	private void handleMana() {
		if(flying) {
			dampen();
			mana -= flyCost;
			if(mana <= 0) {
				toggleFlying();
			}
		}
		if(speedy) {
			mana -= speedCost;
			if(mana <= 0) {
				toggleSpeedy();
			}
		}
		if(invisible) {
			for(Shape3 s : TheThread.objects) {
				if(Unit.class.equals(s.getClass()) && !s.equals(this)) {
					s.excludes.add(this);
					excludes.add(s);
				}
			}
			mana -= invisCost;
			if(mana <= 0) {
				toggleInvisible();
			}
		}
		if(mana < maxMana) {
			mana += manaRegen;
		}
	}
	
	private void handlePunch() {
		if(punch != null) {
			excludes.add(punch);
			punch.excludes.add(this);
		}
		if(punch != null) {
			if(dir == RIGHT) {
				punch.transformTo(new Point3(maxX, minY+(maxY-minY)/5, minZ), new Point3(punch.maxX-punch.minX+punchCounter, 10, maxZ-minZ), DIMENSIONS);
			} else {
				punch.transformTo(new Point3(minX-(punch.maxX-punch.minX+punchCounter), minY+(maxY-minY)/5, minZ), new Point3(punch.maxX-punch.minX+punchCounter, 10, maxZ-minZ), DIMENSIONS);
			}
			punch.update();
			if(punch.collided != null && punch.collided.getClass().equals(Unit.class)) {
				Unit reciever = (Unit)punch.collided;
				reciever.health -= punchStrength;
				if(dir == RIGHT) {
					reciever.vel.x += 8;
				} else {
					reciever.vel.x -= 8;
				}
				punchCounter = 8;
			}
			punchCounter++;
			if(punchCounter >= 8) {
				stopPunch();
			}
		}
	}
	
	private void stopPunch() {
		TheThread.deleteObject(punch);
		punch = null;
		punchCounter = 0;
		punchReady = true;
	}
	
	private void handleKick() {
		if(kick != null) {
			excludes.add(kick);
			kick.excludes.add(this);
		}
		if(kick != null) {
			if(dir == RIGHT) {
				kick.transformTo(new Point3(maxX, minY+2*(maxY-minY)/3, minZ), new Point3(kick.maxX-kick.minX+kickCounter*1.2, 10, maxZ-minZ), DIMENSIONS);
			} else {
				kick.transformTo(new Point3(minX-(kick.maxX-kick.minX+kickCounter*1.2), minY+2*(maxY-minY)/3, minZ), new Point3(kick.maxX-kick.minX+kickCounter*1.2, 10, maxZ-minZ), DIMENSIONS);
			}
			kick.update();
			if(kick.collided != null && kick.collided.getClass().equals(Unit.class)) {
				Unit reciever = (Unit)kick.collided;
				reciever.health -= kickStrength;
				if(dir == RIGHT) {
					reciever.vel.x += 8;
				} else {
					reciever.vel.x -= 8;
				}
				kickCounter = 10;
			}
			kickCounter++;
			if(kickCounter >= 10) {
				stopKick();
			}
		}
	}
	
	private void stopKick() {
		TheThread.deleteObject(kick);
		kick = null;
		kickCounter = 0;
		kickReady = true;
	}
	
	private void dealWithJumpNum() {
		if(hasDown) {
			jumpNum = 0;
		} else if(jumpNum == 0){
			jumpNum = 1;
		}
	}
	
	private void keepZWithinBounds() {
		if(minZ < 0) {
			translate(0, 0, 0-minZ);
			vel.z = 0;
		}
		if(maxZ > MAX_DEPTH) { 
			translate(0, 0, MAX_DEPTH-maxZ);
			vel.z = 0;
		}
	}
	
	public void move(int dir) {
		switch(dir){
		case LEFT:
			if(this.dir == RIGHT) {
				if(punch != null) {
					punchCounter = 8;
				}
				if(kick != null) {
					kickCounter = 10;
				}
			}
			this.dir = dir;
			vel.x -= acc;
			applyFrictionX = false;
			break;
		case RIGHT:
			if(this.dir == LEFT) {
				if(punch != null) {
					punchCounter = 8;
				}
				if(kick != null) {
					kickCounter = 10;
				}
			}
			this.dir = dir;
			vel.x += acc;
			applyFrictionX = false;
			break;
		case IN: 
			vel.z += acc;
			applyFrictionZ = false;
			break;
		case OUT:
			vel.z -= acc;
			applyFrictionZ = false;
			break;
		case UP: 
			vel.y -= acc;
			break;
		case DOWN:
			vel.y += acc;
			break;
		}
	}

	public void dampen() {
		applyFriction();
		if(!up && !down) {
			vel.y *= .8;
			if(Math.abs(vel.y) <= 1) {
				vel.y = 0;
			}
		}
	}
	
	public void jump() {
		if(jumpNum < 2) {
			if(crouched) {
				toggleCrouch();
			}
			vel.y -= jumpPower;
			jumpNum++;
		}
	}

	public void punch() {
		if(dir == RIGHT) {
			punch = new Shape3(new Point3(maxX, minY+(maxY-minY)/5, minZ), new Point3(0, 10, maxZ), DIMENSIONS, color, 10, 0, true);
		} else {
			punch = new Shape3(new Point3(minX, minY+(maxY-minY)/5, minZ), new Point3(0, 10, maxZ), DIMENSIONS, color, 10, 0, true);
		}
		TheThread.addObject(punch);
		punch.gravAffect = false;
	}
	
	public void kick() {
		if(dir == RIGHT) {
			kick = new Shape3(new Point3(maxX, minY+2*(maxY-minY)/3, minZ), new Point3(0, 10, maxZ), DIMENSIONS, color, 10, 0, true);
		} else {
			kick = new Shape3(new Point3(minX, minY+2*(maxY-minY)/3, minZ), new Point3(0, 10, maxZ), DIMENSIONS, color, 10, 0, true);
		}
		TheThread.addObject(kick);
		kick.gravAffect = false;
	}
	
	public void toggleCrouch() {
		if(hasDown) {
			if(crouched) {
				transformTo(new Point3(minX, minY-SIZE.y/2, minZ), SIZE, DIMENSIONS);
				crouched = false;
			} else {
				transformTo(new Point3(minX, minY+SIZE.y/2, minZ), new Point3(SIZE.x, SIZE.y/2, SIZE.z), DIMENSIONS);
				crouched = true;
			}
		} else {
			if(crouched) {
				transformTo(new Point3(minX, minY, minZ), SIZE, DIMENSIONS);
			} else {
				transformTo(new Point3(minX, minY, minZ), new Point3(SIZE.x, SIZE.y/2, SIZE.z), DIMENSIONS);
				crouched = true;
			}
		}
	}

	public void toggleFlying() {
		if(!flying) {
			maxAxialSpeed *= flyMultiplyer;
			acc *= flyMultiplyer;
			vel.y = 0;
			vel.x = 0;
			vel.z = 0;
		} else {
			maxAxialSpeed /= flyMultiplyer;
			acc /= flyMultiplyer;
		}
		gravAffect = !gravAffect;
		flying = !flying;
	}

	public void toggleSpeedy() {
		if(speedy) {
			maxAxialSpeed /= speedMultiplyer;
		} else {
			maxAxialSpeed *= speedMultiplyer;
		}
		speedy = !speedy;
	}

	public void toggleInvisible() {
		invisible = !invisible;
	}
}
