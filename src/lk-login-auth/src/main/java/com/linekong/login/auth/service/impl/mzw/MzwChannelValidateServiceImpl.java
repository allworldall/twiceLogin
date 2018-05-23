package com.linekong.login.auth.service.impl.mzw;

import java.util.Map;

import net.sf.json.JSONObject;


import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * MZW语音登陆认证
 * @author jianhong
 * 2017-05-17
 */
public class MzwChannelValidateServiceImpl implements IVerifierRuleService {
	private static final String PARAM_NAMES[] = {"token", "appkey"};	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//appKey
		String appKey = channelInfoMap.get("appkey");
		
		//token值
		String token = jsonData.getString("token");
        String[] paramValues = {token,appKey};
        
		//http返回值
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
		
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
            Map<String, Object> userMap = (Map<String, Object>)httpMap.get("user");
            int code = Integer.parseInt((String)httpMap.get("code"));
            String message = (String)httpMap.get("msg");
			//code为1时,验证成功
			if(code == ChannelServiceTemplate.RET_CODE_1){
				String userId = (String)userMap.get("uid");
				loginFormBean.setUserId(userId);
				return true;
			}else{	//渠道返回验证错误
				dataMap.put("error", code);
				dataMap.put("msg", message);

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}		

}
