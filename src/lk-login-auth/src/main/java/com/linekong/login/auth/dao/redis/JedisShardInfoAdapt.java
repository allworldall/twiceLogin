package com.linekong.login.auth.dao.redis;
import redis.clients.jedis.JedisShardInfo;

public class JedisShardInfoAdapt extends JedisShardInfo{

    public JedisShardInfoAdapt(RedisPasswordAdapt redisPasswordAdapt) {
        super(redisPasswordAdapt.getPassword());
    }
}
