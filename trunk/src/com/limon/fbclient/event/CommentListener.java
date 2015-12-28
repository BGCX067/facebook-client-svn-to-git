package com.limon.fbclient.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.limon.fbclient.frame.CommentFrame;
import com.restfb.FacebookClient;
import com.restfb.types.Post;


public class CommentListener implements ActionListener {

	private FacebookClient facebookClient = null;
	private Post post = null;
	

	public CommentListener(FacebookClient facebookClient, Post post) {
		this.facebookClient = facebookClient;
		this.post = post;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new CommentFrame(post, facebookClient).showContent();
	}
}
