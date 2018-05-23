/**
 * 
 */
package com.linekong.login.auth.service.impl.papa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 *	啪啪游戏厅登录认证
 */
public class PapaChannelValidateServiceImpl implements IVerifierRuleService {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//平台生成的secretkey
		String secretKey = channelInfoMap.get("secretkey");
		//平台生成的appkey
		String appKey = channelInfoMap.get("appkey");
		
		//md5生成签名,sign = md5(appKey+secretKey+sort(params));
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_key", 	appKey);
		map.put("token", 	String.valueOf(jsonData.get("token")));
		map.put("uid", 		loginFormBean.getUserId());
		
		String sign = md5Util.getMD5(appKey +  secretKey + channelServiceTemplate.getSignValueInfo(map));
		
		//http post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("app_key", 	appKey));//商户标识
		valuePairs.add(new BasicNameValuePair("token", 		String.valueOf(jsonData.get("token"))));//token值
		valuePairs.add(new BasicNameValuePair("uid", 		loginFormBean.getUserId()));//开放平台用户编号
		valuePairs.add(new BasicNameValuePair("sign", 		sign));//签名
		
		//http请求
		String httpResult = channelServiceTemplate.postHttpsChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http请求返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//error的值为0表示验证成功
			if(httpMap.get("error") != null && StringUtil.objectToInt(httpMap.get("error")) == ChannelServiceTemplate.RET_CODE_0){
				//is_success = true 有效
				if((boolean)((Map<String, Object>)httpMap.get("data")).get("is_success")){
					return true;
				}else{	//is_success = false 无效
					dataMap.put("error", StringUtil.objectToInt(httpMap.get("error")));
					dataMap.put("msg", 	 ((Map<String, Object>) httpMap.get("data")).get("error_msg"));
					
					resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
				}
			}else{	//渠道验证失败
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
