/**
 * 
 */
package com.linekong.login.auth.service.impl.vivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author Administrator
 *
 */
@Service(value="vivoChannelValidateServiceImpl")
public class VivoChannelValidateServiceImpl implements IVerifierRuleService {
	private static final String FROM_COM = "linekong";

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//访问地址
		String url = channelInfoMap.get("goUrl");
		
		//http post请求参数
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("authtoken", 	String.valueOf(jsonData.get("token"))));
		valuePairs.add(new BasicNameValuePair("from", FROM_COM));
		//发送http请求
		String httpResult = channelServiceTemplate.postHttpsChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
		//解析http请求返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//retcode为0表示验证成功
			if(httpMap.containsKey("retcode") && StringUtil.objectToInt(httpMap.get("retcode")) == ChannelServiceTemplate.RET_CODE_0){
				return true;
			}else{	//retcode不为0表示验证失败
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("retcode")));
				dataMap.put("msg", null);

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}

}
