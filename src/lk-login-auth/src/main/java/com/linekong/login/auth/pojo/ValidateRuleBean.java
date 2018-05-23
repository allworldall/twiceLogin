package com.linekong.login.auth.pojo;

import java.util.List;

import com.linekong.login.auth.service.IVerifierRuleService;


public class ValidateRuleBean {
	private String version;
	private String ref; //引用的版本验证
	private List<String> requestParamList;
	private List<String> configParamList;
	private IVerifierRuleService verifier;
	private ChannelValidateBean channelValidateBean;
	
	public ValidateRuleBean(ChannelValidateBean channelValidateBean){
		this.channelValidateBean = channelValidateBean;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public List<String> getRequestParamList() {
		return requestParamList;
	}
	public void setRequestParamList(List<String> requestParamList) {
		this.requestParamList = requestParamList;
	}
	public List<String> getConfigParamList() {
		return configParamList;
	}
	public void setConfigParamList(List<String> configParamList) {
		this.configParamList = configParamList;
	}
	public IVerifierRuleService getVerifier() {
		return verifier;
	}
	public void setVerifier(IVerifierRuleService verifier) {
		this.verifier = verifier;
	}
	public void setVerifier(String clsName) {
		try {
			Class<?> cls = Class.forName(clsName);
			this.verifier = (IVerifierRuleService) cls.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public ChannelValidateBean getChannelValidateBean() {
		return channelValidateBean;
	}
	
}
