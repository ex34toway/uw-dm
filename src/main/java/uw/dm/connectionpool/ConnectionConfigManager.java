package uw.dm.connectionpool;


import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 连接池参数的读取类
 */
public class ConnectionConfigManager {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionConfigManager.class);

	private static XMLConfiguration config;

	/**
	 * 连接池配置
	 */
	private static HashMap<String, ConnectionConfig> poolMap = new HashMap<String, ConnectionConfig>();

	/**
	 * 系统连接池配置。
	 */
	private static ConnectionConfig sysConfig = new ConnectionConfig();

	/**
	 * 默认的连接池名称，默认是第一个连接池。
	 */
	public static String POOL_DEFAULT_NAME;

	/**
	 * 系统连接池名称
	 */
	public static String POOL_SYS_NAME = "_SYS_CONFIG_POOL_" + System.currentTimeMillis();

	/**
	 * 采用静态载入的方式
	 */
	static {
		InputStream in = null;
		try {
			in = ConnectionConfigManager.class.getResourceAsStream("/connectionPool.xml");
			config = new XMLConfiguration();
			config.load(in, "UTF-8");
		} catch (Exception e) {
			logger.error("connectionPool.xml can't init!",e);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
		// 解析global.all全局设定
		ConnectionConfig global = new ConnectionConfig();
		global.setDriver(config.getString("global.all.driver"));
		global.setTestSQL(config.getString("global.all.testSQL"));
		try {
			global.setMinConns(config.getInt("global.all.minConns", 1));
		} catch (Exception e) {
		}
		try {
			global.setMaxConns(config.getInt("global.all.maxConns", 2));
		} catch (Exception e) {
		}
		try {
			global.setConnIdleTimeout(config.getInt("global.all.connIdleTimeout", 60));
		} catch (Exception e) {
		}
		try {
			global.setConnBusyTimeout(config.getInt("global.all.connBusyTimeout", 600));
		} catch (Exception e) {
		}
		try {
			global.setConnMaxAge(config.getInt("global.all.connMaxAge", 3600));
		} catch (Exception e) {
		}
		// 解析global.database全局设定
		HashMap<String, ConnectionConfig> dbMap = new HashMap<String, ConnectionConfig>();
		List<Object> dblist = config.getList("global.database[@type]");
		for (int i = 0; i < dblist.size(); i++) {
			String db = (String)dblist.get(i);
			ConnectionConfig conf = new ConnectionConfig();
			conf.setDriver(config.getString("global.database(" + i + ").driver"));
			conf.setTestSQL(config.getString("global.database(" + i + ").testSQL"));
			try {
				conf.setMinConns(config.getInt("global.database(" + i + ").minConns", global.getMinConns()));
			} catch (Exception e) {
			}
			try {
				conf.setMaxConns(config.getInt("global.database(" + i + ").maxConns", global.getMaxConns()));
			} catch (Exception e) {
			}
			try {
				conf.setConnIdleTimeout(config.getInt("global.database(" + i + ").connIdleTimeout", global.getConnIdleTimeout()));
			} catch (Exception e) {
			}
			try {
				conf.setConnBusyTimeout(config.getInt("global.database(" + i + ").connBusyTimeout", global.getConnBusyTimeout()));
			} catch (Exception e) {
			}
			try {
				conf.setConnMaxAge(config.getInt("global.database(" + i + ").connMaxAge", global.getConnMaxAge()));
			} catch (Exception e) {
			}
			dbMap.put(db, conf);
		}
		// 获得系统连接池配置
		if (true) {// 获得变量域保护
			// 获得dbType
			String dbType = config.getString("poolList.poolSys.dbType");
			ConnectionConfig dbconf = dbMap.get(dbType);
			if (dbconf == null) {
				dbconf = global;
			}
			// 设定
			sysConfig.setName(POOL_SYS_NAME);
			sysConfig.setDbType(dbType);
			sysConfig.setServer(config.getString("poolList.poolSys.server"));
			sysConfig.setUsername(config.getString("poolList.poolSys.username"));
			sysConfig.setPassword(config.getString("poolList.poolSys.password"));
			sysConfig.setDriver(config.getString("poolList.poolSys.driver", dbconf.getDriver()));
			sysConfig.setTestSQL(config.getString("poolList.poolSys.testSQL", dbconf.getTestSQL()));
			try {
				sysConfig.setMinConns(config.getInt("poolList.poolSys.minConns", dbconf.getMinConns()));
			} catch (Exception e) {
			}
			try {
				sysConfig.setMaxConns(config.getInt("poolList.poolSys.maxConns", dbconf.getMaxConns()));
			} catch (Exception e) {
			}
			try {
				sysConfig.setConnIdleTimeout(config.getInt("poolList.poolSys.connIdleTimeout", dbconf.getConnIdleTimeout()));
			} catch (Exception e) {
			}
			try {
				sysConfig.setConnBusyTimeout(config.getInt("poolList.poolSys.connBusyTimeout", dbconf.getConnBusyTimeout()));
			} catch (Exception e) {
			}
			try {
				sysConfig.setConnMaxAge(config.getInt("poolList.poolSys.connMaxAge", dbconf.getConnMaxAge()));
			} catch (Exception e) {
			}
			poolMap.put(POOL_SYS_NAME, sysConfig);
		}
		// 循环解析连接池配置。
		List<Object> plist = config.getList("poolList.pool[@name]");
		for (int i = 0; i < plist.size(); i++) {
			String name = (String)plist.get(i);
			ConnectionConfig conf = new ConnectionConfig();
			// 获得dbType
			String dbType = config.getString("poolList.pool(" + i + ").dbType");
			ConnectionConfig dbconf = dbMap.get(dbType);
			if (dbconf == null) {
				dbconf = global;
			}
			// 设定
			conf.setName(name);
			conf.setDbType(dbType);
			conf.setServer(config.getString("poolList.pool(" + i + ").server"));
			conf.setUsername(config.getString("poolList.pool(" + i + ").username"));
			conf.setPassword(config.getString("poolList.pool(" + i + ").password"));
			conf.setDriver(config.getString("poolList.pool(" + i + ").driver", dbconf.getDriver()));
			conf.setTestSQL(config.getString("poolList.pool(" + i + ").testSQL", dbconf.getTestSQL()));
			try {
				conf.setMinConns(config.getInt("poolList.pool(" + i + ").minConns", dbconf.getMinConns()));
			} catch (Exception e) {
			}
			try {
				conf.setMaxConns(config.getInt("poolList.pool(" + i + ").maxConns", dbconf.getMaxConns()));
			} catch (Exception e) {
			}
			try {
				conf.setConnIdleTimeout(config.getInt("poolList.pool(" + i + ").connIdleTimeout", dbconf.getConnIdleTimeout()));
			} catch (Exception e) {
			}
			try {
				conf.setConnBusyTimeout(config.getInt("poolList.pool(" + i + ").connBusyTimeout", dbconf.getConnBusyTimeout()));
			} catch (Exception e) {
			}
			try {
				conf.setConnMaxAge(config.getInt("poolList.pool(" + i + ").connMaxAge", dbconf.getConnMaxAge()));
			} catch (Exception e) {
			}
			// 放入缓存中。
			poolMap.put(name, conf);
		}
		if (plist.size() > 0) {
			POOL_DEFAULT_NAME = (String)plist.get(0);
		}
	}

	public static void main(String[] args) {
//		for (int i = 0; i < 10; i++) {
//			System.out.println(getPoolNameFromGroup("mysqlDB"));
//		}
	}

	/**
	 * 获得所有连接池列表
	 * 
	 * @return
	 */
	public static List<Object> getPoolNames() {
		return config.getList("poolList.pool[@name]");
	}


	/**
	 * 根据连接池名称获得配置信息。
	 * 
	 * @param poolName
	 * @return
	 */
	public static ConnectionConfig getConfig(String poolName) {
		return poolMap.get(poolName);
	}

	/**
	 * 获得一个指定的系统连接池配置
	 * 
	 * @return
	 */
	public static ConnectionConfig getSysConfig() {
		return sysConfig;
	}

	/**
	 * 根据连接池名获得数据库类型
	 * 
	 * @param poolName
	 * @return
	 */
	public static String getDbType(String poolName) {
		// 需要处理连接组的情况
		// String pn = getPoolNameFromGroup(poolName);
		ConnectionConfig conf = poolMap.get(poolName);
		if (conf != null)
			return conf.getDbType();
		else
			return null;
	}

}