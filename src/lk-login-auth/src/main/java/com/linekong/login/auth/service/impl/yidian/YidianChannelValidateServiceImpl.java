/**
 * 
 */
package com.linekong.login.auth.service.impl.yidian;

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
 * @author Administrator
 *
 */
public class YidianChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		//访问地址:https://api.wx903.com/api/v7/cp/user/check
		String url = channelInfoMap.get("goUrl");
		//开发者平台生成的appkey
		String appKey = channelInfoMap.get("appkey");
		//开发者平台生成的appid
		String appId = channelInfoMap.get("appid");
		
		//sign 的签名规则：md5(app_id=...&mem_id=...&user_token=...&app_key=...)
		StringBuffer signBuffer = new StringBuffer();
		signBuffer.append("app_id=").append(appId).append("&mem_id=").append(loginFormBean.getUserId())
			.append("&user_token=").append(jsonData.getString("token")).append("&app_key=").append(appKey);
		String sign = md5Util.getMD5(signBuffer.toString());
		
		//post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("app_id", 	appId));//appId
		valuePairs.add(new BasicNameValuePair("user_token", jsonData.getString("token")));//客户端发的token
		valuePairs.add(new BasicNameValuePair("mem_id", 	loginFormBean.getUserId()));//用户ID
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
