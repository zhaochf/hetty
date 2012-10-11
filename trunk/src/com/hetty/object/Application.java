package com.hetty.object;

import java.io.Serializable;

public class Application implements Serializable{

	private static final long serialVersionUID = 2485662854684252982L;
	
	private String name;
	private String key;
	private String secret;
	
	private int connectionTimeout = 3000;
	private int methodTimeout = 3000;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getMethodTimeout() {
		return methodTimeout;
	}
	public void setMethodTimeout(int methodTimeout) {
		this.methodTimeout = methodTimeout;
	}
	
}
