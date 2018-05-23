package com.linekong.login.auth.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.linekong.login.auth.utils.log.LoggerUtil;

/**
 * SHA哈希工具
 * @author jianhong
 * 2017-05-23
 */
public class ShaUtil {

	static public String sha256(String text){
		if (text == null)
			return null;
		if (text.isEmpty() == true)
			return null;
		
		try {
			MessageDigest  messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(text.getBytes("utf-8"));
			byte[] byteBuf = messageDigest.digest();
			
		    StringBuffer hash = new StringBuffer();
		    for (int i = 0; i < byteBuf.length; i++) {
		      String hex = Integer.toHexString(0xFF & byteBuf[i]);
		      if (hex.length() == 1) {
		        hash.append('0');
		      }
		      hash.append(hex);
		    }
		    return hash.toString();
		} 
		catch (NoSuchAlgorithmException e) {
			LoggerUtil.error(ShaUtil.class, e.getMessage());
			return null;
		} 
		catch (UnsupportedEncodingException e) {
			LoggerUtil.error(ShaUtil.class, e.getMessage());
			return null;
		}
		catch (Exception e){
			LoggerUtil.error(ShaUtil.class, e.getMessage());
			return null;
		}
	}
	
	static public void main(String[] args){
		String result1 = ShaUtil.sha256("INFO controller.LoginTo");
		String result2 = ShaUtil.sha256("a");
		System.out.println("result1:"+result1+","+result1.equals("3ec53d23fedd847f1eece90af97a1197f7d567fae451c3773240b0efd24dac1c"));
		System.out.println("result2:"+result2+","+result2.equals("ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb"));
	    
		String result3 = ShaUtil.sha256("3ec53d23fedd847f1eece90af97a1197f7d567fae451c3773240b0efd24dac1c");
		System.out.println("result3:"+result3+","+result3.equals("dbf79ddf25a214ef82e8ef5eec028cbdff8fdc1d5d9dec74080aa07fac6ae5a6"));
		System.out.println("result3 len:"+result3.length());
	}
	
}
