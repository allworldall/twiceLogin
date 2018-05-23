package com.linekong.login.auth.service.impl.sogou;

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
 * sogou渠道登陆认证
 * @author jianhong
 * 2017-05-26
 */
public class SogouChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//开发者平台生成的gid
		String gid = channelInfoMap.get("appid");
		//开发者平台生成的appsecret
		String appsecret = channelInfoMap.get("secretkey");
		
		//请求信息
		Map<String, String> signValueMap = new HashMap<String, String>();
		signValueMap.put("gid", 		gid);
		signValueMap.put("sessionKey", 	String.valueOf(jsonData.get("token")));
		signValueMap.put("userId", 		loginFormBean.getUserId());
		
		//md5生成签名,signValue=排序字符串+ appsecret;
		String value = channelServiceTemplate.getSignValueInfo(signValueMap);
		String signValue = value+appsecret;
		String sign = md5Util.getMD5(signValue);
		
		//post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("gid", 	gid));//appkey
		valuePairs.add(new BasicNameValuePair("sessionKey", 		String.valueOf(jsonData.get("token"))));
		valuePairs.add(new BasicNameValuePair("userId", 		loginFormBean.getUserId()));
		valuePairs.add(new BasicNameValuePair("auth", 	sign));//签名
		
		//http返回值
		String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			String strCode = String.valueOf(httpMap.get("code"));
			long code = Long.parseLong(strCode);
			//返回信息code等于0验证成功
			if(code == ChannelServiceTemplate.RET_CODE_0){
				return true;
			}else{ //渠道返回验证错误
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("error_code")));
				dataMap.put("msg", 	 httpMap.get("error"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}

}
