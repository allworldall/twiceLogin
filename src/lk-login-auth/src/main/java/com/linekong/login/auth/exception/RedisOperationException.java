package com.linekong.login.auth.exception;

public class RedisOperationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6365476241514646576L;
	
	public RedisOperationException(String msg){
		super(msg);
	}

}
