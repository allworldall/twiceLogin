/**
 * 
 */
package com.linekong.login.auth.service.impl.letv;

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
 *	乐视登录认证
 */
public class LephoneChannelValidateServiceImpl implements IVerifierRuleService {

	private static final String PARAM_NAMES[] = {"access_token"};

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		
		//http请求参数
		String[] paramValues = {jsonData.getString("token")};
		//http返回值
		String httpResult = channelServiceTemplate.getHttpsChannelService(url, PARAM_NAMES, paramValues, false);
		
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//status值为1时,表明验证成功
			if(httpMap.get("status") != null && StringUtil.objectToInt(httpMap.get("status")) == ChannelServiceTemplate.RET_CODE_1){
				return true;
			}else{	//status值不为1时,表明渠道验证失败
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("error_code")));
				dataMap.put("msg", httpMap.get("error"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}	
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
