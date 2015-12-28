package com.limon.fbclient.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.limon.fbclient.event.AddPostListener;
import com.limon.fbclient.event.CommentListener;
import com.limon.fbclient.event.DeletePostListener;
import com.limon.fbclient.event.LikeListener;
import com.limon.fbclient.frame.panel.FeedPanel;
import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.Post;
import com.restfb.types.User;


public class FeedFrame extends JFrame {
	
	public static final int FEED = 1;
	public static final int WALL = 2;
	public static final int FRIEND_WALL = 3;
	
	private int contentType = 0;
	
	private JPanel mainPanel = null;
	JScrollPane scrollPane = null;
	private JLabel emptyFeedLabel = null;
	
	private int heightContent = 0;
	private FacebookClient facebookClient = null;
	private String userID = null;
	private String meId = null;

	public FeedFrame(FacebookClient facebookClient, String userID, String meId, int frameType) {
		this.facebookClient = facebookClient;
		this.userID = userID;
		this.meId = meId;
		this.contentType = frameType;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(530, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPanel);		
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(mainPanel);
		scrollPane.setColumnHeaderView(buildHeadPanel());
		showContent();
	}
	

	private JPanel buildHeadPanel() {
		JPanel headPanel = new JPanel();
		headPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		headPanel.setPreferredSize(new Dimension(500, 100));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(500, 60));
		headPanel.add(scrollPane);
		
		JTextArea postArea = new JTextArea();
		postArea.setLineWrap(true);
		postArea.setBackground(new Color(255, 254, 254));
		scrollPane.setViewportView(postArea);
		
		JPanel buttonPanel = createButtonPanel();
		headPanel.add(buttonPanel);
		
		JButton addButton = new JButton("Add Post");
		buttonPanel.add(addButton, BorderLayout.EAST);
		addButton.addActionListener(new AddPostListener(facebookClient, userID, postArea));
		return headPanel;
	}
	
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setPreferredSize(new Dimension(500, 25));
		JButton refreshButton = new JButton("Refresh");
		buttonPanel.add(refreshButton, BorderLayout.WEST);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showContent();
			}
		});
		return buttonPanel;
	}
	

	public void showContent() {
		setFrameTitle();
		heightContent = 0;
		mainPanel = new JPanel();
		scrollPane.setViewportView(mainPanel);
		List<Post> postList = loadPosts();
		if(postList.size() == 0) {
			emptyFeedLabel = new JLabel("Empty");
			emptyFeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
			scrollPane.setViewportView(emptyFeedLabel);
		}
		addPostPanels(postList);
		mainPanel.setPreferredSize(new Dimension(500, heightContent));
		setVisible(true);
		getContentPane().repaint();
	}
	

	private void addPostPanels(List<Post> postList) {
		for(Post post : postList) {
			FeedPanel postPanel = new FeedPanel(post, meId, contentType);
			addButtonListeners(post, postPanel);
			heightContent += postPanel.getPreferredSize().height + 10;
			mainPanel.add(postPanel);
		}
	}
	

	private void addButtonListeners(Post post, FeedPanel postPanel) {
		if(contentType == WALL) {
			postPanel.addDeletePostListener(new DeletePostListener(facebookClient));
		}
		postPanel.addLikeListener(new LikeListener(facebookClient, post.getId()));
		postPanel.addCommentListener(new CommentListener(facebookClient, post));
	}
	
	private void setFrameTitle() {
		try {
			User user = facebookClient.fetchObject(userID, User.class);
			if(contentType == FEED) {
				setTitle(user.getName() + " : new\'s feed");
			} else {
				setTitle(user.getName() + " : wall");
			}
		} catch(FacebookNetworkException ex) {
			JOptionPane.showMessageDialog(this, ApplicationMessage.NO_CONNECTION);
		}
	}

	private List<Post> loadPosts() {
		try {
			Connection<Post> con = null;
			if(contentType == FEED) {
				con = facebookClient.fetchConnection(userID + "/home", Post.class);
			} else {
				con = facebookClient.fetchConnection(userID + "/feed", Post.class);
			}
			return con.getData();
		} catch(FacebookNetworkException ex) {
			JOptionPane.showMessageDialog(this, ApplicationMessage.NO_CONNECTION);
			return new LinkedList<Post>();
		}
	}
	

	public FacebookClient getFacebookClient() {
		return facebookClient;
	}
	
}
