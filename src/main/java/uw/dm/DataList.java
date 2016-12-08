package uw.dm;

import java.io.Serializable;
import java.util.*;

/**
 * 组合了互联网应用常见列表所需数据的集合接口。 实现了iterator,Iterable。
 * 
 */
public interface DataList<T> extends Iterator<T>, Iterable<T>, Serializable {

	/**
	 * 检测是否有下一个Item。
	 * 
	 */
	public boolean hasNext();

	/**
	 * 获取下一个Item。
	 * 
	 */
	public T next();

	/**
	 * 检测是否有上一个Item。
	 * 
	 */
	public boolean hasPrevious();

	/**
	 * 获取上一个Item。
	 * 
	 */
	public T previous();

	/**
	 * 获取下一个索引。
	 * 
	 */
	public int nextIndex();

	/**
	 * 获取上一个索引。
	 * 
	 */
	public int previousIndex();

	/**
	 * 获取Iterator的大小。
	 * 
	 */
	public int size();

	/**
	 * 获取大小。
	 * 
	 */
	public int allsize() throws TransactionException;

	/**
	 * 获得返回数值数组。
	 */
	public List<T> results();

	/**
	 * 获得页面数
	 * 
	 */
	public int pagecount();

	/**
	 * 获得指定处的对象
	 */
	public T get(int index);

	/**
	 * 定位到指定索引位置
	 */
	public void absolute(int index);

	/**
	 * 重新设定结果集合
	 */
	public void reset(List<T> objects);

}
