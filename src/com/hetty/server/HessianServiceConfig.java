package com.hetty.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hetty.object.BcService;


public class HessianServiceConfig  {

	private static Map<String, ServiceMetaData> hessianServiceMetaMap = new HashMap<String, ServiceMetaData>();

	public void init(HettyServer server) {
		Map<String, BcService> serviceMap = ServiceHandlerFactory
				.getServiceMap();
		Set<String> serviceNames = serviceMap.keySet();
		for (String sn : serviceNames) {
			BcService bs = serviceMap.get(sn);
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
