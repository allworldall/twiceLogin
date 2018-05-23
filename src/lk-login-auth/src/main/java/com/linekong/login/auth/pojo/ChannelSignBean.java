package com.linekong.login.auth.pojo;

import java.util.List;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年2月1日 下午7:57:31 
 */
public class ChannelSignBean {
	private long channelId;
	private String group;
	private List<SignRuleBean> roleList;
	private SignRuleBean defaultRole;
	private SignServiceBean ssb;
	
	public ChannelSignBean(SignServiceBean ssb){
		this.ssb = ssb;
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
	public List<SignRuleBean> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<SignRuleBean> roleList) {
		this.roleList = roleList;
	}
	public SignRuleBean getDefaultRole() {
		return defaultRole;
	}
	public void setDefaultRole(SignRuleBean defaultRole) {
		this.defaultRole = defaultRole;
	}
	
	public SignServiceBean getSignServiceBean(){
		return ssb;
	}
}
