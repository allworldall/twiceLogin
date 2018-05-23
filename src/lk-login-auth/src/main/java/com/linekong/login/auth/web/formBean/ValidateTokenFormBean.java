/**
 * 
 */
package com.linekong.login.auth.web.formBean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.linekong.login.auth.utils.validate.DataValidate;

/**
 * @author Administrator
 *
 */
public class ValidateTokenFormBean {
	@DataValidate(description = "请求内容中用户ID不能为空",nullable = false )
	private			String 		userId;		//用户ID
	@DataValidate(description = "请求内容中token不能为空",nullable = false )
	private 		String 		token;		//JAVA服务器自己生成的token

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
