package com.linekong.login.auth.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.linekong.login.auth.utils.log.LoggerUtil;

public class HmacSHA1Util {
	
	private static final String MAC_NAME = "HmacSHA1";
	//HmacSHA1加密
	public static byte[] hmacSHA1Encrypt(byte[] encryptKey, byte[] encryptText){
		try{
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(new SecretKeySpec(encryptKey, MAC_NAME));
			return mac.doFinal(encryptText);
		}catch(Exception ex){
			LoggerUtil.error(HmacSHA1Util.class, "hmacSHA1Encrypt is error!" + ex.getMessage());
			return null;
		}
	}

}
