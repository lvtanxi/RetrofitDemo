package com.lv.test.net;

import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 */
public class ArrayUtils {

	/**
	 * 判断list是否为null或empty
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		if (list == null)
			return true;
		return list.isEmpty();
	}
	
	/**
	 * 判断list是否为null或empty
	 * @param list
	 * @return
	 */
	public static boolean isNotEmpty(List<?> list) {
		if(null == list || list.size() ==0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * 将所有类型的数组转换为String数组
	 * @param objs 数组集合
	 * @return 数组
	 */
	public static String[] toStringArray(Object[] objs) {
		if (objs == null)
			return new String[] {};
		String[] strs = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			strs[i] = String.valueOf(objs[i]);
		}
		return strs;
	}

	/**
	 * 将List<String>类型的数组转换为String数组
	 * @param list
	 * @return
	 */
	public static String[] toStringArray(List<String> list) {
		if (isEmpty(list)){
			return new String[] {};
		}
		int size=list.size();
		String[] strs=list.toArray(new String[size]);
		return strs;
	}

	/**
	 * 拷贝数组的值
	 * @param resouce 数据来源
	 * @param target 目标数据(可以为空)
	 */
	public static Object[] copyVal(Object[] resouce, Object[] target){
		if(null==target){
			target=new Object[resouce.length];
		}
		for (int i = 0; i < resouce.length; i++) {
			target[i]=resouce[i];
		}
		return target;
	}

	/**
	 * 判断数组是不是为空
	 * @param array 祖耀判断的数据
	 */
	public static boolean arrayEmpty(Object[] array){
		if(null==array||array.length==0){
			return true;
		}
		return false;
	}

	/**
	 * 判断数组是不是不为空
	 * @param array 祖耀判断的数据
	 */
	public static boolean arrayNotEmpty(Object[] array){
		if(null==array||array.length==0){
			return false;
		}
		return true;
	}

	/**
	 * 判断map集合为空
	 * @param map
	 * @return
	 */
	public static boolean mapEmpty(Map<?, ?> map){
		if(null==map||map.size()==0){
			return true;
		}
		return false;
	}

	/**
	 * 判断map集合为不为空
	 * @param map
	 * @return
	 */
	public static boolean mapNotEmpty(Map<?, ?> map){
		if(null==map||map.size()==0){
			return false;
		}
		return true;
	}

}
