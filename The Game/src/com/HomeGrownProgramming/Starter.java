package com.HomeGrownProgramming;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;

public class Starter {
	public static Dimension screenSize;
	public static BufferStrategy bufferStrategy;
	private static GraphicsDevice device;
	public static GraphicsConfiguration gc;
	public static MouseAction ma;
	public static Canvas frame = null;
	public static JFrame fr;
	public static boolean windowed;
	
	public Starter(GraphicsConfiguration gc) {
		try {
			Starter.gc = gc;
			fr = new JFrame(gc);
		   	fr.setUndecorated(true); // makes it so it doesn't the follow rules of the OS
		   	fr.setIgnoreRepaint(true); // IDK, but we need it
		   	fr.setVisible(true);
		   	fr.setFocusable(true); // makes it so that way can will listen to key actions
		   	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   	screenSize = new Dimension(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
		   	fr.setSize(screenSize);
			System.out.println(screenSize);
			String os = System.getProperty("os.name");
			if (os.indexOf("Mac OS") > -1) {
		        fr.setVisible(false);
		        fr.setVisible(true);
		    } else {
		    	if(device.isFullScreenSupported()) {
			   		device.setFullScreenWindow(fr); // set the frame full screen
			   	}
		    	if (device.isDisplayChangeSupported()) {
			   		chooseBestDisplayMode(device); // selects best display mode
			   	}
		    }
//			setUpLogging();
			resetFrame();
			ma = new MouseAction(); // creates our mouse action listener class with the GBThread thread 
           	KeyAction ka = new KeyAction();
           	frame.addMouseListener(ma); // explanitory
           	frame.addMouseMotionListener(ma); // explanitory
           	frame.addKeyListener(ka);
//           	Music music = new Music();
//           	music.startMusic("music.mp3");
           	TheThread thread = new TheThread(); // creates our main thread
           	thread.start(); // starts our thread
           	frame.requestFocus();
       	} catch (Exception e) {
       		e.printStackTrace(); // If we get an error, prevent it from crashing completely and tell us what is up.
       	}
	}
	
	public static void resetFrame() {
		if(frame != null) {
			fr.remove(frame);
			frame = null;
		}
		frame = new Canvas(gc);
//		frame.setVisible(true);
		frame.setSize(screenSize);
		fr.add(frame);
	   	frame.createBufferStrategy(2);
       	bufferStrategy = frame.getBufferStrategy();		
	}

	@SuppressWarnings("unused")
	private void setUpLogging() {
		try {
			PrintStream out = new PrintStream(new FileOutputStream("log"+System.currentTimeMillis()));
			System.setOut(out);
			System.setErr(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void changeWindowType() {
		if(windowed && device.isFullScreenSupported()) {
		 	windowed = false;
		    device.setFullScreenWindow(fr);
		    screenSize = new Dimension(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
		} else {
			windowed = true;
			device.setFullScreenWindow(null);
			screenSize = new Dimension(800, 640);
		}
		frame.setSize(screenSize);
		fr.setSize(screenSize);
		TheThread.view = new Rectangle(TheThread.view.x, TheThread.view.y, screenSize.width, screenSize.height);
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
           GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
           device = env.getDefaultScreenDevice();
		   GraphicsConfiguration gc = device.getDefaultConfiguration(); // sets the graphics config
           new Starter(gc); // starts our shit
       } catch (Exception e) {
           e.printStackTrace(); // saves our ass
       }
   }
}
