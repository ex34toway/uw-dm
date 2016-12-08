package uw.dm.connectionpool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的数据库连接池。
 * 
 */
public class ConnectionPool {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

	/**
	 * 连接列表
	 */
	CopyOnWriteArrayList<ConnectionWrapper> connList = null;

	/**
	 * 连接池名
	 */
	String poolName;

	/**
	 * 数据库驱动
	 */
	String dbDriver;

	/**
	 * 数据库服务器
	 */
	String dbServer;

	/**
	 * 连接用户名
	 */
	String dbUsername;

	/**
	 * 连接密码
	 */
	String dbPassword;

	/**
	 * 日志路径
	 */
	String logPath;

	/**
	 * 测试sql
	 */
	String testSQL;

	/**
	 * 最小连接数
	 */
	int minConns;

	/**
	 * 最大连接数
	 */
	int maxConns;

	/**
	 * 静默超时
	 */
	long connIdleTimeout;

	/**
	 * 忙超时
	 */
	long connBusyTimeout;

	/**
	 * 连接寿命
	 */
	long connMaxAge;

	/**
	 * 连接池状态
	 */
	boolean available = false;

	/**
	 * 是否支持pooling，默认打开
	 */
	boolean enablePool = true;

	/**
	 * 保持最小连接数
	 */
	boolean keepMinConn = true;

	/**
	 * 数据类型
	 */
	String dbType = "";

	/**
	 * 日志文件名的日期格式
	 */
	SimpleDateFormat nameFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.S");

	/**
	 * 日志内容的日期格式
	 */
	SimpleDateFormat contentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 初始化连接池
	 * 
	 * @param poolName
	 * @throws IOException
	 */
	public ConnectionPool(String poolName) {
		this.poolName = poolName;
		ConnectionConfig config = ConnectionConfigManager.getConfig(this.poolName);
		// 驱动
		dbDriver = config.getDriver();
		// 服务器连接字符串
		dbServer = config.getServer();
		// 登陆用户名
		dbUsername = config.getUsername();
		// 登陆密码
		dbPassword = config.getPassword();
		// 测试sql
		testSQL = config.getTestSQL();
		// 最小连接数
		minConns = config.getMinConns();
		// 最大连接数
		maxConns = config.getMaxConns();
		// 空闲超时(秒钟)
		connIdleTimeout = config.getConnIdleTimeout();
		// 忙超时（秒钟）
		connBusyTimeout = config.getConnBusyTimeout();
		// 连接寿命（秒钟）
		connMaxAge = config.getConnMaxAge();
		// 数据类型
		dbType = config.getDbType();

		if (this.minConns < 1) {
			this.minConns = 1;
			config.setMinConns(minConns);
		}
		if (this.maxConns < 1) {
			this.maxConns = 1;
			config.setMaxConns(maxConns);
		}

		this.connMaxAge = connMaxAge * 1000;
		this.connIdleTimeout = connIdleTimeout * 1000;
		this.connBusyTimeout = connBusyTimeout * 1000;
		if (connIdleTimeout < 60000) {
			// 最小一分钟
			connIdleTimeout = 60000;
			config.setConnIdleTimeout(60);
		}

		if (connBusyTimeout < 30000) {
			// 最小30秒钟
			connBusyTimeout = 30000;
			config.setConnBusyTimeout(30);
		}

		if (connMaxAge < 600000) {
			// 最小10分钟
			connMaxAge = 600000;
			config.setConnMaxAge(60);
		}

		this.connList = new CopyOnWriteArrayList<ConnectionWrapper>();

		// 启动连接池
		if (enablePool)
			start();
	}

	public boolean isAvailable() {
		return this.available;
	}

	/**
	 * 启动连接池
	 */
	public synchronized void start() {
		this.available = true;
		if (logger.isDebugEnabled()) {
			logger.debug("Starting ConnectionPool[" + poolName + "]:");
			logger.debug("dbDriver = " + dbDriver);
			logger.debug("dbServer = " + dbServer);
			logger.debug("dbLogin = " + dbUsername);
			logger.debug("minConnections = " + minConns);
			logger.debug("maxConnections = " + maxConns);
			logger.debug("connIdleTimeout = " + connIdleTimeout / 1000 + " seconds");
			logger.debug("connBusyTimeout = " + connBusyTimeout / 1000 + " seconds");
			logger.debug("connMaxAge = " + connMaxAge / 1000 + " seconds");
		}
	}

	/**
	 * 原来对方法进行了同步，后来发现是一个性能问题。 现在把同步关掉，里面算法基本上是基于retry的了。
	 */
	public Connection getConnection() {
		Connection conn = null;
		if (enablePool) {
			if (available) {
				boolean gotOne = false;
				for (int outerloop = 0; outerloop < 200; outerloop++) {
					for (int loop = 0; loop < connList.size(); loop++) {
						ConnectionWrapper cw = null;
						try {
							// 防止get的时候刚好没有数值
							cw = connList.get(loop);
						} catch (Exception e) {
						}
						if (cw != null && cw.trySetUseStatus()) {
							if (cw.liteCheckAlive()) {
								conn = cw;
								gotOne = true;
								break;
							}
						}
					}
					if (gotOne) {
						break;
					} else if (outerloop > 1) {
						createConn("by can't get a idle connection!");
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
					// 超过30次找不到，报告连接耗尽信息
					if (outerloop > 30)
						logger.warn("-----> ConnectionPool[" + poolName
								+ "] Exhausted!  Will wait and try again in loop " + outerloop);
				}
			} else {
				logger.warn("ConnectionPool[" + poolName + "] Unsuccessful getConnection() request during destroy()");
			}
		} else {// 直接返回一个数据库连接
			try {
				Class.forName(dbDriver);
				conn = DriverManager.getConnection(dbServer, dbUsername, dbPassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	/**
	 * 创建一个新连接
	 * 
	 * @param i
	 * @throws SQLException
	 */
	synchronized void createConn(String reason) {
		if (connList.size() < maxConns) {
			try {
				Class.forName(dbDriver);
				// 获得一个封装对象
				ConnectionWrapper cw = new ConnectionWrapper(
						DriverManager.getConnection(dbServer, dbUsername, dbPassword), dbType, poolName);
				cw.setReadyStatus();
				connList.add(cw);
				logger.debug("ConnectionPool[" + poolName + "] Current connection size:" + connList.size()
						+ ", opening connection : " + cw.toString() + " " + reason);
			} catch (Exception e) {
				logger.debug("ConnectionPool[" + poolName + "] Attempt failed to create new connection ", e);
			}
		}
	}

	/**
	 * 销毁连接池
	 */
	public void destroy() {
		// Stop issuing connections
		available = false;

		// Close all connections, whether safe or not
		for (int i = connList.size() - 1; i >= 0; i--) {
			ConnectionWrapper cw = connList.get(i);
			cw.trueClose();
			connList.remove(cw);
		}

		if (connList.size() > 0) {
			// bt-test successful
			String msg = "ConnectionPool[" + poolName + "] Unsafe shutdown: Had to close " + connList.size()
					+ " active DB connections";
			logger.error(msg);
		}

	}

}
