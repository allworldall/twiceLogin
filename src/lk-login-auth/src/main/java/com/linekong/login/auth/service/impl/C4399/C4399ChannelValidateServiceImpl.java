/**
 * 
 */
package com.linekong.login.auth.service.impl.C4399;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	4399登录认证
 */
public class C4399ChannelValidateServiceImpl implements IVerifierRuleService {
	private static final String PARAM_NAMES[] = {"state", "uid"};
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		
		//token值,my平台的用户id
		String[] paramValues = {String.valueOf(jsonData.get("token")), loginFormBean.getUserId()};
		//http返回值
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//code为100时,验证成功
			if(httpMap.get("code") != null && StringUtil.objectToInt(httpMap.get("code")) == ChannelServiceTemplate.RET_CODE_100){
				
				return true;
			}else{	//渠道返回验证错误
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("code")));
				dataMap.put("msg", httpMap.get("message"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
