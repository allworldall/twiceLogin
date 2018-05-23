package com.linekong.login.auth.service;

/**
 * 验证传上来的channelId, gameId, appId是否正确
 * @author jianhong
 * 2017-6-21
 */
public interface ValidateAppIdService {
    boolean validateAppId(long channelId, long gameId, long appId);
}
