package uw.dm.util;

/**
 * dm的数值工具类。
 * 
 */
public class DmValueUtils {

	/**
	 * 从日期类型转换为java.sql.Timestamp
	 * 
	 * @param date
	 * @return
	 */
	public static final java.sql.Timestamp dateToTimestamp(java.util.Date date) {
		if (date != null) {
			return new java.sql.Timestamp(date.getTime());
		} else {
			return null;
		}
	}

	/**
	 * 获得真正的列名。 在sql里面可能会使用一些函数并使用as进行区分。
	 * 
	 * @return
	 */
	public static final String getTrueColumnName(String columnName) {
		int p = columnName.indexOf(" as ");
		if (p > -1) {
			return columnName.substring(p + 4).trim();
		} else {
			return columnName;
		}
	}

	/**
	 * 把null转换为空字符
	 * 
	 * @param str
	 * @return
	 */
	public static final String nullToStr(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

}
