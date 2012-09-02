package com.hetty.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;

import com.hetty.jdbc.HConnection.Transaction;
import com.hetty.plugin.Plugin;
import com.hetty.server.HettyServer;
import com.hetty.server.conf.ServerConfig;

public class ProxoolProvider implements Plugin, ConnectionProvider {
	private static ThreadLocal<Connection> localConnection = new ThreadLocal<Connection>();
	private static ThreadLocal<Integer> localCount = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> localTransaction=new ThreadLocal<Integer>();
	private static String alias = "proxool.hetty";


	@Override
	public void start(HettyServer server) {
		Properties config = ServerConfig.getInstance().getProperties();
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			String user = config.getProperty("config.jdbc.username");
			String password = config.getProperty("config.jdbc.password");
			String driverUrl = config.getProperty("config.jdbc.url");
			String driverClass = config.getProperty("config.jdbc.driverClass");
			final Properties info = new Properties();
			info.setProperty("user", user);
			info.setProperty("password", password);
			convert(info, config);
			String url = alias + ":" + driverClass + ":" + driverUrl;
			ProxoolFacade.registerConnectionPool(url, info);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ProxoolException e) {
			e.printStackTrace();
		}
		
		HConnection cnn=DbUtils.getConnection();
		Transaction tra=cnn.getTransaction();
		
		try {
			tra.begin();
			Statement smt=cnn.createStatement();
			ResultSet rs=smt.executeQuery("select * from bcs_apps");
			while(rs.next()){
				String name=rs.getString("app_name");
				System.out.println(name);
			}
			rs.close();
			tra.end();
			cnn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
/*
		Transaction tr=DbUtils.getTransacation();
		
		try {
			tr.begin();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		
		
		/*Connection cnn=this.getConnection();
		try {
			Statement smt=cnn.createStatement();
			ResultSet rs=smt.executeQuery("select * from bcs_apps");
			while(rs.next()){
				String name=rs.getString("app_name");
				System.out.println(name);
			}
			rs.close();
			DbUtils.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private void convert(final Properties info, Properties config) {
		Set<String> keySet = config.stringPropertyNames();
		String prefix = "config.jdbc.proxool";
		for (String key : keySet) {
			if (key.startsWith(prefix)) {
				String newKey = key.substring(prefix.length() + 1);
				String value = config.getProperty(key);
				info.setProperty(newKey, value);
			}
		}
	}

	@Override
	public void stop() {

	}

	@Override
	public Connection getConnection() {
		Connection cnn = getConnection1();
		return cnn;
	}

	public static Connection getConnection1() {
		Connection cnn = localConnection.get();
		if (cnn == null) {
			try {
				cnn = DriverManager.getConnection(alias);
				localConnection.set(cnn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		synchronized (cnn) {
			if(localCount.get()==null){
				localCount.set(1);
			}else{
				int c = localCount.get();
				c++;
				localCount.set(c);
			}
		}
		return cnn;
	}

	@Override
	public void close() {
		closeConnection();
	}

	public static void closeConnection() {
		Connection cnn = localConnection.get();
		if (cnn != null) {
			synchronized (cnn) {
				int c = localCount.get();
				if (c > 0) {
					c--;
					localCount.set(c);
				} else {
					try {
						if (!cnn.isClosed()) {
							cnn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						cnn = null;
					}
				}
				
			}
		}
	}

	/*public static Transaction getTransaction() {
		Transaction tra=new Transaction();
		return tra;
	}
	
	
	public static class Transaction{
		public void begin() throws SQLException{
			Connection cnn = ProxoolProvider.getConnection1();
			if(cnn!=null&&cnn.getAutoCommit()){
				cnn.setAutoCommit(false);
			}
			int t=localTransaction.get();
			t++;
			localTransaction.set(t);
			
		}
		
		public void commit() throws SQLException{
			Connection cnn = ProxoolProvider.getConnection1();
			int t=localTransaction.get();
			t--;
			localTransaction.set(t);
			if(cnn!=null&&!cnn.getAutoCommit()&&!cnn.isClosed()){
				cnn.commit();
			}
			
		}
		
		public void rollback() throws SQLException{
			Connection cnn = ProxoolProvider.getConnection1();
			localTransaction.set(0);
			if(cnn!=null&&!cnn.getAutoCommit()&&!cnn.isClosed()){
				cnn.rollback();
			}
		}
	}
*/
}
