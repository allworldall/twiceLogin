/**
 * 
 */
package com.linekong.login.auth.service.impl.downjoy;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	当乐登录认证
 */
public class DownjoyChannelValidateServiceImpl implements IVerifierRuleService {

	private static final String PARAM_NAMES[] = {"appid", "umid", "token", "sig"};
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url 		= channelInfoMap.get("goUrl");
		//开发者平台生成的appid
		String appId 	= channelInfoMap.get("appid");
		//开发者平台生成的appkey
		String appKey 	= channelInfoMap.get("appkey");
		
		//客户端传递的token
		String token = jsonData.getString("token");
		//签名:MD5(appId|appKey|token|umid)字符串
		String sign = md5Util.getMD5(appId + "|" + appKey + "|" + token + "|" + loginFormBean.getUserId());
		//参数:appid,token,提供的cp的用户id,签名
		String[] paramValues = {appId, loginFormBean.getUserId(), token, sign};
		
		//http返回值
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//msg_code为2000验证成功
			if(httpMap.get("msg_code") != null && StringUtil.objectToInt(httpMap.get("msg_code")) == ChannelServiceTemplate.RET_CODE_2000){
				//token有效
				if(httpMap.get("valid") != null && StringUtil.objectToInt(httpMap.get("valid")) == ChannelServiceTemplate.RET_CODE_1){
					return true;
				}else{ //token无效或者未验证
					dataMap.put("error", StringUtil.objectToInt(httpMap.get("msg_code")));
					dataMap.put("msg", "未验证或者无效");

					resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
				}
			}else{ //渠道返回验证失败
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("msg_code")));
				dataMap.put("msg", httpMap.get("msg_desc"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求异常
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
