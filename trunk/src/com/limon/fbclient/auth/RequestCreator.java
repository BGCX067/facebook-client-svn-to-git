package com.limon.fbclient.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RequestCreator {
	
	private static final String REQUEST = "https://www.facebook.com/dialog/oauth?" 
		+ "client_id=&redirect_uri=http://www.facebook.com/connect/login_success.html&"
		+ "scope=email,read_stream,publish_stream,offline_access,user_status,read_insights,create_event," +
				"manage_pages,rsvp_event,publish_checkins,manage_friendlists,sms&response_type=token";
	
	private static final String APP_ID = "191336477572374";
	
	public RequestCreator() {
	}
	

	public static String createRequest() {
		Pattern pattern = Pattern.compile("client_id=");
		Matcher matcher = pattern.matcher(REQUEST);
		if(matcher.find()) {
			int end = matcher.end();
			StringBuilder buffer = new StringBuilder(REQUEST);
			buffer.insert(end, APP_ID);
			return new String(buffer);
		} else {
			throw new IllegalArgumentException(REQUEST);
		}
	}
	
}
