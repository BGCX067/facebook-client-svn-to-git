package com.limon.fbclient.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.limon.fbclient.frame.ApplicationMessage;
import com.limon.fbclient.frame.FeedFrame;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.FacebookType;


public class AddPostListener implements ActionListener {

	private FacebookClient facebookClient = null;
	private String userID = null;
	private JTextArea postArea = null;
	
	public AddPostListener(FacebookClient facebookClient, String userID, JTextArea postArea) {
		this.facebookClient = facebookClient;
		this.userID = userID;
		this.postArea = postArea;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(!"".equals(postArea.getText())) {
				facebookClient.publish(userID + "/feed", FacebookType.class, 
						Parameter.with("message", postArea.getText()));
				JButton button = (JButton)e.getSource();
				FeedFrame feedFrame = (FeedFrame)button.getTopLevelAncestor();
				postArea.setText("");
				try {
					Thread.sleep(1000);
				} catch(Exception ex) {
					
				}
				feedFrame.showContent();
			}
		} catch(FacebookNetworkException ex) {
			JOptionPane.showMessageDialog((Component)e.getSource(), ApplicationMessage.NO_CONNECTION);
		}
	}

}
