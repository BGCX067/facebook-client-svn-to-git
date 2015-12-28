package com.limon.fbclient.auth;


public class Account {

	
	private static Account instance = null;
	
	private String accessToken = null;
	private String userId = null;
	
	private Account() {
	}
	
	
	public static Account getInstance() {
		if(instance == null) {
			instance = new Account();
		}
		return instance;
	}
	
	
	public String getAccessToken() {
		return accessToken;
	}
	
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
