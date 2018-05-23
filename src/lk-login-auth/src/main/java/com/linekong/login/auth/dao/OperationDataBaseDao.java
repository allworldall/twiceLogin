package com.linekong.login.auth.dao;

import java.util.List;
import java.util.Map;

import com.linekong.login.auth.web.formBean.ValidateTokenFormBean;

public interface OperationDataBaseDao {
	/**
	 * 渠道服务器验证成功保存生成token并保存信息
	 * @param paramMap 保存(channelId, gameId, userId, version, token)
	 * **/
    int insertLogLoginTokenPOJO(Map<String, Object> paramMap);
	
	/**
	 * 游戏服务器重复调用并把token赋为已使用状态
	 * @param pojo SDK获取信息
	 * **/
    int updateLogLoginTokenPOJO(ValidateTokenFormBean pojo);
	
	/**
	 * 游戏服务器返回成功删除token
	 * @param pojo SDK获取信息
	 * **/
    int deleteLogLoginTokenPOJO();
	/**
	 * 检查token信息是否正确
	 * @param param[]  查询信息(channelId, gameId, userId, version, token)
	 * **/
    Object validateToken(Object param[]);
	
	/**
	 * 根据channelId,gameId查找appId
	 * @param channelId
	 * @param gameId
	 * @return
	 */
    long queryAppId(long channelId, long gameId);
	
	/**
	 * 根据游戏ID和属性key值获取value
     * @param   cpId   渠道id
	 * @param 	gameId	游戏ID
	 * @param	version  SDK版本号
	 * **/
	Map<String, String> queryChannelInfo(long cpId, long gameId, String version);

    /**
     * 查询用户未使用的token
     * @param   cpId   渠道id
     * @param 	gameId	游戏ID
     * @param	version  SDK版本号
     * @param userId 用户id
     * @return
     */
	String queryTokenByUser (long cpId, long gameId, String version, String userId);

    /**
     * 获取所有渠道验证的url
     * @return
     */
	List<Object> queryAllVerifyUrl();

}
