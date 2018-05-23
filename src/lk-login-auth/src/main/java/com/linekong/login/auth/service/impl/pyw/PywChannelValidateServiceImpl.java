package com.linekong.login.auth.service.impl.pyw;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;


import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.UUIDUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * 朋友玩登陆认证
 * @author jianhong
 * 2017-05-16
 */
public class PywChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		
		//pyw需要的请求流水号
		String tid = UUIDUtil.getUuid();
		//token值
		String token = jsonData.getString("token");
		//uid
		String uid = loginFormBean.getUserId();
		
		//构造请求参数
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("tid", tid);
		requestMap.put("token", token);
		requestMap.put("uid", uid);

		//http返回值
		String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, JSONUtil.objToJsonString(requestMap));
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			long ack = channelServiceTemplate.double2Int((double)httpMap.get("ack"));
			String message = (String)httpMap.get("msg");
			//code为200时,验证成功
			if(ack == ChannelServiceTemplate.RET_CODE_200){
				return true;
			}else{	//渠道返回验证错误
				dataMap.put("error", ack);
				dataMap.put("msg", message);

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}	

}
