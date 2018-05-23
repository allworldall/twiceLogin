package com.linekong.login.auth.utils;

public class TokenConstant {
	
	public static final int		TOKEN_EXPIRE_KEY     		= 864000;			//Key值过期时间（单位是秒）10天
	
	public static final int		TOKEN_EXPIRE_VALUE			= 300;				//Token值过期时间
	
	public static final int		TOKEN_EXPIRE				= -1309;			//Token过期
	
	public static final int		TOKEN_ERROR					= -1310;			//Token不正确
	
	public static final int		TOKEN_USE					= -1311;			//Token已经使用
	
	public static final int		TOKEN_KEY_NOT_EXIST			= -1312;			//Key值不存在
	
	public static final String  TOKEN_EXPIRE_STRING			= "expireTime";
	
	public static final String  TOKEN_STRING				= "token";
	
	public static final String  TOKEN_STATUS_STRING			= "status";
	
	public static final String  SET_TOKEN_SUCCESS			= "OK";

}
