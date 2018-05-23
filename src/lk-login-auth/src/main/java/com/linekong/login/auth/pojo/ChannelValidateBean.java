package com.linekong.login.auth.pojo;

import java.util.List;

public class ChannelValidateBean {
	private long channelId;
	private String group;
	private List<ValidateRuleBean> roleList;
	private ValidateRuleBean defaultRole;
	private ValidateServiceBean vsb;
	
	public ChannelValidateBean(ValidateServiceBean vsb){
		this.vsb = vsb;
	}
	
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<ValidateRuleBean> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<ValidateRuleBean> roleList) {
		this.roleList = roleList;
	}
	public ValidateRuleBean getDefaultRole() {
		return defaultRole;
	}
	public void setDefaultRole(ValidateRuleBean defaultRole) {
		this.defaultRole = defaultRole;
	}
	
	public ValidateServiceBean getValidateServiceBean(){
		return vsb;
	}
}
