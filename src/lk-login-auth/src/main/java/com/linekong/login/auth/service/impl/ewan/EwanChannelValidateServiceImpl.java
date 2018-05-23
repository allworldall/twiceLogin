/**
 * 
 */
package com.linekong.login.auth.service.impl.ewan;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	益玩游戏登录认证
 */
public class EwanChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//开发者平台生成的appkey
		String appKey = channelInfoMap.get("appkey");
		
		String signValue = jsonData.get("openid") + "|" + jsonData.get("token") + "|" + appKey;
		String sign = md5Util.getMD5(signValue);
		//如果返回的sign和生成的sign值一致,验证成功
		if(sign.equals(jsonData.get("sign"))){
			return true;
			
		}else{ //渠道验证失败
			dataMap.put("error", SysCodeConstant.ERROR);
			dataMap.put("msg", 	 "渠道验证失败");
			
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
		}
		return false;
	}

}
