package uw.dm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 匹配连接路由规则。
 * 通过connectionRouter.xml文件进行配置。
 * 
 * @author axeon
 * 
 */
public class ConnectionRouter {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionRouter.class);

	private static XMLConfiguration config;

	/**
	 * 匹配成功后的缓存信息表
	 */
	private static HashMap<String, String> tmap = new HashMap<String, String>();

	/**
	 * 存储原始的tableMap信息
	 */
	private static HashMap<String, ArrayList<MapObj>> configMap = new HashMap<String, ArrayList<MapObj>>();

	/**
	 * 采用静态载入的方式
	 */
	static {
		InputStream in = null;
		try {
			config = new XMLConfiguration();
			in = ConnectionRouter.class.getResourceAsStream("/connectionRouter.xml");
			config.load(in, "UTF-8");
		} catch (Exception e) {
			logger.error("connectionRouter.xml can't init!", e);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}

		// 规则解析
		List<Object> list = config.getList("mapList.map[@table]");
		for (int i = 0; i < list.size(); i++) {
			String table = (String) list.get(i);
			MapObj mo = new MapObj();
			mo.setTable(table);
			mo.setAccess(config.getString("mapList.map(" + i + ")[@access]", "all"));
			mo.setDatabase(config.getString("mapList.map(" + i + ")[@database]"));
			// 针对access方法进行分组。
			ArrayList<MapObj> mol = configMap.get(mo.getAccess());
			if (mol == null) {
				mol = new ArrayList<MapObj>();
				configMap.put(mo.getAccess(), mol);
			}
			mol.add(mo);
		}
	}

	/**
	 * 根据提供的table信息来获得映射连接。
	 * 
	 * @param table 表名
	 * @param access 方法all/write/read
	 * @return 连接名
	 */
	public static String getMappedDatabase(String table, String access) {
		String mk = table + "^" + access;
		String mv = tmap.get(mk);
		if (mv == null) {
			mv = map(table, access);
			tmap.put(mk, mv);
		}
		return mv;
	}

	/**
	 * 根据提供的table信息来获得映射连接。
	 * 
<<<<<<< HEAD
	 * @param table 表名
	 * @param access 方法all/write/read
=======
	 * @param 表名
	 * @param 方法all/write/read
>>>>>>> refs/remotes/github/master
	 * @return 连接名
	 */
	private static String map(String table, String access) {
		String mvalue = null;
		// 从适配的访问级别检查
		ArrayList<MapObj> mol = configMap.get(access);
		if (mol != null) {
			for (MapObj mo : mol) {
				if (table.matches(mo.getTable())) {
					mvalue = mo.getDatabase();
				}
			}
		}
		// 如果查不到access对应级别，那么从access=all中再查一边。
		if (mvalue == null) {
			mol = configMap.get("all");
			if (mol != null) {
				for (MapObj mo : mol) {
					if (table.matches(mo.getTable())) {
						mvalue = mo.getDatabase();
					}
				}
			}
		}
		return mvalue;
	}
}

class MapObj {

	/**
	 * 匹配的table名称
	 */
	private String table;

	/**
	 * 访问权限。
	 */
	private String access;

	/**
	 * 连接池名称
	 */
	private String database;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

}