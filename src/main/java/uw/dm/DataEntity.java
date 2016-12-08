package uw.dm;

import java.util.Set;

public interface DataEntity {
	/**
	 * 获得更改的字段列表.
	 */
	public Set<String> GET_UPDATED_COLUMN() ;

	/**
	 * 得到_UPDATED_INFO
	 */
	public String GET_UPDATED_INFO();
}
