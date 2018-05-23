package com.linekong.login.auth.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RsaCryptoUtils {
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	public static final String SIGN_256_ALGORITHMS = "SHA256WithRSA";
	public static final String RSA = "RSA";
	public static final String SIGN_MD5 = "MD5WithRSA";
	
	/**
	 * RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String input_charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(RSA);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_256_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	


	/**
	 * RSA验签名检查
	 * 
	 * @param content
	 *            待签名数据
	 * @param sign
	 *            签名值
	 * @param ali_public_key
	 *            支付宝公钥
	 * @param input_charset
	 *            编码格式
	 * @return 布尔值
	 */
	public static boolean verify(String content, String sign, String ali_public_key, String input_charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			byte[] encodedKey = Base64.decode(ali_public_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 验证签名
	 * @param content 待签名数据
	 * @param sign	签名值
	 * @param publicKey 公钥
	 * @param aignAlgorithms 签名算法
	 * @return
	 */
	public static boolean doCheck(String content, String sign, String publicKey, String aignAlgorithms)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(aignAlgorithms);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            byte[] bs = Base64.decode(sign);
            return signature.verify(bs);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
	
	/**
	 * 解密
	 * 
	 * @param content
	 *            密文
	 * @param private_key
	 *            商户私钥
	 * @param input_charset
	 *            编码格式
	 * @return 解密后的字符串
	 */
	public static String decrypt(String content, String private_key, String input_charset) throws Exception {
		PrivateKey prikey = getPrivateKey(private_key);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prikey);

		InputStream ins = new ByteArrayInputStream(Base64.decode(content));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[128];
		int bufl;

		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}
			writer.write(cipher.doFinal(block));
		}
		return new String(writer.toByteArray(), input_charset);
	}

	
	/**
	 * RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @param input_charset
	 *            编码格式
	 * @param rsaSignature
	 *            RSA加密类型
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String input_charset, String rsaSignature) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(RSA);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(rsaSignature);

			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * RSA验签名检查
	 * 
	 * @param content
	 *            待签名数据
	 * @param sign
	 *            签名值
	 * @param ali_public_key
	 *            支付宝公钥
	 * @param input_charset
	 *            编码格式
	 * @param rsaSignature
	 *            RSA加密类型
	 * @return 布尔值
	 */
	public static boolean verify(String content, String sign, String ali_public_key, String input_charset, String rsaSignature) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			byte[] encodedKey = Base64.decode(ali_public_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(rsaSignature);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64.decode(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	public static void main(String[] args){
		String content = "{\"appid\":\"500000185\",\"count\":1,\"cporderid\":\"1404124310243\",\"cpprivate\":\"cpprivateinfo123456\",\"feetype\":0,\"money\":100,\"paytype\":5,\"result\":\"0\",\"transid\":\"32011406301831300001\",\"transtime\":\"2014-06-30 18:31:32\",\"transtype\":0,\"waresid\":1}";
		
//		// 私钥
		String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKyCyqYIDLttvcH4mVnKuM5xkhf71SAwQOU921xDXdP3MHlXIRJLtA19qEH0hWn2Gamq183QfZd/OBiycGkDg4kj8hyLsyLP3Vqi5eJDeM0rpqQewtbCZ+tA9Iqf5Ulsu/b0P44E6b7oI6EsOmyJ9z42xL8gdbObxQ9LhGXXlevVAgMBAAECgYEAk2YputWz803gTmWYuhUx9QsrNhzyTM+OE5HX5ayV9jAGlhcxZc2TadeNNdW1TQV/he44+F2FgjWse8WzsLbU4qOBJzpxp/Y1P14iLNyvWLqCKo2dpYIwBQ1MtjZo8LjisYXjgOuVdu8trD6pWvsEAf/2yv+uHRCrFtzD7ZH0DMECQQD5q97dG78L9JDmqgV9jDytYtdpqU2BSXieyK5pi51T4AjtpIWHqZz54vh//if/1+zvT4tVDGDuWsbi6iaCS4alAkEAsOI5HLbgpnnXC9esl8kqAGS4UG46+8cnuHftCkKnPPuJViu+6+bLi8RNh8c1BFrvxkzC76MISJc8/mmjy9X5cQJAaGcbeaBZolkxopMkWpyi3uo/I9r9IvwjKVrHvDD6qBE+ConKoLEniEMGIReZiF21oVjE7dqQSfvBwS+izMdiVQJACUuaHLwvihAVEPZAh6l2n5araO6iAuEwYS1MM6HVEAtX6ENcQSFbyPiD6oK5coUk3JLkG5vBr67i8auzg962wQJAZRXCBKq6h6AlKhHV3a3P8osmX3ZXe2g0KnQ6M/bkmvv2FnJWhOxnMkN+hvNOadlJs40gS9Hcu417Fz9G/5IlYA==";
//		// 公钥
		String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKbOP/Mw8IyZFGOKuhiFfTIs7D9GT3xk8vpRe6t7wCDdlruc7jkt2BPiH5czaKtBYW3UtyqZ++tbKhg+NX6nujHP3EQSoZADH0qZ/8J3kNKUYJnM1AJA2zzLcmkrPKwLySy6tmqLI6OE4UTYhVfOGzmrJg2YuxrDRE7PyCLLiPuQIDAQAB";

		// 签名
		String sign = sign(content, priKey, "utf-8" , SIGN_MD5);
		
		// 验签
		if (verify(content, sign, pubKey,"utf-8" , SIGN_MD5))
		{
			System.out.println("verify ok");
		}
		else
		{
			System.out.println("verify fail");
		}
	}

}
