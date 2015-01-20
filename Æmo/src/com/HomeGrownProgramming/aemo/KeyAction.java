package com.HomeGrownProgramming.aemo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyAction implements KeyListener {

	boolean holdingSpace = false;
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if(TheThread.getMode() == TheThread.GAME) {
			if(key == KeyEvent.VK_LEFT) {
				TheThread.currentStage.aemo.movementDir = Æmo.LEFT;
			} else if(key == KeyEvent.VK_RIGHT) {
				TheThread.currentStage.aemo.movementDir = Æmo.RIGHT;
			} else if(key == KeyEvent.VK_SPACE && !holdingSpace) {
				holdingSpace = true;
				TheThread.currentStage.aemo.jumpPressed = true;
			} else if(key == KeyEvent.VK_ENTER) {
				TheThread.setMode(TheThread.START_GAME);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(TheThread.getMode() == TheThread.GAME) {
			if(key == KeyEvent.VK_LEFT) {
				TheThread.currentStage.aemo.movementDir = Æmo.NEITHER;
			} else if(key == KeyEvent.VK_RIGHT) {
				TheThread.currentStage.aemo.movementDir = Æmo.NEITHER;
			} else if(key == KeyEvent.VK_SPACE) {
				holdingSpace = false;
			}
		}	
	}

}
