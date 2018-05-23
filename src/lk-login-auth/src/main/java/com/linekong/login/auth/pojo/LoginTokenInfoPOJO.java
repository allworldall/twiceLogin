package com.linekong.login.auth.pojo;

public class LoginTokenInfoPOJO {
	private long channelId;
	
	private long gameId;
	
	private String version;
	
	private String userId;
	
	private String token;
	
	private short state;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}
	
}
