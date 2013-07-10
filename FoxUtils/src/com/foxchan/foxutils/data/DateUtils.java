package com.foxchan.foxutils.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 处理日期类型的工具类
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @created 2012-11-9
 */
public class DateUtils {
	
	/** 日期的默认格式，1990-09-22 12:59:59 */
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 日期的中文格式，1990年09月22日 12点59分59秒 */
	public static final String DATE_PATTERN_CN = "yyyy年MM月dd日 HH点mm分ss秒";
	/** 秒的单位 */
	private static final String SECOND = "秒钟";
	/** 分的单位 */
	private static final String MINUTE = "分钟";
	/** 小时的单位 */
	private static final String HOUR = "小时";
	/** 天的单位 */
	private static final String DAY = "天";
	/** 月的单位 */
	private static final String MONTH = "月";
	/** 年的单位 */
	private static final String YEAR = "年";
	
	/**
	 * 格式化日期对象
	 * @param date		需要格式化的日期
	 * @param pattern	日期的格式：形如：yyyy-MM-dd HH:mm:ss
	 * @return			返回格式化后的日期字符串
	 */
	public static String formatDate(Date date, String pattern){
		if(date == null) return "";
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat (pattern);
	    return formatter.format(date);
	}
	
	/**
	 * 将日期格式化为形如：1990-09-22 23:59:59的样式
	 * @param date	需要格式化的日期
	 * @return		返回格式化后的日期字符串
	 */
	public static String formatDate(Date date){
		return formatDate(date, DATE_PATTERN);
	}
	
	/**
	 * 格式化当前的日期
	 * @param pattern	日期的格式：形如：yyyy-MM-dd HH:mm:ss
	 * @return			返回格式化后的日期字符串
	 */
	public static String formatNowDate(String pattern){
		return formatDate(new Date(), pattern);
	}
	
	/**
	 * 将当前的日期格式化为形如：1990-09-22 23:59:59的样式
	 * @return	返回格式化后的日期字符串
	 */
	public static String formatNowDate(){
		return formatNowDate(DATE_PATTERN);
	}
	
	/**
	 * 返回两个时间点之间的时间间隔
	 * @param from	开始的时间
	 * @param to	结束的时间
	 * @return		返回两个时间点之间的时间间隔，形如：10秒钟、1分钟、1小时、1天、1月、1年
	 */
	public static String datesInterval(Date from, Date to){
		long second = 1000;//转化为秒的进制
		long minute = 60 * second;//转化为分的进制
		long hour = 60 * minute;//转化为小时的进制
		long day = 24 * hour;//转化为天的进制
		long month = 30 * day;//转化为月份的进制
		long year = 12 * month;//转化为年的进制
		
		long fromTime = from.getTime();
		long toTime = to.getTime();
		long interval = toTime - fromTime;
		if(interval < second){
			return StringUtils.concat(new Object[]{"0", SECOND});
		} else if(interval < minute){
			return StringUtils.concat(new Object[]{interval / second, SECOND});
		} else if(interval < hour){
			return StringUtils.concat(new Object[]{interval / minute, MINUTE});
		} else if(interval < day){
			return StringUtils.concat(new Object[]{interval / hour, HOUR});
		} else if(interval < month){
			return StringUtils.concat(new Object[]{interval / day, DAY});
		} else if(interval < year){
			return StringUtils.concat(new Object[]{interval / month, MONTH});
		} else {
			return StringUtils.concat(new Object[]{interval / year, YEAR});
		}
	}
	
	/**
	 * 返回两个时间点之间的时间间隔，形如：昨天  16:14
	 * @param from	开始的时间
	 * @param to	结束的时间
	 * @param useFront	以哪一个时间点为准产生详细的时间：即16:14部分
	 * @return		返回两个时间点之间的时间间隔，形如：10秒钟、1分钟、1小时、1天、1月、1年
	 */
	public static String datesIntervalDetail(Date from, Date to, boolean useFront){
		String dateDetail = "";
		if(useFront){
			dateDetail = formatDate(from, "HH:mm");
		} else {
			dateDetail = formatDate(to, "HH:mm");
		}
		
		long second = 1000;//转化为秒的进制
		long minute = 60 * second;//转化为分的进制
		long hour = 60 * minute;//转化为小时的进制
		long day = 24 * hour;//转化为天的进制
		long twoDays = 2*day;//昨天
		long threeDays = 3*day;//前天
		
		long fromTime = from.getTime();
		long toTime = to.getTime();
		long interval = toTime - fromTime;
		if(interval < second){
			return StringUtils.concat(new Object[]{"0", SECOND, "前"});
		} else if(interval < minute){
			return StringUtils.concat(new Object[]{interval / second, SECOND, "前"});
		} else if(interval < hour){
			return StringUtils.concat(new Object[]{interval / minute, MINUTE, "前"});
		} else if(interval < day){
			return StringUtils.concat(new Object[]{interval / hour, HOUR, "前"});
		} else if(interval < twoDays){
			return StringUtils.concat(new Object[]{"昨天  ", dateDetail});
		} else if(interval < threeDays){
			return StringUtils.concat(new Object[]{"前天  ", dateDetail});
		} else {
			return useFront ? formatDate(from, "MM月dd日 HH:mm") : formatDate(to, "MM月dd日 HH:mm");
		}
	}
	
	/**
	 * 返回从目标时间到现在的时间间隔
	 * @param from	开始的时间
	 * @return		返回从目标时间到现在的时间间隔，形如：10秒钟、1分钟、1小时、1天、1月、1年
	 */
	public static String dateInterval(Date from){
		return datesInterval(from, new Date());
	}
	
	/**
	 * 从用户输入的日期字符串中构造日期对象
	 * @param dateStr	日期字符串，形如：1990-09-22 12:59:59
	 * @return			返回构造好的日期对象
	 * @throws ParseException 
	 */
	public static Date generateDateFrom(String dateStr) throws ParseException{
		return new SimpleDateFormat(DATE_PATTERN).parse(dateStr);
	}
	
	/**
	 * 获得目标日期的时间参数
	 * @return	返回目标日期的时间参数，一次为：年、月、日、时、分、秒
	 */
	private static int[] getDateParams(Date date){
		int[] dateParams = new int[6];
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		dateParams[0] = c.get(Calendar.YEAR);
		dateParams[1] = c.get(Calendar.MONTH);
		dateParams[2] = c.get(Calendar.DAY_OF_MONTH);
		dateParams[3] = c.get(Calendar.HOUR_OF_DAY);
		dateParams[4] = c.get(Calendar.MINUTE);
		dateParams[5] = c.get(Calendar.SECOND);
		return dateParams;
	}
	
	/**
	 * 获得目标日期的年份
	 * @param date	目标日期
	 * @return		返回目标日期的年份
	 */
	public static int getYear(Date date){
		return getDateParams(date)[0];
	}
	
	/**
	 * 获得目标日期的月份
	 * @param date	目标日期
	 * @return		返回目标日期的月份
	 */
	public static int getMonth(Date date){
		return getDateParams(date)[1];
	}
	
	/**
	 * 获得目标日期的天数
	 * @param date	目标日期
	 * @return		返回目标日期的天数
	 */
	public static int getDay(Date date){
		return getDateParams(date)[2];
	}
	
	/**
	 * 获得目标日期的小时
	 * @param date	目标日期
	 * @return		返回目标日期的小时
	 */
	public static int getHour(Date date){
		return getDateParams(date)[3];
	}
	
	/**
	 * 获得目标日期的分钟
	 * @param date	目标日期
	 * @return		返回目标日期的分钟
	 */
	public static int getMinute(Date date){
		return getDateParams(date)[4];
	}
	
	/**
	 * 获得目标日期的秒数
	 * @param date	目标日期
	 * @return		返回目标日期的秒数
	 */
	public static int getSecond(Date date){
		return getDateParams(date)[5];
	}
	
	/**
	 * 获得现在的年份
	 * @param date	目标日期
	 * @return		返回现在的年份
	 */
	public static int getYear(){
		return getDateParams(new Date())[0];
	}
	
	/**
	 * 获得现在的月份
	 * @param date	目标日期
	 * @return		返回现在的月份
	 */
	public static int getMonth(){
		return getDateParams(new Date())[1];
	}
	
	/**
	 * 获得现在的天数
	 * @param date	目标日期
	 * @return		返回现在的天数
	 */
	public static int getDay(){
		return getDateParams(new Date())[2];
	}
	
	/**
	 * 获得现在的小时
	 * @param date	目标日期
	 * @return		返回现在的小时
	 */
	public static int getHour(){
		return getDateParams(new Date())[3];
	}
	
	/**
	 * 获得现在的分钟
	 * @param date	目标日期
	 * @return		返回现在的分钟
	 */
	public static int getMinute(){
		return getDateParams(new Date())[4];
	}
	
	/**
	 * 获得现在的秒数
	 * @param date	目标日期
	 * @return		返回现在的秒数
	 */
	public static int getSecond(){
		return getDateParams(new Date())[5];
	}
	
	/**
	 * 将时间段格式化为00:00:00这样的形式
	 * @param milliseconds	时间段的秒数（单位：秒）
	 * @return				返回格式化后的时间字符串
	 */
	public static String formatTimeLong(long milliseconds){
		String datetime = "";
		String hour;
		String minute;
		String seconds;
		hour = (milliseconds / 3600 + "");
		milliseconds %= 3600;
		minute = (milliseconds / 60 + "");
		milliseconds %= 60;
		seconds = (milliseconds + "");
		//将字符串格式化为两位数
		hour = StringUtils.extendStringPrefix(hour, 2);
		minute = StringUtils.extendStringPrefix(minute, 2);
		seconds = StringUtils.extendStringPrefix(seconds, 2);
		datetime = StringUtils.concat(new Object[]{
				hour, ":", minute, ":", seconds
		});
		return datetime;
	}

}
