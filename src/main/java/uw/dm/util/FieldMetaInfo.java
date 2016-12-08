package uw.dm.util;

import java.lang.reflect.Field;

/**
 * 实体属性信息。
 * 
 * @author axeon
 *
 */
public class FieldMetaInfo {
	
	/**
	 * java属性名
	 */
	private String propertyName;
	
	/**
	 * 数据库字段名
	 */
	private String columnName;
	
	/**
	 * 是否是主键
	 */
	private boolean primaryKey = false;
	
	/**
	 * 属性反射句柄
	 */
	private Field field;

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}