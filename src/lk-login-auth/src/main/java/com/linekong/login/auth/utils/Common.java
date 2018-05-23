package com.linekong.login.auth.utils;

/**
 * @author huxiaoming@linekong.com
 * @version 2017年9月4日 下午3:44:08 
 */
public class Common {
	
	public static final String MD5_LOGIN_VALIDATE_KEY = "linekongline";

	private static final String KEY_PREFIX = "login-";

    /**
     * 获取存储渠道信息的redis key
     * @param cpId
     * @return
     */
    public static String getCpInfoKey(long cpId){
        String redisKey = KEY_PREFIX.concat(Long.toString(cpId));
        return  redisKey;
    }

    /**
     * 获取存储渠道游戏信息的redis key
     * @param cpId
     * @param gameId
     * @return
     */
    public static String getCpGameInfoKey(long cpId, long gameId){
        String redisKey = KEY_PREFIX.concat(Long.toString(cpId)).concat("-").concat(Long.toString(gameId));
        return  redisKey;
    }

    /**
     * 获取存储渠道游戏版本信息的redis key
     * @param cpId 渠道id
     * @param gameId 联运游戏id
     * @param version sdk版本号
     * @return
     */
    public static String getChannelInfoKey(long cpId, long gameId, String version){
        String redisKey = KEY_PREFIX.concat(Long.toString(cpId)).concat("-").concat(Long.toString(gameId)).concat("-").concat(version);
        return redisKey;
    }

    /**
     * 获取用户登录token的redis key
     * @param token
     * @param userId 用户id
     * @return
     */
    public static String getUserTokenKey (String userId, String token) {
        String redisKey = userId.concat("-").concat(token);
        return redisKey;
    }

    /**
     * 获取用户唯一标识
     * @param cpId 渠道id
     * @param gameId 联运游戏id
     * @param version sdk版本号
     * @param userId 用户id
     * @return
     */
    public static String getUserInfoKey(long cpId, long gameId, String version, String userId){
        String key = Long.toString(cpId).concat("-").concat(Long.toString(gameId)).concat("-").concat(version).concat("-").concat(userId);
        return key;
    }
}
