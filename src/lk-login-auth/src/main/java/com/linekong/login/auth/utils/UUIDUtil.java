/**
 * 
 */
package com.linekong.login.auth.utils;

import java.util.UUID;

/**
 * @author Administrator
 *
 */
public class UUIDUtil {
	public static String getUuid(){
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString().replace("-", "");
		return uuidStr;
	}
}
