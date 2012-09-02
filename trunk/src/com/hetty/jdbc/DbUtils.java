package com.hetty.jdbc;

import java.sql.Connection;

import com.hetty.jdbc.ProxoolProvider.Transaction;


public class DbUtils {
	public static Connection getConnection(){
		return ProxoolProvider.getConnection1();
	}
	
	public static void close(){
		ProxoolProvider.closeConnection();
	}
	
	public static Transaction getTransacation(){
		return ProxoolProvider.getTransaction();
	}
}
