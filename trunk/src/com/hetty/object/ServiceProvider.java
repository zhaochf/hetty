package com.hetty.object;

import java.io.Serializable;

public class ServiceProvider implements Serializable{

	private static final long serialVersionUID = 2323169647020692097L;
	
	private String version;
	
	public static final String DEFAULT_VERSION="0";
	
	private Class<?> processorClass;

	public ServiceProvider(){
		
	}
	public ServiceProvider(String version,Class<?> processorClass){
		this.version=version;
		this.processorClass=processorClass;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Class<?> getProcessorClass() {
		return processorClass;
	}

	public void setProcessorClass(Class<?> processorClass) {
		this.processorClass = processorClass;
	}
	
	
}
