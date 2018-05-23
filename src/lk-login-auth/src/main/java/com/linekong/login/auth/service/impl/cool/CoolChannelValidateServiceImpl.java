package com.linekong.login.auth.service.impl.cool;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * 
 * @author kangyf
 * 酷派登录认证
 *
 */
public class CoolChannelValidateServiceImpl implements IVerifierRuleService{

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//客户端SDK返回的登陆令牌
		String code = jsonData.optString("code");//通过酷派账户SDK获取的authorization code值，只能使用一次，有效期300秒，相同的参数, 请求一次就失效.
		
		//请求参数
		String grantType = channelInfoMap.get("grant_type");//授权类型，在本步骤中，此值为“authorization_code”。
		String clientId = channelInfoMap.get("appid");//申请酷派账户接入后，分配给应用的appid。
		String clientSecret = channelInfoMap.get("appkey");//申请酷派账户接入后，分配给应用的appkey。 
		String goUrl =channelInfoMap.get("goUrl");//接口地址
		String directUri =channelInfoMap.get("appkey");

		String[] paramNames = new String[]{"grant_type", "client_id", "redirect_uri", "client_secret", "code"};
		String[] paramValues = new String[]{grantType, clientId, clientSecret, directUri, code};

		//获取接口返回结果
		String result = channelServiceTemplate.getHttpsChannelService(goUrl, paramNames, paramValues, true);
		//解析http返回值
		if(!result.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			//json解析
			JSONObject jo = JSONObject.fromObject(result); 
	
			String error = jo.optString("error");
			//ResultCode=0则代表接口返回信息成功
			if(StringUtils.isBlank(error) || error.equals("0")){
				//根据获取的信息，执行业务处理
				loginFormBean.setUserId(jo.getString("openid"));
				//回传酷派的token
				dataMap.put("accessToken", jo.get("access_token"));
				return true;
	
			} else {
				dataMap.put("error", jo.optString("error"));
				dataMap.put("msg", jo.getString("error_description"));
	
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}
}
