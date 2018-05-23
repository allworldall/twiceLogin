/**
 * 
 */
package com.linekong.login.auth.service.impl.sina;

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
 * @author huxiaoming@lineklong.com
 *	新浪游戏登录认证
 */
public class SinaChannelValidateServiceImpl implements IVerifierRuleService {

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
		//开发者平台生成的secretkey
		String secretKey = channelInfoMap.get("secretkey");
		
		
		//请求信息
		Map<String, String> signValueMap = new HashMap<String, String>();
		signValueMap.put("token", 		String.valueOf(jsonData.get("token")));
		signValueMap.put("deviceid", 	String.valueOf(jsonData.get("deviceid")));
		signValueMap.put("appkey", 		appKey);
		signValueMap.put("suid", 		loginFormBean.getUserId());
		
		//md5生成签名,signValue=排序字符串+ "|" + secretKey;
		String signValue = channelServiceTemplate.getSignValueInfo(signValueMap) + "|" +secretKey;
		String sign = md5Util.getMD5(signValue);
		
		//post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("appkey", 	appKey));//appkey
		valuePairs.add(new BasicNameValuePair("token", 		String.valueOf(jsonData.get("token"))));//客户端发的token
		valuePairs.add(new BasicNameValuePair("suid", 		loginFormBean.getUserId()));//用户ID
		valuePairs.add(new BasicNameValuePair("signature", 	sign));//签名
		valuePairs.add(new BasicNameValuePair("deviceid", 	String.valueOf(jsonData.get("deviceid"))));//设备ID
		
		//http返回值
		String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//返回信息不包含error_code的字段时,验证成功
			if(!httpMap.containsKey("error_code")){
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
