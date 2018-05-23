/**
 * 
 */
package com.linekong.login.auth.service.impl.ali;

import java.util.HashMap;
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
 *	豌豆荚游戏登录认证
 */
public class WDJChannelValidateServiceImpl implements IVerifierRuleService{

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//开发者平台生成的appkey
		String appKey = channelInfoMap.get("appkey");
		//开发者平台生成的gameid
		String gameId = channelInfoMap.get("gameid");
		
		
		//md5生成签名,signValue="sid=" + sid + appKey;
		Map<String, String> signValueMap = new HashMap<String, String>();
		signValueMap.put("sid", String.valueOf(jsonData.get("token")));
		//加密值
		String signValue = channelServiceTemplate.getSignValueInfo(signValueMap) + appKey;
		String sign = md5Util.getMD5(signValue);
		
		//post请求信息id:时间戳, data:{"sid":token}, game:{"gameId":gameId}, sign:sign
		//http请求参数
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("id", 	System.currentTimeMillis());
		String token = String.valueOf(jsonData.get("token"));
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sid", token);
		valueMap.put("data", 	paramMap);
		paramMap = new HashMap<String, Object>();
		paramMap.put("gameId", Long.valueOf(gameId));
		valueMap.put("game", 	paramMap);
		valueMap.put("sign", 	sign);
		
		//http返回值
		String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, JSONUtil.objToJsonString(valueMap));
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//state中的code为1时,验证成功
			if(httpMap.get("state") != null && StringUtil.objectToInt(((Map<String, Object>)httpMap.get("state")).get("code")) == ChannelServiceTemplate.RET_CODE_1){
				return true;
			}else{ //渠道返回验证错误
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
