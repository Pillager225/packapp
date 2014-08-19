package com.HomeGrownProgramming;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyAction implements KeyListener {

	public int flyBinding, speedBinding, invisBinding;
	private boolean flyReady = true, speedReady = true, invisReady = true;
	
	public KeyAction() {
		flyBinding = KeyEvent.VK_Q;
		speedBinding = KeyEvent.VK_W;
		invisBinding = KeyEvent.VK_E;
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
					TheThread.player.jump();
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
			} else if(keyCode == flyBinding && flyReady) {
				flyReady = false;
				TheThread.player.toggleFlying();
			} else if(keyCode == speedBinding && speedReady) {
				speedReady = false;
				TheThread.player.toggleSpeedy();
			} else if(keyCode == invisBinding && invisReady) {
				invisReady = false;
				TheThread.player.toggleInvisible();
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
			} else if(keyCode == KeyEvent.VK_N) {
				System.out.println("Objects length: " + TheThread.objects.length);
				Shape3.a = true;
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
			} else if(keyCode == flyBinding) {
				flyReady = true;
			} else if(keyCode == speedBinding) {
				speedReady = true;
			} else if(keyCode == invisBinding) {
				invisReady = true;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
