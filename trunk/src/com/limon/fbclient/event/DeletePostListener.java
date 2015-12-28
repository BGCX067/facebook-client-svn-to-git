package com.limon.fbclient.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.limon.fbclient.frame.ApplicationMessage;
import com.limon.fbclient.frame.FeedFrame;
import com.restfb.FacebookClient;


public class DeletePostListener implements ActionListener {

	private FacebookClient facebookClient = null;
	

	public DeletePostListener(FacebookClient facebookClient) {
		this.facebookClient = facebookClient;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String postId = e.getActionCommand();
			facebookClient.deleteObject(postId);
			JButton deleteButton = (JButton)e.getSource();
			FeedFrame feedFrame = (FeedFrame)deleteButton.getTopLevelAncestor();
			feedFrame.showContent();
		} catch(Exception ex) {
			JOptionPane.showMessageDialog((Component)e.getSource(), ApplicationMessage.FAIL_DELETE);
		}
	}

}
