package com.hetty.server;

import java.util.HashMap;

import org.jboss.netty.handler.codec.http.HttpRequest;

public class ServiceContext {

	private static final ThreadLocal<ServiceContext> _localContext = new ThreadLocal<ServiceContext>();

	private HttpRequest _request;
	private String _serviceName;
	private String _objectId;
	private int _count;
	private HashMap<String, Object> _headers = new HashMap<String, Object>();

	private ServiceContext() {
	}

	/**
	 * Sets the request object prior to calling the service's method.
	 * 
	 * @param request
	 *            the calling servlet request
	 * @param serviceId
	 *            the service identifier
	 * @param objectId
	 *            the object identifier
	 */
	public static void begin(HttpRequest request, String serviceName,
			String objectId) {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context == null) {
			context = new ServiceContext();
			_localContext.set(context);
		}

		context._request = request;
		context._serviceName = serviceName;
		context._objectId = objectId;
		context._count++;
	}

	/**
	 * Returns the service request.
	 */
	public static ServiceContext getContext() {
		return (ServiceContext) _localContext.get();
	}

	/**
	 * Adds a header.
	 */
	public void addHeader(String header, Object value) {
		_headers.put(header, value);
	}

	/**
	 * Gets a header.
	 */
	public Object getHeader(String header) {
		return _headers.get(header);
	}

	/**
	 * Gets a header from the context.
	 */
	public static Object getContextHeader(String header) {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null)
			return context.getHeader(header);
		else
			return null;
	}

	/**
	 * Returns the service request.
	 */
	public static HttpRequest getContextRequest() {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null)
			return context._request;
		else
			return null;
	}

	/**
	 * Returns the service id, corresponding to the pathInfo of the URL.
	 */
	public static String getContextServiceName() {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null)
			return context._serviceName;
		else
			return null;
	}

	/**
	 * Returns the object id, corresponding to the ?id= of the URL.
	 */
	public static String getContextObjectId() {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null)
			return context._objectId;
		else
			return null;
	}

	/**
	 * Cleanup at the end of a request.
	 */
	public static void end() {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null && --context._count == 0) {
			context._request = null;

			context._headers.clear();
		}
	}

	/**
	 * Returns the service request.
	 * 
	 * @deprecated
	 */
	public static HttpRequest getRequest() {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null)
			return context._request;
		else
			return null;
	}

	/**
	 * Returns the service id, corresponding to the pathInfo of the URL.
	 * 
	 * @deprecated
	 */
	public static String getServiceName() {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null)
			return context._serviceName;
		else
			return null;
	}

	/**
	 * Returns the object id, corresponding to the ?id= of the URL.
	 * 
	 * @deprecated
	 */
	public static String getObjectId() {
		ServiceContext context = (ServiceContext) _localContext.get();

		if (context != null)
			return context._objectId;
		else
			return null;
	}

}
