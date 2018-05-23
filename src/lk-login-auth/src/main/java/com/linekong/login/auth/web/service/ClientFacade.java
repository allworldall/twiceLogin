
package com.linekong.login.auth.web.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linekong.login.auth.pojo.ChannelSignBean;
import com.linekong.login.auth.pojo.ChannelValidateBean;
import com.linekong.login.auth.service.impl.SignContext;
import com.linekong.login.auth.service.impl.SignServiceProxy;
import com.linekong.login.auth.service.impl.ValidateContext;
import com.linekong.login.auth.service.impl.ValidateServiceProxy;
import com.linekong.login.auth.task.IDNSTask;
import com.linekong.login.auth.utils.CreateServiceProxy;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import com.linekong.login.auth.web.formBean.LoginSignFormBean;

@Service
public class ClientFacade {

    @Autowired
    private ValidateServiceProxy validateServiceProxy;
    @Autowired
    private SignServiceProxy signServiceProxy;
	@Autowired
	private IDNSTask idnsTask;

	/**
	 * 
	 * @param loginFormBean
	 * @param call 第三方认证渠道ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doClient(LoginFormBean loginFormBean,Long call) throws Exception{
		ChannelValidateBean validateBean = ValidateContext.getChannelValidateBean(call);
		
		if(validateBean == null){
			throw new Exception("call:" + call + " ClientService " + "version_" + loginFormBean.getVersion() + " not found!");
		}else{
			return CreateServiceProxy.getChannelValidateServiceProxy(validateServiceProxy).validate(loginFormBean, validateBean);
		}
	}
	
	
	/**
	 * 
	 * @param loginFormBean
	 * @param call 第三方认证渠道ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doClient(LoginSignFormBean loginSignFormBean,Long call) throws Exception{
		ChannelSignBean signBean = SignContext.getChannelSignBean(call);
		if(signBean == null){
			throw new Exception("call:" + call + " ClientService " + "version_" + loginSignFormBean.getVersion() + " not found!");
		}else{
			return CreateServiceProxy.getChannelSignServiceProxy(signServiceProxy).sign(loginSignFormBean, signBean);
		}
	}

	@PostConstruct
	private void initCallService(){
		ValidateContext.readXmlToBean(Service.class.getResource("/").getPath());
		SignContext.readXmlToBean(Service.class.getResource("/").getPath());
        //启动域名解析定时任务
        idnsTask.parseDomainName();
        //启动重连超时域名定时任务
        idnsTask.reParseTimeoutUrl();
	}
	

}
