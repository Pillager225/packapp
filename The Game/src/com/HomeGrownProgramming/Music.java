package com.HomeGrownProgramming;

import java.io.BufferedInputStream;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;

import com.sun.media.codec.audio.mp3.JS_MP3FileReader;

public class Music {

	BigClip clip;
	File musicFile;
	AudioInputStream inputStream;
	
	public Music() { }
	
	public void startMusic(String file) {
		try {
			JS_MP3FileReader m = new JS_MP3FileReader();
			clip = new BigClip(); // initialize the clip
			inputStream = m.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(file))); // turn it into something readable by the clip class
			clip.open(inputStream); // open the file
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // gets control of the volume
			volume.setValue(0.0f); // sets the volume to normal
			clip.loop(32768); // plays 2^15 times. Why 2^15? Why not?
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopMusic() {
		clip.stop();
	}
}
