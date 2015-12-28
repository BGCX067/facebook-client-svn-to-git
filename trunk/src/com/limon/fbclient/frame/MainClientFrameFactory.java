package com.limon.fbclient.frame;

public class MainClientFrameFactory {

	public MainClientFrameFactory() {
	}
	

	public IMainClientFrame getMainClientFrame() {
		return new FBClientFrame();
	}
	
}
