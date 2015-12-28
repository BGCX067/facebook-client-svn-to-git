package com.limon.fbclient.frame.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import com.limon.fbclient.event.LikeListener;
import com.limon.fbclient.frame.FeedFrame;
import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;


public class FeedPanel extends JPanel {
	
	private int contentType = 0;
	
	private JButton deleteButton = null;
	private JButton goButton = null;
	private JButton commentButton = null;
	private JButton likeButton = null;
	
	private int height = 30;
	private String link = null;
	
	private String meId = null;
	

	public FeedPanel(Post post, String meId, int contentType) {
		super();
		this.contentType = contentType;
		this.meId = meId;
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout());
		add(buildTitlePanel(post), BorderLayout.NORTH);
		add(buildContentPanel(post, contentType), BorderLayout.CENTER);
		add(buildButtonPanel(post), BorderLayout.SOUTH);
		setPreferredSize(new Dimension(500, height));
	}

	private JPanel buildTitlePanel(Post post) {
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(new Dimension(490, 25));
		
		JLabel nameLabel = new JLabel(post.getFrom().getName() + " (" + post.getCreatedTime() + ")");
		titlePanel.add(nameLabel, BorderLayout.WEST);
		
		if(contentType == FeedFrame.WALL) {
			deleteButton = new JButton("X");
			deleteButton.setFont(new Font("Dialog", Font.PLAIN, 9));
			deleteButton.setActionCommand(post.getId());
			titlePanel.add(deleteButton, BorderLayout.EAST);
		}
		height += 25;
		return titlePanel;
	}

	private JPanel buildContentPanel(Post post, int type) {
		JPanel contentPanel = new JPanel();
		if(post.getMessage() != null) {
			contentPanel.add(buildMessagePane(post));
		}
		if(post.getLink() != null) {
			contentPanel.add(buildLinkPanel(post));
		}
		if(type == FeedFrame.WALL) {
			if(post.getComments() != null) {
				contentPanel.add(buildCommentPanel(post));
			}
		}
		contentPanel.setPreferredSize(new Dimension(500, height - 25));
		return contentPanel;
	}

	private JScrollPane buildMessagePane(Post post) {
		JScrollPane messageScrollPane = new JScrollPane();
		messageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		messageScrollPane.setPreferredSize(new Dimension(490, 75));
		height += 75;
		
		JTextArea postArea = new JTextArea();
		postArea.setLineWrap(true);
		postArea.setEditable(false);
		postArea.setText(post.getMessage());
		postArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		messageScrollPane.setViewportView(postArea);
		return messageScrollPane;
	}
	
	private JPanel buildLinkPanel(Post post) {
		JPanel linkPanel = new JPanel();
		linkPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		JLabel typeLabel = new JLabel(post.getType() + ": ");
		linkPanel.add(typeLabel);
		/*if(Desktop.isDesktopSupported()) {
			linkPanel.setPreferredSize(new Dimension(490, 30));
			height += 30;
			link = post.getLink();			
			linkPanel.add(createLinkLabel());
			goButton = new JButton("GO");
			goButton.setFont(new Font("Dialog", Font.PLAIN, 8));
			goButton.setActionCommand(link);
			goButton.addActionListener(new GoButtonListener());
			linkPanel.add(goButton);
		} else {*/
			linkPanel.setPreferredSize(new Dimension(490, 60));
			height += 60;
			JTextArea linkArea = new JTextArea();
			linkArea.setText(post.getLink());
			linkArea.setEditable(false);
			linkPanel.add(linkArea);
		//}		
		return linkPanel;
	}

	private JLabel createLinkLabel() {
		JLabel linkLabel = null;
		if(link.length() < 55) {
			linkLabel = new JLabel(link);
		} else {
			linkLabel = new JLabel(link.substring(0, 55) + "...");
		}
		return linkLabel;
	}

	private JPanel buildCommentPanel(Post post) {
		JPanel commentPanel = new JPanel();
		commentPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		commentPanel.setPreferredSize(new Dimension(490, 110));
		
		JScrollPane commentScrollPane = new JScrollPane();
		commentScrollPane.setPreferredSize(new Dimension(430, 100));
		commentPanel.add(commentScrollPane);
		
		JTextArea commentArea = new JTextArea();
		commentArea.setLineWrap(true);
		commentArea.setEditable(false);
		commentArea.setText(getComments(post));
		commentArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		commentArea.setBackground(new Color(247, 247, 247));
		commentScrollPane.setViewportView(commentArea);
		height += 110;
		return commentPanel;
	}
	
	private JPanel buildButtonPanel(Post post) {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JLabel likesLabel = null;
		JLabel commentsLabel = null;
		if(post.getComments() == null) {
			commentsLabel = new JLabel("Comments: 0");
		} else {
			commentsLabel = new JLabel("Comments: " + post.getComments().getCount());
		}
		if(post.getLikesCount() == null) {
			likesLabel = new JLabel("Likes: 0");
		} else {
			likesLabel = new JLabel("Likes: " + post.getLikesCount());
		}
		buttonPanel.add(likesLabel);
		buttonPanel.add(commentsLabel);
		commentButton = new JButton("Comment");
		createLikeButton(post);
		buttonPanel.add(likeButton);
		buttonPanel.add(commentButton);
		height += 25;
		return buttonPanel;
	}
	
	private void createLikeButton(Post post) {
		Post.Likes likes = post.getLikes();
		boolean likeFlag = false;
		if(likes != null) {
			List<NamedFacebookType> likeList = likes.getData();
			for(NamedFacebookType liker : likeList) {
				if(meId.equals(liker.getId())) {
					likeFlag = true;
				}
			}
		}
		initLikeButton(likeFlag);
	}
	
	private void initLikeButton(boolean likeFlag) {
		if(likeFlag) {
			likeButton = new JButton("Unlike");
			likeButton.setActionCommand(LikeListener.UNLIKE_COMMAND);
		} else {
			likeButton = new JButton("Like");
			likeButton.setActionCommand(LikeListener.LIKE_COMMAND);
		}
	}
	
	private String getComments(Post post) {
		String text = "";
		Comments comments = post.getComments();
		if(comments != null) {
			List<Comment> commentList = post.getComments().getData();
			for(Comment comment : commentList) {
				text = comment.getMessage() + "\n" + text;
				text = comment.getCreatedTime() + ")\n" + text;
				text = comment.getFrom().getName() + "  (" + text;
			}
		}
		return text;
	}
	
	public void addDeletePostListener(ActionListener listener) {
		deleteButton.addActionListener(listener);
	}
	

	public void addCommentListener(ActionListener listener) {
		commentButton.addActionListener(listener);
	}

	public void addLikeListener(ActionListener listener) {
		likeButton.addActionListener(listener);
	}
}
