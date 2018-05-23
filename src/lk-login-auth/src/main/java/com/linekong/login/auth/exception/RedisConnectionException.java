package com.linekong.login.auth.exception;
/**
 * Redis连接异常自定义异常处理类
 */
public class RedisConnectionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3715218178041745536L;
	
	
	public RedisConnectionException(String msg){
		super(msg);
	}
	

}
