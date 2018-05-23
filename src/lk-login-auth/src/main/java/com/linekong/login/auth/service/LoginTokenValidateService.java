package com.linekong.login.auth.service;

import com.linekong.login.auth.web.formBean.ValidateTokenFormBean;

public interface LoginTokenValidateService {
	/**
	 * 登录token信息认证
	 * @param	pojo
	 */
    int validateToken(ValidateTokenFormBean pojo);
	
	/**
	 * 更新token状态,由未使用(0) 标记为 已使用(1)
	 * @param	pojo
	 */
    int updateLoginTokenInfo(ValidateTokenFormBean pojo);
	

}
