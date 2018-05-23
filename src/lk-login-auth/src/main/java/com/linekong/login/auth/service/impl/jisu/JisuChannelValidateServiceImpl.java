/**
 * 
 */
package com.linekong.login.auth.service.impl.jisu;

import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.service.impl.iqiyi.IqiyiChannelValidateServiceImpl;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**极速渠道二次登陆认证
 * @author Administrator
 *
 */
public class JisuChannelValidateServiceImpl implements IVerifierRuleService {

	
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

        //时间戳
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        
        //生成的md5加密串,md5( token + time + secret )
        String sign = md5Util.getMD5(token + time + secretKey);
        
        String[] paramNames = new String[]{"token", "time", "sign"};
        String[] paramValues = new String[]{token, time, sign};

        //http返回值
        String httpResult = channelServiceTemplate.getChannelService(url, paramNames, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
        //输出极速渠道的认证信息
        LoggerUtil.info(IqiyiChannelValidateServiceImpl.class, "httpResult = " + httpResult);
  		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
  			JSONObject jsonResult = JSONObject.fromObject(httpResult);
            int resultCode = jsonResult.getInt("code");
  			//code为200,表示验证成功
  			if(resultCode == ChannelServiceTemplate.RET_CODE_1){
  				//union_user_id作为用户名
  				loginFormBean.setUserId(jsonResult.getString("uid"));
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
