/**
 * 
 */
package com.linekong.login.auth.service;

import java.util.*;

import com.linekong.login.auth.utils.*;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linekong.login.auth.dao.LoginTokenCacheOperationDao;
import com.linekong.login.auth.dao.OperationDataBaseDao;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author Administrator
 *
 */
@Service
@Transactional
public class ChannelServiceTemplate {
	public	static 	final 	String							DEFAULT_ENCODING			= "UTF-8";
	public 	static 	final 	String  						DEFAULT_HTTP_CONTENT 		= "default";	//http请求错误默认值

    @Autowired
	private 					OperationDataBaseDao 			operationDataBaseDao;
	@Autowired
	private 					LoginTokenCacheOperationDao 	loginTokenCacheOperationDao;
    @Autowired
	private                     LockUtil                        lockUtil;

	public 	static 	final 	Header[] DEFAULT_HEADERS = {
            new BasicHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"),
    };
	
	/*
	 * 
	 * 表示成功的状态码
	 */
	
	public   static final 	int 	RET_CODE_0   = 0;
	public   static final 	int 	RET_CODE_1   = 1;
	public   static final 	int 	RET_CODE_100 = 100;
	public   static final 	int 	RET_CODE_200 = 200;
	public   static final 	int 	RET_CODE_2000 = 2000;
	public	 static final	String 	RET_STRING_SUCCESS = "success";
	public	 static final	String 	RET_STRING_SUCCESS_000 = "000";
	
	
	/**
	 * 发送HTTP的GET请求
	 * @param	url 		请求地址
	 * @param	paramNames	请求参数名称
	 * @param	paramValues	请求参数值
	 * @param	headers		请求信息头
	 * **/
	public String getChannelService(String url, String[] paramNames, String[] paramValues, Header[] headers){
		return HttpUtil.doGetWithResultContent(url, paramNames, paramValues, headers);
	}
	
	/**
	 * 发送HTTPS的GET请求
	 * @param	url 		请求地址
	 * @param	paramNames	请求参数名称
	 * @param	paramValues	请求参数值
	 * **/
	public String getHttpsChannelService(String url, String[] paramNames, String[] paramValues, boolean isEncode){
		return HttpUtil.doGetHttpsWithResultContent(url, paramNames, paramValues, isEncode);
	}
	
	/**
	 * 发送HTTP的POST请求
	 * @param	url 		请求地址
	 * @param	headers		请求参数头信息
	 * @param	valuePairs	请求参数值
	 * **/
	public String postChannelService(String url, Header[] headers, List<NameValuePair> valuePairs){
		return HttpUtil.doPostWithHeaders(url, headers, valuePairs);
	}
	
	/**
	 * 发送HTTP的POST请求
	 * @param url      请求地址
	 * @param headers  请求参数头信息
	 * @param jsonData 请求参数值
	 */
	public String postChannelService(String url, Header[] headers, String jsonData){
		return HttpUtil.doPostWithHeaders(url, headers, jsonData);
	}
	
	/**
	 * 发送HTTPS的POST请求
	 * @param	url 		请求地址
	 * @param	headers		请求参数头信息
	 * @param	jsonData	请求参数
	 * **/
	public String postHttpsChannelService(String url, Header[] headers, String jsonData){
		return HttpUtil.doHttpsPostWithHeaders(url, headers, jsonData);
	}
	/**
	 * 发送HTTPS的POST请求
	 * @param	url 		请求地址
	 * @param	headers		请求参数头信息
	 * @param	valuePairs	请求参数值
	 * **/
	public String postHttpsChannelService(String url, Header[] headers, List<NameValuePair> valuePairs){
		return HttpUtil.doHttpsPostWithHeaders(url, headers, valuePairs);
	}

	/**
	 * 保存token信息
	 * @param	loginFormBean 包括	渠道ID，游戏ID，版本号，玩家ID，token
	 * **/
	public Map<String, Object> addLoginTokenInfoPOJO(LoginFormBean loginFormBean){

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("channelId", loginFormBean.getChannelId());
        paramMap.put("gameId", loginFormBean.getGameId());
        paramMap.put("version", loginFormBean.getVersion());
        paramMap.put("userId", loginFormBean.getUserId());

        //查询该用户之前登录时未被使用的token，因为校验token时只能接收到userId和token，所以无法使用channelId + gameId + version + userId作为redis key
        //此时token亦未生成，无法作为查询条件
        Map<String, Object> map = null;
        int resultCode = RET_CODE_1;
        String unionKey = Common.getUserInfoKey(loginFormBean.getChannelId(), loginFormBean.getGameId(), loginFormBean.getVersion(), loginFormBean.getUserId());
        try {
            lockUtil.lock(unionKey);
            String token = operationDataBaseDao.queryTokenByUser(loginFormBean.getChannelId(), loginFormBean.getGameId(), loginFormBean.getVersion(), loginFormBean.getUserId());
            //如果没有未被使用的token，生成新的token
            if (token == null) {
                token = UUIDUtil.getUuid();
                paramMap.put("token", token);
                resultCode = operationDataBaseDao.insertLogLoginTokenPOJO(paramMap);
            }
            if (resultCode > 0) {
                //拼接redis key
                String redisKey = Common.getUserTokenKey(loginFormBean.getUserId(), token);
                //将token写入redis
                map = loginTokenCacheOperationDao.writeLoginToken(redisKey, token);
                //返回userId
                map.put("userId", loginFormBean.getUserId());
            } else {
                map = new HashMap<String, Object>();
                map.put("result", SysCodeConstant.ERROR_DB);
                map.put("token", null);
            }
        } catch (InterruptedException e) {
            LoggerUtil.error(ChannelServiceTemplate.class, "更新token同步操作异常，error_" + e.getMessage());
        } finally {
            lockUtil.release(unionKey);
        }
        return map;
	}

	/**
	 * 根据游戏ID和属性key值获取value
	 * @param 	cpId	渠道ID
	 * @param 	gameId	游戏ID
	 * @param	version		SDK版本号
	 * update ： 修改返回值类型 string -> Map<String,String> 2017/9/8
	 **/
	public Map<String,String> queryChannelInfo(long cpId, long gameId, String version){
		Map<String,String> resultMap = null;
		try{
			String redisKey = Common.getChannelInfoKey(cpId, gameId, version);
			resultMap = loginTokenCacheOperationDao.getChannelInfoMap(redisKey);
			if (MapUtils.isEmpty(resultMap)){
				resultMap = operationDataBaseDao.queryChannelInfo(cpId, gameId, version);
				if(MapUtils.isEmpty(resultMap)){
					LoggerUtil.error(ChannelServiceTemplate.class, "channel_" + cpId + ", version_ " + version + ", gameId_" + gameId + " 未配置参数");
				}else{
					loginTokenCacheOperationDao.writeChannelInfo(cpId, gameId, version, resultMap);
				}
			}
		}catch(Exception ex){
			LoggerUtil.error(ChannelServiceTemplate.class, ex.getMessage(), ex);
		}
		return resultMap;
	}
	
	/**
	 * 根据map的key排序并返回拼接字符串(Key值从小到大排序)
	 * @param 	map		map信息
	 * **/
	public String getSignValueInfo(Map<String, String> map){
		Map<String, String> comparatorMap = new TreeMap<String, String>(
		        new Comparator<String>() {
		            public int compare(String obj1, String obj2) {
		                // 降序排序
		                return obj1.compareTo(obj2);
		            }
	        });
		comparatorMap.putAll(map);
		String result = "";
		for(Map.Entry<String, String> entry : comparatorMap.entrySet()){
			result += entry.getKey() + "=" + entry.getValue() + "&";
		}
		if(result != ""){
			result = result.substring(0, result.length() - 1);
		}
		return result;
		
	}
	
	public int double2Int(double value){
		return (int)value;
	}
	
	public long double2Long(double value){
		return (long)value;
	}

}
