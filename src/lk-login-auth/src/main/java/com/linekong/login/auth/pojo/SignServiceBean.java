package com.linekong.login.auth.pojo;

import java.util.List;

public class SignServiceBean {
	private String namespace;
	private List<ChannelSignBean> channelSignList;
	private Class<?> cls;
	
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public List<ChannelSignBean> getChannelSignList() {
		return channelSignList;
	}
	public void setChannelSignList(List<ChannelSignBean> channelSignList) {
		this.channelSignList = channelSignList;
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
