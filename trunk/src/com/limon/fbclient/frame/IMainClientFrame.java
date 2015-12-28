package com.limon.fbclient.frame;

import javax.swing.JFrame;

import com.restfb.FacebookClient;


public abstract class IMainClientFrame extends JFrame {

	public abstract void showProfile();
	
	public abstract void showButtonMenu();
	
	public abstract void setFacebookClient(FacebookClient facebookClient);
	
	/*public abstract void setUserNameProxy(String userNameProxy);
	
	public abstract void setPasswordProxy(String passwordProxy);*/
}
