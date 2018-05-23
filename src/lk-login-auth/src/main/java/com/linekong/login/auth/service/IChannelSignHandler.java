package com.linekong.login.auth.service;

import java.util.Map;

import com.linekong.login.auth.pojo.ChannelSignBean;
import com.linekong.login.auth.web.formBean.LoginSignFormBean;

public interface IChannelSignHandler {
	/**
     * 渠道请求验证服务
     * @param loginSignFormBean 请求者
     * @param signBean 验证者
     * @return
     */
	Map<String, Object> sign(LoginSignFormBean loginSignFormBean, ChannelSignBean signBean) ;
}
