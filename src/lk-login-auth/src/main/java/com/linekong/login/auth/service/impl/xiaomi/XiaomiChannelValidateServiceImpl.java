/**
 * 
 */
package com.linekong.login.auth.service.impl.xiaomi;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.HmacSHA1Util;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author Administrator
 *
 */
public class XiaomiChannelValidateServiceImpl implements IVerifierRuleService {
	private final String PARAM_NAMES[] = {"appId", "session", "uid", "signature"};

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
		//开发者平台生成的appid(游戏ID)
		String appId = channelInfoMap.get("appid");
		
		//以secretKey作为key,使用hmac-sha1带密钥(secret)的哈希算法对代签字符串进行签名计算,签名的结果由16进制表示
		Map<String, String> signValueMap = new HashMap<String, String>();
		signValueMap.put("appId", 	appId);
		signValueMap.put("session", jsonData.getString("token"));
		signValueMap.put("uid", 	String.valueOf(loginFormBean.getUserId()));
		//获取加密签名
		byte[] signValue = null;
		try {
			signValue = HmacSHA1Util.hmacSHA1Encrypt(secretKey.getBytes(ChannelServiceTemplate.DEFAULT_ENCODING), channelServiceTemplate.getSignValueInfo(signValueMap).getBytes(ChannelServiceTemplate.DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String sign = bytesToHexString(signValue).toString();
		
		//http请求参数
		String paramValues[] = {appId, jsonData.getString("token"), String.valueOf(loginFormBean.getUserId()), sign};
		//发送http请求
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
		//解析http返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//errcode=200表示验证成功
			if(httpMap.get("errcode") != null && StringUtil.objectToInt(httpMap.get("errcode")) == ChannelServiceTemplate.RET_CODE_200){
				return true;
			}else{	//errcode不为200表示渠道验证失败
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("errcode")));
				dataMap.put("msg", httpMap.get("errMsg"));
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}
	
	//HmacSHA1加密后转格式
	private static StringBuilder bytesToHexString( byte[] bytesArray ){
        if ( bytesArray == null ){
            return null;
        }
        StringBuilder sBuilder = new StringBuilder();
        for ( byte b : bytesArray ){
            String hv = String.format("%02x", b);
            sBuilder.append( hv );
        }
        return sBuilder;
    }

}
