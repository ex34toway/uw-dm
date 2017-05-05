package uw.dm.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库连接管理器
 */
public class ConnectionManager {
	
	/**
	 * 连接池缓存表
	 */
	private static Map<String, ConnectionPool> poolMap = new ConcurrentHashMap<String, ConnectionPool>();

	static {
		ConnectionPoolMonitor.start();
	}

	/**
	 * 获得一个connection，在poolList中排名第一的为默认连接。
	 * 
	 * @return
	 */
	public static Connection getConnection() throws SQLException {
		return getConnection(ConnectionConfigManager.POOL_DEFAULT_NAME);
	}

	/**
	 * 获得连接池的集合
	 */
	static HashSet<ConnectionPool> getConnectionPoolSet() {
		HashSet<ConnectionPool> set = new HashSet<ConnectionPool>();
		set.addAll(poolMap.values());
		return set;
	}

	/**
	 * 获得一个系统连接。
	 * 
	 * @return
	 */
	public static Connection getSysConnection() throws SQLException {
		return getConnection(ConnectionConfigManager.POOL_SYS_NAME);
	}

	/**
	 * 获得一个新连接
	 */
	public static Connection getConnection(String poolName) throws SQLException {
//		long start = System.currentTimeMillis();
		Connection con = null;
		ConnectionPool connpool = getConnectionPool(poolName);
		if (connpool == null) {
			connpool = initConnectionPool(poolName);
		}
		if (connpool == null) {
			throw new SQLException("ConnectionManager.getConnection() failed to init connectionPool[" + poolName+"]");
		}
		
		con = connpool.getConnection();
		if (con == null)
		{
			throw new SQLException("ConnectionManager.getConnection() failed to obtain a connection in connectionPool[" + poolName+"]");
		}
//		logger.error("***连接池{}耗费时间为{}ms",poolName,(System.currentTimeMillis()-start));
		return con;
	}

	/**
	 * 销毁一个连接池
	 * 
	 * @param poolName
	 */
	public static synchronized void destroyConnectionPool(String poolName) {
		ConnectionPool cp = poolMap.get(poolName);
		if (cp != null) {
			poolMap.remove(poolName);
			cp.destroy();
		}
	}

	/**
	 * 销毁全部连接池
	 * 
	 * @param poolName
	 */
	public static synchronized void destroyAllConnectionPool() {
		ConnectionPoolMonitor.stop();
		for (String poolName : poolMap.keySet()) {
			destroyConnectionPool(poolName);
		}
	}

	/**
	 * 获得连接池。
	 * 
	 * @param configName
	 *            String
	 * @return Connection
	 */
	private static ConnectionPool getConnectionPool(String poolName) {
		return poolMap.get(poolName);
	}

	/**
	 * 初始化连接池。
	 * 
	 * @param configName
	 *            String
	 * @return Connection
	 */
	private static synchronized ConnectionPool initConnectionPool(String poolName) {
		ConnectionPool connpool = getConnectionPool(poolName);
		if (connpool!=null) {
			ConnectionConfig cc = ConnectionConfigManager.getConfig(poolName);
			if (cc != null) {
				connpool = new ConnectionPool(cc.getName());
				poolMap.put(poolName, connpool);
			}
		}
		return connpool;
	}

}
