/**
 * 
 */
package com.linekong.login.auth.service.impl.pps;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 *	PPS登录认证
 *
 */
public class PpsChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String appKey = channelInfoMap.get("appkey");
		
		//md5生成签名,signValue=uid + "&" + time + "&" + appKey;
		String sign = md5Util.getMD5(loginFormBean.getUserId() + "&" + jsonData.get("time") + "&" + appKey);
						
		if(sign.equals(jsonData.get("sign"))){
			return true;
		}else{	//渠道返回验证错误
			dataMap.put("error", SysCodeConstant.ERROR);
			dataMap.put("msg", 	 "渠道验证错误");
			
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
		}
		
		return false;
	}

}
