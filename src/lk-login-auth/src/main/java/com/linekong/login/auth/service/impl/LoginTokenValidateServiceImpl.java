package com.linekong.login.auth.service.impl;

import javax.annotation.Resource;

import com.linekong.login.auth.utils.Common;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linekong.login.auth.dao.LoginTokenCacheOperationDao;
import com.linekong.login.auth.dao.OperationDataBaseDao;
import com.linekong.login.auth.service.LoginTokenValidateService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.TokenConstant;
import com.linekong.login.auth.web.formBean.ValidateTokenFormBean;

@Service
@Transactional
public class LoginTokenValidateServiceImpl implements LoginTokenValidateService{
	@Resource
	private LoginTokenCacheOperationDao loginTokenCacheOperationDao;

	@Resource
	private OperationDataBaseDao 		operationDataBaseDao;
	
	public int validateToken(ValidateTokenFormBean validateTokenFormBean) {
		String redisKey = Common.getUserTokenKey(validateTokenFormBean.getUserId(), validateTokenFormBean.getToken());
		
		Object result = loginTokenCacheOperationDao.validateToken(redisKey, validateTokenFormBean.getToken());
		if(result == Integer.valueOf((String) SysCodeConstant.ERROR_REDIS_OPERATION) ||
				result == Integer.valueOf((String) SysCodeConstant.ERROR_REDIS_CONNECTION)){
			//redis连接异常到数据库中去进行token信息认证
			Object[] param = {validateTokenFormBean.getUserId(), validateTokenFormBean.getToken()};
			result =  operationDataBaseDao.validateToken(param);
			if(result == null){
				result = TokenConstant.TOKEN_KEY_NOT_EXIST;
			}
		}
		
		return (int)result;
	}

	public LoginTokenCacheOperationDao getLoginTokenCacheOperationDao() {
		return loginTokenCacheOperationDao;
	}

	public void setLoginTokenCacheOperationDao(LoginTokenCacheOperationDao loginTokenCacheOperationDao) {
		this.loginTokenCacheOperationDao = loginTokenCacheOperationDao;
	}


	@Override
	public int updateLoginTokenInfo(ValidateTokenFormBean pojo) {
		return operationDataBaseDao.updateLogLoginTokenPOJO(pojo);
	}
	
}
