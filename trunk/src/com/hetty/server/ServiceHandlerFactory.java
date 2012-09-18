package com.hetty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.hetty.RequestWrapper;
import com.hetty.object.Service;

/**
 * 
 * @author guolei
 *
 */
public class ServiceHandlerFactory {
	
	private final static Map<String, Service> serviceObjectMap = new ConcurrentHashMap<String, Service>();
	
	private ServiceHandlerFactory() {

	}

	/**
	 * get the LocalServiceHandler object
	 * @param request
	 * @return
	 */
	public static ServiceHandler getServiceHandler(RequestWrapper request) {
		
		String sname = request.getServiceName();
		Service so = getService(sname);
		if(so==null){
			throw new RuntimeException("Service【"+sname+"】不存在！");
		}
		ServiceHandler sh = new LocalServiceHandler();
		return sh;
	}

	public static Object handleRequest(RequestWrapper request) throws Exception {
		ServiceHandler handler = getServiceHandler(request);

		Object rw = null;
		try{
			rw=handler.handleRequest(request);
		}catch(Exception e){
			rw=e;
			e.printStackTrace();
			throw e;
		}
		return rw;
	}

	/**
	 * register service to serviceObjectMap
	 * @param service
	 */
	public static void registerService(Service so) {
		serviceObjectMap.put(so.getName(), so);
		LocalServiceHandler.publishService(so);
	}

	public static Service getService(String name) {
		return serviceObjectMap.get(name);
	}
	
	public static Map<String, Service> getServiceMap(){
		return serviceObjectMap;
	}

}
