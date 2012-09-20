package com.hetty.object;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocalService extends Service {

	private static final long serialVersionUID = -7977353532368464473L;

	public final static int SCOPE_SINGETON = 1;
	public final static int SCOPE_PROTOTYPE = 2;
	
	private int scope = SCOPE_SINGETON;
	
	private String defaultVersion;
	
	//key:version value:ServiceProvider
	private Map<String,ServiceProvider> serviceProviderMap = new HashMap<String, ServiceProvider>();
	
	public LocalService(String id,String name){
		super(id,name);
	}
	
	/**
	 * According to the version number get service information
	 * @param version
	 * @return
	 */
	public Class<?> getProcessorClass(int version) {
		if(serviceProviderMap.containsKey(version)){
			return serviceProviderMap.get(version).getProcessorClass();
		}
		throw new RuntimeException("the version does not exit");
	}


	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}

	public String getDefaultProvider() {
		return defaultVersion;
	}

	public void setDefaultVersion(String defaultVersion) {
		this.defaultVersion = defaultVersion;
	}
	public void addProvider(String vid,Class<?> cls,boolean defaultV){
		ServiceProvider sv =new ServiceProvider(vid,cls);
		addProvider(sv);
		if(defaultV){
			this.setDefaultVersion(vid);
		}
	}
	public void addProvider(String vid,Class<?> cls){
		ServiceProvider sv =new ServiceProvider(vid,cls);
		addProvider(sv);
	}
	public void addDefaultVersion(String vid,Class<?> cls){
		addProvider(vid,cls,true);
	}
	/**
	 * put the version to the map and set the provider's version as default version
	 * @param sv
	 * @param defaultV
	 */
	public void addProvider(ServiceProvider sv,boolean defaultV){
		String v=sv.getVersion();
		if(!serviceProviderMap.containsKey(v)){
			serviceProviderMap.put(v, sv);
		}
		if(defaultV){
			this.setDefaultVersion(sv.getVersion());
		}
	}
	public void addDefaultVersion(ServiceProvider sv){
		addProvider(sv,true);
	}
	/**
	 * put the version and ServiceProvider to map
	 * @param sv
	 */
	public void addProvider(ServiceProvider sv){
		String v = sv.getVersion();
		if(!serviceProviderMap.containsKey(v)){
			serviceProviderMap.put(v, sv);
		}
	}
	
	public Map<String,ServiceProvider> getServiceProviderMap(){
		return Collections.unmodifiableMap(serviceProviderMap);
	}
	/**
	 * according version to get provider which save in the map
	 * @param version
	 * @return
	 */
	public ServiceProvider getProviderByVersion(String version){
		return serviceProviderMap.get(version);
	}

}
