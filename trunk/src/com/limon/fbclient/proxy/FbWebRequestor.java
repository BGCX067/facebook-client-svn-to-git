package com.limon.fbclient.proxy;

import com.restfb.DefaultWebRequestor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

public class FbWebRequestor extends DefaultWebRequestor {
	
	public FbWebRequestor() {
	}
	
	protected HttpURLConnection openConnection(URL url) throws IOException {
		ConnectionConfig config = ConnectionConfig.getInstance();
		HttpURLConnection connection = null;
		if(config.useProxy()) {
			String host = config.getProxyHost();
			int port = Integer.parseInt(config.getProxyPort());
			SocketAddress addres = new InetSocketAddress(host, port);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, addres);
			connection = (HttpURLConnection)url.openConnection(proxy);
		} else {
			connection = (HttpURLConnection)url.openConnection();
		}
		return connection;
	}
	
}
