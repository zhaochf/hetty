package com.hetty.server;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.hetty.RequestWrapper;
import com.hetty.object.Service;

/**
 * handler all the service
 * 
 * @author guolei
 *
 */
public class ServiceHandlerFactory {
	
	//key:serviceName value:service
	private final static Map<String, Service> serviceObjectMap = new ConcurrentHashMap<String, Service>();
	
	private ServiceHandlerFactory() {
	}

	/**
	 * get the LocalServiceHandler object
	 * @param request
	 * @return
	 */
	public static ServiceHandler getServiceHandler(RequestWrapper request) {
		
		String serviceName = request.getServiceName();
		Service so = getService(serviceName);
		if(so==null){
			throw new RuntimeException("Service【"+serviceName+"】 can not find,please check.");
		}
		ServiceHandler serviceHandler = new LocalServiceHandler();
		return serviceHandler;
	}

	/**
	 * handler request and return the result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Object handleRequest(RequestWrapper request) throws Exception {
		
		ServiceHandler handler = getServiceHandler(request);

		Object rw = null;
		try{
			rw = handler.handleRequest(request);
		}catch(Exception e){
			rw = e;
			e.printStackTrace();
			throw e;
		}
		return rw;
	}

	/**
	 * register service to serviceObjectMap
	 * @param service
	 */
	public static void registerService(Service service) {
		serviceObjectMap.put(service.getName(), service);
		LocalServiceHandler.publishService(service);
	}

	/**
	 * get the service by serviceName
	 * @param serviceName
	 * @return
	 */
	public static Service getService(String serviceName) {
		return serviceObjectMap.get(serviceName);
	}
	/**
	 * return service map
	 * @return
	 */
	public static Map<String, Service> getServiceMap(){
		return Collections.unmodifiableMap(serviceObjectMap);
	}

}
