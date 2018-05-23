package com.linekong.login.auth.service.impl.samsung;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleSignService;
import com.linekong.login.auth.utils.RsaCryptoUtils;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginSignFormBean;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年2月1日 下午4:24:05
 * @version samsong渠道获取签名
 */
public class SamSungChannelSignServiceImpl implements IVerifierRuleSignService {


	@SuppressWarnings("unchecked")
	@Override
	public boolean sign(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginSignFormBean loginSignFormBean, ChannelServiceTemplate channelServiceTemplate) {
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginSignFormBean.getJsonData();
       //接三星SDK的游戏包名
        String packageName =  jsonData.optString("packageName");
        
        //appid
        String appId = channelInfoMap.get("appid");
        //三星SDK给的RSA加密私钥
        String privateKey = channelInfoMap.get("privatekey");
		
        String content = appId+"&"+packageName;
        String sign = RsaCryptoUtils.sign(content, privateKey, "utf-8", RsaCryptoUtils.SIGN_MD5);
        if(!StringUtils.isBlank(sign)){
        	dataMap.put(SysCodeConstant.SIGN_STRING, sign);
        	dataMap.put(SysCodeConstant.RESULT_STRING, SysCodeConstant.SUCCESS);
        	resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.SUCCESS);
        	return true;
        }else{
        	dataMap.put(SysCodeConstant.SIGN_STRING, sign);
        	dataMap.put(SysCodeConstant.RESULT_STRING, SysCodeConstant.ERROR);
        	resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR);
        }
		return false;
	} 

}
