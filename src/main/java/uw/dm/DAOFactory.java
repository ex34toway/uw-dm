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
	 * 此实例是线程安全的。
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
	 * 获得一个batchupdate handle。
	 * 
	 * @return
	 * @throws TransactionException
	 */
	public abstract BatchupdateManager beginBatchupdate() throws TransactionException;

	/**
	 * 开始一个数据库事务。
	 * 
	 * 
	 */
	public abstract TransactionManager beginTransaction() throws TransactionException;

	/**
	 * 根据主键删除一个Entity实例，等效于delete。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int delete(String connName, T entity) throws TransactionException;

	/**
	 * 根据主键删除一个Entity实例，等效于delete。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int delete(String connName, T entity, String tableName)
			throws TransactionException;

	/**
	 * 根据主键删除一个Entity实例，等效于delete。
	 * @param entity 要更新的对象
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int delete(T entity) throws TransactionException;

	/**
	 * 根据主键删除一个Entity实例，等效于delete。
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int delete(T entity, String tableName) throws TransactionException;


	/**
	 * 关闭sql执行统计，将会影响getSqlExecuteStatsList的数据
	 */
	public abstract void disableSqlExecuteStats();

	/**
	 * 打开sql执行统计，将会影响getSqlExecuteStatsList的数据
	 */
	public abstract void enableSqlExecuteStats();


	/**
	 * 执行一条SQL语句。
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return 影响的行数
	 * @throws TransactionException
	 */
	public abstract int executeCommand(String sql, Object... paramList) throws TransactionException;

	/**
	 * 执行一条SQL语句。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return 影响的行数
	 * @throws TransactionException
	 */
	public abstract int executeCommand(String connName, String sql, Object... paramList) throws TransactionException;

	/**
	 * 获得一个java.sql.Connection连接。
	 * 请注意，这是一个原生的Connection对象，需确保手工关闭。
	 * 
	 * @param configName 配置名
	 * @return
	 * @throws SQLException
	 */
	public abstract Connection getConnection(String configName) throws SQLException;

	/**
	 * 根据表名和访问类型获得一个java.sql.Connection。
	 * 请注意，这是一个原生的Connection对象，需确保手工关闭。
	 * @param table 表名
	 * @param access 访问类型。支持all/read/write
	 * @return
	 * @throws SQLException
	 */
	public abstract Connection getConnection(String table, String access) throws SQLException;

	/**
	 * 根据表名和访问类型获得一个数据库连接配置名。
	 * @param table 表名
	 * @param access 访问类型。支持all/read/write
	 * @return
	 */
	public abstract String getConnectionName(String table, String access);

	/**
	 * 获得当前DAOFactory实例下sql执行次数
	 * 
	 * @return
	 */
	public abstract int getInvokeCount();

	/**
	 * 根据Entity来获得seq序列。
	 * 此序列通过一个系统数据库来维护，可以保证在分布式下的可用性。
	 * @param entity
	 * @return
	 */
	public abstract long getSequenceId(Class<?> entity);

	/**
	 * 根据表名来获得seq序列。
	 * 此序列通过一个系统数据库来维护，可以保证在分布式下的可用性。
	 * @param tablename
	 * @return
	 */
	public abstract long getSequenceId(String tablename);

	/**
	 * 获得当前DAOFactory实例下的sql执行统计列表
	 * 
	 * @return 
	 */
	public abstract List<SqlExecuteStats> getSqlExecuteStatsList();

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(Class<T> cls, String selectsql) throws TransactionException;

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, int startIndex, int resultNum)
			throws TransactionException;	
	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, Object[] paramList, int startIndex,
			int resultNum) throws TransactionException;


	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(Class<T> cls, String selectsql, Object[] paramList, int startIndex,
			int resultNum, boolean autoCount) throws TransactionException;

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql) throws TransactionException;

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, int startIndex, int resultNum)
			throws TransactionException;
	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	
	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object[] paramList,
			int startIndex, int resultNum) throws TransactionException;

	/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object[] paramList,
			int startIndex, int resultNum, boolean autoCount) throws TransactionException;

	/**
	 * 根据指定的主键ID载入一个Entity实例。
	 * @param cls 要映射的对象类型
	 * @param id 主键数值
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T load(Class<T> cls, Serializable id) throws TransactionException;

	/**
	 * 根据指定的主键ID载入一个Entity实例。
	 * @param cls 要映射的对象类型
	 * @param tableName 指定表名
	 * @param id 主键数值
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T load(Class<T> cls, String tableName, Serializable id) throws TransactionException;


	/**
	 * 根据指定的主键ID载入一个Entity实例。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param id 主键数值
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T load(String connName, Class<T> cls, Serializable id) throws TransactionException;

	/**
	 * 根据指定的主键ID载入一个Entity实例。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param tableName 指定表名
	 * @param id 主键数值
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T load(String connName, Class<T> cls, String tableName, Serializable id)
			throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param selectsql 查询的SQL
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String selectsql) throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String selectsql, int startIndex, int resultNum)
			throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String selectsql, int startIndex, int resultNum, boolean autoCount)
			throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String selectsql, Object[] paramList) throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String selectsql, Object[] paramList, int startIndex, int resultNum)
			throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String selectsql, Object[] paramList, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param connName 连接名，当设置为null时候，根据sql语句或表名确定
	 * @param selectsql 查询的SQL
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String connName, String selectsql) throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String connName, String selectsql, int startIndex, int resultNum)
			throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String connName, String selectsql, int startIndex, int resultNum,
			boolean autoCount) throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String connName, String selectsql, Object[] paramList)
			throws TransactionException;


	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String connName, String selectsql, Object[] paramList, int startIndex,
			int resultNum) throws TransactionException;

	/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String connName, String selectsql, Object[] paramList, int startIndex,
			int resultNum, boolean autoCount) throws TransactionException;

	/**
	 * 查询单个基本数值列表（多行单个字段）。
	 * @param cls 要映射的基础类型，如int.class,long.class,String.class,Date.class
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> List<T> queryForSingleList(Class<T> cls, String sql, Object... paramList)
			throws TransactionException;

	/**
	 * 查询单个基本数值列表（多行单个字段）。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的基础类型，如int.class,long.class,String.class,Date.class
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> List<T> queryForSingleList(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException;

	/**
	 * 查询单个对象（单行数据）。
	 * 使用sql中探测到的表名来决定连接名。
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleObject(Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;

	/**
	 * 查询单个对象（单行数据）。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleObject(String connName, Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;
	
	/**
	 * 查询单个基本数值（单个字段）。
	 * @param cls 要映射的基础类型，如int.class,long.class,String.class,Date.class
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleValue(Class<T> cls, String sql, Object... paramList)
			throws TransactionException;
	
	/**
	 * 查询单个基本数值（单个字段）。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的基础类型，如int.class,long.class,String.class,Date.class
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleValue(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException;

	/**
	 * 保存一个Entity实例，等效于insert。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> T save(String connName, T entity) throws TransactionException;

	/**
	 * 保存一个Entity实例，等效于insert。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> T save(String connName, T entity, String tableName)
			throws TransactionException;

	/**
	 * 保存一个Entity实例，等效于insert。
	 * @param entity 要更新的对象
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> T save(T entity) throws TransactionException;

	/**
	 * 保存一个Entity实例，等效于insert。
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> T save(T entity, String tableName) throws TransactionException;

	/**
	 * 根据主键更新一个Entity实例，等效于update。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int update(String connName, T entity) throws TransactionException;

	/**
	 * 根据主键更新一个Entity实例，等效于update。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int update(String connName, T entity, String tableName)
			throws TransactionException;

	/**
	 * 根据主键更新一个Entity实例，等效于update。
	 * @param entity 要更新的对象
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int update(T entity) throws TransactionException;

	/**
	 * 根据主键更新一个Entity实例，等效于update。
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int update(T entity, String tableName) throws TransactionException;

}
