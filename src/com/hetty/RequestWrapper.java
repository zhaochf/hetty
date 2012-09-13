package com.hetty;

import java.io.Serializable;

/**
 * RequestWrapper
 * 
 * @author <a href="mailto:zhuzhsh@gmail.com">Raymond</a>
 */
public class RequestWrapper implements Serializable {

	protected static final long serialVersionUID = -6017954186180888313L;

	protected String appKey = null;

	protected String appSecret = null;

	protected String serviceName;

	protected String methodName;
	
	protected int timeout = 0;
	
	protected Object[] args = null;

	public RequestWrapper() {

	}

	public RequestWrapper(String appKey, String appSecret, String serviceName,
			String methodName,Object[] args, int timeout) {
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.args = args;
		this.timeout = timeout;
		this.appKey = appKey;
		this.appSecret = appSecret;
	}

	public int getTimeout() {
		return timeout;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(byte[][] args) {
		this.args = args;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
}
