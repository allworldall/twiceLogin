package com.linekong.login.auth.service.impl.C8868;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;


import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;
/**
 * 8868登陆认证
 * @author jianhong
 * 2017-05-16
 */
public class C8868ChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		//cpId
		String cpId = channelInfoMap.get("appid");
		//appKey
		String appKey = channelInfoMap.get("appkey");
		//gameId
		String tGameId = channelInfoMap.get("gameId");
		
		//8868平台需要的请求唯一标识
		long id = System.currentTimeMillis()/1000;
		//8868平台需要接口服务名称
		String service = "user.suidInfo";
		//token值,8868平台的用户sid
		String sid = jsonData.getString("token");
		//8868平台需要的data字段
		HashMap<String, Object> sidMap = new HashMap<String, Object>();
		sidMap.put("sid", sid);
		//8868平台需要的game字段
		HashMap<String, Object> gameMap = new HashMap<String, Object>();
		gameMap.put("cpId", Integer.parseInt(cpId));
		gameMap.put("gameId", tGameId);
		gameMap.put("channelId", loginFormBean.getChannelId());
		gameMap.put("serverId", 0);
		//8868需要的签名
		String signValue = cpId + "sid=" + sid + appKey; 
		String sign = md5Util.getMD5(signValue);
		//构造请求参数
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("id", id);
		requestMap.put("service", service);
		requestMap.put("data", sidMap);
		requestMap.put("game", gameMap);
		requestMap.put("sign", sign);
		//http返回值
		String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, JSONUtil.objToJsonString(requestMap));
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
//			long retId = double2Long((double)httpMap.get("id"));
			Map<String, Object> stateMap = (Map<String, Object>)httpMap.get("state");
			int code = channelServiceTemplate.double2Int((double)stateMap.get("code"));
			String message = (String)stateMap.get("msg");
			//code为1时,验证成功
			if(code == ChannelServiceTemplate.RET_CODE_1){
				Map<String, Object> tmpMap = (Map<String, Object>)httpMap.get("data");
				double suid = (Double)tmpMap.get("suid");
				String userId = String.valueOf((long)suid);
				
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
