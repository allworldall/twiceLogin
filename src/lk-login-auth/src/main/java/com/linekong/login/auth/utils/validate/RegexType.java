package com.linekong.login.auth.utils.validate;
/**
 * 常用的数据类型枚举
 */
public enum RegexType {
	
	NONE,
	SPECIALCHAR,
	CHINESE,
	EMAIL,
	IP,
	NUMBER,
	NUMBER1,//数据必须是大于零的正整数
	PHONENUMBER;

}
