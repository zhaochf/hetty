package com.hetty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.hetty.RequestWrapper;
import com.hetty.object.Service;


public class ServiceHandlerFactory {
	private final static Map<String, Service> serviceObjectMap = new ConcurrentHashMap<String, Service>();
	
	private ServiceHandlerFactory() {

	}

	public static ServiceHandler getServiceHandler(RequestWrapper request) {
		
		String sname = request.getServiceName();
		Service so = getService(sname);
		if(so==null){
			throw new RuntimeException("Service【"+sname+"】不存在！");
		}
		ServiceHandler sh = new LocalServiceHandler();
	

		return sh;
	}

	public static Object handleRequest(RequestWrapper request) {
		ServiceHandler handler = getServiceHandler(request);

		Object rw = null;
		try{
			rw=handler.handleRequest(request);
		}catch(Exception e){
			rw=e;
			e.printStackTrace();
		}
		return rw;
	}

	public static void registerService(Service so) {
		serviceObjectMap.put(so.getName(), so);
		if(so.getType()==Service.TYPE_LOCAL){
			LocalServiceHandler.publishService(so);
		}
	}

	public static Service getService(String name) {
		return serviceObjectMap.get(name);
	}
	
	public static Map<String, Service> getServiceMap(){
		return serviceObjectMap;
	}

}
