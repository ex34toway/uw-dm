package uw.dm.performance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uw.dm.DAOFactory;
import uw.dm.SequenceManager;
import uw.dm.util.DmValueUtils;

/**
 * 性能日志写入任务。
 * 
 * @author axeon
 * 
 */
public class StatsLogWriteTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(StatsLogWriteTask.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);

	private ArrayList<SqlExecuteStats> datalist = new ArrayList<SqlExecuteStats>();

	/**
	 * pageLog的写入锁
	 */
	private ReentrantLock lock = new ReentrantLock();


	private DAOFactory dao = DAOFactory.getInstance();

	public StatsLogWriteTask() {
	}

	/**
	 * 发送pageDbLog
	 * 
	 * @param poa
	 */
	public void writeStats(SqlExecuteStats ses) {
		lock.lock();
		try {
			this.datalist.add(ses);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获得pageLog列表，并重新构造列表
	 * 
	 * @return
	 */
	private ArrayList<SqlExecuteStats> getStatsList() {
		ArrayList<SqlExecuteStats> list = null;
		lock.lock();
		try {
			list = datalist;
			datalist = new ArrayList<SqlExecuteStats>();
		} finally {
			lock.unlock();
		}
		return list;
	}

	/**
	 * 执行pageDb部分数据库插入
	 */
	private void writeStatsList() {
		ArrayList<SqlExecuteStats> list = this.getStatsList();
		if (list.size() ==0 )
			return;
		String tableName = "dm_stats_"+dateFormat.format(new java.util.Date());
		Connection conn = null;
		PreparedStatement pstmt = null;
		String pdsql = "INSERT INTO " + tableName + "(conn_name,sql_info,sql_param,use_time,exception,exe_date) " + "values "
				+ "(?,?,?,?,?,?) ";
		try {
			conn = dao.getConnection(tableName, "write");
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(pdsql);
			for (int i = 0; i < list.size(); i++) {
				SqlExecuteStats ss = list.get(i);
				pstmt.setString(1, ss.getConnName());
				pstmt.setString(2, ss.getSql());
				pstmt.setString(3, ss.getParam());
				pstmt.setInt(4, (int) ss.getTime());
				pstmt.setString(5, ss.getException());
				pstmt.setTimestamp(6, DmValueUtils.dateToTimestamp(ss.getActionDate()));
				pstmt.addBatch();
				if ((i + 1) % 100 == 0 && i > 0) {
					// 每隔100次自动提交
					pstmt.executeBatch();
				}
			}
			// 剩余部分也要执行提交。
			if (list.size() % 100 > 0)
				pstmt.executeBatch();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public void run() {
		writeStatsList();
	}



}
