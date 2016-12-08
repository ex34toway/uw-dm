package uw.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 仅仅支持yyyy-MM-dd格式
 * 
 * @author axeon
 * 
 */
public class FastDateTools {

	private static final Logger logger = LoggerFactory.getLogger(FastDateTools.class);

	/**
	 * 通常的日期格式
	 */
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

	public static Date parseDate(String date) {
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static Date parseDateTime(String dateTime) {
		try {
			return dateTimeFormat.parse(dateTime);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getToday() {
		Calendar day = Calendar.getInstance();
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		if (day.get(Calendar.DAY_OF_MONTH) < 10) {
			sb1.append("-0").append(day.get(Calendar.DAY_OF_MONTH));
		} else {
			sb1.append('-').append(day.get(Calendar.DAY_OF_MONTH));
		}
		return sb1.toString();
	}

	/**
	 * 获得以今天为坐标偏移的量
	 * 
	 * @param offset
	 * @return
	 */
	public static String getTodayOffset(int offset) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DAY_OF_YEAR, offset);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		if (day.get(Calendar.DAY_OF_MONTH) < 10) {
			sb1.append("-0").append(day.get(Calendar.DAY_OF_MONTH));
		} else {
			sb1.append('-').append(day.get(Calendar.DAY_OF_MONTH));
		}
		return sb1.toString();
	}

	/**
	 * 获得以天数为坐标偏移的量
	 * 
	 * @param offset
	 * @return
	 */
	public static Date getDateOffset(Date date, int offset) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.add(Calendar.DAY_OF_YEAR, offset);
		return day.getTime();
	}

	/**
	 * 获得本月第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonth(java.util.Date date) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		sb1.append("-01");
		return sb1.toString();
	}

	/**
	 * 获得本月第一天
	 * 
	 * @return
	 */
	public static Date getFirstDateOfMonth(java.util.Date date) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.set(Calendar.DAY_OF_MONTH, 1);
		return day.getTime();
	}

	/**
	 * 获得本月最后一天
	 * 
	 * @return
	 */
	public static String getEndDayOfMonth(java.util.Date date) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.add(Calendar.MONTH, 1);
		day.set(Calendar.DAY_OF_MONTH, 1);
		day.add(Calendar.DAY_OF_MONTH, -1);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		if (day.get(Calendar.DAY_OF_MONTH) < 10) {
			sb1.append("-0").append(day.get(Calendar.DAY_OF_MONTH));
		} else {
			sb1.append('-').append(day.get(Calendar.DAY_OF_MONTH));
		}
		return sb1.toString();
	}

	/**
	 * 获得本月最后一天
	 * 
	 * @return
	 */
	public static Date getEndDateOfMonth(java.util.Date date) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.add(Calendar.MONTH, 1);
		day.set(Calendar.DAY_OF_MONTH, 1);
		day.add(Calendar.DAY_OF_MONTH, -1);
		return day.getTime();
	}

	/**
	 * 获得日期列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getDayWeekList(Date startDate, Date endDate) {
		List<String> list = new ArrayList<String>();
		Calendar now = Calendar.getInstance();
		now.setTime(startDate);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		while (now.getTime().getTime() <= endDate.getTime()) {
			list.add(formatDateWeek(now.getTime()));
			now.add(Calendar.DAY_OF_YEAR, 1);
		}
		return list;
	}

	/**
	 * 获得日期列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getDayList(Date startDate, Date endDate) {
		List<String> list = new ArrayList<String>();
		Calendar now = Calendar.getInstance();
		now.setTime(startDate);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		while (now.getTime().getTime() <= endDate.getTime()) {
			list.add(formatDate(now.getTime()));
			now.add(Calendar.DAY_OF_YEAR, 1);
		}
		return list;
	}

	/**
	 * 获得月份列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getMonthList(Date startDate, Date endDate) {
		List<String> list = new ArrayList<String>();
		Calendar now = Calendar.getInstance();
		now.setTime(startDate);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		while (now.getTime().getTime() <= endDate.getTime()) {
			list.add(formatMonth(now.getTime()));
			now.add(Calendar.MONTH, 1);
		}
		return list;
	}

	/**
	 * 获得年份列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getYearList(Date startDate, Date endDate) {
		List<String> list = new ArrayList<String>();
		Calendar now = Calendar.getInstance();
		now.setTime(startDate);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		while (now.getTime().getTime() <= endDate.getTime()) {
			list.add(formatYear(now.getTime()));
			now.add(Calendar.YEAR, 1);
		}
		return list;
	}

	/**
	 * 获得日期列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Date> getDateList(Date startDate, Date endDate) {
		List<Date> list = new ArrayList<Date>();
		Calendar now = Calendar.getInstance();
		now.setTime(startDate);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		while (now.getTime().getTime() <= endDate.getTime()) {
			list.add(now.getTime());
			now.add(Calendar.DAY_OF_YEAR, 1);
		}
		return list;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if (date == null)
			return null;
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		if (day.get(Calendar.DAY_OF_MONTH) < 10) {
			sb1.append("-0").append(day.get(Calendar.DAY_OF_MONTH));
		} else {
			sb1.append('-').append(day.get(Calendar.DAY_OF_MONTH));
		}
		return sb1.toString();
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatMonth(Date date) {
		if (date == null)
			return null;
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		return sb1.toString();
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatYear(Date date) {
		if (date == null)
			return null;
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR));
		return sb1.toString();
	}

	/**
	 * 格式化日期带星期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateWeek(Date date) {
		if (date == null)
			return null;
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		if (day.get(Calendar.DAY_OF_MONTH) < 10) {
			sb1.append("-0").append(day.get(Calendar.DAY_OF_MONTH));
		} else {
			sb1.append('-').append(day.get(Calendar.DAY_OF_MONTH));
		}
		int week = day.get(Calendar.DAY_OF_WEEK);
		switch (week) {
		case Calendar.MONDAY:
			sb1.append("(一)");
			break;
		case Calendar.TUESDAY:
			sb1.append("(二)");
			break;
		case Calendar.WEDNESDAY:
			sb1.append("(三)");
			break;
		case Calendar.THURSDAY:
			sb1.append("(四)");
			break;
		case Calendar.FRIDAY:
			sb1.append("(五)");
			break;
		case Calendar.SATURDAY:
			sb1.append("(六)");
			break;
		case Calendar.SUNDAY:
			sb1.append("(日)");
			break;
		}

		return sb1.toString();
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		if (date == null)
			return null;
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.YEAR)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		if (day.get(Calendar.DAY_OF_MONTH) < 10) {
			sb1.append("-0").append(day.get(Calendar.DAY_OF_MONTH));
		} else {
			sb1.append('-').append(day.get(Calendar.DAY_OF_MONTH));
		}
		sb1.append(" ");
		sb1.append(day.get(Calendar.HOUR_OF_DAY)).append(":");
		int minute = day.get(Calendar.MINUTE);
		sb1.append(minute < 10 ? "0" + minute : minute);
		return sb1.toString();
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		if (date == null)
			return null;
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(day.get(Calendar.HOUR_OF_DAY)).append(":");
		int minute = day.get(Calendar.MINUTE);
		sb1.append(minute < 10 ? "0" + minute : minute);
		return sb1.toString();
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatShortDateTime(Date date) {
		if (date == null)
			return null;
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.setTime(date);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(String.valueOf(day.get(Calendar.YEAR)).substring(2)).append('-');
		if (day.get(Calendar.MONTH) + 1 < 10) {
			sb1.append('0').append(day.get(Calendar.MONTH) + 1);
		} else {
			sb1.append(day.get(Calendar.MONTH) + 1);
		}
		if (day.get(Calendar.DAY_OF_MONTH) < 10) {
			sb1.append("-0").append(day.get(Calendar.DAY_OF_MONTH));
		} else {
			sb1.append('-').append(day.get(Calendar.DAY_OF_MONTH));
		}
		sb1.append(" ");
		sb1.append(day.get(Calendar.HOUR_OF_DAY)).append(":");
		int minute = day.get(Calendar.MINUTE);
		sb1.append(minute < 10 ? "0" + minute : minute);
		return sb1.toString();
	}

	/**
	 * 将整型的时间转换为可供计费的单元数字
	 * 
	 * @param totalTime
	 *            秒
	 * 
	 */
	public static String tranferByMinuter(int totalTime) {
		int hour = 0;
		int minuter = 0;
		int second = 0;
		hour = totalTime / 3600;
		minuter = totalTime % 3600 / 60;
		second = totalTime % 3600 % 60;
		StringBuilder sb = new StringBuilder(64);
		if (hour != 0) {
			sb.append(hour + "小时");
		}
		if (minuter != 0) {
			sb.append(minuter + "分钟");
		}
		sb.append(second + "秒");
		return sb.toString();
	}
}
