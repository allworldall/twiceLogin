package com.linekong.login.auth.dao.redis;

import org.springframework.beans.factory.annotation.Autowired;

import com.linekong.login.auth.utils.log.LoggerUtil;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisDataSource{
	@Autowired
	private ShardedJedisPool shardedJedisPool;
	
	/**
	 * 获取redis连接
	 */
	public ShardedJedis getRedisClient(){
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			return shardedJedis;
		} catch (Exception e) {
			LoggerUtil.error(JedisDataSource.class, "get redis connection error:"+e.getMessage());
			if(null!=shardedJedis){
				shardedJedis.close();
			}
		}
		return null;
	}
	/**
	 * 关闭连接
	 * @param ShardedJedis shardedJedis
	 */
	public void closeRedis(ShardedJedis shardedJedis){
		shardedJedis.close();
	}

	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}
	
}
