package com.limon.fbclient.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.limon.fbclient.auth.AuthManagerFactory;
import com.limon.fbclient.auth.IAuthManager;
import com.limon.fbclient.frame.IMainClientFrame;


public class SignInButtonListener implements ActionListener {
	
	private IMainClientFrame mainFrame = null;
	

	public SignInButtonListener(IMainClientFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IAuthManager manager = new AuthManagerFactory().getAuthManager();
		manager.authorise(mainFrame);
	}	
	
}
