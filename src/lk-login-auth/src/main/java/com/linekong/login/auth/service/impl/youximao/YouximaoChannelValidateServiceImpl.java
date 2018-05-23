package com.linekong.login.auth.service.impl.youximao;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年4月24日 下午4:17:34 
 */
public class YouximaoChannelValidateServiceImpl implements IVerifierRuleService {


	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//渠道生成的openId
		String openId = jsonData.getString("openId");
		//url:https://sdk.youximao.tv/cp/getUserByOpenId
		String url = channelInfoMap.get("goUrl");
        String[] paramNames = new String[]{"openId"};
        String[] paramValues = new String[]{openId};
		//http返回值
        String httpResult = channelServiceTemplate.getHttpsChannelService(url, paramNames, paramValues, true);

		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			JSONObject jsonResult = JSONObject.fromObject(httpResult);
            String resultCode = jsonResult.getString("code");
            if (resultCode.equals(ChannelServiceTemplate.RET_STRING_SUCCESS_000)) {
            	loginFormBean.setUserId(openId);
                return true;
            } else {
                dataMap.put("error", resultCode);
                dataMap.put("msg", 	jsonResult.getString("message"));
                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
		}else{	//请求渠道验证错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}

}
