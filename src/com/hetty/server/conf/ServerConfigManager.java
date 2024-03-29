package com.hetty.server.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.hetty.object.AppServiceSecurity;
import com.hetty.object.Application;
import com.hetty.object.Service;
import com.hetty.plugin.Plugin;
import com.hetty.server.HettyServer;

/**
 * a plugin to read the service config,the client application config(privilege),the client's invoke service(privilege)
 * 
 * @author guolei
 *
 */
public class ServerConfigManager implements Plugin{
	
	private final static Map<String,Application> applicationMap=new HashMap<String, Application>();
	
	private String configFile;
	public ServerConfigManager(){
		this.configFile="server.properties";
	}
	
	public ServerConfigManager(String config){
		if(config==null){
			this.configFile="server.properties";
		}else{
			this.configFile=config;
		}
	}
	
	/**
	 * 1.set HettyServer's currentApp
	 * 2.put config xml's service infor into ServiceHandlerFactory's serviceObjectMap map
	 * 3.put service method info into LocalServiceHandler's cacheMethodAccess map 
	 * 4.put config xml's application info into HettyServer's applicationMap
	 * 5.put config xml's security info into HettyServer's appServiceSecurityMap
	 */
	public void start(HettyServer server) {
		ServerConfig sc = ServerConfig.getInstance();
		String xmlfile = sc.getConfigFile();
		ConfigParser cp = new XmlConfigParser(xmlfile);
		Application app = new Application();
		app.setKey(sc.getServerKey());
		app.setSecret(sc.getServerSecret());
		app.setConnectionTimeout(sc.getConnectionTimeout());
		app.setMethodTimeout(sc.getMethodTimeout());
		app.setName(sc.getServerName());
		
		HettyServer.setCurrentApp(app);
		
		List<Service> serviceList = cp.parseService();
		for(Service bs:serviceList){
			server.registerService(bs);
		}
		
		List<Application> appList=cp.parseApplication();
		for(Application a:appList){
			server.addApplication(a);
		}
		
		List<Object> securityList=cp.parseSecurity();
		for(Object o:securityList){
			if(o instanceof AppServiceSecurity){
				server.addAppServiceSecurity((AppServiceSecurity)o);
			}
		}
	}
	
	public void stop() {
		
	}
	
	public static void addApplication(Application app){
		applicationMap.put(app.getKey(), app);
	}

	public static Application getApplication(String appKey){
		return applicationMap.get(appKey);
	}
}
