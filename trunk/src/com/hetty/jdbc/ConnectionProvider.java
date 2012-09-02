package com.hetty.jdbc;

import java.sql.Connection;

public interface ConnectionProvider {
	Connection getConnection();
	
	void close();
}	
