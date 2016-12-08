package uw.dm;

import java.util.*;

/**
 * 批量更新的管理类。
 */

public interface BatchupdateManager {

	/**
	 * 设置批量更新的数量
	 * 
	 * @return boolean
	 */
	public void setBatchSize(int size);

	/**
	 * 获得批量更新的数量
	 * 
	 * @return boolean
	 */
	public int getBatchSize();

	/**
	 * 获得Batch的sql列表
	 * 
	 * @return
	 */
	public List<String> getBatchList();

	/**
	 * 提交该事务.
	 */
	public List<List<Integer>> submit() throws TransactionException;

}
