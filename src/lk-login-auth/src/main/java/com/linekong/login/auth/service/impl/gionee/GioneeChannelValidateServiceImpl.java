/**
 * 
 */
package com.linekong.login.auth.service.impl.gionee;

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
 *	金立登录认证
 */
public class GioneeChannelValidateServiceImpl implements IVerifierRuleService {
	private final String METHOD 	= "POST";//请求方式
	private final String PORT 		= "443";//请求端口
	
	/**
	 * 金立登录验证版本3.2.0
	 **/
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		//接口地址
		String url = channelInfoMap.get("url");
		//开发者平台生成的appid
		String appkey = channelInfoMap.get("appkey");
		//开发者平台生成的secretkey
		String secretKey = channelInfoMap.get("secretkey");
		//访问域名
		String host = channelInfoMap.get("host");
		
		
		//设置https信息头
		Header[] headers = {
	            new BasicHeader("Content-Type", "application/json"),
	            new BasicHeader("Authorization", builderAuthorization(appkey, host, PORT, secretKey, METHOD, url)),
	    };
		//获取完整请求地址
		String verify_url = getVerify_url(host, PORT, url);
		//发送https请求
		String httpResult = channelServiceTemplate.postHttpsChannelService(verify_url, headers, jsonData.getString("token"));
		//对请求返回值做处理
		if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			Map<String, Object> httpMap = JSONUtil.objFromJsonString(httpResult, Map.class);
			//返回值不包含r或者r为0表示验证成功
			if(!httpMap.containsKey("r") || StringUtil.objectToInt(httpMap.get("r")) == ChannelServiceTemplate.RET_CODE_0){
				return true;
			}else{ //渠道返回验证失败,r值和wid值标记错误原因
				dataMap.put("error", httpMap.get("r"));
				dataMap.put("wid", httpMap.get("wid"));
				dataMap.put("msg", httpMap.get("err"));

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
				resultMap.put("data", dataMap);
			}
		}else{ //http请求失败
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
			resultMap.put("data", null);
		}
		
		return false;
	}


	//https请求的信息
	private static String builderAuthorization(String appkey, String host, String port, String secretKey, String method, String url) {
		Long ts = System.currentTimeMillis() / 1000;
		String nonce = StringUtil.randomStr().substring(0, 8);
		String mac = macSig(host, port, secretKey, ts.toString(), nonce, method, url);
		mac = mac.replace("\n", "");
		StringBuilder authStr = new StringBuilder();
		authStr.append("MAC ");
		authStr.append(String.format("id=\"%s\"", appkey));
		authStr.append(String.format(",ts=\"%s\"", ts));
		authStr.append(String.format(",nonce=\"%s\"", nonce));
		authStr.append(String.format(",mac=\"%s\"", mac));
		return authStr.toString();
	}
	
	//进行加密
	private static String macSig(String host, String port, String macKey, String timestamp, String nonce, String method, String url) {
		// 1. build mac string
		// 2. hmac-sha1
		// 3. base64-encoded

		StringBuffer buffer = new StringBuffer();
		buffer.append(timestamp).append("\n");
		buffer.append(nonce).append("\n");
		buffer.append(method.toUpperCase()).append("\n");
		buffer.append(url).append("\n");
		buffer.append(host.toLowerCase()).append("\n");
		buffer.append(port).append("\n");
		buffer.append("\n");
		String text = buffer.toString();

		byte[] ciphertext = null;
		try {
			ciphertext = HmacSHA1Util.hmacSHA1Encrypt(StringUtil.getBytes(macKey), StringUtil.getBytes(text));
		} catch (Exception e) {
			LoggerUtil.error(GioneeChannelValidateServiceImpl.class, "hmacSHA1Encrypt is error!" + e.getMessage());
			return null;
		}

		String sigString = Base64.encode(ciphertext);
		return sigString;
	}
	
	private static String getVerify_url(String host, String port, String url){
		StringBuffer sb = new StringBuffer("https://");
		sb.append(host).append(":").append(port).append(url);
		
		return sb.toString();
	}
}
