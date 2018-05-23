package com.linekong.login.auth.dao.redis;

import com.linekong.login.auth.utils.DBSecurityUtils;

public class RedisPasswordAdapt {
	
	private String password;

	public String getPassword() {
		String str = password.substring(password.indexOf(":")+1);
	    String encodePwd = str.substring(str.indexOf(":")+1,str.indexOf("@"));
	    //对密码进行解密
	    String decodePwd = "";
	    try {
	    	decodePwd = DBSecurityUtils.decode(encodePwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password.replace(encodePwd, decodePwd);
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public static void main(String[] args) {
		String password = "redis://:64c5fd2979a86168@192.168.252.37:6379/0";
		String str = password.substring(password.indexOf(":")+1);
	    String encodePwd = str.substring(str.indexOf(":")+1,str.indexOf("@"));
	    //对密码进行解密
	    String decodePwd = "";
	    try {
	    	decodePwd = DBSecurityUtils.decode(encodePwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 String str1 = password.replace(encodePwd, decodePwd);
		 System.out.println(str1);
	}
}
