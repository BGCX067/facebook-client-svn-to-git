package com.limon.fbclient.main;

import java.awt.EventQueue;

import com.limon.fbclient.auth.AuthManagerFactory;
import com.limon.fbclient.auth.IAuthManager;
import com.limon.fbclient.frame.IMainClientFrame;
import com.limon.fbclient.frame.MainClientFrameFactory;
import com.limon.fbclient.proxy.ConnectionConfig;

public class LaunchClass {
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionConfig.getInstance().configure();
					IMainClientFrame window = new MainClientFrameFactory().getMainClientFrame();
					IAuthManager manager = new AuthManagerFactory().getAuthManager();
					manager.authorise(window);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
