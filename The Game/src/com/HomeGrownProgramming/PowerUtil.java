package com.HomeGrownProgramming;

public class PowerUtil {
	public boolean ready;
	public int binding;
	
	public PowerUtil() {
		ready = false;
		binding = -1;
	}
	
	public PowerUtil(int binding) {
		ready = true;
		this.binding = binding;
	}
}
