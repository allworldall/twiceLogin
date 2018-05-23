package com.linekong.login.auth.service;

import java.util.Map;

import com.linekong.login.auth.pojo.ChannelValidateBean;
import com.linekong.login.auth.web.formBean.LoginFormBean;

public interface IChannelValidateService {
    /**
     * 渠道请求验证服务
     * @param loginFormBean 请求者
     * @param validateBean 验证者
     * @return
     */
	Map<String, Object> validate(LoginFormBean loginFormBean, ChannelValidateBean validateBean);
}
