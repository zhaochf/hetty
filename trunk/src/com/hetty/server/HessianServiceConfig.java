package com.hetty.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hetty.object.Service;
import com.hetty.plugin.Plugin;

/**
 * 
 * put the service info to map
 *
 */
public class HessianServiceConfig  implements Plugin{

	private static Map<String, ServiceMetaData> hessianServiceMetaMap = new HashMap<String, ServiceMetaData>();

	public void start(HettyServer server) {
		Map<String, Service> serviceMap = ServiceHandlerFactory
				.getServiceMap();
		Set<String> serviceNames = serviceMap.keySet();
		for (String sn : serviceNames) {
			Service bs = serviceMap.get(sn);
			Class<?> tc = bs.getTypeClass();
			ServiceMetaData smd = new ServiceMetaData(tc);
			hessianServiceMetaMap.put(sn, smd);
		}

	}

	public static ServiceMetaData getServiceMetaData(String sname) {
		return hessianServiceMetaMap.get(sname);
	}

	public void stop() {

	}

}
