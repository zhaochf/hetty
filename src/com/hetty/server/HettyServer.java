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


	public HettyServer(String configFile) {

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

	public void start() throws Exception {
		ThreadFactory tf = new NamedThreadFactory("hetty-pool-");
		int minSize = Runtime.getRuntime().availableProcessors();
		int maxSize = minSize * 100;
		ExecutorService threadPool = new ThreadPoolExecutor(minSize, maxSize,
				3000, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), tf);
		start(threadPool);
	}

	public void start(final ExecutorService threadPool) throws Exception {
		if (!startFlag.compareAndSet(false, true)) {
			return;
		}
		
		ServerConfig sc=ServerConfig.getInstance();
		
		sc.loadProperties("server.properties");
		
		Plugin scm = new ServerConfigManager();
		this.registerPlugin(scm);
		Plugin hsc = new HessianServiceConfig();
		this.registerPlugin(hsc);
		
		List<Class<?>> pluginList=sc.getPluginClassList();
		for(Class<?> cls:pluginList){
			Plugin p=(Plugin)cls.newInstance();
			p.start(this);
		}

		ServerBootstrap httpBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		httpBootstrap.setPipelineFactory(new HessianChannelPipelineFactory(
				threadPool));

		port = sc.getPort();

		httpBootstrap.bind(new InetSocketAddress(port));
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
		/*
		 * if(currentApp.getKey().equals(appKey)){ AppServiceSecurity ass=new
		 * AppServiceSecurity(); ass.setApplicationKey(appKey);
		 * ass.setServiceName(serviceName);
		 * 
		 * return ass; }
		 */
		StringBuilder sb = new StringBuilder();
		sb.append(appKey);
		sb.append("_");
		sb.append(serviceName);
		String key = sb.toString();
		AppServiceSecurity ass = appServiceSecurityMap.get(key);
		if (ass == null) {
			throw new RuntimeException("应用【" + appKey + "】没有权限访问:"
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
			throw new RuntimeException("没有注册应用【" + key + "】");
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

		ThreadFactory tf = new NamedThreadFactory("hetty-");
		String coreSizeString=ServerConfig.getInstance().getProperty("server.thread.corePoolSize", "4");
		String maxSizeString=ServerConfig.getInstance().getProperty("server.thread.maxPoolSize", "16");
		String keepAliveTimeString=ServerConfig.getInstance().getProperty("server.thread.keepAliveTime", "3000");
		int coreSize=Integer.parseInt(coreSizeString);
		int maxSize=Integer.parseInt(maxSizeString);
		int keepAlive=Integer.parseInt(keepAliveTimeString);
		ExecutorService threadPool = new ThreadPoolExecutor(coreSize, maxSize, keepAlive,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), tf);
		// 启动服务器
		try {
			server.start(threadPool);
		} catch (Exception e) {
			e.printStackTrace();
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
