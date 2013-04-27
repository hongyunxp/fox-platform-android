package com.foxchan.foxutils.data;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 关于集合的工具类
 * @author foxchan@live.cn
 * @create 2013-4-27
 */
public class CollectionUtils {
	
	/** 用于连接字符串的连接符 */
	public static final String CONCAT_STR = "###";
	
	/** 用于字符串连接的容器 */
	private static StringBuilder sb = new StringBuilder();
	
	/**
	 * 判断数组是否为NULL或者为空
	 * @param objs	需要判断的对象数组
	 * @return		如果被检测的对象数组为NULL或者为空则返回true，否则返回false
	 */
	public static boolean isEmpty(final Object[] objs){
		if(objs == null || objs.length <= 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断列表容器是否为NULL或者没有元素
	 * @param list	需要判断的列表容器
	 * @return		如果被检测的列表容器为NULL或者为空，则返回true，否则返回false
	 */
	public static <E> boolean isEmpty(final List<E> list){
		if(list == null || list.size() <= 0) return true;
		return false;
	}
	
	/**
	 * 判断Map容器是否为NULL或者没有元素
	 * @param map	需要判断的Map容器
	 * @return		如果被检测的Map为NULL或者为空，则返回true，否则返回false
	 */
	public static boolean isEmpty(final Map<Object, Object> map){
		if(map == null || map.size() <= 0) return true;
		return false;
	}
	
	/**
	 * 判断Map容器是否为NULL或者没有元素
	 * @param map	需要判断的Map容器
	 * @return		如果被检测的Map为NULL或者为空，则返回true，否则返回false
	 */
	public static boolean isEmpty(final LinkedHashMap<String, String> map){
		if(map == null || map.size() <= 0) return true;
		return false;
	}
	
	/**
	 * 将指定的枚举类型转换成列表
	 * @param <E>		枚举类型的泛型
	 * @param enumType	需要转换的枚举类型
	 * @return			返回该枚举类型的所有枚举项的Map
	 */
	public static <E extends Enum<E>> Map<Integer, Object> getEnumMap(final Class<E> enumType){
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        //方法1
        EnumSet<E> set = EnumSet.allOf(enumType);
        for(Enum<E> e : set){
            map.put(e.ordinal(), e);
        }
        //方法2
        /*E[] enums = enumType.getEnumConstants();
        for(Enum<E> e : enums){
            map.put(e.ordinal(), e.toString());
        }*/
        return map;
    }
	
	/**
	 * 将指定的枚举类型转换成列表
	 * @param <E>		枚举类型的泛型
	 * @param enumType	需要转换的枚举类型
	 * @return			返回该枚举类型的所有枚举项的List
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> List<E> getEnumList(final Class<E> enumType){
		List<E> list = new ArrayList<E>();
		Map<Integer, Object> map = getEnumMap(enumType);
		for(Integer key : map.keySet()){
			list.add((E)map.get(key));
		}
		return list;
	}
	
	/**
	 * 将指定的枚举类型转换成字符串
	 * @param <E>		枚举类型的泛型
	 * @param enumType	需要转换的枚举类型
	 * @param concatStr	连接每一个枚举值的字符串，默认，默认连接符为“###”
	 * @return			返回该枚举类型的所有枚举项的字符串，每一个枚举类型间用concatStr隔开
	 */
	public static <E extends Enum<E>> String getEnumString(final Class<E> enumType, String concatStr){
		if(StringUtils.isEmpty(concatStr)) concatStr = StringUtils.CONCAT_STR;
		clearStringBuilder();
		Map<Integer, Object> map = getEnumMap(enumType);
		for(Integer key : map.keySet()){
			sb.append(map.get(key).toString()).append(concatStr);
		}
		sb.delete(sb.length()-concatStr.length(), sb.length());
		return sb.toString();
	}
	
	/**
	 * 将指定的枚举类型转换成字符串，连接符为“###”
	 * @param <E>		枚举类型的泛型
	 * @param enumType	需要转换的枚举类型
	 * @return			返回该枚举类型的所有枚举项的字符串，每一个枚举类型间用concatStr隔开
	 */
	public static <E extends Enum<E>> String getEnumString(final Class<E> enumType){
		return getEnumString(enumType, CONCAT_STR);
	}
	
	/**
	 * 清空StringBuilder中的内容
	 */
	private static final void clearStringBuilder(){
		sb = sb.delete(0, sb.length());
	}
	
	/**
	 * 将字符串的列表转化成字符串的数组
	 * @param list	需要转化的字符串列表
	 * @return		返回转化完成的字符串数组
	 */
	public static String[] tranferListToArray(List<String> list){
		if(!isEmpty(list)){
			String[] strs = new String[list.size()];
			for(int i = 0; i < list.size(); i++){
				strs[i] = list.get(i);
			}
			return strs;
		}
		return null;
	}
	
	/**
	 * 将字符串的数组转化成字符串的列表
	 * @param array	需要转化的字符串数组
	 * @return		返回转化好的字符串列表
	 */
	public static List<String> transferArrayToList(String[] array){
		if(!isEmpty(array)){
			List<String> list = new ArrayList<String>();
			for(String str : array){
				list.add(str);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 将字符串类型的LinkedHashMap转化为List<String>
	 * @param map	需要转化的LinkedHashMap
	 * @return		返回转化成的字符串列表
	 */
	public static List<String> transferMapToList(LinkedHashMap<String, String> map){
		if(!isEmpty(map)){
			List<String> list = new ArrayList<String>();
			for(String key : map.keySet()){
				list.add(map.get(key));
			}
			return list;
		}
		return null;
	}

}
