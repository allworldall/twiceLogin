package com.linekong.login.auth.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linekong.login.auth.dao.OperationDataBaseDao;
import com.linekong.login.auth.service.ValidateAppIdService;

@Service
@Transactional
public class ValidateAppIdServiceImpl implements ValidateAppIdService {
	@Resource
	private OperationDataBaseDao 		operationDataBaseDao;
	
	@Override
	public boolean validateAppId(long channelId, long gameId, long appId) {
		long ret = this.operationDataBaseDao.queryAppId(channelId, gameId);
        return ret == appId;
    }

}
