package com.linekong.login.auth.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.linekong.login.auth.utils.log.LoggerUtil;

public class md5Util {

	/**
	 * MD5加密算法
	 * @param  str
	 * @return String
	 */
	public static String getMD5(String str){
		return DigestUtils.md5Hex(str);
	}
	
	public static byte[] md5(String str){
		return DigestUtils.md5(str);
	}
	
	/**
	 * HmacMD5算法
	 * @param msg 加密信息
	 * @param keyString 秘钥
	 * @return digest 结果
	 */
	  public static String getHmacMd5(String msg, String keyString) {
	    String digest = null;
	    try {
	      SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
	      Mac mac = Mac.getInstance("HmacMD5");
	      mac.init(key);

	      byte[] bytes = mac.doFinal(msg.getBytes("UTF-8"));

	      StringBuffer hash = new StringBuffer();
	      for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(0xFF & bytes[i]);
	        if (hex.length() == 1) {
	          hash.append('0');
	        }
	        hash.append(hex);
	      }
	      digest = hash.toString();
	    } catch (Exception ex) {
	    	LoggerUtil.error(md5Util.class, ex.getMessage());
	    }
	    return digest;
	  }
	  
	  
	  /**
		 * 验证MD5加密正确性
		 * @param 	map 	验证参数
		 * @return 	sign	md5验证值
		 */
		public static boolean comparatSign(Map<String, Object> map, String sign){
			Map<String, Object> comparatorMap = new TreeMap<String, Object>(
			        new Comparator<String>() {
			            public int compare(String obj1, String obj2) {
			                // 降序排序
			                return obj1.compareTo(obj2);
			            }
		        });
			comparatorMap.putAll(map);
			String result = "";
			for(Map.Entry<String, Object> entry : comparatorMap.entrySet()){
				result += entry.getKey() + "=" + entry.getValue() + "&";
			}
			
			if(result != ""){
				result = result.substring(0, result.length() - 1);
			}

            return getMD5(result).equals(sign);
			
		}
		
		
		/**
		 * 对排序后的字符串进行md5加密
		 * @param 	map 	验证参数
		 * @return 	String	md5值
		 */
		public static String getCompareStringMD5(Map<String, String> map){
			Map<String, Object> comparatorMap = new TreeMap<String, Object>(
			        new Comparator<String>() {
			            public int compare(String obj1, String obj2) {
			                // 降序排序
			                return obj1.compareTo(obj2);
			            }
		        });
			comparatorMap.putAll(map);
			String result = "";
			for(Map.Entry<String, Object> entry : comparatorMap.entrySet()){
				result += entry.getKey() + "=" + entry.getValue() + "&";
			}
			
			if(result != ""){
				result = result.substring(0, result.length() - 1);
			}
            return getMD5(result);
		}
}
