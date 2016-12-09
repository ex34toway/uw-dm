package uw.dm.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uw.dm.BatchupdateManager;
import uw.dm.DAOFactory;
import uw.dm.DataEntity;
import uw.dm.DataList;
import uw.dm.DataSet;
import uw.dm.SequenceManager;
import uw.dm.TransactionException;
import uw.dm.TransactionManager;
import uw.dm.performance.SqlExecuteStats;
import uw.dm.performance.StatsLogService;

/**
 * 
 * DAOFactory实现类
 * 
 * @author axeon
 * 
 */
public class DAOFactoryImpl extends DAOFactory {

	/**
	 * 批量更新实例
	 */
	private BatchupdateManagerImpl batchupdate;

	/**
	 * 事务处理实例
	 */
	private TransactionManagerImpl transaction;

	/**
	 * 统计信息。
	 */
	private List<SqlExecuteStats> statsList = null;

	/**
	 * 获得一个DAOFactory的实现
	 */
	public DAOFactoryImpl() {
		transaction = new TransactionManagerImpl();
		batchupdate = new BatchupdateManagerImpl();
	}

	public void disableSqlExecuteStats() {
		statsList = null;
	}

	public void enableSqlExecuteStats() {
		statsList = new ArrayList<SqlExecuteStats>();
	}

	/**
	 * 添加性能统计数据
	 * 
	 * @param connName
	 *            连接名称
	 * @param sql
	 *            sql
	 * @param param
	 *            sql参数
	 * @param time
	 *            执行时间
	 * @param exception
	 *            异常信息
	 */
	protected void addSqlExecuteStats(String connName, String sql, String param, long time, String exception) {
		SqlExecuteStats ses = new SqlExecuteStats(connName, sql, param, time, exception);
		if (statsList != null) {
			statsList.add(ses);
		}
		StatsLogService.logStats(ses);
	}

	/**
	 * 获得sql性能统计数据列表
	 * 
	 * @return
	 */
	public List<SqlExecuteStats> getSqlExecuteStatsList() {
		return statsList;
	}

	/**
	 * 获得数据库调用次数
	 * 
	 * @return
	 */
	public int getInvokeCount() {
		return transaction.getInvokeCount();
	}

	/**
	 * 使用固定连接名获得一个DAOFactory实现
	 * 
	 * @param connname
	 */
	public DAOFactoryImpl(String connname) {
		transaction = new TransactionManagerImpl(connname);
		batchupdate = new BatchupdateManagerImpl();
	}

	/**
	 * 开始批量更新
	 */
	public BatchupdateManager beginBatchupdate() throws TransactionException {
		this.batchupdate.startBatchUpdate();
		return this.batchupdate;
	}

	/**
	 * 开始处理事务
	 */
	public TransactionManager beginTransaction() throws TransactionException {
		this.transaction.startTransaction();
		return this.transaction;
	}

	/**
	 * 执行一个命令
	 */

	public int executeCommand(String sql) throws TransactionException {
		return executeCommand(null, sql, (Object[]) null);
	}

	public int executeCommand(String sql, Object[] paramList) throws TransactionException {
		int ret = executeCommand(null, sql, paramList);
		return ret;
	}

	public int executeCommand(String connName, String sql) throws TransactionException {
		return executeCommand(connName, sql, (Object[]) null);
	}

	public int executeCommand(String connName, String sql, Object[] paramList) throws TransactionException {
		int ret = SQLCommandImpl.executeSQL(this, connName, sql, paramList);
		return ret;
	}

	/**
	 * 获得控制器
	 * 
	 * @return
	 */
	public BatchupdateManagerImpl getBatchUpdateController() {
		return batchupdate;
	}

	/**
	 * 获得批量更新管理器。
	 * 
	 * @return
	 */
	public BatchupdateManager getBatchUpdateManager() {
		return batchupdate;
	}

	/**
	 * 提供连接名，获得一个连接
	 */
	public Connection getConnection(String configName) throws SQLException {
		return transaction.getConnection(configName);
	}

	/**
	 * 根据配置中表和访问类型获得一个数据库链接。
	 */
	public Connection getConnection(String table, String access) throws SQLException {
		return transaction.getConnection(table, access);
	}

	/**
	 * 根据配置中表和访问类型获得连接池名
	 */
	public String getConnectionName(String table, String access) {
		return transaction.getConnName(table, access);
	}

	/**
	 * 获得sequnce序列
	 */
	public long getSequenceId(String tableName) {
		long sequence = -1;
		sequence = SequenceManager.nextId(tableName);
		return sequence;
	}

	@Override
	public long getSequenceId(Class<?> cls) {
		String tableName = EntityCommandImpl.getTableName(cls);
		if (tableName != null) {
			return getSequenceId(tableName);
		} else {
			return -1;
		}
	}

	/**
	 * 获得事务控制器。
	 * 
	 * @return
	 */
	public TransactionManagerImpl getTransactionController() {
		return transaction;
	}

	/**
	 * 获得事务管理器
	 * 
	 * @return
	 */
	public TransactionManager getTransactionManager() {
		return transaction;
	}

	/**
	 * 获得一个DataList列表
	 */
	@Override
	public <T> DataList<T> list(Class<T> cls, String selectsql) throws TransactionException {
		return list(null, cls, selectsql, (Object[]) null, 0, 0, false);
	}

	@Override
	public <T> DataList<T> list(Class<T> cls, String selectsql, int startIndex, int resultNum)
			throws TransactionException {
		return list(null, cls, selectsql, (Object[]) null, startIndex, resultNum, false);
	}

	@Override
	public <T> DataList<T> list(Class<T> cls, String selectsql, int startIndex, int resultNum, boolean autoCount)
			throws TransactionException {
		return list(null, cls, selectsql, (Object[]) null, startIndex, resultNum, autoCount);
	}

	@Override
	public <T> DataList<T> list(Class<T> cls, String selectsql, Object... paramList) throws TransactionException {
		return list(null, cls, selectsql, paramList, 0, 0, false);
	}

	@Override
	public <T> DataList<T> list(Class<T> cls, String selectsql, Object[] paramList, int startIndex, int resultNum)
			throws TransactionException {
		return list(null, cls, selectsql, paramList, startIndex, resultNum, false);
	}

	@Override
	public <T> DataList<T> list(Class<T> cls, String selectsql, Object[] paramList, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException {
		return list(null, cls, selectsql, paramList, startIndex, resultNum, autoCount);
	}

	@Override
	public <T> DataList<T> list(String connName, Class<T> cls, String selectsql) throws TransactionException {
		return list(null, cls, selectsql, (Object[]) null, 0, 0, false);

	}

	@Override
	public <T> DataList<T> list(String connName, Class<T> cls, String selectsql, int startIndex, int resultNum)
			throws TransactionException {
		return list(connName, cls, selectsql, (Object[]) null, startIndex, resultNum, false);
	}

	@Override
	public <T> DataList<T> list(String connName, Class<T> cls, String selectsql, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException {
		return list(connName, cls, selectsql, (Object[]) null, startIndex, resultNum, autoCount);
	}

	@Override
	protected <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException {
		return list(connName, cls, selectsql, paramList, 0, 0, false);
	}

	@Override
	public <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object[] paramList, int startIndex,
			int resultNum) throws TransactionException {
		return list(null, cls, selectsql, paramList, startIndex, resultNum, false);
	}

	@Override
	public <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object[] paramList, int startIndex,
			int resultNum, boolean autoCount) throws TransactionException {
		DataList<T> list = EntityCommandImpl.list(this, connName, cls, selectsql, paramList, startIndex, resultNum,
				autoCount);
		return list;
	}

	/**
	 * 获得单条数据。
	 */

	@Override
	public <T> T queryForSingleObject(Class<T> cls, String selectsql, Object... paramList) throws TransactionException {
		return queryForSingleObject(null, cls, selectsql, paramList);
	}

	@Override
	public <T> T queryForSingleObject(String connName, Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException {
		T t = EntityCommandImpl.listSingle(this, connName, cls, selectsql, paramList);
		return t;
	}

	/**
	 * 载入一个对象
	 */
	@Override
	public <T> T load(Class<T> cls, Serializable id) throws TransactionException {
		return load(null, cls, null, id);
	}

	@Override
	public <T> T load(Class<T> cls, String tableName, Serializable id) throws TransactionException {
		return load(null, cls, tableName, id);
	}

	@Override
	public <T> T load(String connName, Class<T> cls, Serializable id) throws TransactionException {
		return load(connName, cls, null, id);
	}

	@Override
	public <T> T load(String connName, Class<T> cls, String tableName, Serializable id) throws TransactionException {
		return EntityCommandImpl.load(this, connName, cls, tableName, id);
	}

	/**
	 * 建立一个DataSet的查询
	 */
	@Override
	public DataSet queryForDataSet(String selectsql, int startIndex, int resultNum, boolean autoCount)
			throws TransactionException {
		return this.queryForDataSet(selectsql, (Object[]) null, startIndex, resultNum, autoCount);
	}

	@Override
	public DataSet queryForDataSet(String selectsql, int startIndex, int resultNum) throws TransactionException {
		return this.queryForDataSet(selectsql, (Object[]) null, startIndex, resultNum, true);
	}

	@Override
	public DataSet queryForDataSet(String selectsql) throws TransactionException {
		return this.queryForDataSet(selectsql, (Object[]) null, 0, 0, false);
	}

	@Override
	public DataSet queryForDataSet(String selectsql, Object[] paramList, int startIndex, int resultNum)
			throws TransactionException {
		return this.queryForDataSet(selectsql, paramList, startIndex, resultNum, true);
	}

	@Override
	public DataSet queryForDataSet(String selectsql, Object[] paramList) throws TransactionException {
		return this.queryForDataSet(selectsql, paramList, 0, 0, false);
	}

	@Override
	public DataSet queryForDataSet(String selectsql, Object[] paramList, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException {
		return queryForDataSet(null, selectsql, paramList, startIndex, resultNum, autoCount);
	}

	/**
	 * 建立一个DataSet的查询
	 */
	@Override
	public DataSet queryForDataSet(String connName, String selectsql, int startIndex, int resultNum, boolean autoCount)
			throws TransactionException {
		return queryForDataSet(connName, selectsql, (Object[]) null, startIndex, resultNum, autoCount);
	}

	@Override
	public DataSet queryForDataSet(String connName, String selectsql, int startIndex, int resultNum)
			throws TransactionException {
		return queryForDataSet(connName, selectsql, (Object[]) null, startIndex, resultNum, true);
	}

	@Override
	public DataSet queryForDataSet(String connName, String selectsql) throws TransactionException {
		return queryForDataSet(connName, selectsql, (Object[]) null, 0, 0, false);
	}

	@Override
	public DataSet queryForDataSet(String connName, String selectsql, Object[] paramList, int startIndex, int resultNum)
			throws TransactionException {
		return queryForDataSet(connName, selectsql, paramList, startIndex, resultNum, true);
	}

	@Override
	public DataSet queryForDataSet(String connName, String selectsql, Object[] paramList) throws TransactionException {
		return queryForDataSet(connName, selectsql, paramList, 0, 0, false);
	}

	@Override
	public DataSet queryForDataSet(String connName, String selectsql, Object[] paramList, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException {
		DataSet ds = SQLCommandImpl.selectForDataSet(this, connName, selectsql, paramList, startIndex, resultNum,
				autoCount);
		return ds;
	}

	/**
	 * 建立一个单列多行数据的查询
	 */

	@Override
	public <T> List<T> queryForSingleList(Class<T> cls, String sql, Object... paramList) throws TransactionException {
		return queryForSingleList(null, sql, paramList);
	}

	@Override
	public <T> List<T> queryForSingleList(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException {
		List<T> ret = SQLCommandImpl.selectForSingleList(this, connName, cls, sql, paramList);
		return ret;
	}

	/**
	 * 建立一个单行单数据的查询
	 */

	@Override
	public <T> T queryForSingleValue(Class<T> cls, String sql, Object... paramList) throws TransactionException {
		return queryForSingleValue(null, cls, sql, paramList);
	}

	@Override
	public <T> T queryForSingleValue(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException {
		T ret = SQLCommandImpl.selectForSingleValue(this, connName, cls, sql, paramList);
		return ret;
	}

	/**
	 * 保存一个对象
	 */
	@Override
	public <T extends DataEntity> T save(T entity) throws TransactionException {
		return save(null, entity, null);
	}

	@Override
	public <T extends DataEntity> T save(T entity, String tableName) throws TransactionException {
		return save(null, entity, null);
	}

	@Override
	public <T extends DataEntity> T save(String connName, T entity) throws TransactionException {
		return save(connName, entity, null);
	}

	@Override
	public <T extends DataEntity> T save(String connName, T entity, String tableName) throws TransactionException {
		T t = EntityCommandImpl.save(this, connName, entity, tableName);
		return t;
	}

	/**
	 * 更新一个对象
	 */
	@Override
	public <T extends DataEntity> int update(T entity) throws TransactionException {
		return update(null, entity, null);
	}

	@Override
	public <T extends DataEntity> int update(T entity, String tableName) throws TransactionException {
		return update(null, entity, tableName);

	}

	@Override
	public <T extends DataEntity> int update(String connName, T entity) throws TransactionException {
		return update(connName, entity, null);
	}

	@Override
	public <T extends DataEntity> int update(String connName, T entity, String tableName) throws TransactionException {
		int effect = EntityCommandImpl.update(this, connName, entity, tableName);
		return effect;
	}

	/**
	 * 删除一条记录
	 */
	@Override
	public <T extends DataEntity> int delete(T entity) throws TransactionException {
		return delete(null, entity, null);
	}

	@Override
	public <T extends DataEntity> int delete(T entity, String tableName) throws TransactionException {
		return delete(null, entity, tableName);
	}

	@Override
	public <T extends DataEntity> int delete(String connName, T entity) throws TransactionException {
		return delete(connName, entity, null);
	}

	@Override
	public <T extends DataEntity> int delete(String connName, T entity, String tableName) throws TransactionException {
		int effect = EntityCommandImpl.delete(this, null, entity, tableName);
		return effect;
	}

}
