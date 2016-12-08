package uw.dm.connectionpool;

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
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Connection实现，用于封装Connection
 */
public class ConnectionWrapper implements Connection {

	/**
	 * 状态锁，用于防止争用
	 */
	private ReentrantLock statusLocker = new ReentrantLock();

	/**
	 * 创建时间
	 */
	long createTime = System.currentTimeMillis();

	/**
	 * 开始使用时间
	 */
	long busyTime;

	/**
	 * 开始静默时间
	 */
	long idleTime;

	/**
	 * 连接状态。 -1：不可用 0：可用 1：使用中 2：检测中
	 */
	private int status = -1;

	/**
	 * 实际的数据库连接
	 */
	private Connection connection;

	/**
	 * 获得数据库类型
	 */
	private String dbType;

	/**
	 * 获得数据库连接池名
	 */
	private String poolName;

	/**
	 * 获得调用堆栈
	 * 
	 * @param connection
	 * @param dbType
	 */
	Exception ex = null;

	/**
	 * 是否追踪堆栈。
	 */
	private boolean TRACE = true;

	public ConnectionWrapper(Connection connection, String dbType, String poolName) {
		this.connection = connection;
		this.dbType = dbType;
		this.poolName = poolName;
	}

	public Connection getSourceObject() {
		return this.connection;
	}

	public String getDbType() {
		return this.dbType;
	}

	/**
	 * 设定不使用的状态
	 */
	void setUnuseStatus() {
		statusLocker.lock();
		this.status = -1;
		this.busyTime = -1;
		this.idleTime = System.currentTimeMillis();
		statusLocker.unlock();
	}

	/**
	 * 设定为正常状态
	 */
	void setReadyStatus() {
		statusLocker.lock();
		this.status = 0;
		this.busyTime = -1;
		this.idleTime = System.currentTimeMillis();
		statusLocker.unlock();
	}

	/**
	 * 设为使用状态
	 */
	boolean trySetUseStatus() {
		boolean flag = false;
		statusLocker.lock();
		try {
			if (status == 0) {
				this.status = 1;
				this.busyTime = System.currentTimeMillis();
				this.idleTime = -1;
				if (TRACE) {
					ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date());
				}
				flag = true;
			}
		} finally {
			statusLocker.unlock();
		}
		return flag;
	}

	/**
	 * 设定为测试状态
	 */
	boolean trySetTestStatus() {
		boolean flag = false;
		statusLocker.lock();
		try {
			if (status == 0) {
				this.status = 2;
				flag = true;
			}
		} finally {
			statusLocker.unlock();
		}
		return flag;
	}

	/**
	 * 设定为退出测试状态，一般状态为0
	 */
	void setTestSuccessStatus() {
		statusLocker.lock();
		this.status = 0;
		this.busyTime = -1;
		statusLocker.unlock();
	}

	/**
	 * 获得状态
	 * 
	 * @return
	 */
	int getStatus() {
		statusLocker.lock();
		int status = this.status;
		statusLocker.unlock();
		return status;
	}

	/**
	 * 真正关闭掉
	 * 
	 */
	protected void trueClose() {
		this.setUnuseStatus();
		if (connection != null) {
			try {
				if (!connection.getAutoCommit()) {
					try {
						connection.rollback();
					} catch (Exception e) {
					}
					try {
						connection.setAutoCommit(true);
					} catch (Exception e) {
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				this.connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 轻量级别的检测是否存在。 true是ok
	 */
	boolean liteCheckAlive() {
		boolean flag = false;
		try {
			flag = !this.connection.isClosed();
		} catch (SQLException e) {
		}
		return flag;
	}

	/**
	 * 关闭操作。 将连接池放回。
	 */
	public void close() throws SQLException {
		if (connection != null) {
			try {
				if (!connection.getAutoCommit()) {
					connection.rollback();
					connection.setAutoCommit(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.trueClose();
			}
			this.setReadyStatus();
		} else {
			this.trueClose();
		}
		this.ex = null;
		idleTime = System.currentTimeMillis();
		busyTime = -1;
	}

	public int getHoldability() throws SQLException {
		return this.connection.getHoldability();
	}

	public void setHoldability(int holdability) throws SQLException {
		this.connection.setHoldability(holdability);
	}

	public Savepoint setSavepoint() throws SQLException {
		return this.connection.setSavepoint();
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.connection.releaseSavepoint(savepoint);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		this.connection.rollback(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date());
		}
		return this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return this.connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return this.connection.prepareStatement(sql, autoGeneratedKeys);
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return this.connection.setSavepoint(name);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return this.connection.prepareStatement(sql, columnNames);
	}

	public String toString() {
		if (connection != null) {
			return connection.toString();
		} else {
			return super.toString();
		}
	}

	public Statement createStatement() throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date());
		}
		return connection.createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return connection.prepareStatement(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return connection.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return connection.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		return connection.getAutoCommit();
	}

	public void commit() throws SQLException {
		connection.commit();
	}

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public boolean isClosed() throws SQLException {
		return (connection == null) ? true : false;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		connection.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return connection.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		connection.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return connection.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return connection.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return connection.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		connection.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date());
		}
		return connection.createStatement(resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		if (TRACE) {
			ex = new Exception("Connection[" + poolName + "] busy timeout! TIME:" + new Date() + " SQL:" + sql);
		}
		return this.connection.prepareStatement(sql, columnIndexes);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getTypeMap() throws SQLException {
		return connection.getTypeMap();
	}

	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		return connection.createArrayOf(arg0, arg1);
	}

	public Blob createBlob() throws SQLException {
		return connection.createBlob();
	}

	public Clob createClob() throws SQLException {
		return connection.createClob();
	}

	public NClob createNClob() throws SQLException {
		return connection.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return connection.createSQLXML();
	}

	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		return connection.createStruct(arg0, arg1);
	}

	public Properties getClientInfo() throws SQLException {
		return connection.getClientInfo();
	}

	public String getClientInfo(String arg0) throws SQLException {
		return connection.getClientInfo(arg0);
	}

	public boolean isValid(int arg0) throws SQLException {
		return connection.isValid(arg0);
	}

	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		connection.setClientInfo(arg0);
	}

	public void setClientInfo(String arg0, String arg1) throws SQLClientInfoException {
		connection.setClientInfo(arg0, arg1);
	}

	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		connection.setTypeMap(arg0);
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return connection.isWrapperFor(arg0);
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return connection.unwrap(arg0);
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		connection.abort(executor);

	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return connection.getNetworkTimeout();
	}

	@Override
	public String getSchema() throws SQLException {
		return connection.getSchema();
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		connection.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		connection.setSchema(schema);
	}

}