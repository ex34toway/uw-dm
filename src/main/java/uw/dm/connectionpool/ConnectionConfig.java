package uw.dm.connectionpool;

/**
 * 连接参数的配置类。
 * @author axeon
 *
 */
public class ConnectionConfig {

	/**
	 * 连接池名
	 */
	private String name;

	/**
	 * 连接池别名，支持使用","分割来实现多个别名
	 */
	private String[] aliasName;

	/**
	 * 数据库类型
	 */
	private String dbType;

	/**
	 * 数据库驱动
	 */
	private String driver;

	/**
	 * 服务器连接地址
	 */
	private String server;

	/**
	 * 登录用户名
	 */
	private String username;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 测试sql，用于测试连接是否可用
	 */
	private String testSQL;

	/**
	 * 最小连接数
	 */
	private int minConns;

	/**
	 * 最大连接数
	 */
	private int maxConns;

	/**
	 * 连接闲时超时秒数
	 */
	private int connIdleTimeout;

	/**
	 * 连接忙时超时秒数
	 */
	private int connBusyTimeout;

	/**
	 * 连接最大寿命秒数
	 */
	private int connMaxAge;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getAliasName() {
		return aliasName;
	}

	public void setAliasName(String[] aliasName) {
		this.aliasName = aliasName;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTestSQL() {
		return testSQL;
	}

	public void setTestSQL(String testSQL) {
		this.testSQL = testSQL;
	}

	public int getMinConns() {
		return minConns;
	}

	public void setMinConns(int minConns) {
		this.minConns = minConns;
	}

	public int getMaxConns() {
		return maxConns;
	}

	public void setMaxConns(int maxConns) {
		this.maxConns = maxConns;
	}

	public int getConnIdleTimeout() {
		return connIdleTimeout;
	}

	public void setConnIdleTimeout(int connIdleTimeout) {
		this.connIdleTimeout = connIdleTimeout;
	}

	public int getConnBusyTimeout() {
		return connBusyTimeout;
	}

	public void setConnBusyTimeout(int connBusyTimeout) {
		this.connBusyTimeout = connBusyTimeout;
	}

	public int getConnMaxAge() {
		return connMaxAge;
	}

	public void setConnMaxAge(int connMaxAge) {
		this.connMaxAge = connMaxAge;
	}

}
