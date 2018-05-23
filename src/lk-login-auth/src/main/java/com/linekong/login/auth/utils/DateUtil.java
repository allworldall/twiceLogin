/**
 * 
 */
package com.linekong.login.auth.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 *时间工具类
 */

public class DateUtil {

	//获取当前时间
	public static String getNowDateTime(){
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
	
}
