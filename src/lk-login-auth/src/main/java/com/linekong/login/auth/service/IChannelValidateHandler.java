package com.linekong.login.auth.service;

import com.linekong.login.auth.pojo.ChannelValidateBean;
import com.linekong.login.auth.web.formBean.LoginFormBean;

import java.util.Map;

/**
 * 也许以后IChannelValidateService中会增加新的校验方法
 * 所以将接口中的每一个方法拆分为单个接口，以免在具体实现类中
 * 书写过多的方法体
 */
public interface IChannelValidateHandler {
    /**
     * 渠道请求验证服务
     * @param loginFormBean 请求者
     * @param validateBean 验证者
     * @return
     */
	Map<String, Object> validate(LoginFormBean loginFormBean, ChannelValidateBean validateBean);
}
