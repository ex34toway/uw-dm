package uw.dm;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import uw.dm.impl.DAOFactoryImpl;
import uw.dm.performance.SqlExecuteStats;

/**
 * 
 * 整个DM模块的入口，所有数据库操作都从这个类开始。
 * 
 * @author axeon
 * 
 */
public abstract class DAOFactory {

	/**
	 * 获取一个MainFactory实例。
	 * 
	 */
	public static DAOFactory getInstance() {
		return new DAOFactoryImpl();
	}

	/**
	 * 获取一个DAOFactory实例。 指定connName，这时候将不会使用dm来决定数据库联接。
	 * 
	 */
	public static DAOFactory getInstance(String connName) {
		return new DAOFactoryImpl(connName);
	}

	/**
	 * 打开sql执行统计，将会影响getSqlExecuteStatsList的数据
	 */
	public abstract void enableSqlExecuteStats();

	/**
	 * 关闭sql执行统计，将会影响getSqlExecuteStatsList的数据
	 */
	public abstract void disableSqlExecuteStats();

	/**
	 * 获得数据库调用次数
	 * 
	 * @return
	 */
	public abstract List<SqlExecuteStats> getSqlExecuteStatsList();

	/**
	 * 统计sql执行次数
	 * 
	 * @return
	 */
	public abstract int getInvokeCount();

	/**
	 * 获得一个batchupdate handle.
	 * 
	 * @return
	 * @throws TransactionException
	 */
	public abstract BatchupdateManager beginBatchupdate() throws TransactionException;

	/**
	 * 开始一个数据库事务
	 * 
	 */
	public abstract TransactionManager beginTransaction() throws TransactionException;


	/**
	 * 获得Sequence序列
	 * 
	 * @param entity
	 * @return
	 */
	public abstract long getSequenceId(Class<?> entity);

	public abstract long getSequenceId(String tablename);


	/**
	 * 返回一个DataSet
	 * 
	 * @param connName
	 * @param selectsql
	 * @param startIndex
	 * @param resultNum
	 * @param autoCount
	 * @return DataSet
	 * @throws TransactionException
	 */

	public abstract DataSet queryForDataSet(String selectsql) throws TransactionException;

	public abstract DataSet queryForDataSet(String selectsql, Object[] paramList) throws TransactionException;

	public abstract DataSet queryForDataSet(String selectsql, int startIndex, int resultNum)
			throws TransactionException;

	public abstract DataSet queryForDataSet(String selectsql, Object[] paramList, int startIndex, int resultNum)
			throws TransactionException;

	public abstract DataSet queryForDataSet(String selectsql, int startIndex, int resultNum, boolean autoCount)
			throws TransactionException;

	public abstract DataSet queryForDataSet(String selectsql, Object[] paramList, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	public abstract DataSet queryForDataSet(String connName, String selectsql) throws TransactionException;

	public abstract DataSet queryForDataSet(String connName, String selectsql, Object[] paramList)
			throws TransactionException;

	public abstract DataSet queryForDataSet(String connName, String selectsql, int startIndex, int resultNum)
			throws TransactionException;

	public abstract DataSet queryForDataSet(String connName, String selectsql, Object[] paramList, int startIndex,
			int resultNum) throws TransactionException;

	public abstract DataSet queryForDataSet(String connName, String selectsql, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	public abstract DataSet queryForDataSet(String connName, String selectsql, Object[] paramList, int startIndex,
			int resultNum, boolean autoCount) throws TransactionException;

	/**
	 * 根据指定的基础数据类型返回数值。
	 * 
	 * @param connName
	 * @param cls
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleValue(Class<T> cls, String sql, Object... paramList)
			throws TransactionException;

	public abstract <T> T queryForSingleValue(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException;

	/**
	 * 列出单个对象。
	 * 
	 * @param entity
	 * @param selectsql
	 * @return
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleObject(Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;

	public abstract <T> T queryForSingleObject(String connName, Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;

	/**
	 * 根据自定义的sql来获得单行结果，以String List方式输出
	 * 
	 * @param connName
	 * @param sql
	 * @return
	 * @throws TransactionException
	 */

	public abstract <T> List<T> queryForSingleList(Class<T> cls, String sql, Object... paramList)
			throws TransactionException;

	public abstract <T> List<T> queryForSingleList(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException;

	/**
	 * 执行sql命令
	 * 
	 * @param connName
	 * @param sql
	 * @return
	 * @throws TransactionException
	 */
	public abstract int executeCommand(String sql) throws TransactionException;

	public abstract int executeCommand(String sql, Object[] paramList) throws TransactionException;

	public abstract int executeCommand(String connName, String sql) throws TransactionException;

	public abstract int executeCommand(String connName, String sql, Object[] paramList) throws TransactionException;

	/**
	 * 获得一个新连接
	 * 
	 * @param configName
	 * @return
	 * @throws SQLException
	 */
	public abstract Connection getConnection(String configName) throws SQLException;

	public abstract Connection getConnection(String table, String access) throws SQLException;

	public abstract String getConnectionName(String table, String access);

	/**
	 * 列表
	 * 
	 * @param entity
	 * @param selectsql
	 * @param startIndex
	 * @param resultNum
	 * @param autoCount
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(Class<T> cls, String selectsql) throws TransactionException;

	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, int startIndex, int resultNum)
			throws TransactionException;

	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;

	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, Object[] paramList, int startIndex,
			int resultNum) throws TransactionException;

	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, Object[] paramList, int startIndex,
			int resultNum, boolean autoCount) throws TransactionException;

	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql) throws TransactionException;

	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, int startIndex, int resultNum)
			throws TransactionException;

	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	protected abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;

	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object[] paramList,
			int startIndex, int resultNum) throws TransactionException;

	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object[] paramList,
			int startIndex, int resultNum, boolean autoCount) throws TransactionException;

	/**
	 * 获得一个数据库对象
	 * 
	 * @return
	 */
	public abstract <T> T load(Class<T> cls, Serializable id) throws TransactionException;

	public abstract <T> T load(Class<T> cls, String tableName, Serializable id) throws TransactionException;

	public abstract <T> T load(String connName, Class<T> cls, Serializable id) throws TransactionException;

	public abstract <T> T load(String connName, Class<T> cls, String tableName, Serializable id)
			throws TransactionException;

	/**
	 * 保存一个数据库对象
	 * 
	 * @return
	 */
	public abstract <T extends DataEntity> T save(T entity) throws TransactionException;

	public abstract <T extends DataEntity> T save(T entity, String tableName) throws TransactionException;

	public abstract <T extends DataEntity> T save(String connName, T entity) throws TransactionException;

	public abstract <T extends DataEntity> T save(String connName, T entity, String tableName)
			throws TransactionException;

	/**
	 * 修改一个数据库对象
	 * 
	 */
	public abstract <T extends DataEntity> int update(T entity) throws TransactionException;

	public abstract <T extends DataEntity> int update(T entity, String tableName) throws TransactionException;

	public abstract <T extends DataEntity> int update(String connName, T entity) throws TransactionException;

	public abstract <T extends DataEntity> int update(String connName, T entity, String tableName)
			throws TransactionException;

	/**
	 * 删除一个数据库对象
	 * 
	 */
	public abstract <T extends DataEntity> int delete(String connName, T entity) throws TransactionException;

	public abstract <T extends DataEntity> int delete(String connName, T entity, String tableName)
			throws TransactionException;

	public abstract <T extends DataEntity> int delete(T entity) throws TransactionException;

	public abstract <T extends DataEntity> int delete(T entity, String tableName) throws TransactionException;
}
