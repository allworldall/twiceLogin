/**
 * 
 */
package com.linekong.login.auth.service.impl.douyu;

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
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *斗鱼登录认证
 */
public class DouyuChannelValidateServiceImpl implements IVerifierRuleService {

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
		//appid
		String appid = channelInfoMap.get("appid");
		
		//客户端传递的token
		String token = jsonData.getString("token");
		//时间戳
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		//md5签名 value = token + timestamp + secretKey
		String sign = md5Util.getMD5(token + "&" + timestamp + "&" + secretKey);
		
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("game_id", 	appid));//分配的游戏id
		valuePairs.add(new BasicNameValuePair("sid", 		token));//token
		valuePairs.add(new BasicNameValuePair("timestamp", 	timestamp));//服务端自己获取当前时间戳
		valuePairs.add(new BasicNameValuePair("sign", 		sign));//签名
		
		//http返回值
		String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//error为0时,验证成功,返回data里面有uid
			if(httpMap.get("error") != null && StringUtil.objectToInt(httpMap.get("error")) == ChannelServiceTemplate.RET_CODE_0){
				String uid = String.valueOf(httpMap.get("data"));
				loginFormBean.setUserId(uid);
				return true;
			}else{ //渠道返回验证错误
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("error")));
				dataMap.put("msg", String.valueOf(httpMap.get("data")));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
