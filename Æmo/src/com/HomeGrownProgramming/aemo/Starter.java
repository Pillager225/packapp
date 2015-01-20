package com.HomeGrownProgramming.aemo;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Starter {
	public static JFrame frame;
	public static Dimension screenSize;
	public static BufferStrategy bufferStrategy;
	public static boolean windowed;
	private static GraphicsDevice device;
	//This file is crazy, don't worry about anything and just look at the main function and the constructor
   
	public Starter() {
	   try {
		   	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	       	device = env.getDefaultScreenDevice();
		   	GraphicsConfiguration gc = device.getDefaultConfiguration(); // sets the graphics config
		   	frame = new JFrame(gc); //makes our frame
		   	frame.setUndecorated(true); // makes it so it doesn't the follow rules of the OS
		   	frame.setIgnoreRepaint(true); // IDK, but we need it
		   	frame.setVisible(true); // makes it visible
		   	frame.setFocusable(true); // makes it so that way can will listen to key actions
//			   	frame.setResizable(true);
		   	frame.setSize(1920, 1080);
		   	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   	if(device.isFullScreenSupported()) {
//		   		device.setFullScreenWindow(frame); // set the frame full screen
		   	} else {
		   		frame.setSize(1920, 1080);
		   	}
		   	do {
		   		screenSize = frame.getSize();
		   	} while(screenSize.width == 1 && screenSize.height == 1);
			System.out.println(screenSize);
		   	if (device.isDisplayChangeSupported()) {
		   		chooseBestDisplayMode(device); // selects best display mode
		   	}
		   	frame.createBufferStrategy(2);
           	bufferStrategy = frame.getBufferStrategy();
           	TheThread thread = new TheThread(); // creates our main thread
           	thread.start(); // starts our thread
           	MouseAction ma = new MouseAction(); // creates our mouse action listener class with the GBThread thread 
           	KeyAction ka = new KeyAction(); // Same as above, but for key presses. We need to start it with this instance of the thread to avoid creating new threads that don't do anything. This way we stay on one thread.
           	frame.addMouseListener(ma); // explanitory
           	frame.addMouseMotionListener(ma); // explanitory
           	frame.addKeyListener(ka); // explanitory
       	} catch (Exception e) {
       		e.printStackTrace(); // If we get an error, prevent it from crashing completely and tell us what is up.
       	}
   }
   
   private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[] {
	   new DisplayMode(1920, 1080, 32, 0),
	   new DisplayMode(1920, 1080, 24, 0),
	   new DisplayMode(1920, 1080, 16, 0),
	   new DisplayMode(1920, 1080, 8, 0),
	   new DisplayMode(1440, 900, 32, 0),
	   new DisplayMode(1440, 900, 24, 0),
	   new DisplayMode(1440, 900, 16, 0),
	   new DisplayMode(1440, 900, 8, 0),
	   new DisplayMode(1366, 748, 32, 0),
	   new DisplayMode(1366, 748, 24, 0),
	   new DisplayMode(1366, 748, 16, 0),
	   new DisplayMode(1366, 748, 8, 0), 
	   new DisplayMode(1024, 738, 32, 0),
	   new DisplayMode(1024, 738, 24, 0),
	   new DisplayMode(1024, 738, 16, 0),
	   new DisplayMode(1024, 600, 8, 0),
	   new DisplayMode(1024, 600, 32, 0),
	   new DisplayMode(1024, 600, 24, 0),
	   new DisplayMode(1024, 600, 16, 0),
	   new DisplayMode(1024, 600, 8, 0),
       new DisplayMode(640, 480, 32, 0),
       new DisplayMode(640, 480, 24, 0),
       new DisplayMode(640, 480, 16, 0),
       new DisplayMode(640, 480, 8, 0)
    };
   
   private static DisplayMode getBestDisplayMode(GraphicsDevice device) {  
	   for (int x = 0; x < BEST_DISPLAY_MODES.length; x++) {
	       DisplayMode[] modes = device.getDisplayModes();
	       for (int i = 0; i < modes.length; i++) {
	           if (modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth() && modes[i].getHeight() == BEST_DISPLAY_MODES[x].getHeight() && modes[i].getBitDepth() == BEST_DISPLAY_MODES[x].getBitDepth()) {
	               return BEST_DISPLAY_MODES[x];
	           }
	       }
	   }
	   return null; 
}
   
   public static void chooseBestDisplayMode(GraphicsDevice device) {
	   DisplayMode best = getBestDisplayMode(device);
	   if (best != null) {
	       device.setDisplayMode(best);
	   }
}  

   public static void main(String[] args) {
	   try {
	       new Starter(); // starts our shit
	   } catch (Exception e) {
	       e.printStackTrace(); // saves our ass
	   }
	}
}
