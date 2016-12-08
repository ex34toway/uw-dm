package uw.dm;

import java.util.List;

/**
 * 存储并转化ResultSet对象数据。
 * 
 * @author zhangjin
 * 
 */
public interface DataSet {

	/**
	 * 获得列名列表
	 * 
	 * @return
	 */
	public String[] getColumnNames();

	/**
	 * 到下一条记录。 检查是否还有下一行数据。
	 * 
	 */
	public boolean next();

	/**
	 * 到上一条记录
	 * 
	 * @return
	 */
	public boolean previous();

	/**
	 * remove当前行
	 */
	public void remove();

	/**
	 * 返回结果集数组
	 * 
	 * @return
	 */
	public List<Object[]> results();

	/**
	 * 获得列名位置
	 * 
	 * @param colname
	 * @return
	 */
	public int getColumnPos(String colname);

	/**
	 * 定位到指定的位置
	 * 
	 * @param index
	 */
	public void absolute(int index);

	/**
	 * 获得整个结果集的大小
	 * 
	 * @return
	 */
	public int allsize();

	/**
	 * 当前结果集大小
	 * 
	 * @return
	 */
	public int size();

	/**
	 * 取得整个分页的大小
	 * 
	 * @return
	 */
	public int pagecount();

	/**
	 * 获得数组中指定位置的数据
	 * 
	 * @param colname
	 * @return
	 */
	public Object get(String colname);

	/**
	 * 返回值为int
	 * 
	 * @param colname
	 * @return
	 */
	public int getInt(String colname);

	/**
	 * 返回值为long
	 * 
	 * @param colname
	 * @return
	 */
	public long getLong(String colname);

	/**
	 * 返回值为double
	 * 
	 * @param colname
	 * @return
	 */
	public double getDouble(String colname);

	/**
	 * 返回值为float
	 * 
	 * @param colname
	 * @return
	 */
	public float getFloat(String colname);

	/**
	 * 返回值为String
	 * 
	 * @param colname
	 * @return
	 */
	public String getString(String colname);

	/**
	 * 返回值为Date
	 * 
	 * @param colname
	 * @return
	 */
	public java.util.Date getDate(String colname);


	/**
	 * 获得数组中指定位置的数据
	 * 
	 * @param colname
	 * @return
	 */
	public Object get(int colIndex);

	/**
	 * 返回值为int
	 * 
	 * @param colname
	 * @return
	 */
	public int getInt(int colIndex);

	/**
	 * 返回值为long
	 * 
	 * @param colname
	 * @return
	 */
	public long getLong(int colIndex);

	/**
	 * 返回值为double
	 * 
	 * @param colname
	 * @return
	 */
	public double getDouble(int colIndex);

	/**
	 * 返回值为float
	 * 
	 * @param colname
	 * @return
	 */
	public float getFloat(int colIndex);

	/**
	 * 返回值为String
	 * 
	 * @param colname
	 * @return
	 */
	public String getString(int colIndex);

	/**
	 * 返回值为Date
	 * 
	 * @param colname
	 * @return
	 */
	public java.util.Date getDate(int colIndex);

	
}
