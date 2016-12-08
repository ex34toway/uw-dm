package uw.dm.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uw.dm.DataSet;
import uw.dm.TransactionException;
import uw.dm.connectionpool.ConnectionPool;
import uw.dm.connectionpool.ConnectionWrapper;
import uw.dm.dialect.Dialect;
import uw.dm.dialect.DialectManager;
import uw.dm.util.DmReflectUtils;

/**
 * 为了更为高效的执行数据库命令，是该类产生的根本原因。 具体使用请自行参照源代码。
 */
public class SQLCommandImpl {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

	/**
	 * 获得单个数值。
	 * 
	 * @param dao
	 * @param connName
	 * @param selectsql
	 * @return
	 * @throws TransactionException
	 */
	public static final Object selectForSingleValue(DAOFactoryImpl dao, String connName, String selectsql,
			Object[] paramList) throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		if (connName == null) {
			connName = SQLUtils.getConnNameFromSQL(selectsql);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		Object value = null;
		try {
			con = dao.getTransactionController().getConnection(connName);
			pstmt = con.prepareStatement(selectsql);
			if (paramList != null && paramList.length > 0) {
				for (int i = 0; i < paramList.length; i++) {
					DmReflectUtils.CommandUpdateReflect(pstmt, i + 1, paramList[i]);
				}
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				switch (rs.getMetaData().getColumnType(1)) {
				case Types.NUMERIC:
					value = rs.getBigDecimal(1);
					break;
				case Types.VARCHAR:
					value = rs.getString(1);
					break;
				case Types.CLOB:
					value = rs.getString(1);
					break;
				case Types.DATE:
					value = rs.getTimestamp(1);
					break;
				case Types.TIME:
					value = rs.getTimestamp(1);
					break;
				case Types.TIMESTAMP:
					value = rs.getTimestamp(1);
					break;
				case Types.BIGINT:
					value = rs.getLong(1);
					break;
				case Types.INTEGER:
					value = rs.getInt(1);
					break;
				case Types.SMALLINT:
					value = rs.getInt(1);
					break;
				case Types.TINYINT:
					value = rs.getInt(1);
					break;
				case Types.FLOAT:
					value = rs.getFloat(1);
					break;
				case Types.DOUBLE:
					value = rs.getDouble(1);
					break;
				case Types.BIT:
					value = rs.getInt(1);
					break;
				default:
					value = rs.getObject(1);
				}
			}
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in SQLCommandImpl.selectForSingleValue()", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, selectsql, Arrays.toString(paramList), time, exception);
		return value;
	}

	/**
	 * 获得单列数据列表。
	 * 
	 * @param dao
	 * @param connName
	 * @param selectsql
	 * @return
	 * @throws TransactionException
	 */
	public static final List<Object> selectForSingleList(DAOFactoryImpl dao, String connName, String selectsql,
			Object[] paramList, int startIndex, int resultNum) throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		if (connName == null) {
			connName = SQLUtils.getConnNameFromSQL(selectsql);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ArrayList<Object> list = new ArrayList<Object>();
		Object[] po = null;
		try {
			con = dao.getTransactionController().getConnection(connName);
			if (resultNum > 0 && startIndex >= 0) {
				Dialect dialect = DialectManager.getDialect(((ConnectionWrapper) con).getDbType());
				po = dialect.getPagedSQL(selectsql, startIndex, resultNum);
				selectsql = po[0].toString();
			}
			pstmt = con.prepareStatement(selectsql);
			int i = 0;
			if (paramList != null && paramList.length > 0) {
				for (i = 0; i < paramList.length; i++) {
					DmReflectUtils.CommandUpdateReflect(pstmt, i + 1, paramList[i]);
				}
			}

			if (resultNum > 0 && startIndex >= 0) {
				pstmt.setInt(i + 1, (Integer) po[1]);
				pstmt.setInt(i + 2, (Integer) po[2]);
			}
			ResultSet rs = pstmt.executeQuery();
			switch (rs.getMetaData().getColumnType(1)) {

			case Types.NUMERIC:
				while (rs.next()) {
					list.add(rs.getBigDecimal(1));
				}
				break;
			case Types.VARCHAR:
				while (rs.next()) {
					list.add(rs.getString(1));
				}
				break;
			case Types.CLOB:
				while (rs.next()) {
					list.add(rs.getString(1));
				}
				break;
			case Types.DATE:
				while (rs.next()) {
					list.add(rs.getTimestamp(1));
				}
				break;
			case Types.TIME:
				while (rs.next()) {
					list.add(rs.getTimestamp(1));
				}
				break;
			case Types.TIMESTAMP:
				while (rs.next()) {
					list.add(rs.getTimestamp(1));
				}
				break;
			case Types.BIGINT:
				while (rs.next()) {
					list.add(rs.getLong(1));
				}
				break;
			case Types.INTEGER:
				while (rs.next()) {
					list.add(rs.getInt(1));
				}
				break;
			case Types.SMALLINT:
				while (rs.next()) {
					list.add(rs.getInt(1));
				}
				break;
			case Types.TINYINT:
				while (rs.next()) {
					list.add(rs.getInt(1));
				}
				break;
			case Types.FLOAT:
				while (rs.next()) {
					list.add(rs.getFloat(1));
				}
				break;
			case Types.DOUBLE:
				while (rs.next()) {
					list.add(rs.getDouble(1));
				}
				break;
			case Types.BIT:
				while (rs.next()) {
					list.add(rs.getInt(1));
				}
				break;
			default:
				while (rs.next()) {
					list.add(rs.getObject(1));
				}
			}
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in SQLCommandImpl.selectForSingleList()", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, selectsql, Arrays.toString(paramList), time, exception);
		return list;
	}

	/**
	 * 获得以DataSet为结果的数据集合。
	 * 
	 * @param dao
	 * @param connName
	 * @param selectsql
	 * @return
	 * @throws TransactionException
	 */
	public static final DataSet selectForDataSet(DAOFactoryImpl dao, String connName, String selectsql,
			Object[] paramList, int startIndex, int resultNum, boolean autoCount) throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		if (connName == null) {
			connName = SQLUtils.getConnNameFromSQL(selectsql);
		}
		int allsize = 0;

		if (autoCount) {
			String countsql = "select count(1) from (" + selectsql + ") must_alias";
			allsize = Integer.parseInt(
					SQLCommandImpl.selectForSingleValue(dao, connName, countsql, paramList).toString());
		}
		
		DataSetImpl ds = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		Object[] po = null;
		try {
			con = dao.getTransactionController().getConnection(connName);
			if (resultNum > 0 && startIndex >= 0) {
				Dialect dialect = DialectManager.getDialect(((ConnectionWrapper) con).getDbType());
				po = dialect.getPagedSQL(selectsql, startIndex, resultNum);
				selectsql = po[0].toString();
			}

			pstmt = con.prepareStatement(selectsql);
			int i = 0;
			if (paramList != null && paramList.length > 0) {
				for (i = 0; i < paramList.length; i++) {
					DmReflectUtils.CommandUpdateReflect(pstmt, i + 1, paramList[i]);
				}
			}
			if (resultNum > 0 && startIndex >= 0) {
				pstmt.setInt(i + 1, (Integer) po[1]);
				pstmt.setInt(i + 2, (Integer) po[2]);
			}
			ResultSet rs = pstmt.executeQuery();
			ds = new DataSetImpl(rs, startIndex, resultNum, allsize);
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in SQLCommandImpl.selectForDataSet()", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, selectsql, Arrays.toString(paramList), time, exception);
		return ds;
	}

	/**
	 * 执行任意sql
	 * 
	 * @param dao
	 * @param connName
	 * @param selectsql
	 * @return
	 * @throws TransactionException
	 */
	public static final int executeSQL(DAOFactoryImpl dao, String connName, String executesql, Object[] paramList)
			throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		
		if (connName == null) {
			connName = SQLUtils.getConnNameFromSQL(executesql);
		}

		Connection con = null;
		PreparedStatement pstmt = null;
		int effect = 0;
		try {
			con = dao.getTransactionController().getConnection(connName);
			pstmt = dao.getBatchUpdateController().prepareStatement(con, executesql);
			if (paramList != null && paramList.length > 0) {
				for (int i = 0; i < paramList.length; i++) {
					DmReflectUtils.CommandUpdateReflect(pstmt, i + 1, paramList[i]);
				}
			}
			if (dao.getBatchUpdateController().getBatchStatus())
				pstmt.addBatch();
			else
				effect = pstmt.executeUpdate();
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in SQLCommandImpl.java:executeSQLCommand()", e);
		} finally {
			if (!dao.getBatchUpdateController().getBatchStatus() && con != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (dao.getTransactionController().isAutoCommit() && con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, executesql, Arrays.toString(paramList), time, exception);
		return effect;
	}

}
