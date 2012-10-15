package com.hetty.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hetty.object.AppServiceSecurity;
import com.hetty.object.Application;
import com.hetty.object.Service;
import com.hetty.plugin.Plugin;
import com.hetty.server.conf.ServerConfig;
import com.hetty.server.conf.ServerConfigManager;

public class HettyServer {
	private final static Logger logger = LoggerFactory
			.getLogger(HettyServer.class);
	private ServerBootstrap bootstrap = null;

	private AtomicBoolean startFlag = new AtomicBoolean(false);
	private final static Map<String, Application> applicationMap = new HashMap<String, Application>();

	private final static Map<String, AppServiceSecurity> appServiceSecurityMap = new HashMap<String, AppServiceSecurity>();
	private static Application currentApp;

	private String host;
	private int port;

	private int connectionTimeout = 3000;

	private int methodTimeout = 3000;

	private String serviceConfigFile = null;

	public HettyServer(String configFile) {

		serviceConfigFile = configFile;
		ServerConfig.loadConfig(configFile);
		ThreadFactory serverBossTF = new NamedThreadFactory("HETTY-BOSS-");
		ThreadFactory serverWorkerTF = new NamedThreadFactory("HETTY-WORKER-");
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(serverBossTF),
				Executors.newCachedThreadPool(serverWorkerTF)));
		bootstrap.setOption("tcpNoDelay", Boolean.parseBoolean(ServerConfig.getInstance()
				.getProperty("hetty.tcp.nodelay", "true")));
		bootstrap.setOption("reuseAddress", Boolean.parseBoolean(ServerConfig.getInstance()
				.getProperty("hetty.tcp.reuseaddress", "true")));
	}


	public HettyServer() {
		this("server.properties");
	}


	/**
	 * start netty server with threadPool(used to invoke method)
	 * @param threadPool
	 * @throws Exception
	 */
	public void start(final ExecutorService threadPool) throws Exception {
		if (!startFlag.compareAndSet(false, true)) {
			return;
		}
		
		ServerConfig sc = ServerConfig.getInstance();
		
		Plugin scm = new ServerConfigManager(serviceConfigFile);
		this.registerPlugin(scm);
		Plugin hsc = new HessianServiceConfig();
		this.registerPlugin(hsc);
		
		List<Class<?>> pluginList = sc.getPluginClassList();
		for(Class<?> cls:pluginList){
			Plugin p = (Plugin)cls.newInstance();
			p.start(this);
		}
		
		bootstrap.setPipelineFactory(new HessianChannelPipelineFactory(
				threadPool));

		port = sc.getPort();

		bootstrap.bind(new InetSocketAddress(port));
		logger.info("Server started,http listen at: " + port);
	}

	public void addApplication(Application app) {
		applicationMap.put(app.getKey(), app);
	}

	public void addAppServiceSecurity(AppServiceSecurity ass) {
		StringBuilder sb = new StringBuilder();
		sb.append(ass.getApplicationKey());
		sb.append("_");
		sb.append(ass.getServiceName());
		appServiceSecurityMap.put(sb.toString(), ass);
	}

	public static AppServiceSecurity getAppServiceSecurity(String appKey,
			String serviceName) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(appKey);
		sb.append("_");
		sb.append(serviceName);
		String key = sb.toString();
		AppServiceSecurity ass = appServiceSecurityMap.get(key);
		if (ass == null) {
			throw new RuntimeException("application 【" + appKey + "】does not have priviledge on "
					+ serviceName);
		}
		return ass;
	}

	public static Map<String, AppServiceSecurity> getAppServiceSecurityMap() {
		return appServiceSecurityMap;
	}

	public Map<String, Application> getApplicationMap() {
		return applicationMap;
	}

	public static Application getApplication(String key) {
		if (key.equals(currentApp.getKey())) {
			return currentApp;
		}

		Application app = applicationMap.get(key);
		if (app == null) {
			throw new RuntimeException("we cannot find application 【" + key + "】");
		}
		return app;
	}

	public void stop() throws Exception {
		logger.warn("Server stop!");

		bootstrap.releaseExternalResources();
		startFlag.set(false);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMethodTimeout() {
		return methodTimeout;
	}

	public void setMethodTimeout(int methodTimeout) {
		this.methodTimeout = methodTimeout;
	}

	public void registerService(Service service) {
		ServiceHandlerFactory.registerService(service);
	}

	public static Service getService(String name) {
		return ServiceHandlerFactory.getService(name);
	}

	public static Map<String, Service> getServiceMap() {
		return ServiceHandlerFactory.getServiceMap();
	}

	public static void main(String[] args) {
		String config = System.getProperty("config");
		if (config == null) {
			config = "server.properties";
		}
		HettyServer server = new HettyServer(config);

		ServerConfig serverConfig = ServerConfig.getInstance();
		
		int coreSize = serverConfig.getServerCorePoolSize();
		int maxSize = serverConfig.getServerMaximumPoolSize();
		int keepAlive = serverConfig.getServerKeepAliveTime();
		
		ThreadFactory tf = new NamedThreadFactory("hetty-");
		
		ExecutorService threadPool = new ThreadPoolExecutor(coreSize, maxSize, keepAlive,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), tf);
		// start server
		try {
			server.start(threadPool);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	public static Application getCurrentApp() {
		return currentApp;
	}

	public static void setCurrentApp(Application currentApp) {
		HettyServer.currentApp = currentApp;
	}

	public static Map<String, Application> getApplicationmap() {
		return applicationMap;
	}

	public static Map<String, AppServiceSecurity> getAppservicesecuritymap() {
		return appServiceSecurityMap;
	}
	
	private void registerPlugin(Plugin p){
		p.start(this);
	}

}
