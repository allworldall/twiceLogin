/**
 * 
 */
package com.linekong.login.auth.service.impl.anzhi;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.Base64;
import com.linekong.login.auth.utils.DateUtil;
import com.linekong.login.auth.utils.Des3Util;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	安智登录认证
 */
public class AnzhiChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//开发者平台生成的secretkey
		String secretKey = channelInfoMap.get("secretkey");
		//开发者平台生成的appkey
		String appkey = channelInfoMap.get("appkey");
		
		//客户端回传的地址
		String url = jsonData.getString("requesturl");
		
		//md5生成签名,signValue=appKey + token + secretKey;
		String sign = md5Util.getMD5(appkey + String.valueOf(jsonData.get("token")) + secretKey);
		
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("appkey", 	appkey));//appkey
		valuePairs.add(new BasicNameValuePair("cptoken", 	String.valueOf(jsonData.get("token"))));//客户端发的token
		valuePairs.add(new BasicNameValuePair("time", 		DateUtil.getNowDateTime()));//当前时间,类型为yyyyMMddHHmmssSSS
		valuePairs.add(new BasicNameValuePair("sign", 		sign));//签名
		valuePairs.add(new BasicNameValuePair("deviceId", 	String.valueOf(jsonData.get("deviceid"))));//设备ID
		
		//http返回值
		String httpResult = channelServiceTemplate.postHttpsChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//code为200时,验证成功
			if(httpMap.get("code") != null && StringUtil.objectToInt(httpMap.get("code")) == ChannelServiceTemplate.RET_CODE_1){
				byte[] retData = Base64.decode((String)httpMap.get("data"));
				try {
					String retDataJson = new String(retData, "utf-8");
					Map<String, Object> retDataMap = JSONUtil.objFromJsonString(retDataJson, Map.class);
					String uidSecret = (String)retDataMap.get("uid");
					String uid = Des3Util.decrypt(uidSecret, secretKey);
					
					loginFormBean.setUserId(uid);
					
					return true;
				} catch (UnsupportedEncodingException e) {
					LoggerUtil.error(AnzhiChannelValidateServiceImpl.class, "返回值转码(utf-8)异常" , e);
				}
			}else{ //渠道返回验证错误
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("code")));
				dataMap.put("msg", httpMap.get("msg"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}


}
