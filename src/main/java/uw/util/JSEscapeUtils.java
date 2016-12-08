package uw.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模拟javascript的Escape工具。
 * 
 * @author axeon
 * 
 */
public class JSEscapeUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JSEscapeUtils.class);


	public static String escape(String src) {
		int i;
		char j;
		StringBuilder tmp = new StringBuilder();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		
		return tmp.toString();
	}

	public static String unescape(String src) {
		StringBuilder tmp = new StringBuilder();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else if (src.length() >= pos + 3) {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				} else {
					tmp.append('%');
					lastPos = pos + 1;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static void main(String[] args) {
//		System.out.println(unescape("where 1=1  and nick_name like '%ytang%'"));

	}

}