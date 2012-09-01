package com.hetty.object;

import java.io.Serializable;

public class AppServiceSecurity implements Serializable {

	private static final long serialVersionUID = 8040350724507199149L;
	

	private String applicationKey;

	private String serviceName;

	private String serviceVersion=ServiceProvider.DEFAULT_VERSION;


	public String getApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

}
