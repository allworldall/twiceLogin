package com.linekong.login.auth.service.impl.cgame;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author maxuan
 * 2017-09-20
 */
public class CGameChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		final String success = "success";
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		String accessToken = jsonData.optString("token");
		String userid = loginFormBean.getUserId();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//appKey
		String appkey = channelInfoMap.get("appkey");
		//appid
		String appid = channelInfoMap.get("appid");
		//生成签名
		String sign = md5Util.getMD5(appid.concat(userid).concat(accessToken).concat(appkey));
		
		String[] paramNames = {"userid", "appid", "token", "sign" };
		String[] paramValues = {userid, appid, accessToken, sign};
		
		//获取接口返回结果
		String result = channelServiceTemplate.getChannelService(url, paramNames, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
		//验证成功后
		if(success.equalsIgnoreCase(result)){
			return true;
		}else {
			dataMap.put("error", null);
			dataMap.put("msg", result);
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
		}
		
		return false;
	}
}
