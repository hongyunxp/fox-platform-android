package com.foxchan.foxutils.data;

import java.util.UUID;

/**
 * 处理字符串的辅助类
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @created 2012-11-9
 */
public class StringUtils {
	
	/** 用于替换字符串的符号 */
	public static final String MASK = "...";
	/** 前缀 */
	public static final int PREFIX = 0x0000;
	/** 后缀 */
	public static final int SUFFIX = 0x0001;
	/** 用于连接字符串的连接符 */
	public static final String CONCAT_STR = "###";
	
	/** 用于字符串连接的容器 */
	private static StringBuilder sb = new StringBuilder();
	
	/**
	 * 判断对象是否为NULL
	 * @param obj	需要判断的对象
	 * @return		如果被检测的对象为NULL则返回true，否则返回true
	 */
	public static boolean isEmpty(final Object obj){
		if(obj == null) return true;
		return false;
	}
	
	/**
	 * 判断字符串是否为NULL或者为空值
	 * @param str	需要判断的字符串
	 * @return		如果被检测的字符串为NULL或者为空值则返回true，否则返回false
	 */
	public static boolean isEmpty(final String str){
		if(str == null || "".equals(str)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 缩短字符串的长度，默认的替换字符串为“...”
	 * @param str		需要处理的字符串
	 * @param maxLength	字符串的最大长度
	 * @return
	 */
	public static String cutString(final String str, final int maxLength){
		return cutString(str, maxLength, null);
	}
	
	/**
	 * 缩短字符串的长度，默认的替换字符串为“...”
	 * @param str		需要处理的字符串
	 * @param maxLength	字符串的最大长度
	 * @param mask		超过最大长度后显示的内容
	 * @return
	 */
	public static String cutString(String str, final int maxLength, String mask){
		if(isEmpty(mask)) mask = MASK;
		if(str.length() > maxLength){
			return str.substring(0, maxLength).concat(mask);
		}
		return str;
	}
	
	/**
	 * 延长字符串，在字符串的前面或者后面添加占位符
	 * @param str		需要处理的字符串
	 * @param length	字符串最后的长度
	 * @param position	占位符添加的位置，有两个取值：PREFIX为前缀，SUFFIX为后缀
	 * @return			返回延长后的字符串
	 */
	public static String extendString(String str, final int length, final int position){
		return extendString(str, length, null, position, false);
	}
	
	/**
	 * 延长字符串，在字符串的前面或者后面添加占位符
	 * @param str			需要处理的字符串
	 * @param length		字符串最后的长度
	 * @param position		占位符添加的位置，有两个取值：PREFIX为前缀，SUFFIX为后缀
	 * @param isDirected	为true时直接在原字符串前添加length个占位符，否则构建总长度一定的字符串
	 * @return				返回延长后的字符串
	 */
	public static String extendString(String str, final int length, final int position, boolean isDirected){
		return extendString(str, length, null, position, isDirected);
	}
	
	/**
	 * 延长字符串，在字符串的前面或者后面添加占位符
	 * @param str			需要处理的字符串
	 * @param length		字符串最后的长度
	 * @param mask			占位符，默认为0
	 * @param position		占位符添加的位置，有两个取值：PREFIX为前缀，SUFFIX为后缀
	 * @param isDirected	为true时直接在原字符串前添加length个占位符，否则构建总长度一定的字符串
	 * @return				返回延长后的字符串
	 */
	public static String extendString(String str, final int length,
			String mask, final int position, boolean isDirected) {
		if(isEmpty(mask)) mask = "0";
		int maskCount = 0;
		if(isDirected){
			maskCount = length;
		} else {
			if(str.length() >= length) return str;
			maskCount = length - str.length();
		}
		sb = sb.delete(0, sb.length());
		for(int i = 0; i < maskCount; i++){
			sb.append(mask);
		}
		if(position == PREFIX){
			sb = sb.append(str);
		} else if(position == SUFFIX){
			sb = sb.insert(0, str);
		}
		return sb.toString();
	}
	
	/**
	 * 延长字符串，在字符串的前面添加占位符
	 * @param str		需要处理的字符串
	 * @param length	字符串最后的长度
	 * @param mask		占位符，默认为0
	 * @return			返回延长后的字符串
	 */
	public static String extendStringPrefix(String str, final int length, String mask){
		return extendString(str, length, mask, PREFIX, false);
	}
	
	/**
	 * 延长字符串，在字符串的前面添加占位符
	 * @param str			需要处理的字符串
	 * @param length		字符串最后的长度
	 * @param mask			占位符，默认为0
	 * @param isDirected	为true时直接在原字符串前添加length个占位符，否则构建总长度一定的字符串
	 * @return				返回延长后的字符串
	 */
	public static String extendStringPrefix(String str, final int length, String mask, boolean isDirected){
		return extendString(str, length, mask, PREFIX, isDirected);
	}
	
	/**
	 * 延长字符串，在字符串的前面添加占位符
	 * @param str		需要处理的字符串
	 * @param length	字符串最后的长度
	 * @return			返回延长后的字符串
	 */
	public static String extendStringPrefix(String str, final int length){
		return extendString(str, length, null, PREFIX, false);
	}
	
	/**
	 * 延长字符串，在字符串的前面添加占位符
	 * @param str			需要处理的字符串
	 * @param length		字符串最后的长度
	 * @param isDirected	为true时直接在原字符串前添加length个占位符，否则构建总长度一定的字符串
	 * @return				返回延长后的字符串
	 */
	public static String extendStringPrefix(String str, final int length, boolean isDirected){
		return extendString(str, length, null, PREFIX, isDirected);
	}
	
	/**
	 * 延长字符串，在字符串的后面添加占位符
	 * @param str		需要处理的字符串
	 * @param length	字符串最后的长度
	 * @param mask		占位符，默认为0
	 * @return			返回延长后的字符串
	 */
	public static String extendStringSuffix(String str, final int length, String mask){
		return extendString(str, length, mask, SUFFIX, false);
	}
	
	/**
	 * 延长字符串，在字符串的后面添加占位符
	 * @param str			需要处理的字符串
	 * @param length		字符串最后的长度
	 * @param mask			占位符，默认为0
	 * @param isDirected	为true时直接在原字符串前添加length个占位符，否则构建总长度一定的字符串
	 * @return				返回延长后的字符串
	 */
	public static String extendStringSuffix(String str, final int length, String mask, boolean isDirected){
		return extendString(str, length, mask, SUFFIX, isDirected);
	}
	
	/**
	 * 延长字符串，在字符串的后面添加占位符
	 * @param str		需要处理的字符串
	 * @param length	字符串最后的长度
	 * @return			返回延长后的字符串
	 */
	public static String extendStringSuffix(String str, final int length){
		return extendString(str, length, null, SUFFIX, false);
	}
	
	/**
	 * 延长字符串，在字符串的后面添加占位符
	 * @param str			需要处理的字符串
	 * @param length		字符串最后的长度
	 * @param isDirected	为true时直接在原字符串前添加length个占位符，否则构建总长度一定的字符串
	 * @return				返回延长后的字符串
	 */
	public static String extendStringSuffix(String str, final int length, boolean isDirected){
		return extendString(str, length, null, SUFFIX, isDirected);
	}
	
	/**
	 * 将对象连接字符串
	 * @param strs	需要进行连接的内容
	 * @return		返回一次连接好的字符串
	 */
	public static String concat(final Object[] strs){
		clearStringBuilder();
		if(!isEmpty(strs)){
			for(Object str : strs){
				if(str != null){
					sb.append(str.toString());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 将对象连接成字符串，中间使用sign中的字符隔开
	 * @param strs	需要进行连接的内容
	 * @param sign	字符串与字符串之间的分隔符
	 * @return		返回一次连接好的字符串
	 */
	public static String concat(final Object[] strs, final String sign){
		if(!isEmpty(strs)){
			clearStringBuilder();
			for(Object str : strs){
				if(str != null){
					sb.append(sign).append(str.toString());
				}
			}
			return sb.substring(1);
		}
		return "";
	}
	
	/**
	 * 获得一个UUID字符串
	 * @return	返回一个UUID字符串，长度为32位
	 */
	public static String getUUID(){
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		return uuid;
	}
	
	/**
	 * 清空StringBuilder中的内容
	 */
	private static final void clearStringBuilder(){
		sb = sb.delete(0, sb.length());
	}
	
	/**
	 * 将一段字符串插入到原来的字符串中组成新的字符串
	 * @param str		需要拆分的字符串
	 * @param pointCut	用作拆分的字符串
	 * @param strToJoin	需要插入的字符串
	 * @return			返回重新组合后的字符串
	 */
	public static String joinAtPoint(String str, String pointCut, String strToJoin){
		String[] strs = str.split(pointCut);
		pointCut = pointCut.replace("[", "");
		pointCut = pointCut.replace("]", "");
		return concat(new Object[]{
				strs[0], strToJoin, pointCut, strs[1]
		});
	}
	
}
