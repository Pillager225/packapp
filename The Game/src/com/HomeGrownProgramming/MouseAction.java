package com.HomeGrownProgramming;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseAction implements MouseListener, MouseMotionListener {

	Point3 mousePos = new Point3(0, 0, 0);
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos = new Point3(e.getX()+TheThread.view.x, e.getY()+TheThread.view.y, 0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int Mode = TheThread.getMode();
		if(Mode == TheThread.TITLE) {
			for(Button b : TheThread.buttons) {
				if(b.contains(e.getPoint())) {
					b.doAction();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
