package com.linekong.login.auth.pojo;

import java.util.List;

public class ValidateServiceBean {
	private String namespace;
	private List<ChannelValidateBean> channelValidateList;
	private Class<?> cls;
	
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public List<ChannelValidateBean> getChannelValidateList() {
		return channelValidateList;
	}
	public void setChannelValidateList(List<ChannelValidateBean> channelValidateList) {
		this.channelValidateList = channelValidateList;
	}
	
	public Class<?> getCls() {
		try {
			cls = Class.forName(namespace);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cls;
	}
	
}
