package uw.dm.performance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uw.dm.DAOFactory;
import uw.dm.TransactionException;

/**
 * 检查表的创建情况。
 * 
 * @author axeon
 * 
 */
public class StatsCheckTableThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(StatsCheckTableThread.class);

	private DAOFactory dao = DAOFactory.getInstance();

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);

	public static long TODAY_VALUE = 0l;

	public static long TOMORROW_VALUE = 0l;

	public static String TODAY_TEXT = "";

	public static String TOMORROW_TEXT = "";

	public StatsCheckTableThread() {

	}

	private void init() {
		// 计算日历
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		// 当天开始
		TODAY_VALUE = cal.getTimeInMillis();
		TODAY_TEXT = dateFormat.format(cal.getTime());
		// 第二天开始
		cal.add(Calendar.DAY_OF_MONTH, 1);
		TOMORROW_VALUE = cal.getTimeInMillis();
		TOMORROW_TEXT = dateFormat.format(cal.getTime());
	}

	/**
	 * 获得当前的表Set
	 */
	@SuppressWarnings("unchecked")
	private HashSet<String> getCurrentTableSet() {
		HashSet<String> set = new HashSet<String>();
		List<String> list = null;
		try {
			list = (List<String>) dao.queryForSingleList(dao.getConnectionName("dm_stats", "all"), "show tables");
			if (list != null) {
				for (String s : list) {
					if (s.startsWith("dm_stats_"))
						set.add(s);
				}
			}
		} catch (TransactionException e) {
			logger.error(e.getMessage());
		}
		return set;
	}

	/**
	 * 检查是否应该新建表
	 */
	private void checkForCreateTable() {
		try {
			init();
			HashSet<String> tSet = getCurrentTableSet();
			String tableName = "dm_stats_" + TODAY_TEXT;
			if (!tSet.contains(tableName)) {
				String sql = "create table " + tableName + "(\n" + "id bigint(20) NOT NULL AUTO_INCREMENT,\n"
						+ "conn_name varchar(100) DEFAULT NULL,\n" + "sql_info varchar(1000) DEFAULT NULL,\n"
						+ "sql_param varchar(1000) DEFAULT NULL,\n" + "use_time int(11) DEFAULT NULL,\n"
						+ "exception varchar(200) DEFAULT NULL,\n" + "exe_date datetime DEFAULT CURRENT_TIMESTAMP,\n"
						+ "PRIMARY KEY (id)\n" + ")";
				dao.executeCommand(dao.getConnectionName(tableName, "all"), sql);
				logger.info("创建数据表" + tableName);
			}
			tableName = "dm_stats_" + TOMORROW_TEXT;
			if (!tSet.contains(tableName)) {
				String sql = "create table " + tableName + "(\n" + "id bigint(20) NOT NULL AUTO_INCREMENT,\n"
						+ "conn_name varchar(100) DEFAULT NULL,\n" + "sql_info varchar(1000) DEFAULT NULL,\n"
						+ "sql_param varchar(1000) DEFAULT NULL,\n" + "use_time int(11) DEFAULT NULL,\n"
						+ "exception varchar(200) DEFAULT NULL,\n" + "exe_date datetime DEFAULT CURRENT_TIMESTAMP,\n"
						+ "PRIMARY KEY (id)\n" + ")";
				dao.executeCommand(dao.getConnectionName(tableName, "all"), sql);
				logger.info("创建数据表" + tableName);
			}
		} catch (TransactionException e) {
			logger.error(e.getMessage());
		}
	}

	public void run() {
		logger.info("StatsCheckTableThread is run start!");
		checkForCreateTable();
		logger.info("StatsCheckTableThread is run end!");
	}

}
