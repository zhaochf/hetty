package com.hetty.server.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * a kit for reading the server config,default is server.properties
 * @author guolei
 *
 */
public class ServerConfig {
	private static Properties config = new Properties();
	private static ServerConfig _instance;

	private ServerConfig() {

	}
	public List<Class<?>> getPluginClassList() {
		
		Set<String> keySet = config.stringPropertyNames();
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (String key : keySet) {
			if (key.startsWith("server.plugin")) {
				String cls = config.getProperty(key);
				try {
					ClassLoader cl = Thread.currentThread()
							.getContextClassLoader();
					Class<?> cls1 = cl.loadClass(cls);

					list.add(cls1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	

	public String getServerKey() {
		String serverKey = config.getProperty("server.key");
		if (serverKey == null) {
			throw new RuntimeException("server.key 没有设置！");
		}
		return serverKey;
	}

	public String getServerSecret() {
		String serverSecret = config.getProperty("server.secret");
		if (serverSecret == null) {
			throw new RuntimeException("server.secret 没有设置！");
		}
		return serverSecret;
	}

	public String getConfigFile() {
		String f = config.getProperty("config.file", "config.xml");
		return f;
	}

	public String getServerName() {
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return config.getProperty("server.name", hostname);
	}

	public int getConnectionTimeout() {
		String timeOutStr = config.getProperty("server.connection.timeout",
				"3000");
		return Integer.parseInt(timeOutStr);
	}

	public int getMethodTimeout() {
		String timeOutStr = config.getProperty("server.method.timeout", "3000");
		return Integer.parseInt(timeOutStr);
	}

	public String getHost() {
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return config.getProperty("server.host", hostname);
	}

	public int getPort() {
		String port = config.getProperty("server.port", "8080");
		return Integer.parseInt(port);
	}


	public static ServerConfig getInstance() {
		if (_instance == null) {
			_instance = new ServerConfig();
		}
		return _instance;
	}

	public ServerConfig loadProperties(String file) {
		_instance.loadConfig(file);
		return _instance;
	}

	public String getProperty(String key) {
		return config.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return config.getProperty(key, defaultValue);
	}

	public Properties getProperties() {
		return config;
	}

	protected void loadConfig(String file) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = cl.getResourceAsStream(file);
		try {
			config.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("close the inputstream failed.");
			}
		}
	}
}
