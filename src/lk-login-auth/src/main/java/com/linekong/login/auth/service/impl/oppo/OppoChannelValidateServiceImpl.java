
package com.linekong.login.auth.service.impl.oppo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.Base64;
import com.linekong.login.auth.utils.HmacSHA1Util;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@lineklong.com
 *	oppo登录认证
 */
public class OppoChannelValidateServiceImpl implements IVerifierRuleService {

	private static final String OAUTH_CONSUMER_KEY 		= "oauthConsumerKey";//签名appKey名称
	private static final String OAUTH_TOKEN 				= "oauthToken";//签名token名称
	private static final String OAUTH_SIGNATURE_METHOD 	= "oauthSignatureMethod";//签名方式名称
	private static final String OAUTH_SIGNATURE 			= "oauthSignature";
	private static final String OAUTH_TIMESTAMP 			= "oauthTimestamp";//签名时间戳名称
	private static final String OAUTH_NONCE 				= "oauthNonce";//签名随机数名称
	private static final String OAUTH_VERSION 			= "oauthVersion";//签名版本名称
	private static final String CONST_SIGNATURE_METHOD 	= "HMAC-SHA1";//签名方式
	private static final String CONST_OAUTH_VERSION 		= "1.0";//签名版本
	
	private static final String PARAM_NAMES[] = {"fileId", "token"};

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
		//开发者平台生成的appkey
		String appKey = channelInfoMap.get("appkey");
		
		String timestamp = String.valueOf(System.currentTimeMillis());
		String token = new String(Base64.decode(jsonData.getString("token")));
		//获取签名的数据
		String oauthSignatureValue = generateBaseString(appKey, token, timestamp, StringUtil.randomNum());
		//获取签名的key
		String oauthSignatureKey = secretKey + "&";
		//生成签名
		byte[] byteHMAC = HmacSHA1Util.hmacSHA1Encrypt(oauthSignatureKey.getBytes(), oauthSignatureValue.getBytes()); 
		String sign = null;
		try {
			sign = URLEncoder.encode(String.valueOf(Base64.encode(byteHMAC)), ChannelServiceTemplate.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//请求参数
		String[] paramValues = {jsonData.getString("ssoid"), token};
		//请求信息头，包含基串信息和签名
		Header[] headers = {
				new BasicHeader("param", oauthSignatureValue),
				new BasicHeader(OAUTH_SIGNATURE, sign),
		};
		
		//http get请求
		String httpResult = channelServiceTemplate.getChannelService(url, PARAM_NAMES, paramValues, headers );
		//解析http请求返回值
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//code为200表示验证成功
			if(httpMap.get("resultCode") != null && StringUtil.objectToInt(httpMap.get("resultCode")) == ChannelServiceTemplate.RET_CODE_200){
				loginFormBean.setUserId(String.valueOf(httpMap.get("ssoid")));
				return true;
			}else{	//渠道验证失败
				dataMap.put("error", StringUtil.objectToInt(httpMap.get("resultCode")));
				dataMap.put("msg", httpMap.get("message"));
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{	//http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}

	//生成基串信息
	private  static String generateBaseString(String appKey, String token, String timestamp,String nonce){
		StringBuilder sb = new StringBuilder();
	    try{
	    	sb.append(OAUTH_CONSUMER_KEY).append("=").append(URLEncoder.encode(appKey, ChannelServiceTemplate.DEFAULT_ENCODING)).append("&")
	    			.append(OAUTH_TOKEN).append("=").append(URLEncoder.encode(token, ChannelServiceTemplate.DEFAULT_ENCODING)).append("&")
	    			.append(OAUTH_SIGNATURE_METHOD).append("=").append(URLEncoder.encode(CONST_SIGNATURE_METHOD, ChannelServiceTemplate.DEFAULT_ENCODING)).append("&")
	    			.append(OAUTH_TIMESTAMP).append("=").append(URLEncoder.encode(timestamp, ChannelServiceTemplate.DEFAULT_ENCODING)).append("&")
	    			.append(OAUTH_NONCE).append("=").append(URLEncoder.encode(nonce, ChannelServiceTemplate.DEFAULT_ENCODING)).append("&")
	    			.append(OAUTH_VERSION).append("=").append(URLEncoder.encode(CONST_OAUTH_VERSION, ChannelServiceTemplate.DEFAULT_ENCODING)).append("&");
	    }catch(Exception ex){
	        LoggerUtil.error(OppoChannelValidateServiceImpl.class, "generateBaseString is error!" + ex.getMessage());
	    }
		return sb.toString();
	}
	
}
