package com.HomeGrownProgramming;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyAction implements KeyListener {

	public PowerUtil[] powers = new PowerUtil[6];
	
	public static int FLY = 0, SPEED = 1, INVIS = 2, STRENGTH = 3, FIRE = 4, ICE = 5;
	
	public KeyAction() {
		powers[FLY] = new PowerUtil(KeyEvent.VK_Q);
		powers[SPEED] = new PowerUtil(KeyEvent.VK_W);
		powers[INVIS] = new PowerUtil(KeyEvent.VK_E);
		powers[STRENGTH] = new PowerUtil(KeyEvent.VK_R);
		powers[FIRE] = new PowerUtil(KeyEvent.VK_T);
		powers[ICE] = new PowerUtil(KeyEvent.VK_Y);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int Mode = TheThread.getMode();
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		} else if(keyCode == KeyEvent.VK_F2) {
			Starter.changeWindowType();
		}
		if(Mode == TheThread.GAME) {
			// This is here so that when strength is on the player can bounce. I know it isn't a good solution, but it was the best I could figure out.
			if((keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP) && TheThread.player.strength) {
				TheThread.player.elasticity = 1;
			}
			if(keyCode == KeyEvent.VK_LEFT) {
				TheThread.player.left = true;
				TheThread.player.right = false;
			} else if(keyCode == KeyEvent.VK_RIGHT) {
				TheThread.player.right = true;
				TheThread.player.left = false;
			} else if(keyCode == KeyEvent.VK_UP) {
				TheThread.player.in = true;
				TheThread.player.out = false;
			} else if(keyCode == KeyEvent.VK_DOWN) {
				TheThread.player.out = true;
				TheThread.player.in = false;
			} else if(keyCode == KeyEvent.VK_SPACE) {
				if(TheThread.player.flying) {
					TheThread.player.up = true;
					TheThread.player.down = false;
				} else {
					if(TheThread.player.jumpNum < 2) {
						TheThread.player.jump();
						TheThread.player.jumpNum++;
					}
				}
			} else if(keyCode == KeyEvent.VK_ENTER) {
				TheThread.setMode(TheThread.START_GAME);
			} else if(keyCode == KeyEvent.VK_SHIFT) {
				if(TheThread.player.flying) {
					TheThread.player.down = true;
					TheThread.player.up = false;
				} else {
					if(!TheThread.player.crouched) {
						TheThread.player.toggleCrouch();
					}
				}
			} else if(keyCode == powers[FLY].binding && powers[FLY].ready) {
				powers[FLY].ready = false;
				TheThread.player.toggleFlying();
			} else if(keyCode == powers[SPEED].binding && powers[SPEED].ready) {
				powers[SPEED].ready = false;
				TheThread.player.toggleSpeedy();
			} else if(keyCode == powers[INVIS].binding && powers[INVIS].ready) {
				powers[INVIS].ready = false;
				TheThread.player.toggleInvisible();
			} else if(keyCode == powers[STRENGTH].binding && powers[STRENGTH].ready) {
				powers[STRENGTH].ready = false;
				TheThread.player.toggleStrength();
			} else if(keyCode == KeyEvent.VK_P) {
				TheThread.setMode(TheThread.PAUSE);
			} else if(keyCode == KeyEvent.VK_M) {
				TheThread.infiniMana = !TheThread.infiniMana;
			} else if(keyCode == KeyEvent.VK_A && TheThread.player.punchReady) {
				TheThread.player.punch();
				TheThread.player.punchReady = false;
			} else if(keyCode == KeyEvent.VK_S && TheThread.player.kickReady) {
				TheThread.player.kick();
				TheThread.player.kickReady = false;
			} else if(keyCode == powers[FIRE].binding && powers[FIRE].ready) {
				TheThread.player.fireToward(Starter.ma.mousePos);
				powers[FIRE].ready = false;
			} else if(keyCode == powers[ICE].binding && powers[ICE].ready) {
				TheThread.player.iceToward(Starter.ma.mousePos);
				powers[ICE].ready = false;
			}
		} else if(Mode == TheThread.PAUSE) {
			if(keyCode == KeyEvent.VK_P) {
				TheThread.setMode(TheThread.GAME);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int Mode = TheThread.getMode();
		int keyCode = e.getKeyCode();
		if(Mode == TheThread.GAME) {
			// opposite of the one in keyPressed
			if((keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP) && TheThread.player.strength) {
				TheThread.player.elasticity = 0;
			}
			if(keyCode == KeyEvent.VK_LEFT) {
				TheThread.player.left = false;
			} else if(keyCode == KeyEvent.VK_RIGHT) {
				TheThread.player.right = false;
			} else if(keyCode == KeyEvent.VK_UP) {
				TheThread.player.in = false;
			} else if(keyCode == KeyEvent.VK_DOWN) {
				TheThread.player.out = false;
			} else if(keyCode == KeyEvent.VK_SPACE) {
				TheThread.player.up = false;
			} else if(keyCode == KeyEvent.VK_SHIFT) {
				if(TheThread.player.flying) {
					TheThread.player.down = false;
				} else {
					if(TheThread.player.crouched) {
						TheThread.player.toggleCrouch();
					}
				}
			} else if(keyCode == powers[FLY].binding) {
				powers[FLY].ready = true;
			} else if(keyCode == powers[SPEED].binding) {
				powers[SPEED].ready = true;
			} else if(keyCode == powers[INVIS].binding) {
				powers[INVIS].ready = true;
			} else if(keyCode == powers[STRENGTH].binding) {
				powers[STRENGTH].ready = true;
			} else if(keyCode == powers[FIRE].binding) {
				powers[FIRE].ready = true;
			} else if(keyCode == powers[ICE].binding) {
				powers[ICE].ready = true;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
