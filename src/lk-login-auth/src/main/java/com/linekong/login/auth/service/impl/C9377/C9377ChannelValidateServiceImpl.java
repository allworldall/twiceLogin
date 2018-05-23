package com.linekong.login.auth.service.impl.C9377;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年4月23日 上午9:57:13 
 */
public class C9377ChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//渠道的userName
		String userName = jsonData.getString("userName");
		//渠道的timeStamp
		String timeStamp = jsonData.getString("timeStamp");
		//渠道的token
		String token = jsonData.getString("token");
		//secretkey,双方约定，同支付的key
		String secretkey = channelInfoMap.get("secretkey");

		//通过userName + timeStamp + secretkey
		String signValue = md5Util.getMD5(userName + timeStamp + secretkey);
		if(token.equals(signValue)){
			return true;
		}else{	//渠道返回验证错误
			dataMap.put("error", SysCodeConstant.ERROR_PARAM);
			dataMap.put("msg", "参数错误,token验证失败");
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
		}
		return false;
	}

}
