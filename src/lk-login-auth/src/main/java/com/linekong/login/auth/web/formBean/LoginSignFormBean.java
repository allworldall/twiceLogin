package com.linekong.login.auth.web.formBean;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.linekong.login.auth.utils.Common;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.StringUtil;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.utils.validate.DataValidate;

/**
 * @author huxiaoming@linekong.com
 * @version 2018年2月1日 下午4:13:25 
 */
public class LoginSignFormBean {
	private long		channelId;
	private long		gameId;
	private String      appId;
	@DataValidate(description = "请求内容中版本号不能为空",nullable = false )
	private String		version;
	@DataValidate(description = "请求内容中非公共参数不能为空",nullable = false )
	private String		data;
	@DataValidate(description = "请求内容中sign值不能为空",nullable = false )
	private String		sign;
	
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public long getGameId() {
		return gameId;
	}
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	
	public Map<String, Object> getDataBody(){
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtil.isNotBlank(data)){
			map = JSONUtil.objFromJsonString(data, Map.class);
		}
		return map;
	}
	
	public JSONObject getJsonData(){
		return JSONObject.fromObject(data);
	}
	
	public boolean validateSign(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("channelId", this.channelId);
		map.put("gameId", this.gameId);
		map.put("appId", this.appId);
		map.put("version", this.version);
		map.put("data", this.data);
		map.put("key", Common.MD5_LOGIN_VALIDATE_KEY);
		
		return md5Util.comparatSign(map, this.sign);
		
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
