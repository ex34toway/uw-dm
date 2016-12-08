package uw.dm.gencode;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uw.dm.connectionpool.ConnectionManager;
import uw.dm.util.DmStringUtils;

/**
 * 数据库信息的工具类。
 * @author axeon
 *
 */
public class DataMetaUtils {

	private static final Logger logger = LoggerFactory.getLogger(DataMetaUtils.class);

	public static void main(String[] args) {
	}

	private static String CONN_NAME = null;

	/**
	 * 设置连接池名称。
	 * 
	 * @param connName
	 */
	public static void setConnName(String connName) {
		CONN_NAME = connName;
	}

	/**
	 * 获得数据库链接。
	 */
	private static Connection getConnection() throws SQLException {
		if (CONN_NAME == null || CONN_NAME.equals("")) {
			return ConnectionManager.getConnection();
		} else {
			return ConnectionManager.getConnection(CONN_NAME);
		}
	}

	/**
	 * 获取数据库中的表名称与视图名称
	 * 
	 * @return 表信息的列表
	 */
	public static List<MetaTableInfo> getTablesAndViews(Set<String> tables) {
		Connection conn = null;
		ResultSet rs = null;
		List<MetaTableInfo> list = new ArrayList<MetaTableInfo>();
		try {
			conn = getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getTables(null, null, null, new String[] { "TABLE", "VIEW" });
			while (rs.next()) {
				MetaTableInfo meta = new MetaTableInfo();
				meta.setTableName(rs.getString("TABLE_NAME").toLowerCase());
				meta.setEntityName(DmStringUtils.toClearCase(meta.getTableName()));
				meta.setTableType(rs.getString("TABLE_TYPE").toLowerCase());
				meta.setRemarks(rs.getString("REMARKS"));
				if (tables.size() > 0) {
					if (tables.contains(meta.getTableName())) {
						list.add(meta);
					}
				}else{
					list.add(meta);
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 利用表名和数据库用户名查询出该表对应的字段类型
	 * 
	 * @param tableName
	 *            表名
	 * @return 表字段信息的列表
	 * @throws Exception
	 */
	public static List<MetaColumnInfo> getColumnList(String tableName, List<MetaPrimaryKeyInfo> pklist) {
		Connection conn = null;
		ResultSet rs = null;
		List<MetaColumnInfo> list = new ArrayList<MetaColumnInfo>();
		// 加载主键列表
		HashSet<String> pset = new HashSet<String>();
		for (MetaPrimaryKeyInfo pk : pklist) {
			pset.add(pk.getColumnName());
		}
		try {
			conn = getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getColumns(null, null, tableName, null);
			while (rs.next()) {
				MetaColumnInfo meta = new MetaColumnInfo();
				meta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase());// 列名
				meta.setPropertyName(DmStringUtils.toClearCase(meta.getColumnName()));
				meta.setDataType(rs.getInt("DATA_TYPE"));// 字段数据类型(对应java.sql.Types中的常量)
				meta.setTypeName(rs.getString("TYPE_NAME").toLowerCase());// 字段类型名称(例如：VACHAR2)
				meta.setColumnSize(rs.getInt("COLUMN_SIZE"));// 列的大小
				meta.setRemarks(rs.getString("REMARKS"));// 描述列的注释
				meta.setIsNullable(rs.getString("IS_NULLABLE").equals("YES") ? "true" : "false");// 确定列是否包括
																									// null
				meta.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT").equals("YES") ? "true" : null);// 确定列是否包括
																										// null
				if (pset.contains(meta.getColumnName())) {
					meta.setIsPrimaryKey("true");
				}
				switch (meta.getDataType()) {
				case Types.NUMERIC:
					meta.setPropertyType("java.math.BigDecimal");
					break;
				case Types.VARCHAR:
					meta.setPropertyType("String");
					break;
				case Types.CLOB:
					meta.setPropertyType("String");
					break;
				case Types.DATE:
					meta.setPropertyType("java.util.Date");
					break;
				case Types.TIME:
					meta.setPropertyType("java.util.Date");
					break;
				case Types.TIMESTAMP:
					meta.setPropertyType("java.util.Date");
					break;
				case Types.BIGINT:
					meta.setPropertyType("long");
					break;
				case Types.INTEGER:
					meta.setPropertyType("int");
					break;
				case Types.SMALLINT:
					meta.setPropertyType("int");
					break;
				case Types.TINYINT:
					meta.setPropertyType("int");
					break;
				case Types.FLOAT:
					meta.setPropertyType("float");
					break;
				case Types.DOUBLE:
					meta.setPropertyType("double");
					break;
				case Types.BIT:
					meta.setPropertyType("int");
					break;

				default:
					meta.setPropertyType("Object");
				}
				list.add(meta);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 获得主键名。
	 * 
	 * @param tableName
	 *            表名
	 * @return 主键列表。
	 * @throws Exception
	 */
	public static List<MetaPrimaryKeyInfo> getPrimaryKey(String tableName) {
		Connection conn = null;
		ResultSet rs = null;
		List<MetaPrimaryKeyInfo> list = new ArrayList<MetaPrimaryKeyInfo>();
		try {
			conn = getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getPrimaryKeys(null, null, tableName);
			while (rs.next()) {
				MetaPrimaryKeyInfo meta = new MetaPrimaryKeyInfo();
				meta.setTableName(rs.getString("TABLE_NAME").toLowerCase());
				meta.setColumnName(rs.getString("COLUMN_NAME").toLowerCase());
				meta.setPropertyName(DmStringUtils.toClearCase(meta.getColumnName()));
				meta.setKeySeq(rs.getInt("KEY_SEQ"));
				meta.setPkName(rs.getString("PK_NAME").toLowerCase());
				list.add(meta);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
