/**
 * 
 */
package com.linekong.login.auth.service.impl.toutiao;

import java.util.ArrayList;
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
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	今日头条游戏登录认证
 */
public class ToutiaoChannelValidateServiceImpl implements IVerifierRuleService {

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
		
		//post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("client_key", 	appKey));//appkey
		valuePairs.add(new BasicNameValuePair("access_token", 	String.valueOf(jsonData.get("token"))));//客户端发的token
		valuePairs.add(new BasicNameValuePair("uid", 			loginFormBean.getUserId()));//用户ID
		valuePairs.add(new BasicNameValuePair("check_safe", 	String.valueOf(jsonData.get("checksafe"))));//是否检查用户安全
		
		//http返回值
		String httpResult = channelServiceTemplate.postHttpsChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//message值为success时,请求响应成功
			if(httpMap.containsKey("message") && httpMap.get("message").equals(ChannelServiceTemplate.RET_STRING_SUCCESS)){
				httpMap = (Map<String, Object>)httpMap.get("data");
				//retCode为1表示验证通过
				int retCode = StringUtil.objectToInt(httpMap.get("verify_result"));
				if(httpMap.containsKey("verify_result") && retCode == ChannelServiceTemplate.RET_CODE_1){
					//1表示不安全,0表示安全
					if(httpMap.containsKey("safe_password")){
						dataMap.put("safepassword", StringUtil.objectToInt(httpMap.get("safe_password")));
					}
					return true;
				}else{	//retCode为0表示异常
					dataMap.put("error", retCode);
					dataMap.put("msg", 	 "验证失败");

					resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
				}

			}else{ //message值为error时,请求响应失败
				dataMap.put("error", StringUtil.objectToInt(((Map<String, Object>)httpMap.get("data")).get("error_code")));
				dataMap.put("msg", 	 ((Map<String, Object>)httpMap.get("data")).get("description"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
