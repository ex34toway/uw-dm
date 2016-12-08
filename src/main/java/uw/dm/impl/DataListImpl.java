package uw.dm.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uw.dm.DataList;
import uw.dm.TransactionException;

/**
 * DataList实体类。
 */
public class DataListImpl<T> implements DataList<T> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 开始的索引
	 */
	private int startIndex = 0;
	
	/**
	 * 返回的结果集大小
	 */
	private int resultNum = 0;

	/**
	 * 当前索引
	 */
	private int currentIndex = -1;
	/**
	 * 整个表数据量大小
	 */
	private int allsize = 0;
	/**
	 * List大小（实际返回的结果集大小）
	 */
	private int size = 0;
	/**
	 * 总页数
	 */
	private int pagecount = 0;

	/**
	 * 返回的value object数组
	 */
	private List<T> results = null;



	/**
	 * DataList构造器。
	 * @param results 结果集
	 * @param startIndex 开始位置
	 * @param resultNum 每页大小
	 * @param allSize 所有的数量
	 */
	public DataListImpl(List<T> results,int startIndex, int resultNum, int allSize) {
		this.results = results;
		this.resultNum = resultNum;
		this.size = this.results.size();
		this.allsize = allSize;

		if (this.allsize > 0 || this.resultNum == 0) {
			// 计算总页数
			this.pagecount = (int) Math.ceil((double) allsize / resultNum);
		} else {
			results = new ArrayList<T>(1);
		}
	}

	/**
	 * 定位到某条位置
	 */
	public void absolute(int index) {
		this.currentIndex = index - 1;
	}

	/**
	 * 获得指定处的对象
	 * 
	 * @param index
	 * @return
	 */
	public T get(int index) {
		return results.get(index);
	}

	/**
	 * 是否有下一条记录
	 * 
	 * @return 是否有下一条记录
	 */
	public boolean hasNext() {
		return (currentIndex + 1 < this.size);
	}

	/**
	 * 获取下一条记录
	 * 
	 * @return 下一条记录
	 */
	public T next() {
		if (currentIndex < this.size) {
			currentIndex++;
		}
		return results.get(currentIndex);
	}

	/**
	 * 获取下一条记录索引
	 * 
	 * @return 下一条记录索引
	 */
	public int nextIndex() {
		return currentIndex + 1;
	}

	/**
	 * 是否有上一条记录
	 * 
	 * @return 是否有上一条记录
	 */
	public boolean hasPrevious() {
		return (currentIndex > 0);
	}

	/**
	 * 获取上一条记录
	 * 
	 * @return 上一条记录
	 */
	public T previous() {
		if (currentIndex > -1) {
			currentIndex--;
		}
		return results.get(currentIndex);
	}

	/**
	 * 获取上一条记录索引
	 * 
	 * @return 上一条记录索引
	 */
	public int previousIndex() {
		return currentIndex - 1;
	}

	/**
	 * 为了兼容List接口，不实现，不起作用。 抛出异常
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 获取当前List大小
	 * 
	 * @return 当前List大小
	 */
	public int size() {
		return this.size;
	}

	/**
	 * 获取该表/视图所有的数据大小
	 * 
	 * @return 该表/视图所有的数据大小
	 */
	public int allsize() throws TransactionException {
		return this.allsize;
	}

	/**
	 * 按照总记录数和每页条数计算出页数
	 * 
	 * @return 页数
	 */
	public int pagecount() {
		return this.pagecount;
	}

	/**
	 * 返回该结果集
	 * 
	 * @return 结果集
	 */
	public List<T> results() {
		return this.results;
	}

	/**
	 * 重新设定结果集合
	 * 
	 * @param objects
	 */
	public void reset(List<T> objects) {
		this.results = objects;
		this.currentIndex = -1;
		if (this.size != objects.size()) {
			this.size = objects.size();
			this.allsize = objects.size();
		}
	}

	/**
	 * 获得iterator列表。
	 */
	public Iterator<T> iterator() {
		return this;
	}

}
