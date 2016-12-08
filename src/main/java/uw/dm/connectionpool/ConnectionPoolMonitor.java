package uw.dm.connectionpool;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接池监控
 * 
 * @author axeon
 * 
 */
class ConnectionPoolMonitor {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolMonitor.class);

	/**
	 * 扫描时间间隔
	 */
	private static long TIME_INTERVAL = 20000;

	/**
	 * 后台检查线程。
	 */
	private static ConnCheckThread checkThread = null;


	/**
	 * 开始服务
	 */
	static void start() {
		checkThread = new ConnCheckThread();
		checkThread.init();
		checkThread.setDaemon(true);
		checkThread.start();
	}

	/**
	 * 停止服务
	 */
	static void stop() {
		checkThread.destroy();
	}

	/**
	 * 后台检查线程。
	 * 
	 * @author axeon
	 * 
	 */
	private static class ConnCheckThread extends Thread {

		/**
		 * 运行标记。
		 */
		private boolean isRunning = false;

		/**
		 * 初始化
		 */
		public void init() {
			isRunning = true;
		}

		/**
		 * 销毁标记
		 */
		public void destroy() {
			isRunning = false;
		}

		@Override
		public void run() {
			while (isRunning) {
				// 循环遍历所有的连接池
				HashSet<ConnectionPool> poolSet = ConnectionManager.getConnectionPoolSet();
				for (ConnectionPool pool : poolSet) {
					if (!isRunning) {
						break;
					}
					ConnectionWrapper cw = null;
					// 空闲数量
					int idleConnCount = 0;
					// 循环链接，并检查。
					for (int i = pool.connList.size() - 1; i >= 0; i--) {
						if (!isRunning) {
							break;
						}
						cw = pool.connList.get(i);
						long now = System.currentTimeMillis();
						long age = now - cw.createTime;// 连接全寿命
						long busyTimeout = -1;
						long idleTimeout = -1;
						if (cw.busyTime > 0)
							busyTimeout = now - cw.busyTime;// 连接超时未关闭时间
						if (cw.idleTime > 0)
							idleTimeout = now - cw.idleTime;// 空闲超时
						// 使用中并且未超时的链接不用检测
						if (cw.getStatus() == 1 && busyTimeout < pool.connBusyTimeout) {
							// 正在忙呢
							continue;
						} else {
							idleConnCount++;
						}
						// 处理各种超时类的问题
						if (age > pool.connMaxAge) {// 超过最大寿命
							logger.debug(" ***** ConnectionPool[" + pool.poolName + "] Recycling connection "
									+ String.valueOf(cw) + " by over connection max age!");
							if (cw.trySetTestStatus()) {
								pool.connList.remove(cw);
								cw.setUnuseStatus();
								cw.trueClose();// 真正的关闭掉连接
							}
							cw = null;
						} else if (busyTimeout > pool.connBusyTimeout) {// busy
							// timeout超时
							logger.error("!!!!! ConnectionPool[" + pool.poolName + "] Connection " + String.valueOf(cw)
									+ " locked detected!\n", cw.ex);
							logger.debug(" ***** ConnectionPool[" + pool.poolName + "] Recycling connection "
									+ String.valueOf(cw) + " by over connection busy timout!");
							if (cw.trySetTestStatus()) {
								pool.connList.remove(cw);
								cw.setUnuseStatus();
								cw.trueClose();// 真正的关闭掉连接
							}
							cw = null;
						} else if (idleTimeout > pool.connIdleTimeout) {// idle超时
							logger.debug(" ***** ConnectionPool[" + pool.poolName + "] Recycling connection "
									+ String.valueOf(cw) + " by over connection idle timout!");
							if (cw.trySetTestStatus()) {
								pool.connList.remove(cw);
								cw.setUnuseStatus();
								cw.trueClose();// 真正的关闭掉连接
							}
							cw = null;
						} else if (((double) idleConnCount / (double) pool.connList.size()) > 0.5d
								&& (pool.keepMinConn && pool.connList.size() > pool.minConns)) {// 每次检查，空闲数量不能超过50%，超过部分就要考虑关掉。
							logger.debug(" ***** ConnectionPool[" + pool.poolName + "] Recycling connection "
									+ String.valueOf(cw) + " by idle count percent over 50%!");
							if (cw.trySetTestStatus()) {
								pool.connList.remove(cw);
								cw.setUnuseStatus();
								cw.trueClose();// 真正的关闭掉连接
							}
							cw = null;
						}
						if (cw != null) {
							if (cw.trySetTestStatus()) {
								// 进入检测设定
								Statement stmt = null;
								// 处理异常类型问题
								try {
									// 清除sql warning
									SQLWarning currSQLWarning = cw.getWarnings();
									if (currSQLWarning != null) {
										logger.warn("ConnectionPool[" + pool.poolName + "] Warnings on connection "
												+ String.valueOf(i) + " " + currSQLWarning);
										cw.clearWarnings();
									}
									// 检测conn是否可用
									stmt = cw.createStatement();
									if (pool.testSQL != null && !pool.testSQL.equals(""))
										stmt.execute(pool.testSQL);
									cw.setTestSuccessStatus();
								} catch (SQLException e) {
									logger.error(" ***** ConnectionPool[" + pool.poolName + "] Recycling connection "
											+ String.valueOf(cw) + e.getMessage());
									pool.connList.remove(cw);
									cw.setUnuseStatus();
									cw.trueClose();// 真正的关闭掉连接
									cw = null;
								} finally {
									try {
										if (stmt != null) {
											stmt.close();
										}
									} catch (SQLException e1) {
									}
								}
							} else {
								continue;
							}
						}
					}

					if (!isRunning) {
						break;
					}
					// 为了节省连接，不再自动创建
					// 创建新连接。
					if (pool.connList.size() < pool.minConns) {
						int size = pool.minConns - pool.connList.size();
						for (int i = 0; i < size; i++) {
							if (!isRunning) {
								break;
							}
							pool.createConn("by keep minConns");
						}
					}
				}
				if (!isRunning) {
					break;
				}
				try {
					Thread.sleep(TIME_INTERVAL);
				} catch (InterruptedException e) {
					return;
				}
			}

		}
	}
}
