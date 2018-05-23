package com.linekong.login.auth.service.impl.baidu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;
/**
 * 
 * @author kangyf
 * 百度登录认证
 *
 */
public class BaiduChannelValidateServiceImpl implements IVerifierRuleService{

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//客户端SDK返回的登陆令牌
		String accessToken = jsonData.optString("token");

		String appid = channelInfoMap.get("appid");
		String secretkey = channelInfoMap.get("secretkey");
		String goUrl = channelInfoMap.get("goUrl");//接口地址

		String sign = md5Util.getMD5(appid + accessToken + secretkey);//签名

		//请求报头信息
		Header[] headers = new Header[]{
				new BasicHeader("accept","*/*"),
				new BasicHeader("connection","Keep-Alive"),
				new BasicHeader("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)"),
		};
		
		//请求参数
		List<NameValuePair> listParam = new ArrayList<NameValuePair>();
		listParam.add(new BasicNameValuePair("AppID", appid));
		listParam.add(new BasicNameValuePair("AccessToken", accessToken));
		listParam.add(new BasicNameValuePair("Sign", sign.toLowerCase()));
		//获取接口返回结果
		String result= channelServiceTemplate.postChannelService(goUrl, headers, listParam);
		//json解析
		JSONObject jo = JSONObject.fromObject(result); 

		//ResultCode=1则代表接口返回信息成功
		try {
			if(Integer.parseInt(jo.getString("ResultCode"))==1  && md5Util.getMD5(appid + jo.getString("ResultCode")+
					java.net.URLDecoder.decode(jo.getString("Content"),"utf-8") + secretkey).equals(jo.getString("Sign").toLowerCase())){

				//根据获取的信息，执行业务处理
				return true;
			} else {
				dataMap.put("error", jo.getString("ResultCode"));
				dataMap.put("msg", jo.getString("ResultMsg"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		} catch (Exception e) {
			LoggerUtil.error(BaiduChannelValidateServiceImpl.class, "URL解码异常" + e.getMessage() + jo);
		}
		
		return false;
	}
}
