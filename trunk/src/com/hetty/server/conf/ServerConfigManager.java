package com.hetty.server.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hetty.object.AppMessageSecurity;
import com.hetty.object.AppServiceSecurity;
import com.hetty.object.Application;
import com.hetty.object.Service;
import com.hetty.server.HettyServer;

public class ServerConfigManager {
	
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
	
	public void init(HettyServer server) {
		ServerConfig sc=ServerConfig.getInstance().loadProperties(configFile);
		String xmlfile=sc.getConfigFile();
		ConfigParser cp=new XmlConfigParser(xmlfile);
		Application app=new Application();
		app.setKey(sc.getServerKey());
		app.setSecret(sc.getServerSecret());
		app.setConnectionTimeout(sc.getConnectionTimeout());
		app.setMethodTimeout(sc.getMethodTimeout());
		app.setName(sc.getServerName());
		
		//.parseCurrentApp(server);
		server.setCurrentApp(app);
		
		
		List<Service> serviceList=cp.parseService();
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
