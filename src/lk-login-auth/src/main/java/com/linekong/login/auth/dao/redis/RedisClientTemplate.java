package com.linekong.login.auth.dao.redis;


import java.util.Map;

import com.linekong.login.auth.exception.RedisConnectionException;
import com.linekong.login.auth.exception.RedisOperationException;
import com.linekong.login.auth.utils.SysCodeConstant;

import redis.clients.jedis.ShardedJedis;
public class RedisClientTemplate extends JedisDataSource{
	
	/**
	 * 设置单个键值
	 * @param  key
	 * @param   value
	 * @return String
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException 
	 */
	public String set(String key,String value) throws RedisConnectionException, RedisOperationException{
		Object result = SysCodeConstant.ERROR_REDIS_CONNECTION;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.set(key, value);
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis set error:"+e.getMessage());
		}finally {
			this.closeRedis(shardedJedis);
		}
		return (String) result;
		
	}
	/**
	 * 获取对应的key值
	 * @param  key
	 * @return String
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException 
	 */
	public String get(String key) throws RedisConnectionException, RedisOperationException{
		Object result = SysCodeConstant.ERROR_REDIS_CONNECTION;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.get(key); 
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis get error:"+e.getMessage());
		}finally{
			this.closeRedis(shardedJedis);
		}
		return (String) result;
	}
	/**
	 * 设置key值过期时间
	 * @param   key
	 * @param  expire 过期时间秒
	 * @return String
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException 
	 */
	public Long setExpire(String key,int expire) throws RedisConnectionException, RedisOperationException{
		Long result = 0L;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.expire(key,expire);
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis setExpire error:"+e.getMessage());
		}finally{
			this.closeRedis(shardedJedis);
		}
		return result;
	}
	/**
	 * 设置Redis的hashMap数据结构
	 * @param  key
	 * @param  map
	 * @return String 
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException 
	 */
	public String setMap(String key,Map<String, String> map) throws RedisConnectionException, RedisOperationException{
		Object result = SysCodeConstant.ERROR_REDIS_CONNECTION;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.hmset(key, map);
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis setMap error:"+e.getMessage());
		}finally{
			this.closeRedis(shardedJedis);
		}
		return (String) result;
	
	}
	/**
	 * 判断key值是否存在
	 * @param  key
	 * @return Boolean
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException 
	 */
	public boolean existKey(String key) throws RedisConnectionException, RedisOperationException{
		boolean result = false;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.exists(key); 
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis exists error:"+e.getMessage());
		}finally{
			this.closeRedis(shardedJedis);
		}
		return result;
	}
	/**
	 * 获取redis中key对应的map值
	 * @param  key
	 * @return Object
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException
	 */
	public Object hgetAll(String key) throws RedisConnectionException, RedisOperationException{
		Object result = SysCodeConstant.ERROR_REDIS_CONNECTION;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.hgetAll(key); 
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis hgetAll error:"+e.getMessage());
		}finally{
			this.closeRedis(shardedJedis);
		}
		return result;
	}
	/**
	 * 获取Redis中key对应Map field的值
	 * @param  key
	 * @param  field
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException 
	 */
	public Object hget(String key,String field) throws RedisConnectionException, RedisOperationException{
		Object result = SysCodeConstant.ERROR_REDIS_CONNECTION;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.hget(key, field); 
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis hget error:"+e.getMessage());
		}finally{
			this.closeRedis(shardedJedis);
		}
		return result;
	}
	/**
	 * 设置Redis key对应的Map中的值
	 * @param  key
	 * @param  field
	 * @param  value
	 * @return Long
	 * @throws RedisConnectionException 
	 * @throws RedisOperationException 
	 */
	public Object hset(String key,String field,String value) throws RedisConnectionException, RedisOperationException{
		Object result = SysCodeConstant.ERROR_REDIS_CONNECTION;
		ShardedJedis shardedJedis = this.getRedisClient();
		if(shardedJedis == null){
			throw new RedisConnectionException("get Redis Connection error");
		}
		try {
			result = shardedJedis.hset(key, field, value); 
		} catch (Exception e) {
			throw new RedisOperationException("invoke redis hset error:"+e.getMessage());
		}finally{
			this.closeRedis(shardedJedis);
		}
		return result;
	
	}
	
}
