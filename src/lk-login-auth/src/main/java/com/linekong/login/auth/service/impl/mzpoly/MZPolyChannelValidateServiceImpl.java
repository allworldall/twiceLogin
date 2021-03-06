package com.linekong.login.auth.service.impl.mzpoly;

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
 * 魅族聚合渠道版本登录验证
 **/
public class MZPolyChannelValidateServiceImpl  implements IVerifierRuleService {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//开发者平台生成的appid
		String appId = channelInfoMap.get("appid");
		
		//http post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("app_id", 	appId));//游戏ID
		valuePairs.add(new BasicNameValuePair("token", jsonData.getString("token")));//token
		valuePairs.add(new BasicNameValuePair("ts", 		String.valueOf(System.currentTimeMillis())));//用户ID
		valuePairs.add(new BasicNameValuePair("uid", 		String.valueOf(loginFormBean.getUserId())));//当前时间戳
		
		//http返回值
		String httpResult = channelServiceTemplate.postHttpsChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//code为200,表示验证成功
			if(httpMap.get("code") != null && StringUtil.objectToInt(httpMap.get("code")) == ChannelServiceTemplate.RET_CODE_200){
				return true;
			}else{	//渠道验证失败
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("code")));
				dataMap.put("msg", 	httpMap.get("message"));
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}

}
