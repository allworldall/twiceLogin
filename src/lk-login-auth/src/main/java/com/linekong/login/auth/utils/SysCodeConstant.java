package com.linekong.login.auth.utils;

public class SysCodeConstant {
	
	public static final Object					ERROR							= -1;			//未知错误
	
	public static final Object					ERROR_VERSION					= -2;			//未知版本
	
	public static final Object					SUCCESS							= 1;			//操作成功
	
	public static final Object					ERROR_PARAM						= -100;			//参数错误
	
	public static final Object					ERROR_VALIDATE_PARAM			= -101;			//参数验证错误
	
	public static final Object					ERROR_DB						= -200;			//数据库操作异常
	
	public static final Object					ERROR_CHANNEL					= -300;			//第三方认证错误
	
	public static final Object					ERROR_HTTP						= -400;			//http请求错误
	
	public static final Object					ERROR_PARAM_CFG					= -500;			//数据库参数配置
	
	public static final Object                  ERROR_APPID                     = -600;         //appid不正确
	
	public static final Object 					ERROR_REDIS_CONNECTION			= "-13000";		//redis连接异常
	
	public static final Object					ERROR_REDIS_OPERATION			= "-130001";	//redis操作异常
	
	
	public static final Object					ERROR_SIGN_INFO					= "验签不通过";	//签名验证不通过

	
	public static final String					RESULT_STATUS_STRING			= "ret_status";	//返回状态
	
	public static final String					RESULT_STRING					= "result";		//data数据返回状态
	
	public static final String					SIGN_STRING						= "sign";		//data数据返回状态
	
}
