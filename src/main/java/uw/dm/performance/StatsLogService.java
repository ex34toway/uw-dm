package uw.dm.performance;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uw.dm.TransactionException;

/**
 * 性能计数器。 将性能数据输出到mysql中。
 * 
 * @author axeon
 * 
 */
public class StatsLogService {

	private static final Logger logger = LoggerFactory.getLogger(StatsLogService.class);

	/**
	 * 是否已经启动。
	 */
	private static boolean isStarted = false;

	/**
	 * 启动锁。
	 */
	private static volatile Object locker = new Object();

	/**
	 * 任务定时器
	 */
	private static ScheduledExecutorService scheduler = null;
	/**
	 * 数据清除任务。
	 */
	private static StatsCleanDataThread cleanTask = new StatsCleanDataThread();

	/**
	 * 表创建检查任务。
	 */
	private static StatsCheckTableThread checkTask = new StatsCheckTableThread();

	/**
	 * 后台批量写任务
	 */
	private static StatsLogWriteTask task = new StatsLogWriteTask();

	/**
	 * 后台批量写时间间隔
	 */
	private static long TIME_INTERVAL = 10;

	/**
	 * 启动 任务
	 */
	public static void start() {
		if (!isStarted) {
			synchronized (locker) {
				if (!isStarted) {
					isStarted = true;
					scheduler = Executors.newScheduledThreadPool(3, new ThreadFactory() {

						@Override
						public Thread newThread(Runnable task) {
							Thread t = new Thread(task);
							t.setDaemon(true);
							t.setName("StatsLogService-%d");
							return null;
						}

					});
					// 登录退出记录，按照固定间隔执行。
					scheduler.scheduleAtFixedRate(task, 0, TIME_INTERVAL, TimeUnit.SECONDS);
					// 表创建检查任务，每天执行一次。
					scheduler.scheduleAtFixedRate(checkTask, 0, 3600, TimeUnit.SECONDS);
					// 表清除任务，每天执行一次。
					scheduler.scheduleAtFixedRate(cleanTask, 86400, 86400, TimeUnit.SECONDS);
					logger.info("启动性能记录服务");
				}
			}
		}
	}

	/**
	 * 停止任务
	 */
	public static void stop() {
		if (isStarted) {
			synchronized (locker) {
				if (isStarted) {
					isStarted = false;
					if (scheduler != null) {
						scheduler.shutdown();
						scheduler = null;
					}
					logger.info("停止性能记录服务");
				}
			}
		}
	}

	/**
	 * 是否已启动
	 * 
	 * @return
	 */
	public static boolean isStarted() {
		return isStarted;
	}

	/**
	 * 记录性能参数
	 * 
	 * @param module
	 *            模块名
	 * @param page
	 *            页面名
	 * @param useTime
	 *            使用时间
	 * @param slist
	 *            dao返回的sql执行结果统计
	 */
	public static void logStats(SqlExecuteStats ses) {
		if (isStarted) {
			task.writeStats(ses);
		}
	}

	public static void main(String[] args) throws TransactionException {

	}

}
