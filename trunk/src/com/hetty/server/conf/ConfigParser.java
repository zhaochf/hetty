package com.hetty.server.conf;

import java.util.List;

import com.hetty.object.Application;
import com.hetty.object.Service;

public interface ConfigParser {



	List<Service> parseService();


	List<Application> parseApplication();

	List<Object> parseSecurity();

}