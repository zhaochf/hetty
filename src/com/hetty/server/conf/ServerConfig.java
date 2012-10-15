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
	/**
	 * get the plugin list which have configured in the server config file
	 * @return config class List
	 */
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
					throw new RuntimeException(e);
				}
			}
		}
		return list;
	}
	

	/**
	 * get server key
	 * @return server key
	 */
	public String getServerKey() {
		String serverKey = config.getProperty("server.key");
		if (serverKey == null) {
			throw new RuntimeException("we cannot find the server.key,please check and add.");
		}
		return serverKey;
	}

	/**
	 * get server secret
	 * @return server secret
	 */
	public String getServerSecret() {
		String serverSecret = config.getProperty("server.secret");
		if (serverSecret == null) {
			throw new RuntimeException("we cannot find the server.secret,please check and add.");
		}
		return serverSecret;
	}

	/**
	 * get the service config file,default is config.xml
	 * @return
	 */
	public String getConfigFile() {
		String f = config.getProperty("config.file", "config.xml");
		return f;
	}

	/**
	 * get the server's name
	 * @return server name
	 */
	public String getServerName() {
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException("we cannot get the server's hostName.");
		}
		return config.getProperty("server.name", hostname);
	}

	/**
	 * get the server's connect timeout,default is 3s
	 * @return
	 */
	public int getConnectionTimeout() {
		String timeOutStr = config.getProperty("server.connection.timeout",
				"3000");
		return Integer.parseInt(timeOutStr);
	}

	/**
	 * get the method's invoke timeout,default is 3s
	 * @return
	 */
	public int getMethodTimeout() {
		String timeOutStr = config.getProperty("server.method.timeout", "3000");
		return Integer.parseInt(timeOutStr);
	}

	/**
	 * get the server's port,default is 8080
	 * @return
	 */
	public int getPort() {
		String port = config.getProperty("server.port", "8080");
		return Integer.parseInt(port);
	}

	/**
	 * get the core number of threads
	 * @return
	 */
	public int getServerCorePoolSize(){
		String coreSize = config.getProperty("server.thread.corePoolSize", "4");
		return Integer.parseInt(coreSize);
	}
	/**
	 * get the maximum allowed number of threads
	 * @return
	 */
	public int getServerMaximumPoolSize(){
		String maxSize = config.getProperty("server.thread.maxPoolSize","16");
		return Integer.parseInt(maxSize);
	}
	/**
	 * get the thread keep-alive time, which is the amount of time that threads in excess of the core pool size may remain idle before being terminated
	 * @return
	 */
	public int getServerKeepAliveTime(){
		String aleveTime = config.getProperty("server.thread.keepAliveTime", "3000");
		return Integer.parseInt(aleveTime);
	}
	
	public  static ServerConfig getInstance() {
		if (_instance == null) {
			_instance = new ServerConfig();
		}
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

	/**
	 * load the server config file to config properties  
	 * @param file config file name
	 */
	public static void loadConfig(String file) {
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
