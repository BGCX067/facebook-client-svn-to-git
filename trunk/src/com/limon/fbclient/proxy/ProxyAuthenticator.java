package com.limon.fbclient.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuthenticator extends Authenticator {

	private String name = null;
	private String password = null;
	
	public ProxyAuthenticator(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(name, password.toCharArray());
	}
	
}
