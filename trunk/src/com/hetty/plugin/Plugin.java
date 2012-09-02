package com.hetty.plugin;

import com.hetty.server.HettyServer;

public interface Plugin {
	void start(HettyServer hs);
	
	void stop();
}
