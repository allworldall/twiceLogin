/**
 * 
 */
package com.linekong.login.auth.service.impl.guopan;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	果盘登录认证
 */
public class GuopanChannelValidateServiceImpl implements IVerifierRuleService {

	private static final String PARAM_NAMES[] = {"game_uin", "appid", "token", "t", "sign"};
	private static final String RET_TRUE = "true";
	private static final String RET_NEGATIVE_1 = "-1";
	private static final String RET_NEGATIVE_2 = "-2";
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//平台生成的appid
		String appId = channelInfoMap.get("appid");
		//平台生成的secretkey
		String secretKey = channelInfoMap.get("secretkey");
		
		
		//时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());
		//md5签名: value = md5(gameuin + appid + timestamp + secretKey) 
		String sign = md5Util.getMD5(jsonData.get("gameuin") + appId + timestamp + secretKey);
		
		//http请求参数
		String[] paramValues = {jsonData.getString("gameuin"), appId, jsonData.getString("token"), timestamp, sign};
		//http返回值
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);

		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			//返回true表示成功
			if(RET_TRUE.equals(httpResult)){ 
				return true;
			}else if(RET_NEGATIVE_1.equals(httpResult)){ //返回-1表示加密串验证失败
				dataMap.put("error", RET_NEGATIVE_1);
				dataMap.put("msg", "加密串验证失败");

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}else if(RET_NEGATIVE_2.equals(httpResult)){ //返回-2表示APPID不存在
				dataMap.put("error", RET_NEGATIVE_2);
				dataMap.put("msg", "APPID不存在");

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}else{ //返回false表示验证失败
				dataMap.put("error", ChannelServiceTemplate.RET_CODE_0);
				dataMap.put("msg", "失败");

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
