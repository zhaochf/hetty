package com.hetty.object;

import java.util.HashMap;
import java.util.Map;

public class LocalService extends BcService {

	private static final long serialVersionUID = -7977353532368464473L;

	public final static int SCOPE_SINGETON=1;
	public final static int SCOPE_PROTOTYPE=2;
	
	private int scope=SCOPE_SINGETON;
	
	private String defaultVersion;
	
	
	private Map<String,ServiceProvider> serviceProviderMap =new HashMap<String, ServiceProvider>();
	
	public LocalService(){
		
	}
	
	public LocalService(String id,String name){
		super(id,name);
	}
	
	@Override
	public int getType() {
		return BcService.TYPE_LOCAL;
	}
	
	public Class<?> getProcessorClass(int v) {
		if(serviceProviderMap.containsKey(v)){
			return serviceProviderMap.get(v).getProcessorClass();
		}
		throw new RuntimeException("该版本号不存在！");
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
	public void addProvider(ServiceProvider sv){
		String v=sv.getVersion();
		if(!serviceProviderMap.containsKey(v)){
			serviceProviderMap.put(v, sv);
		}
	}
	
	public Map<String,ServiceProvider> getServiceProviderMap(){
		return serviceProviderMap;
	}
	
	public ServiceProvider getVersion(String v){
		return serviceProviderMap.get(v);
	}

}
