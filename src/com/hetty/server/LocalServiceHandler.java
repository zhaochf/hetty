package com.hetty.server;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hetty.RequestWrapper;
import com.hetty.object.AppServiceSecurity;
import com.hetty.object.Application;
import com.hetty.object.Service;
import com.hetty.object.LocalService;
import com.hetty.object.ServiceProvider;
import com.hetty.protocol.ProtocolUtils;


public class LocalServiceHandler implements ServiceHandler {
	private final static Logger logger = LoggerFactory
			.getLogger(LocalServiceHandler.class);

	private static Map<String, Map<String, MethodAccess>> cacheMethodAccess = new HashMap<String, Map<String, MethodAccess>>();

	@Override
	public Object handleRequest(RequestWrapper request) {
		String instanceName = request.getServiceName();
		String methodName = request.getMethodName();

		

		LocalService so = (LocalService) ServiceHandlerFactory
				.getService(instanceName);
		String appKey = request.getAppKey();
		String appSecret = request.getAppSecret();
		String requestVersion = getServiceVersion(appKey, appSecret,
				instanceName);
		String versionValue = ServiceProvider.DEFAULT_VERSION
				.equals(requestVersion) ? so.getDefaultProvider()
				: requestVersion;
		ServiceProvider sv = so.getVersion(versionValue);
		Class<?> processorClass = sv.getProcessorClass();
		Object result=null;
		try {
			Object processor = processorClass.newInstance();
			Object[] args = request.getArgs();
			
			

				MethodAccess method = cacheMethodAccess.get(instanceName).get(
						versionValue);
				int methodIndex = method.getIndex(methodName);

				result = method.invoke(processor, methodIndex, args);
				
				
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 

		return result;
	}

	public static void publishService(Service so) {
		String instanceName = so.getName();
		LocalService lso = (LocalService) so;
		Map<String, ServiceProvider> vmap = lso.getServiceProviderMap();
		Collection<ServiceProvider> versionValue = vmap.values();
		Map<String, MethodAccess> maccess = cacheMethodAccess.get(instanceName);
		if (maccess == null) {
			maccess = new HashMap<String, MethodAccess>();
			cacheMethodAccess.put(instanceName, maccess);
		}

		for (ServiceProvider v : versionValue) {
			Class<?> instanceClass = v.getProcessorClass();
			String versionInt = v.getVersion();

			if (!maccess.containsKey(versionInt)) {
				MethodAccess ma = MethodAccess.get(instanceClass);
				maccess.put(versionInt, ma);
			} else {
				logger.warn("相同版本号的服务已经发布，请检查服务发布的版本是否跟原来的冲突！");
			}

		}

	}

	private String getServiceVersion(String appKey, String appSecret,
			String serviceName) {
		Application app = HettyServer.getApplication(appKey);
		if (!appSecret.equals(app.getSecret())) {
			throw new RuntimeException("应用【" + appKey + "】密码错误！");
		}
		AppServiceSecurity ass = HettyServer.getAppServiceSecurity(appKey, serviceName);
		String requestVersion = ass.getServiceVersion();
		return requestVersion;
	}
}
