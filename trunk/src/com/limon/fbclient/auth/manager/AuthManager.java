package com.limon.fbclient.auth.manager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.limon.fbclient.auth.Account;
import com.limon.fbclient.auth.IAuthManager;
import com.limon.fbclient.auth.RequestCreator;
import com.limon.fbclient.frame.ApplicationMessage;
import com.limon.fbclient.frame.IMainClientFrame;
import com.limon.fbclient.proxy.ConnectionConfig;

public class AuthManager implements IAuthManager {

	private static AuthManager instance = null;
	private IMainClientFrame mainWindow = null;
	private ConnectionConfig config = ConnectionConfig.getInstance();
		
	
	private JFrame frame = null;
	private JTextArea resultArea = null;
	
	private final String ACCESS_TOKEN_REGEX = "access_token=.+&";
	
	private AuthManager() {
		
	}
	
	public static AuthManager getInstance() {
		if(instance == null) {
			instance = new AuthManager();
		}
		return instance;
	}
	
	@Override
	public void authorise(IMainClientFrame mainWindow) {
		this.mainWindow = mainWindow;
		mainWindow.setVisible(false); 
		JFrame authFrame = createAuthFrame(mainWindow);
		authFrame.setVisible(true);
	}

	private JFrame createAuthFrame(IMainClientFrame mainWindow) {
		frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(new Dimension(600, 250));
		JPanel panel = new JPanel();
		JTextArea authArea = new JTextArea();
		authArea.setEditable(false);
		authArea.setLineWrap(true);
		authArea.setPreferredSize(new Dimension(490, 80));
		authArea.setText(RequestCreator.createRequest());
		resultArea = new JTextArea();
		resultArea.setPreferredSize(new Dimension(490, 80));
		JButton okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(60, 40));
		JLabel authLabel = new JLabel("Authorize URl:");
		JLabel resultLabel = new JLabel("Result   URl :");
		panel.add(authLabel);
		panel.add(authArea);
		panel.add(resultLabel);
		panel.add(resultArea);
		panel.add(okButton);
		frame.setContentPane(panel);
		addOkListener(okButton);
		addExitListener();
		return frame;
	}
	
	private void addOkListener(JButton okButton) {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Pattern pattern = Pattern.compile(ACCESS_TOKEN_REGEX);
				String url = resultArea.getText();
				Matcher matcher = pattern.matcher(url);
				if(matcher.find()) {
					int startIndex = matcher.start();
					int endIndex = matcher.end();
					String accessToken = url.substring(startIndex + 13, endIndex - 1);
					Account account = Account.getInstance();
					account.setAccessToken(accessToken);
					frame.dispose();
					mainWindow.setVisible(true);
					mainWindow.showProfile();
				} else {
					JOptionPane.showMessageDialog(frame, ApplicationMessage.BAD_RESULT_URL);
				}
			}
		});
	}
	
	private void addExitListener() {
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				mainWindow.setVisible(true);
				super.windowClosing(arg0);
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				mainWindow.setVisible(true);
				super.windowClosed(arg0);
			}
			
			
		});
	}
	
}
