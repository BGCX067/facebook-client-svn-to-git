package com.limon.fbclient.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.ScrollPaneConstants;

import com.limon.fbclient.event.AddCommentListener;
import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.Comment;
import com.restfb.types.Post;


public class CommentFrame extends JFrame {
	
	private final String COMMENTS = "/comments";
	private final String LIMIT_PARAM = "limit";
	private final String OFFSET_PARAM = "offset";
	
	private Post post = null;
	private FacebookClient facebookClient = null;
	
	private JTextArea commentsArea = null;
	private JTextArea newCommentArea = null;
	
	private JScrollPane commentsPane = null;
	
	private Connection<Comment> connection = null;
	private int headHeight = 0;
	private int limit = 25;
	private String text = "";
	private int pageCount = 0;
	

	public CommentFrame(Post post, FacebookClient facebookClient) {
		super();
		this.post = post;
		this.facebookClient = facebookClient;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(530, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		JPanel contentPanel = new JPanel();
		contentPanel.add(buildHeadPanel());
		contentPanel.add(buildCommentsPane());
		contentPanel.add(buildNewCommentPanel());
		setContentPane(contentPanel);
		setVisible(true);
	}
	
	private JPanel buildHeadPanel() {
		JPanel headPanel = new JPanel();
		headHeight = 0;
		if(post.getMessage() != null) {
			headPanel.add(buildMessagePane(post));
			headHeight += 70;
		}
		/*if(post.getLink() != null) {
			headPanel.add(buildLinkPanel(post));
			headHeight += 40;
		}*/
		headPanel.setPreferredSize(new Dimension(510, headHeight));
		return headPanel;
	}

	private JScrollPane buildMessagePane(Post post) {
		JScrollPane messageScrollPane = new JScrollPane();
		messageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		messageScrollPane.setPreferredSize(new Dimension(510, 70));
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
		linkPanel.setPreferredSize(new Dimension(510, 30));
		linkPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		JLabel typeLabel = new JLabel(post.getType() + ": ");
		linkPanel.add(typeLabel);
		linkPanel.add(createLinkLabel(post.getLink()));
		
		/*JButton goButton = new JButton("GO");
		goButton.setActionCommand(post.getLink());
		goButton.setFont(new Font("Dialog", Font.PLAIN, 8));
		goButton.addActionListener(new GoButtonListener());
		
		linkPanel.add(goButton);*/
		return linkPanel;
	}

	private JLabel createLinkLabel(String link) {
		JLabel linkLabel = null;
		if(link.length() < 55) {
			linkLabel = new JLabel(link);
		} else {
			linkLabel = new JLabel(link.substring(0, 55) + "...");
		}
		return linkLabel;
	}

	private JScrollPane buildCommentsPane() {
		commentsArea = new JTextArea();
		commentsArea.setLineWrap(true);
		commentsArea.setEditable(false);
		commentsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		commentsPane = new JScrollPane();
		commentsPane.setPreferredSize(new Dimension(510, 460 - headHeight));
		commentsPane.setViewportView(commentsArea);
		return commentsPane;
	}

	private JPanel buildNewCommentPanel() {
		JPanel newCommentPanel = new JPanel(new BorderLayout());
		newCommentPanel.setPreferredSize(new Dimension(510, 90));
		newCommentArea = new JTextArea();
		newCommentArea.setLineWrap(true);
		newCommentArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		newCommentArea.setPreferredSize(new Dimension(510, 60));
		JScrollPane newCommentPane = new JScrollPane();
		newCommentPane.setViewportView(newCommentArea);
		newCommentPane.setPreferredSize(new Dimension(510, 60));
		newCommentPanel.add(newCommentPane, BorderLayout.CENTER);
		newCommentPanel.add(buildButtonPanel(), BorderLayout.SOUTH);
		return newCommentPanel;
	}
	
	private JPanel buildButtonPanel() {
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JButton addButton = new JButton("Add");
		buttonPanel.add(addButton, BorderLayout.EAST);
		addButton.addActionListener(new AddCommentListener(post.getId()));
		JPanel panel = new JPanel();
		JButton refreshButton = new JButton("Refresh");
		panel.add(refreshButton);
		addRefreshButtonListener(refreshButton);
		JButton moreButton = new JButton("More");
		addMoreButtonListener(moreButton);
		panel.add(moreButton);
		buttonPanel.add(panel, BorderLayout.WEST);
		return buttonPanel;
	}

	public void showContent() {
		try {
			post = facebookClient.fetchObject(post.getId(), Post.class);
			if(hasComments()) {
				pageCount = 0;
				connection = null;
				text = "";
				showNextContent();
			}
		} catch(FacebookNetworkException ex) {
			JOptionPane.showMessageDialog(this, ApplicationMessage.NO_CONNECTION);
		}
	}

	private void showNextContent() {
		List<Comment> comments = loadComments();
		String page = "";
		pageCount++;
		for(Comment comment : comments) {
			page = comment.getMessage() + "\n" + page;
			page = comment.getCreatedTime() + ")\n" + page;
			page = comment.getFrom().getName() + "  (" + page;
			page = "\n" + page;
		}
		text += page;
		commentsArea.setText(text);
		commentsArea.repaint();
	}

	private List<Comment> loadComments() {
		String endPoint = post.getId() + COMMENTS;
		if(connection == null) {
			int offset = generateStartOffset();
			connection = facebookClient.fetchConnection(endPoint, Comment.class, 
					Parameter.with(LIMIT_PARAM, limit), Parameter.with(OFFSET_PARAM, offset));
			return connection.getData();
		} else if(connection.hasPrevious()) {
			connection = facebookClient.fetchConnectionPage(connection.getPreviousPageUrl(), Comment.class);
			return connection.getData();
		} else {
			return new LinkedList<Comment>();
		}
		
	}
	
	private int generateStartOffset() {
		int commentsCount = post.getComments().getCount().intValue();
		int offset = commentsCount;// - limit;
		if(offset < 25) {
			offset = 0;
		}
		return offset;
	}
	
	private boolean hasComments() {
		if(post.getComments() == null || post.getComments().getCount() == 0) {
			return false;
		}
		return true;
	}

	private void addMoreButtonListener(JButton button) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showNextContent();
			}
		});
	}

	private void addRefreshButtonListener(JButton button) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showContent();
			}
		});
	}

	public FacebookClient getFacebookClient() {
		return facebookClient;
	}

	public JTextArea getNewCommentArea() {
		return newCommentArea;
	}
	
}
