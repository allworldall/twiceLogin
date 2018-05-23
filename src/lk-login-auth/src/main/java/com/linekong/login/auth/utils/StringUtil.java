/**
 * 
 */
package com.linekong.login.auth.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

/**
 * @author Administrator
 *
 */
public class StringUtil {
	public static final String UTF8 = "UTF-8";
	private static final byte[] BYTEARRAY = new byte[0];
	public static final int SizeOfUUID = 16;
	private static final int SizeOfLong = 8;
	private static final int BitsOfByte = 8;
	private static final int MBLShift = (SizeOfLong - 1) * BitsOfByte;
	private static final char[] HEX_CHAR_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	
	public static String randomStr() {
		return uuidToString(UUID.randomUUID());
	}

	public static byte[] getBytes(String value) {
		return getBytes(value, UTF8);
	}

	public static byte[] getBytes(String value, String charset) {
		if (isBlank(value))
			return BYTEARRAY;
		if (isBlank(charset))
			charset = UTF8;
		try {
			return value.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			return BYTEARRAY;
		}
	}
	
	//判断字符串非空
	public static boolean isNotBlank(String value){
        return value != null && !value.isEmpty() && !value.trim().isEmpty();
    }
	
	
	//判断字符串空值
	public static boolean isBlank(String value){
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }
	
	public static String uuidToString(UUID uuid) {
		long[] ll = {uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()};
		StringBuilder str = new StringBuilder(SizeOfUUID * 2);
		for (int m = 0; m < ll.length; ++m) {
			for (int i = MBLShift; i > 0; i -= BitsOfByte)
				formatAsHex((byte) (ll[m] >>> i), str);
			formatAsHex((byte) (ll[m]), str);
		}
		return str.toString();
	}

	public static void formatAsHex(byte b, StringBuilder s) {
		s.append(HEX_CHAR_TABLE[(b >>> 4) & 0x0F]);
		s.append(HEX_CHAR_TABLE[b & 0x0F]);
	}
	
	//Double转为Integer
	public static int objectToInt(Object value){
		String string = String.valueOf(value);
		if(string.contains(".")){
			String str = string.substring(0, string.indexOf("."));
			int intgeo = Integer.parseInt(str);
			return intgeo;
		}else{
			int intgeo = Integer.parseInt(string);
			return intgeo;
		}
		
	}
	
	//生成一个随机的6位数
	public static String randomNum(){
		Random random = new Random();
		long randomNum = random.nextInt(8999999);
		randomNum = randomNum + 1000000;
		return String.valueOf(randomNum);
	}
	
	
}
