package uw.dm.impl;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import uw.dm.DataSet;

/**
 * DataSet实现类
 * 
 * @author zhangjin
 * 
 */
public class DataSetImpl implements DataSet, Serializable, Cloneable {
	
	
	private static final long serialVersionUID = -6406519883263593614L;

	/**
	 * 列名
	 */
	private String[] cols;

	/**
	 * 数据存放数组
	 */
	private ArrayList<Object[]> results;

	/**
	 * 当前索引位置
	 */
	private int currentIndex = -1;

	/**
	 * 整个结果集大小
	 */
	private int allsize;

	/**
	 * 开始的索引
	 */
	private int startIndex = 0;
	
	
	/**
	 * 翻页结果集大小
	 */
	private int resultNum;
	
	/**
	 * 当前结果集大小
	 */
	private int size;

	/**
	 * 构造器
	 * 
	 * @param hits
	 *            Hits对象
	 * @param cols
	 *            列名数组
	 * @param startIndex
	 *            开始位置
	 * @param resultNum
	 *            结果集大小
	 * @throws IOException
	 * @throws SQLException
	 */
	public DataSetImpl(ResultSet rs, int startIndex, int resultNum, int allsize) throws SQLException {
		// 获得字段列表
		ResultSetMetaData rsm = rs.getMetaData();
		int colsCount = rsm.getColumnCount();
		cols = new String[colsCount];
		int[] coltypes = new int[colsCount];
		for (int i = 0; i < colsCount; i++) {
			cols[i] = rsm.getColumnLabel(i + 1).toLowerCase();
			coltypes[i] = rsm.getColumnType(i + 1);
		}
		// this.results = new
		// Object[resultNum][cols.length];//实例化一个定长的二维数组代替pojo list
		if (resultNum > 0)
			this.results = new ArrayList<Object[]>(resultNum);
		else
			this.results = new ArrayList<Object[]>();
		this.startIndex = startIndex;
		this.resultNum = resultNum;
		this.allsize = allsize;
		while (rs.next()) {
			this.size++;
			Object[] result = new Object[cols.length];
			for (int x = 0; x < cols.length; x++) {
				// 将对应列名的值放入二维数组中
				switch (coltypes[x]) {
				case Types.NUMERIC:
					result[x] = rs.getBigDecimal(x+1);
					break;
				case Types.VARCHAR:
					result[x] = rs.getString(x + 1);
					break;
				case Types.CLOB:
					result[x] = rs.getString(x + 1);
					break;
				case Types.DATE:
					result[x] = rs.getTimestamp(x + 1);
					break;
				case Types.TIME:
					result[x] = rs.getTimestamp(x + 1);
					break;
				case Types.TIMESTAMP:
					result[x] = rs.getTimestamp(x + 1);
					break;
				case Types.BIGINT: 
					result[x] = rs.getLong(x+1);
					break;
				case Types.INTEGER:
					result[x] = rs.getInt(x+1);
					break;
				case Types.SMALLINT:
					result[x] = rs.getInt(x+1);
					break;
				case Types.TINYINT:
					result[x] = rs.getInt(x+1);
					break;
				case Types.FLOAT:
					result[x] = rs.getFloat(x+1);
					break;
				case Types.DOUBLE:
					result[x] = rs.getDouble(x+1);
					break;
				case Types.BIT:
					result[x] = rs.getInt(x+1);
					break;
					
				default:
					result[x] = rs.getObject(x + 1);
				}
			}
			results.add(result);
		}
	}

	/**
	 * 获得列名列表
	 */
	public String[] getColumnNames() {
		return cols;
	}

	/**
	 * 到下一条记录。 检查是否还有下一行数据。
	 * 
	 */
	public boolean next() {
		currentIndex++;
		return results.size() > currentIndex;
	}

	/**
	 * 到上一条记录
	 * 
	 * @return
	 */
	public boolean previous() {
		currentIndex--;
		return currentIndex > -1;
	}

	/**
	 * remove当前行
	 */
	public void remove() {
		this.results.remove(currentIndex);
		this.size--;
		this.allsize--;
	}

	/**
	 * 返回结果集数组
	 * 
	 * @return
	 */
	public ArrayList<Object[]> results() {
		return results;
	}

	/**
	 * 定位到指定的位置
	 * 
	 * @param index
	 */
	public void absolute(int index) {
		this.currentIndex = index - 1;
	}

	/**
	 * 获得整个结果集的大小
	 * 
	 * @return
	 */
	public int allsize() {
		if (this.allsize == 0)
			this.allsize = this.size;
		return this.allsize;
	}

	/**
	 * 当前结果集大小
	 * 
	 * @return
	 */
	public int size() {
		return this.size;
	}

	/**
	 * 取得整个分页的大小
	 * 
	 * @return
	 */
	public int pagecount() {
		return (int) Math.ceil((double) allsize / resultNum);
	}

	/**
	 * 获得数组中指定位置的数据
	 * 
	 * @param colname
	 * @return
	 */
	public Object get(String colname) {
		return ((Object[]) results.get(currentIndex))[getColumnPos(colname)];
	}

	/**
	 * 返回值为int
	 * 
	 * @param colname
	 * @return
	 */
	public int getInt(String colname) {
		return (Integer)get(colname);
	}

	/**
	 * 返回值为long
	 * 
	 * @param colname
	 * @return
	 */
	public long getLong(String colname) {
		return (Long)get(colname);
	}

	/**
	 * 返回值为double
	 * 
	 * @param colname
	 * @return
	 */
	public double getDouble(String colname) {
		return  (Double)get(colname);
	}

	/**
	 * 返回值为float
	 * 
	 * @param colname
	 * @return
	 */
	public float getFloat(String colname) {
		return  (Float)get(colname);
	}

	/**
	 * 返回值为String
	 * 
	 * @param colname
	 * @return
	 */
	public String getString(String colname) {
		String s = String.valueOf(get(colname));
		if (s.equals("null"))
			s = "";
		return s;
	}

	/**
	 * 返回值为Date
	 * 
	 * @param colname
	 * @return
	 */
	public java.util.Date getDate(String colname) {
		return (java.util.Date) get(colname);
	}
	

	/**
	 * 获得数组中指定位置的数据
	 * 
	 * @param colname
	 * @return
	 */
	public Object get(int colIndex) {
		return ((Object[]) results.get(currentIndex))[--colIndex];
	}

	/**
	 * 返回值为int
	 * 
	 * @param colname
	 * @return
	 */
	public int getInt(int colIndex) {
		return (Integer)get(colIndex);
	}

	/**
	 * 返回值为long
	 * 
	 * @param colname
	 * @return
	 */
	public long getLong(int colIndex) {
		return (Long)get(colIndex);
	}

	/**
	 * 返回值为double
	 * 
	 * @param colname
	 * @return
	 */
	public double getDouble(int colIndex) {
		return (Double)get(colIndex);
	}

	/**
	 * 返回值为float
	 * 
	 * @param colname
	 * @return
	 */
	public float getFloat(int colIndex) {
		return (Float)get(colIndex);
	}

	/**
	 * 返回值为String
	 * 
	 * @param colname
	 * @return
	 */
	public String getString(int colIndex) {
		String s = String.valueOf(get(colIndex));
		if (s.equals("null"))
			s = "";
		return s;
	}

	/**
	 * 返回值为Date
	 * 
	 * @param colname
	 * @return
	 */
	public java.util.Date getDate(int colIndex) {
		return (java.util.Date) get(colIndex);
	}


	/**
	 * 获得列名位置
	 * 
	 * @param colname
	 * @return
	 */
	public int getColumnPos(String colname) {
		int index = -1;
		int end = cols.length;
		for (int i = 0; i < end; i++) {
			if (colname.equalsIgnoreCase(cols[i])) {
				index = i;
				break;
			}
		}
		return index++;
	}

	public static void main(String[] args) {
		// String[] cols =
		// {"test0","test1","test2","test3","test4","test5","test6","test7","test8","test9"};
	}

}
