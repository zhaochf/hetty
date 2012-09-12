package com.hetty.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HConnection implements Connection {
	private static ThreadLocal<Connection> localConnection = new ThreadLocal<Connection>();
	private static ThreadLocal<Integer> localCount = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> localTransaction = new ThreadLocal<Integer>();
	private static String alias = "proxool.hetty";

	private HConnection() {

	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		Connection cnn = getConnection();
		return cnn.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		Connection cnn = getConnection();
		return cnn.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		Connection cnn = getConnection();
		return cnn.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareCall(sql);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		Connection cnn = getConnection();
		return cnn.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		Connection cnn = getConnection();
		cnn.setAutoCommit(autoCommit);

	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		Connection cnn = getConnection();
		int t = localTransaction.get();
		t--;
		localTransaction.set(t);
		if (cnn != null && !cnn.getAutoCommit() && !cnn.isClosed() && t == 0) {
			cnn.commit();
		}

	}

	@Override
	public void rollback() throws SQLException {
		Connection cnn = getConnection();
		localTransaction.set(0);
		if (cnn != null && !cnn.getAutoCommit() && !cnn.isClosed()) {
			cnn.rollback();
		}

	}

	@Override
	public void close() throws SQLException {
		closeConnection();
	}

	@Override
	public boolean isClosed() throws SQLException {
		Connection cnn = getConnection();
		return cnn.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		Connection cnn = getConnection();
		cnn.setReadOnly(readOnly);

	}

	@Override
	public boolean isReadOnly() throws SQLException {
		Connection cnn = getConnection();
		return cnn.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		Connection cnn = getConnection();
		cnn.setCatalog(catalog);

	}

	@Override
	public String getCatalog() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		Connection cnn = getConnection();
		cnn.setTransactionIsolation(level);

	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		Connection cnn = getConnection();
		cnn.clearWarnings();

	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		Connection cnn = getConnection();
		cnn.setTypeMap(map);

	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		Connection cnn = getConnection();
		cnn.setHoldability(holdability);

	}

	@Override
	public int getHoldability() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		Connection cnn = getConnection();
		return cnn.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		Connection cnn = getConnection();
		return cnn.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		Connection cnn = getConnection();
		cnn.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		Connection cnn = getConnection();
		cnn.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.createStatement(resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareStatement(sql, resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareCall(sql, resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		Connection cnn = getConnection();
		return cnn.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		Connection cnn = getConnection();
		return cnn.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		Connection cnn = getConnection();
		return cnn.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		Connection cnn = getConnection();
		return cnn.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		Connection cnn = getConnection();
		return cnn.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		Connection cnn = getConnection();
		cnn.setClientInfo(name, value);

	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {

		Connection cnn = getConnection();
		cnn.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		Connection cnn = getConnection();
		return cnn.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		Connection cnn = getConnection();
		return cnn.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		Connection cnn = getConnection();
		return cnn.createStruct(typeName, attributes);
	}

	public Transaction getTransaction() {
		Transaction tra = new Transaction();
		return tra;
	}

	public void closeConnection() {
		Connection cnn = localConnection.get();
		if (cnn != null) {
			Long tid = Thread.currentThread().getId();
			synchronized (tid) {
				if (localCount.get() == null) {
					return;
				}
				int c = localCount.get();
				if (c > 0) {
					c--;

				}

				if (c == 0) {

					try {
						if (!cnn.isClosed()) {
							cnn.close();
							/*System.out.println("Thread ["
									+ Thread.currentThread().getName()
									+ "] 关闭连接");*/
						}
					} catch (SQLException e) {
						e.printStackTrace();
						try {
							if (cnn != null && !cnn.isClosed()) {
								cnn.close();
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					} finally {
						if (cnn != null) {
							cnn = null;
						}
						localCount.set(null);
						localConnection.set(null);
					}
				}else{
					localCount.set(c);
				}

			}
		}
	}

	private static Connection getConnection() {
		Connection cnn = localConnection.get();
		try {
			if (cnn == null || cnn.isClosed()) {
				Long tid = Thread.currentThread().getId();
				synchronized (tid) {
					/*System.out.println("Thread ["
							+ Thread.currentThread().getName() + "] 获取连接");*/
					if (localConnection.get() == null) {
						cnn = DriverManager.getConnection(alias);
						localConnection.set(cnn);

					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnn;
	}

	public static HConnection getInstance() {
		if (localCount.get() == null) {
			localCount.set(1);
		} else {
			int c = localCount.get();
			c++;
			localCount.set(c);
		}
		return new HConnection();
	}

	public static class Transaction {
		public void begin() throws SQLException {
			Connection cnn = getConnection();
			if (cnn != null && cnn.getAutoCommit()) {
				cnn.setAutoCommit(false);
			}
			if (localTransaction.get() == null) {
				localTransaction.set(1);
			} else {
				int t = localTransaction.get();
				t++;
				localTransaction.set(t);
			}
		}

		public void end() throws SQLException {
			Connection cnn = getConnection();
			int t = localTransaction.get();
			t--;
			localTransaction.set(t);
			if (cnn != null && !cnn.getAutoCommit() && !cnn.isClosed()
					&& t == 0) {
				localTransaction.set(null);
				cnn.commit();
				cnn.setAutoCommit(true);
			}

		}
	}

	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(1);
		es.shutdownNow();
	}

}
