package com.linekong.login.auth.service;

import java.util.Map;

import com.linekong.login.auth.web.formBean.LoginSignFormBean;

public interface IVerifierRuleSignService {
	boolean sign(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginSignFormBean loginSignFormBean, ChannelServiceTemplate channelServiceTemplate);
}	
