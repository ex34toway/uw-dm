package uw.dm.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uw.dm.ConnectionRouter;
import uw.dm.DataEntity;
import uw.dm.DataList;
import uw.dm.TransactionException;
import uw.dm.annotation.ColumnMeta;
import uw.dm.annotation.TableMeta;
import uw.dm.connectionpool.ConnectionWrapper;
import uw.dm.dialect.Dialect;
import uw.dm.dialect.DialectManager;
import uw.dm.util.DmReflectUtils;
import uw.dm.util.TaskMetaInfo;
import uw.dm.util.FieldMetaInfo;

/**
 * 实体类命令实现。 g
 * 
 * @author axeon
 *
 */
public class EntityCommandImpl {

	private static final Logger logger = LoggerFactory.getLogger(EntityCommandImpl.class);

	/**
	 * 实体信息缓存
	 */
	private static HashMap<String, TaskMetaInfo> entityMetaCache = new HashMap<String, TaskMetaInfo>();

	/**
	 * 保存一个实体
	 * 
	 * @param entity
	 * @return
	 * @throws TransactionException
	 */
	public static <T extends DataEntity> T save(DAOFactoryImpl dao, String connName, T entity, String tableName)
			throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		TaskMetaInfo emi = loadEntityMetaInfo(entity.getClass());
		if (emi == null) {
			throw new TransactionException("TaskMetaInfo[" + entity.getClass() + "] not found! ");
		}
		if (tableName == null || tableName.equals("")) {
			tableName = emi.getTableName();
		}

		if (connName == null || connName.equals("")) {
			connName = ConnectionRouter.getMappedDatabase(tableName, "write");
		}
		StringBuilder sb = new StringBuilder();
		Set<String> cols = entity.GET_UPDATED_COLUMN();
		if (cols.size() > 0) {
			sb.append("insert into ").append(tableName).append(" (");
			for (String col : cols) {
				sb.append(col).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(") values (");
			for (int i = 0; i < cols.size(); i++) {
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}

		Connection con = null;
		PreparedStatement pstmt = null;
		int effect = 0;
		try {
			con = dao.getTransactionController().getConnection(connName);
			pstmt = con.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			int seq = 0;
			for (String col : cols) {
				FieldMetaInfo fmi = emi.getFieldMetaInfo(col);
				if (fmi == null) {
					throw new TransactionException("FieldMetaInfo[" + col + "@" + entity.getClass() + "] not found! ");
				}
				DmReflectUtils.DAOLiteSaveReflect(pstmt, entity, fmi, ++seq);
			}
			effect = pstmt.executeUpdate();
			// 检查是否是自增的，如果是则回取id。
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) {
				long key = keys.getLong(1);
				List<FieldMetaInfo> pks = emi.getPklist();
				if (pks.size() > 0) {
					Field fd = pks.get(0).getField();
					fd.set(entity, key);
				}
			}
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in DAOCommandImpl.java:save()", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (dao.getTransactionController().isAutoCommit() && con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, sb.toString(), "", time, exception);

		return entity;
	}

	/**
	 * 加载一个实体。
	 * 
	 * @return
	 */
	public static <T> T load(DAOFactoryImpl dao, String connName, Class<T> cls, String tableName, Serializable id)
			throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		TaskMetaInfo emi = loadEntityMetaInfo(cls);
		if (emi == null) {
			throw new TransactionException("TaskMetaInfo[" + cls + "] not found! ");
		}
		if (tableName == null || tableName.equals("")) {
			tableName = emi.getTableName();
		}

		if (connName == null || connName.equals("")) {
			connName = ConnectionRouter.getMappedDatabase(tableName, "write");
		}
		StringBuilder sb = new StringBuilder();
		List<FieldMetaInfo> pks = emi.getPklist();
		sb.append("select * from ").append(tableName).append(" where ");
		if (pks.size() > 0) {
			FieldMetaInfo fmi =  pks.get(0);
			sb.append(fmi.getColumnName()).append("=? ");
		}

		T entity = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = dao.getTransactionController().getConnection(connName);
			pstmt = con.prepareStatement(sb.toString());
			int i = 0;
			DmReflectUtils.CommandUpdateReflect(pstmt, i + 1, id);
			ResultSet rs = pstmt.executeQuery();
			// 获得字段列表
			ResultSetMetaData rsm = rs.getMetaData();
			int colsCount = rsm.getColumnCount();
			String[] cols = new String[colsCount];
			for (int k = 0; k < colsCount; k++) {
				cols[k] = rsm.getColumnLabel(k + 1).toLowerCase();
			}

			if (rs.next()) {
				entity = cls.newInstance();
				for (String col : cols) {
					FieldMetaInfo fmi = emi.getFieldMetaInfo(col);
					if (fmi != null) {
						DmReflectUtils.DAOLiteLoadReflect(rs, entity, fmi);
					}
				}
			}
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in EntityCommandImpl.load()", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, sb.toString(), String.valueOf(id), time, exception);
		return entity;
	}

	/**
	 * 加载一个实体。
	 * 
	 * @return
	 */
	public static <T> T listSingle(DAOFactoryImpl dao, String connName, Class<T> cls, String selectsql,
			Object[] paramList) throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		if (connName == null) {
			connName = SQLUtils.getConnNameFromSQL(selectsql);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		TaskMetaInfo emi = loadEntityMetaInfo(cls);
		if (emi == null) {
			throw new TransactionException("TaskMetaInfo[" + cls.getName() + "] not found! ");
		}
		T entity = null;

		try {
			con = dao.getTransactionController().getConnection(connName);
			pstmt = con.prepareStatement(selectsql);
			int i = 0;

			if (paramList != null && paramList.length > 0) {
				for (i = 0; i < paramList.length; i++) {
					DmReflectUtils.CommandUpdateReflect(pstmt, i + 1, paramList[i]);
				}
			}

			ResultSet rs = pstmt.executeQuery();
			// 获得字段列表
			ResultSetMetaData rsm = rs.getMetaData();
			int colsCount = rsm.getColumnCount();
			String[] cols = new String[colsCount];
			for (int k = 0; k < colsCount; k++) {
				cols[k] = rsm.getColumnLabel(k + 1).toLowerCase();
			}

			if (rs.next()) {
				entity = cls.newInstance();
				for (String col : cols) {
					FieldMetaInfo fmi = emi.getFieldMetaInfo(col);
					if (fmi != null) {
						DmReflectUtils.DAOLiteLoadReflect(rs, entity, fmi);
					}
				}
			}
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in EntityCommandImpl.listSingle()", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, selectsql, Arrays.toString(paramList), time, exception);
		return entity;
	}

	/**
	 * 保存一个实体
	 * 
	 * @param entity
	 * @return
	 * @throws TransactionException
	 */
	public static int update(DAOFactoryImpl dao, String connName, DataEntity entity, String tableName)
			throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		TaskMetaInfo emi = loadEntityMetaInfo(entity.getClass());
		if (emi == null) {
			throw new TransactionException("TaskMetaInfo[" + entity.getClass() + "] not found! ");
		}
		if (tableName == null || tableName.equals("")) {
			tableName = emi.getTableName();
		}

		if (connName == null || connName.equals("")) {
			connName = ConnectionRouter.getMappedDatabase(tableName, "write");
		}
		StringBuilder sb = new StringBuilder();
		Set<String> cols = entity.GET_UPDATED_COLUMN();
		List<FieldMetaInfo> pks = emi.getPklist();
		sb.append("update ").append(tableName).append(" set ");
		for (String col : cols) {
			sb.append(col).append("=?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where ");
		for (int i = 0; i < pks.size(); i++) {
			FieldMetaInfo fmi = pks.get(i);
			sb.append(fmi.getColumnName()).append("=? ");
			if (i > 0) {
				sb.append("and ");
			}
		}

		Connection con = null;
		PreparedStatement pstmt = null;
		int effect = 0;
		try {
			con = dao.getTransactionController().getConnection(connName);
			pstmt = dao.getBatchUpdateController().prepareStatement(con, sb.toString());
			int seq = 0;
			for (String col : cols) {
				FieldMetaInfo fmi = emi.getFieldMetaInfo(col);
				if (fmi == null) {
					throw new TransactionException("FieldMetaInfo[" + col + "@" + entity.getClass() + "] not found! ");
				}
				DmReflectUtils.DAOLiteSaveReflect(pstmt, entity, fmi, ++seq);
			}
			// 开始where主键。
			for (FieldMetaInfo fmi : pks) {
				DmReflectUtils.DAOLiteSaveReflect(pstmt, entity, fmi, ++seq);
			}
			effect = pstmt.executeUpdate();
		} catch (Exception e) {
			exception = e.toString();
			throw new TransactionException("TransactionException in DAOCommandImpl.java:update()", e);
		} finally {
			if (!dao.getBatchUpdateController().getBatchStatus() && con != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (dao.getTransactionController().isAutoCommit() && con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, sb.toString(), "", time, exception);
		return effect;
	}

	/**
	 * 删除一个实体
	 * 
	 * @param entity
	 * @return
	 */
	public static int delete(DAOFactoryImpl dao, String connName, DataEntity entity, String tableName)
			throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		TaskMetaInfo emi = loadEntityMetaInfo(entity.getClass());
		if (emi == null) {
			throw new TransactionException("TaskMetaInfo[" + entity.getClass() + "] not found! ");
		}
		if (tableName == null || tableName.equals("")) {
			tableName = emi.getTableName();
		}

		if (connName == null || connName.equals("")) {
			connName = ConnectionRouter.getMappedDatabase(tableName, "write");
		}

		StringBuilder sb = new StringBuilder();
		List<FieldMetaInfo> pks = emi.getPklist();
		sb.append("delete from ").append(tableName);
		sb.append(" where ");
		for (int i = 0; i < pks.size(); i++) {
			FieldMetaInfo fmi = pks.get(i);
			sb.append(fmi.getColumnName()).append("=? ");
			if (i > 0) {
				sb.append("and ");
			}
		}

		Connection con = null;
		PreparedStatement pstmt = null;
		int effect = 0;
		try {
			con = dao.getTransactionController().getConnection(connName);
			pstmt = dao.getBatchUpdateController().prepareStatement(con, sb.toString());
			int seq = 0;
			// 开始where主键。
			for (FieldMetaInfo fmi : pks) {
				DmReflectUtils.DAOLiteSaveReflect(pstmt, entity, fmi, ++seq);
			}
			effect = pstmt.executeUpdate();
		} catch (Exception e) {
			exception = e.toString();

			throw new TransactionException("TransactionException in DAOCommandImpl.java:delete()", e);
		} finally {
			if (!dao.getBatchUpdateController().getBatchStatus() && con != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (dao.getTransactionController().isAutoCommit() && con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, sb.toString(), "", time, exception);
		return effect;
	}

	/**
	 * 获得列表。
	 * 
	 * @return
	 */
	public static <T> DataList<T> list(DAOFactoryImpl dao, String connName, Class<T> cls, String selectsql,
			Object[] paramList, int startIndex, int resultNum, boolean autoCount) throws TransactionException {
		long start = System.currentTimeMillis();
		String exception = null;
		if (connName == null) {
			connName = SQLUtils.getConnNameFromSQL(selectsql);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		Object[] po = null;
		TaskMetaInfo emi = loadEntityMetaInfo(cls);
		if (emi == null) {
			throw new TransactionException("TaskMetaInfo[" + cls.getName() + "] not found! ");
		}

		int allsize = 0;

		if (autoCount) {
			String countsql = "select count(1) from (" + selectsql + ") must_alias";
			allsize = SQLCommandImpl.selectForSingleValue(dao, connName, int.class, countsql, paramList);
		}

		List<T> list = new ArrayList<T>();

		try {
			con = dao.getTransactionController().getConnection(connName);
			if (resultNum > 0 && startIndex >= 0) {
				Dialect dialect = DialectManager.getDialect(((ConnectionWrapper) con).getDbType());
				po = dialect.getPagedSQL(selectsql, startIndex, resultNum);
				selectsql = po[0].toString();
			}

			pstmt = con.prepareStatement(selectsql);
			int i = 0;

			if (paramList != null && paramList.length > 0) {
				for (i = 0; i < paramList.length; i++) {
					DmReflectUtils.CommandUpdateReflect(pstmt, i + 1, paramList[i]);
				}
			}

			if (resultNum > 0 && startIndex >= 0) {
				pstmt.setInt(i + 1, (Integer) po[1]);
				pstmt.setInt(i + 2, (Integer) po[2]);
			}

			ResultSet rs = pstmt.executeQuery();
			// 获得字段列表
			ResultSetMetaData rsm = rs.getMetaData();
			int colsCount = rsm.getColumnCount();
			String[] cols = new String[colsCount];
			for (int k = 0; k < colsCount; k++) {
				cols[k] = rsm.getColumnLabel(k + 1).toLowerCase();
			}

			while (rs.next()) {
				T entity = cls.newInstance();
				for (String col : cols) {
					FieldMetaInfo fmi = emi.getFieldMetaInfo(col);
					if (fmi != null) {
						DmReflectUtils.DAOLiteLoadReflect(rs, entity, fmi);
					}
				}
				list.add(entity);
			}

		} catch (Exception e) {
			exception = e.toString();

			throw new TransactionException("TransactionException in EntityCommandImpl.list()", e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		long time = System.currentTimeMillis() - start;
		dao.addSqlExecuteStats(connName, selectsql, Arrays.toString(paramList), time, exception);
		return new DataListImpl<T>(list, startIndex, resultNum, allsize);
	}

	static String getTableName(Class<?> cls) {
		TaskMetaInfo emi = loadEntityMetaInfo(cls);
		if (emi != null) {
			return emi.getTableName();
		} else {
			return null;
		}
	}

	/**
	 * 加载读取pojo的注解信息
	 * 
	 * @param entityCls
	 * @return
	 */
	static TaskMetaInfo loadEntityMetaInfo(Class<?> entityCls) {

		TaskMetaInfo emi = entityMetaCache.get(entityCls.getName());
		if (emi == null) {
			emi = new TaskMetaInfo();
			if (entityCls.isAnnotationPresent(TableMeta.class)) {
				TableMeta tm = entityCls.getAnnotation(TableMeta.class);
				emi.setTableName(tm.tableName());
			}
			Field[] fields = entityCls.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);

				FieldMetaInfo fieldInfo = new FieldMetaInfo();
				if (field.isAnnotationPresent(ColumnMeta.class)) {
					ColumnMeta meta = field.getAnnotation(ColumnMeta.class);
					fieldInfo.setPropertyName(field.getName());
					fieldInfo.setColumnName(meta.columnName());
					fieldInfo.setPrimaryKey(meta.primaryKey());
					fieldInfo.setField(field);
					fieldInfo.setAutoIncrement(meta.autoIncrement());
					if (fieldInfo.isPrimaryKey()) {
						emi.addPklist(fieldInfo);
					}
					emi.addColumnMap(meta.columnName(), fieldInfo);
				}
			}
			entityMetaCache.put(entityCls.getName(), emi);
		}

		return emi;
	}

}
