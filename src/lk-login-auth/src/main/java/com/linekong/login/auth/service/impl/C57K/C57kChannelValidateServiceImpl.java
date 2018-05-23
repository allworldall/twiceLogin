package com.linekong.login.auth.service.impl.C57K;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年4月25日 下午3:37:50 
 */
public class C57kChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//渠道生成的appid
		String appId = channelInfoMap.get("appid");
		//渠道生成的appkey
		String appKey = channelInfoMap.get("appkey");
		//请求地址:https://api.idielian.com/api/cp/user/check
		String url = channelInfoMap.get("goUrl");
		
		//客户端传过来的memId
		String userId = loginFormBean.getUserId();
		//客户端传过来的token
		String token = jsonData.getString("token");
		
		//md5生成签名,signValue=app_id=appId&mem_id=userId&user_token=token&app_key=appKey;
		String signValue = "app_id=" + appId + "&mem_id=" + userId + "&user_token=" + token + "&app_key=" + appKey;
		String sign = md5Util.getMD5(signValue);
		
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("app_id", 	appId));//appId
		valuePairs.add(new BasicNameValuePair("mem_id", 	userId));//客户端发的userId
		valuePairs.add(new BasicNameValuePair("user_token", token));//客户端传的token
		valuePairs.add(new BasicNameValuePair("sign", 		sign));//签名
		
		//http返回值
		String httpResult = channelServiceTemplate.postHttpsChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			JSONObject jsonResult = JSONObject.fromObject(httpResult);
            int resultCode = jsonResult.getInt("status");
            if (resultCode == ChannelServiceTemplate.RET_CODE_1) {
                return true;
            } else {
                dataMap.put("error", resultCode);
                dataMap.put("msg", 	jsonResult.getString("msg"));
                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
