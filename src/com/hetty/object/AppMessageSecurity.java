package com.hetty.object;

import java.io.Serializable;

public class AppMessageSecurity implements Serializable{

	private static final long serialVersionUID = -727751466014131086L;
	
	private String appKey;
	private String message;
	
	
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
