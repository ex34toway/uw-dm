package uw.util.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * 为JSP提供日志服务。
 * @author axeon
 *
 */
public class JSPLogger {

	private static final Logger logger = LoggerFactory.getLogger("UW.JSPLogger");

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public static void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.debug(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public static void debug(Marker arg0, String arg1, Object arg2) {
		logger.debug(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public static void debug(Marker arg0, String arg1, Object[] arg2) {
		logger.debug(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public static void debug(Marker arg0, String arg1, Throwable arg2) {
		logger.debug(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String)
	 */
	public static void debug(Marker arg0, String arg1) {
		logger.debug(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public static void debug(String arg0, Object arg1, Object arg2) {
		logger.debug(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object)
	 */
	public static void debug(String arg0, Object arg1) {
		logger.debug(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[])
	 */
	public static void debug(String arg0, Object[] arg1) {
		logger.debug(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	public static void debug(String arg0, Throwable arg1) {
		logger.debug(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @see org.slf4j.Logger#debug(java.lang.String)
	 */
	public static void debug(String arg0) {
		logger.debug(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public static void error(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.error(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public static void error(Marker arg0, String arg1, Object arg2) {
		logger.error(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public static void error(Marker arg0, String arg1, Object[] arg2) {
		logger.error(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public static void error(Marker arg0, String arg1, Throwable arg2) {
		logger.error(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String)
	 */
	public static void error(Marker arg0, String arg1) {
		logger.error(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public static void error(String arg0, Object arg1, Object arg2) {
		logger.error(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object)
	 */
	public static void error(String arg0, Object arg1) {
		logger.error(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[])
	 */
	public static void error(String arg0, Object[] arg1) {
		logger.error(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	public static void error(String arg0, Throwable arg1) {
		logger.error(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @see org.slf4j.Logger#error(java.lang.String)
	 */
	public static void error(String arg0) {
		logger.error(arg0);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#getName()
	 */
	public String getName() {
		return logger.getName();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public static void info(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.info(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public static void info(Marker arg0, String arg1, Object arg2) {
		logger.info(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public static void info(Marker arg0, String arg1, Object[] arg2) {
		logger.info(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public static void info(Marker arg0, String arg1, Throwable arg2) {
		logger.info(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String)
	 */
	public static void info(Marker arg0, String arg1) {
		logger.info(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public static void info(String arg0, Object arg1, Object arg2) {
		logger.info(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object)
	 */
	public static void info(String arg0, Object arg1) {
		logger.info(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[])
	 */
	public static void info(String arg0, Object[] arg1) {
		logger.info(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	public static void info(String arg0, Throwable arg1) {
		logger.info(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @see org.slf4j.Logger#info(java.lang.String)
	 */
	public static void info(String arg0) {
		logger.info(arg0);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.slf4j.Logger#isDebugEnabled(org.slf4j.Marker)
	 */
	public boolean isDebugEnabled(Marker arg0) {
		return logger.isDebugEnabled(arg0);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.slf4j.Logger#isErrorEnabled(org.slf4j.Marker)
	 */
	public boolean isErrorEnabled(Marker arg0) {
		return logger.isErrorEnabled(arg0);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.slf4j.Logger#isInfoEnabled(org.slf4j.Marker)
	 */
	public boolean isInfoEnabled(Marker arg0) {
		return logger.isInfoEnabled(arg0);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.slf4j.Logger#isTraceEnabled(org.slf4j.Marker)
	 */
	public boolean isTraceEnabled(Marker arg0) {
		return logger.isTraceEnabled(arg0);
	}

	/**
	 * @return
	 * @see org.slf4j.Logger#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	/**
	 * @param arg0
	 * @return
	 * @see org.slf4j.Logger#isWarnEnabled(org.slf4j.Marker)
	 */
	public boolean isWarnEnabled(Marker arg0) {
		return logger.isWarnEnabled(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public static void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.trace(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public static void trace(Marker arg0, String arg1, Object arg2) {
		logger.trace(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public static void trace(Marker arg0, String arg1, Object[] arg2) {
		logger.trace(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public static void trace(Marker arg0, String arg1, Throwable arg2) {
		logger.trace(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String)
	 */
	public static void trace(Marker arg0, String arg1) {
		logger.trace(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public static void trace(String arg0, Object arg1, Object arg2) {
		logger.trace(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object)
	 */
	public static void trace(String arg0, Object arg1) {
		logger.trace(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[])
	 */
	public static void trace(String arg0, Object[] arg1) {
		logger.trace(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable)
	 */
	public static void trace(String arg0, Throwable arg1) {
		logger.trace(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @see org.slf4j.Logger#trace(java.lang.String)
	 */
	public static void trace(String arg0) {
		logger.trace(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public static void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.warn(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object)
	 */
	public static void warn(Marker arg0, String arg1, Object arg2) {
		logger.warn(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Object[])
	 */
	public static void warn(Marker arg0, String arg1, Object[] arg2) {
		logger.warn(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String,
	 *      java.lang.Throwable)
	 */
	public static void warn(Marker arg0, String arg1, Throwable arg2) {
		logger.warn(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String)
	 */
	public static void warn(Marker arg0, String arg1) {
		logger.warn(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object,
	 *      java.lang.Object)
	 */
	public static void warn(String arg0, Object arg1, Object arg2) {
		logger.warn(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object)
	 */
	public static void warn(String arg0, Object arg1) {
		logger.warn(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[])
	 */
	public static void warn(String arg0, Object[] arg1) {
		logger.warn(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	public static void warn(String arg0, Throwable arg1) {
		logger.warn(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @see org.slf4j.Logger#warn(java.lang.String)
	 */
	public static void warn(String arg0) {
		logger.warn(arg0);
	}

}
