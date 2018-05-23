/**
 * 
 */
package com.linekong.login.auth.utils;

import java.lang.reflect.Type;

import net.sf.json.JSONObject;

import com.google.gson.Gson;

/**
 * @author Administrator
 *
 */
public class JSONUtil {
	
	private static Gson gson = new Gson();
	
	/**
	 * 将对象转换为json数据
	 */
	public static String objToJsonString(Object obj){
		return gson.toJson(obj);
	}
	
	/**
	 * 将String转换为对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T objFromJsonString(String jsonString, Type type){
		if(StringUtil.isNotBlank(jsonString)){
			return (T) gson.fromJson(jsonString, type);
		}
		return null;
	}
	
	/**
	 * 将对象转换为json数据
	 */
	public static String toString(Object obj){
		JSONObject jValue = JSONObject.fromObject(obj); 
		return jValue.toString();
	}
	
	/**
	 * 将String转换为对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(String jsonString, Class<?> clazz){
		if(StringUtil.isNotBlank(jsonString)){
			JSONObject jValue = JSONObject.fromObject(jsonString); 
			return (T) JSONObject.toBean(jValue, clazz);
		}
		return null;
	}
}
