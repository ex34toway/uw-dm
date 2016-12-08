package uw.dm;

import java.sql.Connection;

/**
 * 事务操作管理类。
 */

public interface TransactionManager {

	/**
	 * oracle数据类型
	 */
	public static final int DB_TYPE_ORACLE = 1;

	/**
	 * mysql数据类型
	 */
	public static final int DB_TYPE_MYSQL = 2;

	public static int TRANSACTION_NONE = Connection.TRANSACTION_NONE;
	public static int TRANSACTION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
	public static int TRANSACTION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
	public static int TRANSACTION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
	public static int TRANSACTION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

	public boolean isAutoCommit();

	/**
	 * 提交该事务.
	 */
	public void commit() throws TransactionException;

	/**
	 * 回滚该事务.
	 */
	public void rollback() throws TransactionException;

	/**
	 * 设置事务级别
	 * 
	 * @param level
	 */
	public void setTransactionIsolation(int level) throws TransactionException;

	/**
	 * 获取当前事务级别
	 * 
	 * @param level
	 */
	public int getTransactionIsolation() throws TransactionException;

}
