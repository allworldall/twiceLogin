package com.linekong.login.auth.service;

import java.util.Map;

import com.linekong.login.auth.web.formBean.LoginFormBean;

public interface IVerifierRuleService {
	boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate);
}
