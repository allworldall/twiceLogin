package com.linekong.login.auth.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CryptoUtil {
	
	/**
	 * SDK验签解密
	 * @param String key
	 * @return String
	 */
	public static String sdkDecrypt(String key){
		//byte[] contentByte = Base64.decode(key);
		byte[] contentByte = org.apache.commons.codec.binary.Base64.decodeBase64(key.getBytes());
		byte[] keys = new byte[8];
		System.arraycopy(contentByte, 0, keys, 0, 8);
		byte[] data = new byte[contentByte.length - 8];
		System.arraycopy(contentByte, 8, data, 0, data.length);
		byte[] inputDecBuffer =decrypt(keys, data);
	    return new String(inputDecBuffer).split(":")[0];
	}
	/**
	 * 解密方法
	 * @param byte [] rawKeyData
	 * @param byte [] encryptedData
	 */
	public static byte[] decrypt(byte[] rawKeyData, byte[] encryptedData) {
        byte[] decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 正式执行解密操作
            decryptedData = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }

}
