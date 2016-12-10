package uw.dm.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uw.dm.DAOFactory;
import uw.dm.TransactionException;

/**
 * 数据清理任务。
 * 
 * @author axeon
 * 
 */
public class StatsCleanDataThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(StatsCleanDataThread.class);

	private DAOFactory dao = DAOFactory.getInstance();

	/**
	 * 获得当前的表Set
	 */
	private HashSet<String> getCurrentTableSet() {
		HashSet<String> set = new HashSet<String>();
		List<String> list = null;
		try {
			list = dao.queryForSingleList(dao.getConnectionName("dm_stats", "all"), String.class, "show tables");
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

	public void run() {
		logger.info("StatsCleanData is run start!");
		HashSet<String> tset = getCurrentTableSet();
		ArrayList<String> list = new ArrayList<String>(tset);
		// 自然顺序排序
		Collections.sort(list);
		Collections.reverse(list);
		// 保留100天数据，假设
		int start = 100;
		// 循环删除过期数据
		for (int i = start; i < list.size(); i++) {
			try {
				dao.executeCommand("DROP TABLE IF EXISTS " + list.get(i));
				logger.info("删除数据表" + list.get(i));
			} catch (TransactionException e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("StatsCleanData is run end!");

	}

	public static void main(String[] args) {
		new StatsCleanDataThread().run();
	}

}
