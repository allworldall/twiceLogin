/**
 * 
 */
package com.linekong.login.auth.service.impl.pptv;

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
 *	PPTV登录认证
 */
public class PptvChannelValidateServiceImpl implements IVerifierRuleService {

	//type:固定为login;app固定为mobgame
	private static final String PARAM_NAMES[] = {"type", "sessionid", "username", "app"};

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		String type = channelInfoMap.get("type");
		String app = channelInfoMap.get("app");
		
		//http请求参数
		String paramValues[] = {type, jsonData.getString("token"), jsonData.getString("username"), app};
		//发送http请求
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//status为1表示验证成功
			if(httpMap.get("status") != null && StringUtil.objectToInt(httpMap.get("status")) == ChannelServiceTemplate.RET_CODE_1){
				return true;
			}else{	//status不为1表示验证失败
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("status")));
				dataMap.put("msg", httpMap.get("message"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
