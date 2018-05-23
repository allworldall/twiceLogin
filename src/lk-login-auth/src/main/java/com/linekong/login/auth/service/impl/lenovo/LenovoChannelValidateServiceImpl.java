/**
 * 
 */
package com.linekong.login.auth.service.impl.lenovo;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.XmlUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	联想登录认证
 */
public class LenovoChannelValidateServiceImpl implements IVerifierRuleService {
	
	
	private final String PARAM_NAMES[] = {"lpsust", "realm"};

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//开发者平台生成的appid
		String appId = channelInfoMap.get("appid");
		
		//http请求参数token,open appid
		String paramValues[] = {String.valueOf(jsonData.get("token")), appId};
		String httpResult = channelServiceTemplate.getHttpsChannelService(url, PARAM_NAMES, paramValues, true);
		
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, String> httpMap = XmlUtil.parseXml(httpResult);
			//返回值不含有code的key时,表明验证成功
			if(!httpMap.containsKey("Code")){
				String accountId = httpMap.get("AccountID");
				loginFormBean.setUserId(accountId);
				return true;
			}else{ //返回值含有code的key时,渠道验证失败,返回失败的code值
				dataMap.put("error", httpMap.get("Code"));
				dataMap.put("msg", null);

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
