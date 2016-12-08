package uw.dm.util;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import uw.dm.entity.MscPerm;

/**
 * DM反射工具类。
 * 
 * @author zhangjin
 * 
 */
public class DmReflectUtils {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		Field fd = MscPerm.class.getDeclaredField("id");
		System.out.println(fd.getName());
		System.out.println(fd.getType());
		System.out.println(fd.getType() == long.class);
	}

	private DmReflectUtils() {
	}

	/**
	 * 在preparedStatement中动态set数值
	 * 
	 * @param pstmt
	 *            PreparedStatement
	 * @param classname
	 *            String 类的名称
	 * @param dao
	 *            Object 类的实例
	 * @param sequence
	 *            int 次序
	 * @param col
	 *            String 列的名称
	 * @param type
	 *            int 列的类型
	 * @throws Exception
	 */
	public static final void DAOLiteSaveReflect(PreparedStatement pstmt, Object entity, FieldMetaInfo fmi, int sequence)
			throws Exception {
		Field fd = fmi.getField();
		Class<?> cls = fd.getType();
		if (cls == int.class) {
			pstmt.setInt(sequence, fd.getInt(entity));
		} else if (cls == String.class) {
			pstmt.setObject(sequence, fd.get(entity));
		} else if (cls == long.class) {
			pstmt.setLong(sequence, fd.getLong(entity));
		} else if (cls == Date.class) {
			pstmt.setTimestamp(sequence, DmValueUtils.dateToTimestamp((java.util.Date) fd.get(entity)));
		} else if (cls == double.class) {
			pstmt.setDouble(sequence, fd.getDouble(entity));
		} else if (cls == boolean.class) {
			pstmt.setBoolean(sequence, fd.getBoolean(entity));
		} else {
			pstmt.setObject(sequence, fd.get(entity));
		}
	}

	/**
	 * 通用的反射更新方法
	 * 
	 * @param pstmt
	 * @param sequence
	 * @param value
	 * @param type
	 * @throws Exception
	 */
	public static final void CommandUpdateReflect(PreparedStatement pstmt, int sequence, Object value)
			throws Exception {
		if (value instanceof java.util.Date) {
			pstmt.setTimestamp(sequence, DmValueUtils.dateToTimestamp((java.util.Date) value));
		} else {
			pstmt.setObject(sequence, value);
		}
	}

	/**
	 * 动态载入
	 * 
	 * @param rs
	 * @param entity
	 * @param col
	 * @param type
	 * @throws Exception
	 */
	public static final void DAOLiteLoadReflect(ResultSet rs, Object entity, FieldMetaInfo fmi) throws Exception {
		Field fd = fmi.getField();
		Class<?> cls = fd.getType();
		if (cls == int.class) {
			fd.setInt(entity, new Integer(rs.getInt(fmi.getColumnName())));
		} else if (cls == String.class) {
			fd.set(entity, DmValueUtils.nullToStr((String) rs.getString(fmi.getColumnName())));
		} else if (cls == Date.class) {
			fd.set(entity, rs.getTimestamp(fmi.getColumnName()));
		} else if (cls == long.class) {
			fd.setLong(entity, rs.getLong(fmi.getColumnName()));
		} else if (cls == double.class) {
			fd.setDouble(entity, rs.getDouble(fmi.getColumnName()));
		} else if (cls == boolean.class) {
			fd.setBoolean(entity, rs.getBoolean(fmi.getColumnName()));
		} else {
			fd.set(entity, rs.getObject(fmi.getColumnName()));
		}

	}

}
