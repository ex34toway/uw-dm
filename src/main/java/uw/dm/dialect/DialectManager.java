package uw.dm.dialect;

/**
 * 方言基类。 对于当前的情况，可能只有分页需要处理。
 * 
 * @author axeon
 * 
 */
public class DialectManager {

	public static Dialect getDialect(String type) {
		if ("oracle".equals(type))
			return new OracleDialect();
		else if ("mysql".equals(type))
			return new MySQLDialect();
		else
			return new OracleDialect();
	}

}
