package com.linekong.login.auth.service.impl.youku;

import java.util.ArrayList;
import java.util.HashMap;
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
 * @author huxiaoming@lineklong.com
 *	优酷登录认证
 */
public class YoukuChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//开发者平台生成的secretkey
		String secretKey = channelInfoMap.get("secretkey");
		//开发者平台生成的appkey
		String appKey = channelInfoMap.get("appkey");
		
		//HmacMd5签名:signValue = "appkey=" + appkey + "&sessionid=" + token,HmacMd5(signValue, secretKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("appkey", 		appKey);
		map.put("sessionid", 	String.valueOf(jsonData.get("token")));
		String signValue = channelServiceTemplate.getSignValueInfo(map);
		String sign = md5Util.getHmacMd5(signValue, secretKey);
		//http post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("appkey", 	appKey));//应用key
		valuePairs.add(new BasicNameValuePair("sessionid", 	String.valueOf(jsonData.get("token"))));//客户端token值
		valuePairs.add(new BasicNameValuePair("sign", 		sign));//签名
		
		//发送http请求
		String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http请求返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			//如果返回值有status,表示验证成功
			if(!httpResult.equals("") && httpResult.contains("status")){
				return true;
			}else{	//如果返回值没有status,表示验证失败
				dataMap.put("error", SysCodeConstant.ERROR);
				dataMap.put("msg", "session过期或者没有用户信息");

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
