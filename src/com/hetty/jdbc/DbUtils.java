package com.hetty.jdbc;

public class DbUtils {
	
	public static HConnection getConnection(){
		return HConnection.getInstance();
	}
	
}
