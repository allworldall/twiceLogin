package com.linekong.login.auth.service.impl.tt;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.Base64;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * TT语音登陆认证
 * @author jianhong
 * 2017-05-16
 */
public class TTChannelValidateServiceImpl implements IVerifierRuleService {

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
		String appKey = channelInfoMap.get("secretkey");
		//游戏id
        long gameId = Long.parseLong(channelInfoMap.get("gameid"));
		//token值
		String sid = jsonData.getString("token");
        //uid
        long uid = Long.parseLong(loginFormBean.getUserId());

		//sign
        JSONObject json = new JSONObject();
        json.put("gameId", gameId);
        json.put("uid", uid);

		String signValue = json.toString() + appKey;
		String sign = Base64.encode(md5Util.md5(signValue));

		Header[] headers = {
				new BasicHeader("Content-Type", "application/json"),
				new BasicHeader("sign", sign),
				new BasicHeader("sid", sid)
		};

		//http返回值
		String httpResult = channelServiceTemplate.postChannelService(url, headers, json.toString());
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
            JSONObject resultJson = JSONObject.fromObject(httpResult);
            JSONObject headJson = resultJson.getJSONObject("head");
            int result = headJson.optInt("result");
			//result为0时,验证成功
			if(result == ChannelServiceTemplate.RET_CODE_0){
				return true;
			}else{	//渠道返回验证错误
				dataMap.put("error", result);
				dataMap.put("msg", headJson.optString("message"));
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}		

}
