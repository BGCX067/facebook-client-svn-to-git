package com.limon.fbclient.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.limon.fbclient.frame.ApplicationMessage;
import com.limon.fbclient.frame.CommentFrame;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.FacebookType;


public class AddCommentListener implements ActionListener {

	private final String COMMENTS = "/comments";
	private final String MESSSAGE_PARAM = "message";
	
	private String postID = null;
	
	
	public AddCommentListener(String postID) {
		this.postID = postID;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			JButton button = (JButton)e.getSource();
			CommentFrame frame = (CommentFrame)button.getTopLevelAncestor();
			String text = frame.getNewCommentArea().getText();
			if(!"".equals(text)) {
				FacebookClient facebookClient = frame.getFacebookClient();
				facebookClient.publish(postID + COMMENTS, FacebookType.class,
					    Parameter.with(MESSSAGE_PARAM, text));
				frame.showContent();
				frame.getNewCommentArea().setText("");
			}
		} catch(FacebookNetworkException ex) {
			JOptionPane.showMessageDialog((Component)e.getSource(), ApplicationMessage.NO_CONNECTION);
		}

	}
}
