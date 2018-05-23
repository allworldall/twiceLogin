package com.linekong.login.auth.utils;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 数据库密码加密解密实现参考jboss数据密码加密解密实现
 * @author fangming
 */

public class DBSecurityUtils {
	
	private static final String DEFAULT_SECURE_KEY = "jaas is the way";
	/**
	 * 数据库密码加密
	 * @param String secret 
	 *               明文密码
	 * @return String 
	 *               密文密码
	 * @throws Exception 
	 */
	public static String encode(String secret) throws Exception{
		byte[] kbytes = DEFAULT_SECURE_KEY.getBytes();
		SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");
		Cipher cipher;
		cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encoding = cipher.doFinal(secret.getBytes());
		BigInteger n = new BigInteger(encoding);
		return n.toString(16);
	}
	/**
	 * 数据库密码解密
	 * @param String secret 
	 *               密文密码
	 * @return String 
	 *               明文密码
	 * @throws Exception 
	 */
	public static String decode(String secret) throws Exception{
		byte[] kbytes = DEFAULT_SECURE_KEY.getBytes();
		SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

		BigInteger n = new BigInteger(secret, 16);
		byte[] encoding = n.toByteArray();
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decode = cipher.doFinal(encoding);
		return new String(decode);
	}
	
	public static void main(String[] args) throws Exception {
		String pwd = "123456";
		String pwd2 = "179db2a84b7bcb7606f44f6ff26e5d4";
		System.out.println(encode(pwd));
		System.out.println(decode(pwd2));
	}
	

}
