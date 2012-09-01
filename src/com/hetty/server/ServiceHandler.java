package com.hetty.server;

import com.hetty.RequestWrapper;

public interface ServiceHandler {
	Object handleRequest(RequestWrapper request);
}
