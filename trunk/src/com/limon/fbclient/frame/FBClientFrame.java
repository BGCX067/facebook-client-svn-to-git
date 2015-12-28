package com.limon.fbclient.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Authenticator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.limon.fbclient.auth.Account;
import com.limon.fbclient.event.SignInButtonListener;
import com.limon.fbclient.proxy.FbWebRequestor;
import com.limon.fbclient.proxy.ProxyAuthenticator;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.types.User;


public class FBClientFrame extends IMainClientFrame {
	
	private JPanel headPanel = null;
	private JPanel menuPanel = null;
	private JPanel friendsPanel = null;
	private JPanel buttonMenuPanel = null;
	
	private JScrollPane scrollPane = null;
	private JButton signInButton = null;
	private FacebookClient facebookClient = null;
	private JLabel nameLabel = null;
	
	private String meId = null;
	/*private String userNameProxy = null;
	private String passwordProxy = null;*/
	
	public FBClientFrame() {
		setResizable(false);
		setSize(265, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createHeadPanel();
		createFriendsPanel();
		createMenuPanel();
	}


	private void createFriendsPanel() {
		friendsPanel = new JPanel();
		friendsPanel.setLayout(null);
		friendsPanel.setBackground(Color.WHITE);
		friendsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		scrollPane = new JScrollPane(friendsPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setBackground(new Color(255,255,255));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}

	private void createHeadPanel() {
		headPanel = new JPanel();
		headPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(headPanel, BorderLayout.NORTH);
		headPanel.setLayout(new BorderLayout(0, 0));
		
		signInButton = new JButton("SIGN IN");
		signInButton.addActionListener(new SignInButtonListener(this));
		headPanel.add(signInButton, BorderLayout.SOUTH);
		
		//JLabel titleIcon = new JLabel(new ImageIcon("D:\\eclipse\\FBClient\\image\\logo.jpg"));
		//headPanel.add(titleIcon, BorderLayout.NORTH);
	}
	
	private void createMenuPanel() {
		menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.setPreferredSize(new Dimension(260, 40));
		menuPanel.setVisible(false);
		headPanel.add(menuPanel, BorderLayout.CENTER);
		
		nameLabel = new JLabel();
		menuPanel.add(nameLabel, BorderLayout.CENTER);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void createButtonMenuPanel() {
		buttonMenuPanel = new JPanel();
		buttonMenuPanel.setBackground(new Color(112, 146, 191));
		buttonMenuPanel.setPreferredSize(new Dimension(260, 30));
		buttonMenuPanel.setLayout(new BorderLayout());
		buttonMenuPanel.add(createFeedButton(FeedFrame.WALL), BorderLayout.WEST);
		buttonMenuPanel.add(createFeedButton(FeedFrame.FEED), BorderLayout.EAST);
	}

	private JButton createFeedButton(int type) {
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(125, 30));
		if(type == FeedFrame.FEED) {
			button.setText("News");
		} else {
			button.setText("Wall");
		}
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton)e.getSource();
				if("Wall".equals(button.getText())) {
					new FeedFrame(facebookClient, "me", meId, FeedFrame.WALL);
				} else {
					new FeedFrame(facebookClient, "me", meId, FeedFrame.FEED);
				}
				
			}
		});
		return button;
	}
	
	public void showProfile() {
		try {
			facebookClient = new DefaultFacebookClient(
			Account.getInstance().getAccessToken(),
				new FbWebRequestor(), 
				new DefaultJsonMapper());
			loadUserName();
		    loadFriendList();
		    showButtonMenu();
		    friendsPanel.repaint();
		    scrollPane.revalidate();
		} catch(FacebookNetworkException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(this, ApplicationMessage.NO_CONNECTION);
			System.out.println("end exception");
		}
	} 


	private void loadUserName() {
		User user = facebookClient.fetchObject("me", User.class);
		Account.getInstance().setUserId(user.getId());
		nameLabel.setText(user.getName());
		meId = user.getId();
	}

	private void loadFriendList() {
		Connection<User> friendConnection = facebookClient.fetchConnection("me/friends", User.class);
		List<User> friends = friendConnection.getData();
        int number = 0;
        for(User user : friends) {
        	addFriendPanel(user, number);
        	number++;
        }
        friendsPanel.setPreferredSize(new Dimension(240, number * 40));
	}
	
	private void addFriendPanel(User user, int number) {
		JPanel panel = new JPanel();
    	panel.setLayout(null);
    	panel.setBounds(4, number * 50 + 4, 240, 46);
    	if(number % 2 == 1) {
    		panel.setBackground(new Color(245, 244, 254));
    	} else {
    		panel.setBackground(Color.WHITE);
    	}
    	
        JLabel label = new JLabel(user.getName());
        label.setBounds(10, 10, 160, 14);
        label.setFont(new Font("Tahoma", Font.BOLD, 12));
        panel.add(label);
        panel.add(createFriendWallButton(user.getId()));
        friendsPanel.add(panel);        
	}

	private JButton createFriendWallButton(String userId) {
		JButton wallButton = new JButton("wall");
		wallButton.setActionCommand(userId);
        wallButton.setBounds(10, 27, 70, 19);
        wallButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FeedFrame(facebookClient, e.getActionCommand(), meId, FeedFrame.FRIEND_WALL);
			}
		});
        return wallButton;
	}

	public void showButtonMenu() {
		signInButton.setVisible(false);
		createButtonMenuPanel();
		headPanel.add(buttonMenuPanel, BorderLayout.SOUTH);
		menuPanel.setVisible(true);
	}
	
	public void setFacebookClient(FacebookClient facebookClient) {
		this.facebookClient = facebookClient;
	}
	

	
}
