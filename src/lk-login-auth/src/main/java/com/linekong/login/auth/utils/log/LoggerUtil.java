package com.linekong.login.auth.utils.log;

import org.apache.log4j.Logger;

public class LoggerUtil {
	
	private static Logger log = null;
	/**
	 * 正常日志信息记录
	 * @param Class 		clazz   要记录日志的类
	 * @param StringBuffer  buffer  记录的系统日志信息
	 * @param String		result	系统返回值
	 * @param long			start   记录日志的时间    
	 */
	public static void info(Class<?> clazz,StringBuffer buffer,String result,long begin){
		long end = System.currentTimeMillis();
		log = Logger.getLogger(clazz);
		log.info(buffer.toString()+";result="+result+";times="+(end-begin)+"millis");
	}
	/**
	 * 错误信息记录
	 * @param Class			clazz   要记录日志的类
	 * @param StringBuffer  buffer  记录的系统日志信息
	 * @param String		result  系统返回值
	 * @param long			begin   记录日志的时间
	 * 
	 */
	public static void error(Class<?> clazz,StringBuffer buffer,String result,long begin){
		long end = System.currentTimeMillis();
		log = Logger.getLogger(clazz);
		log.error(buffer.toString()+";result="+result+";times="+(end-begin)+"millis");
	}
	/**
	 * 正常日志信息记录
	 * @param Class         clazz  要记录日志的类
	 * @param String		info   要记录的日志信息
	 */
	public static void info(Class<?> clazz,String info){
		log = Logger.getLogger(clazz);
		log.info(info);
	}
	/**
	 * 错误日志信息记录
	 * @param Class			clazz  要记录日志的类
	 * @param String		info   要记录的错误日志信息
	 */
	public static void error(Class<?> clazz,String info){
		log = Logger.getLogger(clazz);
		log.error(info);
	}

	/**
	 * 错误日志信息记录
	 * @param Class			clazz  要记录日志的类
	 * @param String		info   要记录的错误日志信息
	 * @param Throwable     e      发生的异常信息
	 */
	public static void error(Class<?> clazz,String info, Throwable e){
		log = Logger.getLogger(clazz);
		log.error(info, e);
	}	
}
