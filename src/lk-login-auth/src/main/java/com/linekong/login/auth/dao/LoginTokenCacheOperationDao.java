package com.linekong.login.auth.dao;

import java.util.Map;

public interface LoginTokenCacheOperationDao {
	
	/**
	 * 写入Redis缓存
	 * @param  key    缓存key值
	 * @param  token  缓存token
	 * @return String 返回值
	 */
	Map<String,Object> writeLoginToken(String key,String token);
	/**
	 * 验证缓存中token信息
	 * @param  key 缓存key值
	 * @param  token
	 * @return Integer
	 */
	int validateToken(String key,String token);


	/**
	 * 获取缓存中的渠道信息的键值对
	 * @param key
	 * @return
	 */
	Map<String, String> getChannelInfoMap(String key);
	
	/**
	 * 往缓存中写入渠道信息
     * @param cpId 渠道ID
     * @param gameId 联运游戏ID
     * @param version sdk版本号
	 * @param value
	 */
    void writeChannelInfo(long cpId, long gameId, String version, Map<String, String> value);
}
