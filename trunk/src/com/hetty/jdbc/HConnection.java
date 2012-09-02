package com.hetty.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

public class HConnection implements Connection {
	private static ThreadLocal<Connection> localConnection = new ThreadLocal<Connection>();
	private static ThreadLocal<Integer> localCount = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> localTransaction = new ThreadLocal<Integer>();

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareCall(sql);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setAutoCommit(autoCommit);

	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		int t = localTransaction.get();
		t--;
		localTransaction.set(t);
		if (cnn != null && !cnn.getAutoCommit() && !cnn.isClosed()) {
			cnn.commit();
		}

	}

	@Override
	public void rollback() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		localTransaction.set(0);
		if (cnn != null && !cnn.getAutoCommit() && !cnn.isClosed()) {
			cnn.rollback();
		}

	}

	public Connection getConnection1() {
		Connection cnn = ProxoolProvider.getConnection1();

		synchronized (cnn) {
			if (localCount.get() == null) {
				localCount.set(1);
			} else {
				int c = localCount.get();
				c++;
				localCount.set(c);
			}
		}
		return cnn;
	}

	@Override
	public void close() throws SQLException {
		closeConnection();
	}

	@Override
	public boolean isClosed() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setReadOnly(readOnly);

	}

	@Override
	public boolean isReadOnly() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setCatalog(catalog);

	}

	@Override
	public String getCatalog() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setTransactionIsolation(level);

	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.clearWarnings();

	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setTypeMap(map);

	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setHoldability(holdability);

	}

	@Override
	public int getHoldability() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createStatement(resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareStatement(sql, resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareCall(sql, resultSetType, resultSetConcurrency,
				resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setClientInfo(name, value);

	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {

		Connection cnn = ProxoolProvider.getConnection1();
		cnn.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		Connection cnn = ProxoolProvider.getConnection1();
		return cnn.createStruct(typeName, attributes);
	}

	public Transaction getTransaction() {
		Transaction tra = new Transaction();
		return tra;
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

	public static class Transaction {
		public void begin() throws SQLException {
			Connection cnn = ProxoolProvider.getConnection1();
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
			Connection cnn = ProxoolProvider.getConnection1();
			int t = localTransaction.get();
			t--;
			localTransaction.set(t);
			if (cnn != null && !cnn.getAutoCommit() && !cnn.isClosed()&&t==0) {
					cnn.commit();
			}

		}
	}

}
