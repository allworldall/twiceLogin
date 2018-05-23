package com.linekong.login.auth.service.impl.iqiyi;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年4月3日 下午2:33:32 
 */
public class IqiyiChannelValidateServiceImpl implements IVerifierRuleService {

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();

        //访问地址
        String url = channelInfoMap.get("goUrl");
        //开发者平台生成的secretkey
        String secretKey = channelInfoMap.get("secretkey");


        //SDK传递的token
        String token = jsonData.getString("token");
        //sdk传过来的appId
        String appId = jsonData.getString("appId");
        //sdk传过来的channelId
        String channelId = jsonData.getString("channelId");

        //生成签名,规则是将参数按照key排序生成字符串md5加密,例如：key=value&key1=value1&key2=value2
        Map<String, String> signMap = new HashMap<String, String>();
        signMap.put("app_id", appId);
        signMap.put("channel_id", channelId);
        signMap.put("sign_key",   secretKey);
        signMap.put("extension",  token);
        
        //生成的md5加密串
        String sign = md5Util.getCompareStringMD5(signMap);
        
        String[] paramNames = new String[]{"app_id", "channel_id", "sign", "extension"};
        String[] paramValues = new String[]{appId, channelId, sign, token};

        //http返回值
        String httpResult = channelServiceTemplate.getChannelService(url, paramNames, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
        //输出爱奇艺聚合渠道的认证信息
        LoggerUtil.info(IqiyiChannelValidateServiceImpl.class, "httpResult = " + httpResult);
  		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
  			JSONObject jsonResult = JSONObject.fromObject(httpResult);
            int resultCode = jsonResult.getInt("code");
  			//code为200,表示验证成功
  			if(resultCode == ChannelServiceTemplate.RET_CODE_200){
  				String data = jsonResult.getString("data");
  				jsonResult = JSONObject.fromObject(data);
  				//union_user_id作为用户名
  				loginFormBean.setUserId(jsonResult.getString("union_user_id"));
  				return true;
  			}else{	//渠道验证失败
  				dataMap.put("error", resultCode);
  				dataMap.put("msg", 	jsonResult.getString("msg"));
  				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
  			}
  		}else{	//http请求错误
  			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
  		}
		
		return false;
	}

}
