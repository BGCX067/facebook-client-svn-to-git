package com.limon.fbclient.proxy;

import java.net.Authenticator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ConnectionConfig {

	private static ConnectionConfig instance = null;
	
	private static final String HOST_TAG = "host";
	private static final String PORT_TAG = "port";
	private static final String LOGIN_TAG = "login";
	private static final String PASSWORD_TAG = "password";
	
	private boolean useProxy = false;
	private String proxyHost = null;
	private String proxyPort = null;
	private String login = null;
	private String password = null;
	
	private ConnectionConfig() {
		
	}
	
	public static ConnectionConfig getInstance() {
		if(instance == null) {
			instance = new ConnectionConfig();
		}
		return instance;
	}

	public void configure() {
		try {		
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse("config.xml");
			Element root = document.getDocumentElement();
			NodeList hostNode = root.getElementsByTagName(HOST_TAG);
			setProxyHost(hostNode.item(0).getTextContent());
			NodeList portNode = root.getElementsByTagName(PORT_TAG);
			setProxyPort(portNode.item(0).getTextContent());
			System.out.println(portNode.item(0).getTextContent());
			NodeList loginNode = root.getElementsByTagName(LOGIN_TAG);
			setLogin(loginNode.item(0).getTextContent());
			NodeList passNode = root.getElementsByTagName(PASSWORD_TAG);
			setPassword(passNode.item(0).getTextContent());
			Authenticator.setDefault(new ProxyAuthenticator(getLogin(), getPassword()));
			if(checkProxy()) {
				setUseProxy(true);
			} else {
				setUseProxy(false);
			}
			System.out.println(getProxyHost());
			System.out.println(getProxyPort());
			System.out.println(getLogin());
		} catch(Exception ex) {
			setUseProxy(false);
			System.out.println("no file");
		}
	}
	
	private boolean checkProxy() {
		if("".equals(proxyHost) || "".equals(proxyPort)) {
			return false;
		}
		return true;
	}
	
	public boolean useProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
