package uw.util.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个类用来获取参数。简化操作。
 */
public class ParamUtils {

	private static final Logger logger = LoggerFactory.getLogger(ParamUtils.class);

	
	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            参数名
	 * @return 如果为空，返回null;
	 */
	public static String getParameter(HttpServletRequest request, String paramName) {
		return getParameter(request, paramName, "");
	}

	public static String[] getGbkParameterValues(HttpServletRequest request, String paramName) {
		try {
			request.setCharacterEncoding("gbk");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return request.getParameterValues(paramName);
	}

	public static String getGbkParameter(HttpServletRequest request, String paramName, String defaultStr) {
		try {
			request.setCharacterEncoding("gbk");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		String temp = request.getParameter(paramName);
		if (temp != null) {
			if (temp.equals("")) {
				temp = defaultStr;
			}
		} else {
			temp = defaultStr;
		}
		return temp;
	}

	public static String getGbkParameter(HttpServletRequest request, String paramName) {
		return getGbkParameter(request, paramName, "");
	}

	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            参数名
	 * @param defaultStr
	 *            默认值
	 * @return 如果从request取到的值为null或"",返回defaultStr,否则返回取到的值;
	 */
	public static String getParameter(HttpServletRequest request, String paramName, String defaultStr) {
		String temp = request.getParameter(paramName);
		if (temp != null) {
			if (temp.equals("")) {
				temp = defaultStr;
			}
		} else {
			temp = defaultStr;
		}
		return temp;
	}

	/**
	 * 获取表单参数的数值。
	 * 
	 * param escapeHTMLTags 不允许使用htmltag。
	 */
	public static String getEscapeHTMLParameter(HttpServletRequest request, String paramName) {
		return escapeHTMLTags(ParamUtils.getParameter(request, paramName, ""));
	}

	/**
	 * 清除类似html的tag
	 * 
	 * @param input
	 * @return
	 */
	public static final String escapeHTMLTags(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			// return input;
			input = "";
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuilder buf = new StringBuilder(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * 获得一个boolean类型的参数。.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            你想获得参数。
	 * @return True 如果参数是1，则为真，其他情况为false。
	 */
	public static boolean getBooleanParameter(HttpServletRequest request, String paramName) {
		String temp = getParameter(request, paramName);
		if (temp != null && temp.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得一个返回值为int的boolean类型的参数。
	 * 
	 */
	public static int getIntBooleanParameter(HttpServletRequest request, String paramName) {
		int returnInt = 0;
		if (ParamUtils.getBooleanParameter(request, paramName)) {
			returnInt = 1;
		}
		return returnInt;

	}

	/**
	 * 获得一个Double类型的参数。
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            你想获得参数。
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public static double getDoubleParameter(HttpServletRequest request, String paramName, double defaultNum) {
		String temp = getParameter(request, paramName);
		if (temp != null && !temp.equals("")) {
			double num = defaultNum;
			try {
				num = Double.parseDouble(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	/**
	 * 获得一个Double类型的参数。
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            你想获得参数。
	 * 
	 */
	public static double getDoubleParameter(HttpServletRequest request, String paramName) {
		return getDoubleParameter(request, paramName, 0.00);
	}

	/**
	 * 获得一个int类型的参数。
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            你想获得参数。
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public static int getIntParameter(HttpServletRequest request, String paramName, int defaultNum) {
		String temp = getParameter(request, paramName);
		if (temp != null && !temp.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	/**
	 * 获得一个int类型的参数。
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            你想获得参数。
	 * 
	 */
	public static int getIntParameter(HttpServletRequest request, String paramName) {
		return getIntParameter(request, paramName, 0);
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @paramName request The HttpServletRequest object, known as "request" in a
	 *            JSP page.
	 * @paramName paramName 你想获得的参数名
	 * @paramName defaultNum 默认值
	 * @return 返回指定的long值
	 */
	public static long getLongParameter(HttpServletRequest request, String paramName, long defaultNum) {
		String temp = getParameter(request, paramName);
		if (temp != null && !temp.equals("")) {
			try {
				defaultNum = Long.parseLong(temp);
			} catch (Exception ignored) {
				logger.warn("ParamUtils's method:getLongParameter accured error:" + ignored);
			}
		}
		return defaultNum;
	}

	/**
	 * 获得一个int类型的参数。
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param paramName
	 *            你想获得参数。
	 * 
	 */
	public static long getLongParameter(HttpServletRequest request, String paramName) {
		return getLongParameter(request, paramName, 0);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static Date getDateParameter(HttpServletRequest request, String paramName) {
		return getDateParameter(request, paramName, null);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static Date getDateParameter(HttpServletRequest request, String paramName, Date defaultDate) {
		String temp = getParameter(request, paramName);
		if (temp != null && !temp.equals("")) {
			Date num = defaultDate;
			try {
				num = parseDate(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultDate;
		}
	}

	/**
	 * 获取refererPage
	 * 
	 * @param request
	 * @return
	 */
	public static String getRefererURL(HttpServletRequest request, boolean echoHost) {
		String referer = request.getHeader("referer");
		if (referer == null || referer.equals(""))
			return "";
		if (!echoHost) {
			referer = referer.substring(referer.indexOf("://") + 3, referer.length());
			referer = referer.substring(referer.indexOf("/"), referer.length());
		}
		return referer;
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @param request
	 * @return
	 */
	public static String genHiddenParamCode(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> eu = request.getParameterNames();
		while (eu.hasMoreElements()) {
			String pn = eu.nextElement();
			if (pn != null) {
				String[] pvs = request.getParameterValues(pn);
				if (pvs != null) {
					for (String pv : pvs) {
						sb.append("<input type='hidden' name='").append(pn).append("' value='").append(pv).append("'/>");
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @param request
	 * @return
	 */
	public static String genJS(String info) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type='text/javascript'>");
		sb.append(info);
		sb.append("</script>");
		return sb.toString();
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @param request
	 * @return
	 */
	public static String genJSAlert(String info) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type='text/javascript'>");
		sb.append("alert('").append(info).append("');");
		sb.append("</script>");
		return sb.toString();
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @param request
	 * @return
	 */
	public static String genJSAlert(String info, String addJS) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type='text/javascript'>");
		sb.append("alert('").append(info).append("');");
		sb.append(addJS);
		sb.append("</script>");
		return sb.toString();
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @param request
	 * @return
	 */
	public static String genHiddenParamFormCode(HttpServletRequest request, String actionPage) {
		StringBuilder sb = new StringBuilder();
		long now = System.currentTimeMillis();
		sb.append("<form method='post' id='hiddenForm").append(now).append("' action='").append(actionPage).append("'>");
		Enumeration<String> eu = request.getParameterNames();
		while (eu.hasMoreElements()) {
			String pn = eu.nextElement();
			if (pn != null) {
				String[] pvs = request.getParameterValues(pn);
				if (pvs != null) {
					for (String pv : pvs) {
						sb.append("<input type='hidden' name='").append(pn).append("' value='").append(pv).append("'/>");
					}
				}
			}
		}
		sb.append("<input type='submit' value='如果页面没有跳转，请点击按钮！' style='display:none;' />");
		sb.append("</form>");
		sb.append("<script type='text/javascript'>");
		sb.append("document.getElementById('hiddenForm").append(now).append("').submit();");
		sb.append("</script>");
		return sb.toString();
	}

	public static String getPageParameterString(HttpServletRequest request) {
		return getPageParameterString(request, null);
	}

	/**
	 * 获得当前页面的所有参数
	 * 
	 * @param request
	 *            jsp页面参数
	 * @param overParams
	 *            覆盖的参数，可以为null
	 * @return
	 */
	public static String getPageParameterString(HttpServletRequest request, Map<String, String[]> overParams) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> eu = request.getParameterNames();
		try {
			while (eu.hasMoreElements()) {
				String pn = eu.nextElement();
				if (pn != null) {
					String[] pvs = null;
					if (overParams != null) {
						pvs = overParams.get(pn);
						overParams.remove(pn);
					}
					if (pvs == null) {
						pvs = request.getParameterValues(pn);
					}
					if (pvs != null) {
						for (String pv : pvs) {
							sb.append(pn).append('=').append(java.net.URLEncoder.encode(pv, "UTF-8")).append('&');
						}
					}
				}
			}
			if (overParams != null) {
				for (Map.Entry<String, String[]> op : overParams.entrySet()) {
					String[] pvs = op.getValue();
					for (String pv : pvs) {
						sb.append(op.getKey()).append('=').append(java.net.URLEncoder.encode(pv, "UTF-8")).append('&');
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 获得当前页面的所有参数
	 * 
	 * @param request
	 *            jsp页面参数
	 * @param overParams
	 *            覆盖的参数，可以为null
	 * @return
	 */
	public static String getPageParameterString(HttpServletRequest request, String repalceKey, String repalceValue) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> eu = request.getParameterNames();
		try {
			while (eu.hasMoreElements()) {
				String pn = eu.nextElement();
				if (pn != null) {
					String[] pvs = null;
					if (pn.equals(repalceKey)) {
						pvs = new String[] { repalceValue };
					}
					if (pvs == null) {
						pvs = request.getParameterValues(pn);
					}
					if (pvs != null) {
						for (String pv : pvs) {
							sb.append(pn).append('=').append(java.net.URLEncoder.encode(pv, "UTF-8")).append('&');
						}
					}
				}
			}
			if (request.getParameter(repalceKey) == null) {
				sb.append(repalceKey).append('=').append(java.net.URLEncoder.encode(repalceValue, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 获得自己的url地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getSelfURL(HttpServletRequest request, boolean echoHost, boolean echoQueryString) {
		StringBuffer selfurl = new StringBuffer();
		if (echoHost) {
			selfurl.append("http://");
			selfurl.append(request.getServerName());
			if (request.getServerPort() != 80) {
				selfurl.append(":" + request.getServerPort());
			}
		}
		selfurl.append(request.getRequestURI());
		if (request.getQueryString() != null && echoQueryString) {
			selfurl.append("?").append(request.getQueryString());
		}
		return selfurl.toString();
	}

	/**
	 * encodeurl
	 * 
	 * @param param
	 * @param encode
	 * @return
	 */
	public static String encodeParam(String param, String encode) {
		String s = null;
		try {
			s = URLEncoder.encode(param, encode);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return s;
	}

	/**
	 * encodeurl，默认用utf-8
	 * 
	 * @param param
	 * @return
	 */
	public static String encodeParam(String param) {
		String s = null;
		try {
			s = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return s;
	}

	/**
	 * decode参数
	 * 
	 * @param param
	 * @param encode
	 * @return
	 */
	public static String decodeParam(String param, String encode) {
		String s = null;
		try {
			s = URLDecoder.decode(param, encode);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return s;
	}

	/**
	 * decode参数，默认使用utf-8
	 * 
	 * @param param
	 * @return
	 */
	public static String decodeParam(String param) {
		String s = null;
		try {
			s = URLDecoder.decode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
		return s;
	}

	/**
	 * 解析日期的方法。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param text
	 * @return
	 */
	public static java.util.Date parseDate(String text) {
		int yy = 0, MM = 0, dd = 0, HH = 0, mm = 0, ss = 0;
		if (text.length() >= 4) {
			yy = Integer.parseInt(text.substring(0, 4));
		}
		if (text.length() >= 7) {
			MM = Integer.parseInt(text.substring(5, 7));
		}
		if (text.length() >= 10) {
			dd = Integer.parseInt(text.substring(8, 10));
		}
		if (text.length() >= 13) {
			HH = Integer.parseInt(text.substring(11, 13));
		}
		if (text.length() >= 16) {
			mm = Integer.parseInt(text.substring(14, 16));
		}
		if (text.length() >= 19) {
			ss = Integer.parseInt(text.substring(17, 19));
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(yy, MM - 1, dd, HH, mm, ss);
		if (yy > 0)
			return calendar.getTime();
		else
			return null;
	}

}
