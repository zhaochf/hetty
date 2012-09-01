package com.hetty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.hetty.RequestWrapper;
import com.hetty.object.BcService;


public class ServiceHandlerFactory {
	private final static Map<String, BcService> serviceObjectMap = new ConcurrentHashMap<String, BcService>();
	
	private ServiceHandlerFactory() {

	}

	public static ServiceHandler getServiceHandler(RequestWrapper request) {
		
		String sname = request.getServiceName();
		BcService so = getService(sname);
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

	public static void registerService(BcService so) {
		serviceObjectMap.put(so.getName(), so);
		if(so.getType()==BcService.TYPE_LOCAL){
			LocalServiceHandler.publishService(so);
		}
	}

	public static BcService getService(String name) {
		return serviceObjectMap.get(name);
	}
	
	public static Map<String, BcService> getServiceMap(){
		return serviceObjectMap;
	}

}
