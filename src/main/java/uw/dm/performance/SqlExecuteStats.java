package uw.dm.performance;

import java.util.Date;

/**
 * 用于统计sql执行的性能数据
 * 
 */
public class SqlExecuteStats {

	/**
	 * connName 连接名
	 */
	private String connName;

	/**
	 * 执行的具体sql
	 */
	private String sql;

	/**
	 * 附加的参数
	 */
	private String param;

	/**
	 * 消耗的时间
	 */
	private long time;

	/**
	 * 异常类
	 */
	private String exception;
	
	/**
	 * 动作时间
	 */
	private Date actionDate;

	public SqlExecuteStats(String connName, String sql, String param, long time, String exception) {
		this.connName = connName;
		this.sql = sql;
		this.param = param;
		this.time = time;
		this.exception = exception;
		this.actionDate = new Date();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("connName:").append(connName).append("\n");
		sb.append("sql:").append(sql).append("\n");
		sb.append("param:").append(param).append("\n");
		sb.append("time:").append(time).append("ms\n");
		if (exception != null) {
			sb.append("exception:").append(exception);
		}
		return sb.toString();
	}

	/**
	 * @return the connName
	 */
	public String getConnName() {
		return connName;
	}

	/**
	 * @param connName
	 *            the connName to set
	 */
	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * @return the actionDate
	 */
	public Date getActionDate() {
		return actionDate;
	}

	/**
	 * @param actionDate the actionDate to set
	 */
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	
	

}
