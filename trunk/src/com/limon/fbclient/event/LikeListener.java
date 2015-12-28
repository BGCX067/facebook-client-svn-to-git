package com.limon.fbclient.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.limon.fbclient.auth.Account;
import com.limon.fbclient.frame.ApplicationMessage;
import com.limon.fbclient.frame.FeedFrame;
import com.limon.fbclient.proxy.FbWebRequestor;
import com.restfb.DefaultJsonMapper;
import com.restfb.DefaultLegacyFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.LegacyFacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookNetworkException;


public class LikeListener implements ActionListener {

	public static final String LIKE_COMMAND = "like";
	public static final String UNLIKE_COMMAND = "unlike";
	
	private final String LIKES = "/likes";
	
	private FacebookClient facebookClient = null;
	private String postId = null;
	

	public LikeListener(FacebookClient facebookClient, String postId) {
		this.facebookClient = facebookClient;
		this.postId = postId;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String actionCommand = e.getActionCommand();
			JButton button = (JButton)e.getSource();
			if(actionCommand.equals(LIKE_COMMAND)) {
				facebookClient.publish(postId + LIKES, Boolean.class);
				button.setActionCommand(UNLIKE_COMMAND);
			} else {
				facebookClient.deleteObject(postId + LIKES);
				button.setActionCommand(LIKE_COMMAND);
			}
			FeedFrame frame = (FeedFrame)button.getTopLevelAncestor();
			frame.showContent();
		} catch(FacebookNetworkException ex) {
			JOptionPane.showMessageDialog((Component)e.getSource(), ApplicationMessage.NO_CONNECTION);
		}
	}

}
