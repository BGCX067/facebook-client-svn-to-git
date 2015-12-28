package com.limon.fbclient.auth;

import com.limon.fbclient.auth.manager.AuthManager;


public class AuthManagerFactory {
	
	public AuthManagerFactory() {
	}
	
	
	public IAuthManager getAuthManager() {
		return AuthManager.getInstance();
		//return DjAuthManager.getInstance();
		//return JBrowserAuthManager.getInstance();
	}
	
}
