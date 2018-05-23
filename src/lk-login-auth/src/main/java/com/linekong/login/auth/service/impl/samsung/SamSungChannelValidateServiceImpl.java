package com.linekong.login.auth.service.impl.samsung;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.RsaCryptoUtils;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginFormBean;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年1月31日 下午8:04:17 
 * @version samsong渠道认证
 */
public class SamSungChannelValidateServiceImpl implements IVerifierRuleService {

	//appid:申请的appid;token:SDK获取的token值;sign:Base64(MD5withRSA(toUTF8Bytes(appid=xxx&token=xxx)))
	private static final String PARAM_NAMES[] = {"appid", "token", "sign"};

	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();
        //三星SDK登录生成的登录令牌
        String token =  jsonData.optString("token");
        //访问地址
        String goUrl = channelInfoMap.get("goUrl");
        //RSA加密公钥
        String publicKey = channelInfoMap.get("publickey");
        //RSA加密私钥
        String privateKey = channelInfoMap.get("privatekey");
        //SDK申请的三星的配置appId
        String appId = channelInfoMap.get("appid");
        //获取需要加密的基串
        String content = generateBaseString(appId, token);
        //进行Md5WithRas加密
        String sign = RsaCryptoUtils.sign(content, privateKey, "utf-8", RsaCryptoUtils.SIGN_MD5);
        //get请求的数据
		String[] paramValues = new String[]{appId, token, sign};
		//获取接口返回结果
		String result = channelServiceTemplate.getHttpsChannelService(goUrl, PARAM_NAMES, paramValues, true);
        
		//解析http返回值
		if(!result.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
			//json解析
			JSONObject jo = JSONObject.fromObject(result); 

			String retCode = jo.optString("code");
			String retSign = jo.optString("sign");
			//code=0则代表接口返回信息成功
			if(!StringUtils.isBlank(retCode) && retCode.equals("0")){
				String dataStr = jo.optString("data");
				boolean isSign = RsaCryptoUtils.verify(dataStr, retSign, publicKey, "utf-8", RsaCryptoUtils.SIGN_MD5);
				if(isSign){
					JSONObject dataInfo = JSONObject.fromObject(dataStr); 
					//根据获取的信息，执行业务处理
					loginFormBean.setUserId(dataInfo.getString("uid"));
					return true;
				}else{
					dataMap.put("error", retCode);
					dataMap.put("msg", SysCodeConstant.ERROR_SIGN_INFO);
	                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
				}
	
			} else {
				dataMap.put("error", retCode);
				dataMap.put("msg", jo.getString("msg"));
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}else{ //http请求错误
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}
	
	
	//生成基串信息
	private  static String generateBaseString(String appId, String token){
		StringBuilder sb = new StringBuilder();
	    try{
	    	sb.append("appid=").append(appId).append("&token=").append(token);
	    }catch(Exception ex){
	        LoggerUtil.error(SamSungChannelValidateServiceImpl.class, "generateBaseString is error!" + ex.getMessage());
	    }
		return sb.toString();
	}

}
