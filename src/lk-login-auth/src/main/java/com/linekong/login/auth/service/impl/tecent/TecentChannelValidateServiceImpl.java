package com.linekong.login.auth.service.impl.tecent;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	腾讯登录认证
 */
public class TecentChannelValidateServiceImpl implements IVerifierRuleService {
	private static final String PARAM_NAMES[] = {"appid", "sig", "openid", "openkey", "timestamp", "userip"};
	
	private static final String WX = "WX";
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//手Q访问地址
		String url = channelInfoMap.get("goUrl_SQ");
		//手Q生成的appid
		String appId = channelInfoMap.get("appid_SQ");
		//手Q生成的appkey
		String appKey = channelInfoMap.get("appkey_SQ");
		
		if(WX.equals(jsonData.getString("type"))){
			//微信生成的appid
			appId = channelInfoMap.get("appid_WX");
			//微信访问地址
			url = channelInfoMap.get("goUrl_WX");
			//微信生成的appkey
			appKey = channelInfoMap.get("appkey_WX");
		}
		//（可选）用户的外网IP
		String userIp = jsonData.optString("userip");
		//指当前的标准时间戳
		String timestamp = String.valueOf((System.currentTimeMillis()));
		//sign = md5 ( appkey + timestamp ) 
		String sign = md5Util.getMD5(appKey + timestamp);
		//http请求参数
		String paramValues[] = {appId, sign, String.valueOf(jsonData.get("openid")), 
				String.valueOf(jsonData.get("token")), timestamp, userIp};
		
		//发送http请求
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
		
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//ret返回0表示验证成功
			if(httpMap.get("ret") != null && StringUtil.objectToInt(httpMap.get("ret")) == ChannelServiceTemplate.RET_CODE_0){
				
				return true;
			}else{ //ret不为0表示渠道验证失败
				dataMap.put("error", StringUtil.objectToInt(((Map<String, Object>)httpMap.get("state")).get("code")));
				dataMap.put("msg", ((Map<String, Object>)httpMap.get("state")).get("msg"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}
	
}
