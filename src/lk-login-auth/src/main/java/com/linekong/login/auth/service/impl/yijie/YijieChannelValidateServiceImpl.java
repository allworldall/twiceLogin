package com.linekong.login.auth.service.impl.yijie;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年4月25日 下午5:56:36 
 */
public class YijieChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {

		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		

		//请求地址:http://sync.1sdk.cn/login/check.html
		String url = channelInfoMap.get("goUrl");
		
		//客户端传过来的token
		String token = jsonData.getString("token");
		//CP游戏在易接服务器上的ID。由易接提供。
		String app = jsonData.getString("app");
		//渠道在易接服务器上的ID。由易接提供。
		String sdk = jsonData.getString("sdk");
		
		String[] paramNames = new String[]{"app", "sdk", "uin", "sess"};
        String[] paramValues = new String[]{app, sdk, loginFormBean.getUserId(), token};
		//http返回值
        String httpResult = channelServiceTemplate.getChannelService(url, paramNames, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
        LoggerUtil.info(YijieChannelValidateServiceImpl.class, "YiJie httpResult is " + httpResult);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			if(httpResult.equals(String.valueOf(ChannelServiceTemplate.RET_CODE_0))){
				//易接渠道通过sdk + "_" + loginFormBean.getUserId()唯一标识用户,生成的MD5作为用户名
				String userId = md5Util.getMD5(sdk + "_" + loginFormBean.getUserId());
				loginFormBean.setUserId(userId);
				return true;
			}else{
				if(StringUtils.isBlank(httpResult)){
					dataMap.put("error", SysCodeConstant.ERROR);
	                dataMap.put("msg", 	 "第三方验证错误");
	                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
				}else{
					dataMap.put("error", Integer.valueOf(httpResult));
	                dataMap.put("msg", 	 "第三方验证错误");
	                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
				}
				
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		
		return false;
	}

}
