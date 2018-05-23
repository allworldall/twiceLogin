package com.linekong.login.auth.service.impl.huawei;

import java.util.Map;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.Base64;
import com.linekong.login.auth.utils.RsaCryptoUtils;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;

import net.sf.json.JSONObject;


/**
 * 
 * @author kangyf
 * 华为登录认证
 *
 */
public class HuaweiChannelValidateServiceImpl implements IVerifierRuleService{


	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {
		
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		JSONObject jsonData = loginFormBean.getJsonData();
		
		/*
		 * 游戏可以将gameAuthSign、playerId、appId和ts提交到游戏自己的服务器，使用文档附录一中的公钥进行验签，
		 * 验签方式如下：
		 * a. 将 appId、ts 和 playerId 的值进行连接；
		 * b. 将 a 的值使用 SHA256 计算 HASH；
		 * c. 将 b 的值使用公钥采用 RSA 算法进行验签，即使用 RSA 公钥签名后的结果与gameAuthSign 的值进行比较，
		 * 		相等则表示验签成功，公钥值请参考本文档的“5.1 登录鉴权签名的验签公钥”。
		 * d. 游戏服务器需要判断本地时间戳和ts之间的差值，如果两个值之间相差超过1小时，则认为此签名已过期，判定不合法
		 */
		
		//客户端SDK返回的登陆令牌
		String gameAuthSign = new String(Base64.decode(jsonData.getString("gameAuthSign")));
		String ts = jsonData.getString("ts");
		String playerId = loginFormBean.getUserId();
		//配置的参数
		String appId = channelInfoMap.get("appid");
		String secretkey = channelInfoMap.get("secretkey");
		
		if(checkTimestamp(ts)){
			dataMap.put("error", "签名已过期");
			dataMap.put("msg", "客户端签名已过期");

			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
		}else{
			//此处千万别按文档中要求的计算hash值
			String sign = appId + ts + playerId;
			boolean flag = RsaCryptoUtils.doCheck(sign, gameAuthSign, secretkey, RsaCryptoUtils.SIGN_256_ALGORITHMS);
			if(flag){
				return true;
			} else {
				dataMap.put("error", "鉴权签名验证失败");
				dataMap.put("msg", "");

				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
			}
		}
		
		return false;
	}

	/**
	 * 判断本地时间戳和ts之间的差值，如果两个值之间相差超过1小时， 则认为此签名已过期，判定不合法
	 * @param timestamp
	 * @return
	 */
	private boolean checkTimestamp(String timestamp){
		long nowTime = System.currentTimeMillis() / 1000;
		long time = Long.valueOf(timestamp) / 1000;
		long diff = (nowTime - time) / 3600;
		
		return diff > 1;
	}
}
