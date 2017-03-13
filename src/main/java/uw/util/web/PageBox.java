package uw.util.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import uw.util.Cryptography;

/**
 * 把Page当成一个Box，通过这个类可以获取和Box相关的进出信息。
 * 
 * 获取http get/post信息,cookie信息，session信息。 输出一些常用的js脚本。
 * 
 */
public class PageBox {

	private static final Logger logger = LoggerFactory.getLogger(PageBox.class);

	/**
	 * 对应jsp的request
	 */
	private HttpServletRequest request = null;

	/**
	 * 对应jsp的response
	 */
	private HttpServletResponse response = null;

	/**
	 * 对应jsp的out
	 */
	private JspWriter out = null;

	/**
	 * 构造器
	 * 
	 * @param request
	 * @param response
	 * @param out
	 */
	public PageBox(HttpServletRequest request, HttpServletResponse response, JspWriter out) {
		this.request = request;
		this.response = response;
		this.out = out;
	}

	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return 如果为空，返回null;
	 */
	public String getParam(String paramName) {
		return getParam(paramName, "");
	}

	/**
	 * 获得一个http参数列表
	 * 
	 * @param paramName
	 *            参数名
	 * @return 如果为空，返回null;
	 */
	public String[] getParamNames(String paramName) {
		Map m = request.getParameterMap();
		String[] pns = new String[m.size()];
		int i = 0;
		for (Object o : m.keySet()) {
			pns[i] = o.toString();
		}
		return pns;
	}

	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @param defaultStr
	 *            默认值
	 * @return 如果从request取到的值为null或"",返回defaultStr,否则返回取到的值;
	 */
	public String getParam(String paramName, String defaultStr) {
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
	 * 获得一个boolean类型的参数。.
	 * 
	 * @param paramName
	 *            参数名。
	 * @return True 如果参数是1，则为真，其他情况为false。
	 */
	public boolean getBooleanParam(String paramName) {
		String temp = getParam(paramName);
		if (temp != null && temp.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得一个Double类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public double getDoubleParam(String paramName, double defaultNum) {
		String temp = getParam(paramName);
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
	 * @param paramName
	 *            参数名
	 * 
	 */
	public double getDoubleParam(String paramName) {
		return getDoubleParam(paramName, 0.00);
	}

	/**
	 * 获得一个int类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public int getIntParam(String paramName, int defaultNum) {
		String temp = getParam(paramName);
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
	 * @param paramName
	 *            参数名
	 * 
	 */
	public int getIntParam(String paramName) {
		return getIntParam(paramName, 0);
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @paramName defaultNum 默认值
	 * @return 返回指定的long值
	 */
	public long getLongParam(String paramName, long defaultNum) {
		String temp = getParam(paramName);
		if (temp != null && !temp.equals("")) {
			try {
				defaultNum = Long.parseLong(temp);
			} catch (Exception ignored) {
			}
		}
		return defaultNum;
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 */
	public long getLongParam(String paramName) {
		return getLongParam(paramName, 0);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param paramName
	 *            参数名
	 * @return
	 */
	public Date getDateParam(String paramName) {
		return getDateParam(paramName, null);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param paramName
	 *            参数名
	 * @return
	 */
	public Date getDateParam(String paramName, Date defaultDate) {
		String temp = getParam(paramName);
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

	/************************************************************************************/

	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return 如果为空，返回null;
	 */
	public String getCookie(String paramName) {
		return getCookie(paramName, "");
	}

	/**
	 * 获得一个String类型的Cookie参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @param defaultStr
	 *            默认值
	 * @return 如果从request取到的值为null或"",返回defaultStr,否则返回取到的值;
	 */
	public String getCookie(String paramName, String defaultStr) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(paramName)) {
				return cookie.getValue();
			}
		}
		return defaultStr;
	}

	/**
	 * 获得一个boolean类型的参数。.
	 * 
	 * @param paramName
	 *            参数名。
	 * @return True 如果参数是1，则为真，其他情况为false。
	 */
	public boolean getBooleanCookie(String paramName) {
		String temp = getCookie(paramName);
		if (temp != null && temp.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得一个Double类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public double getDoubleCookie(String paramName, double defaultNum) {
		String temp = getCookie(paramName);
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
	 * @param paramName
	 *            参数名
	 * 
	 */
	public double getDoubleCookie(String paramName) {
		return getDoubleCookie(paramName, 0.00);
	}

	/**
	 * 获得一个int类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public int getIntCookie(String paramName, int defaultNum) {
		String temp = getCookie(paramName);
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
	 * @param paramName
	 *            参数名
	 * 
	 */
	public int getIntCookie(String paramName) {
		return getIntCookie(paramName, 0);
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @paramName paramName 参数名
	 * @paramName defaultNum 默认值
	 * @return 返回指定的long值
	 */
	public long getLongCookie(String paramName, long defaultNum) {
		String temp = getCookie(paramName);
		if (temp != null && !temp.equals("")) {
			try {
				defaultNum = Long.parseLong(temp);
			} catch (Exception ignored) {
			}
		}
		return defaultNum;
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @param paramName
	 *            你想获得参数。
	 * 
	 */
	public long getLongCookie(String paramName) {
		return getLongCookie(paramName, 0);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param paramName
	 * @return
	 */
	public Date getDateCookie(String paramName) {
		return getDateCookie(paramName, null);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param paramName
	 * @return
	 */
	public Date getDateCookie(String paramName, Date defaultDate) {
		String temp = getCookie(paramName);
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

	/************************************************************************************/

	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return 如果为空，返回null;
	 */
	public String getSession(String paramName) {
		return getSession(paramName, "");
	}

	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @param defaultStr
	 *            默认值
	 * @return 如果从request取到的值为null或"",返回defaultStr,否则返回取到的值;
	 */
	public String getSession(String paramName, String defaultStr) {
		Object temp = getObjectSession(paramName);
		if (temp != null) {
			if (temp.toString().equals("")) {
				temp = defaultStr;
			}
		} else {
			temp = defaultStr;
		}
		if (temp == null)
			return null;
		else
			return temp.toString();
	}

	/**
	 * 获得一个session的Object
	 * 
	 * @param paramName
	 *            参数名
	 * @return
	 */
	public Object getObjectSession(String paramName) {
		return request.getSession().getAttribute(paramName);
	}

	/**
	 * 获得一个boolean类型的参数。.
	 * 
	 * @param paramName
	 *            参数名。
	 * @return True 如果参数是1，则为真，其他情况为false。
	 */
	public boolean getBooleanSession(String paramName) {
		String temp = getSession(paramName);
		if (temp != null && temp.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得一个Double类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public double getDoubleSession(String paramName, double defaultNum) {
		String temp = getSession(paramName);
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
	 * @param paramName
	 *            参数名
	 * 
	 */
	public double getDoubleSession(String paramName) {
		return getDoubleSession(paramName, 0.00);
	}

	/**
	 * 获得一个int类型的参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public int getIntSession(String paramName, int defaultNum) {
		String temp = getSession(paramName);
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
	 * @param paramName
	 *            参数名
	 * 
	 */
	public int getIntSession(String paramName) {
		return getIntSession(paramName, 0);
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @paramName paramName 参数名
	 * @paramName defaultNum 默认值
	 * @return 返回指定的long值
	 */
	public long getLongSession(String paramName, long defaultNum) {
		String temp = getSession(paramName);
		if (temp != null && !temp.equals("")) {
			try {
				defaultNum = Long.parseLong(temp);
			} catch (Exception ignored) {
			}
		}
		return defaultNum;
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @param paramName
	 *            你想获得参数。
	 * 
	 */
	public long getLongSession(String paramName) {
		return getLongSession(paramName, 0);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param paramName
	 * @return
	 */
	public Date getDateSession(String paramName) {
		return getDateSession(paramName, null);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param paramName
	 * @return
	 */
	public Date getDateSession(String paramName, Date defaultDate) {
		String temp = getSession(paramName);
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

	/************************************************************************************/
	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @ name 参数名
	 * 
	 * @return 如果为空，返回null;
	 */
	public String getAttribute(String name) {
		return getAttribute(name, "");
	}

	/**
	 * 获得一个String 类型的参数。
	 * 
	 * @ name 参数名 @ defaultStr 默认值
	 * 
	 * @return 如果从request取到的值为null或"",返回defaultStr,否则返回取到的值;
	 */
	public String getAttribute(String name, String defaultStr) {
		Object temp = request.getAttribute(name);
		if (temp != null) {
			if (temp.toString().equals("")) {
				temp = defaultStr;
			}
		} else {
			temp = defaultStr;
		}
		return temp.toString();
	}

	/**
	 * 获得一个Attribute的Object @ name 参数名
	 * 
	 * @return
	 */
	public Object getObjectAttribute(String name) {
		return request.getAttribute(name);
	}

	/**
	 * 获得一个boolean类型的参数。.
	 * 
	 * @ name 参数名。
	 * 
	 * @return True 如果参数是1，则为真，其他情况为false。
	 */
	public boolean getBooleanAttribute(String name) {
		String temp = getAttribute(name);
		if (temp != null && temp.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得一个Double类型的参数。
	 * 
	 * @ name 参数名
	 * 
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public double getDoubleAttribute(String name, double defaultNum) {
		String temp = getAttribute(name);
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
	 * 获得一个Double类型的参数。 @ name 参数名
	 * 
	 */
	public double getDoubleAttribute(String name) {
		return getDoubleAttribute(name, 0.00);
	}

	/**
	 * 获得一个int类型的参数。 @ name 参数名
	 * 
	 * @return int数值是从参数中获得，如果为空，则为一个默认值。
	 * 
	 */
	public int getIntAttribute(String name, int defaultNum) {
		String temp = getAttribute(name);
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
	 * @ name 参数名
	 * 
	 */
	public int getIntAttribute(String name) {
		return getIntAttribute(name, 0);
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @name name 参数名
	 * @name defaultNum 默认值
	 * @return 返回指定的long值
	 */
	public long getLongAttribute(String name, long defaultNum) {
		String temp = getAttribute(name);
		if (temp != null && !temp.equals("")) {
			try {
				defaultNum = Long.parseLong(temp);
			} catch (Exception ignored) {
			}
		}
		return defaultNum;
	}

	/**
	 * 获得一个Long类型的参数。
	 * 
	 * @ name 你想获得参数。
	 * 
	 */
	public long getLongAttribute(String name) {
		return getLongAttribute(name, 0);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @ name
	 * 
	 * @return
	 */
	public Date getDateAttribute(String name) {
		return getDateAttribute(name, null);
	}

	/**
	 * 获得一个日期类型。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @ name
	 * 
	 * @return
	 */
	public Date getDateAttribute(String name, Date defaultDate) {
		String temp = getAttribute(name);
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

	/************************************************************************************/

	/**
	 * 获取refererPage
	 * 
	 * @return
	 */
	public String getRefererURL(boolean echoHost) {
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
	 * @return
	 * @throws IOException
	 */
	public void outJS(String js) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type='text/javascript'>");
		sb.append(js);
		sb.append("</script>");
		out.print(sb.toString());
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @param request
	 * @return
	 */
	public void outJSAlert(String info) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type='text/javascript'>");
		sb.append("alert('").append(info).append("');");
		sb.append("</script>");
		out.print(sb.toString());
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @return
	 */
	public void outJSAlert(String info, String addJS) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type='text/javascript'>");
		sb.append("alert('").append(info).append("');");
		sb.append(addJS);
		sb.append("</script>");
		out.print(sb.toString());
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @return
	 */
	public void outHiddenParamFormCode(String actionPage) throws IOException {
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
		out.print(sb.toString());
	}

	/**
	 * 生成隐藏的参数区域
	 * 
	 * @return
	 */
	public String outHiddenParamCode(HttpServletRequest request) {
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

	public String genAllParamString() {
		return genAllParamString(null);
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
	public String genAllParamString(Map<String, String[]> overParams) {
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
			logger.error(e.getMessage(), e);
		}
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 获得当前页面的所有参数
	 * 
	 * @param overParams
	 *            覆盖的参数，可以为null
	 * @return
	 */
	public String genAllParamString(String repalceKey, String repalce) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> eu = request.getParameterNames();
		try {
			while (eu.hasMoreElements()) {
				String pn = eu.nextElement();
				if (pn != null) {
					String[] pvs = null;
					if (pn.equals(repalceKey)) {
						pvs = new String[] { repalce };
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
				sb.append(repalceKey).append('=').append(java.net.URLEncoder.encode(repalce, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 获得自己的url地址
	 * 
	 * @return
	 */
	public String getSelfURL(boolean echoHost, boolean echoQueryString) {
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
	public String encodeParam(String param, String encode) {
		String s = null;
		try {
			s = URLEncoder.encode(param, encode);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	/**
	 * encodeurl，默认用utf-8
	 * 
	 * @param param
	 * @return
	 */
	public String encodeParam(String param) {
		String s = null;
		try {
			s = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
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
	public String decodeParam(String param, String encode) {
		String s = null;
		try {
			s = URLDecoder.decode(param, encode);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	/**
	 * decode参数，默认使用utf-8
	 * 
	 * @param param
	 * @return
	 */
	public String decodeParam(String param) {
		String s = null;
		try {
			s = URLDecoder.decode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	/**
	 * 解析日期的方法。要求必须满足yyyy|MM|dd|HH|mm|ss的格式。 其中|可为任意字符。
	 * 
	 * @param text
	 * @return
	 */
	public java.util.Date parseDate(String text) {
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


	/**
	 * 判断是否通过代理转发 返回客户端的真实IP
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	public String getRealIp() {
		String forwardip = request.getHeader("X-Forwarded-For");
		if (forwardip == null || forwardip.equals("")) {
			return request.getRemoteAddr();
		} else {
			return forwardip;
		}
	}

	/**
	 * 适用于list界面下查询后对某一条目进行CRUD操作，操作完成后无法返回到原查询界面。
	 * 若暂时不需要跳转可继续调用此方法
	 * @param previousUrl
	 *            原来url链接，比如以‘修改’、‘删除’等字眼的链接
	 * @return
	 */
	public String keepStateBeforeLinkBegin(String previousUrl) {
		String ret = "";
		ret = genAllParamString();
		if (ret.indexOf("referenceUrl") != -1) {// 处于中转状态时，已经从前一页面等到初始状态参数值
			if (previousUrl.indexOf('?') != -1) {
				ret = previousUrl + "&" + ret;
			} else {
				ret = previousUrl + "?" + ret;
			}
			return ret;
		}
		try {
			ret = Cryptography.boxBase64(Base64.encode(ret.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		if (previousUrl.indexOf('?') != -1) {
			ret = previousUrl + "&referenceUrl=" + ret;
		} else {
			ret = previousUrl + "?referenceUrl=" + ret;
		}
		return ret;
	}

	/**
	 * 当‘添加’、‘修改’、‘删除’等操作完成后需要使用此方法封装来跳回到原来的页面
	 * 
	 * @param jumpBackLink
	 *            操作完成后，需要回到最初的list页的url
	 * @return
	 */
	public String keepStateBeforeLinkEnd(String jumpBackLink) {
		String ret = jumpBackLink;
		String param = getParam("referenceUrl", "");
		try {
			param = new String(Base64.decode(Cryptography.unboxBase64(param)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getLocalizedMessage());
		} catch (Base64DecodingException e) {
			logger.error(e.getLocalizedMessage());
		}
		if (ret.indexOf('?') != -1 && !"".equals(param)) {
			ret = ret+"&" + param;
		} else if (!"".equals(param)) {
			ret = ret+"?" + param;
		}
		return ret;
	}
}
