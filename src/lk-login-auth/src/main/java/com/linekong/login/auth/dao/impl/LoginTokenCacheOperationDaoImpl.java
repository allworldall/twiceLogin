package com.linekong.login.auth.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.linekong.login.auth.utils.Common;
import com.linekong.login.auth.utils.JSONUtil;
import org.springframework.stereotype.Repository;

import com.linekong.login.auth.dao.LoginTokenCacheOperationDao;
import com.linekong.login.auth.dao.redis.RedisClientTemplate;
import com.linekong.login.auth.exception.RedisConnectionException;
import com.linekong.login.auth.exception.RedisOperationException;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.TokenConstant;
import com.linekong.login.auth.utils.log.LoggerUtil;
@Repository("loginTokenCacheOperationDaoImpl")
public class LoginTokenCacheOperationDaoImpl extends RedisClientTemplate implements LoginTokenCacheOperationDao{

	public Map<String,Object> writeLoginToken(String key, String token) {
		Map<String,Object> ret = new HashMap<String,Object>();
		Map<String,String> map = new HashMap<String,String>();
		Object result = SysCodeConstant.SUCCESS;
		try {
			if(this.existKey(key)){
				//判断是否已经使用
				if(this.hget(key, TokenConstant.TOKEN_STATUS_STRING).toString().equals(SysCodeConstant.SUCCESS.toString())){
					map.put(TokenConstant.TOKEN_STRING, token);
					map.put(TokenConstant.TOKEN_EXPIRE_STRING, String.valueOf(System.currentTimeMillis()/1000));
					map.put(TokenConstant.TOKEN_STATUS_STRING, String.valueOf(0));//status等于0未使用 1 已经使用
					result = this.setMap(key, map);
					this.setExpire(key, TokenConstant.TOKEN_EXPIRE_KEY);
					if(TokenConstant.SET_TOKEN_SUCCESS.equals(result)){
						ret.put("result", SysCodeConstant.SUCCESS);
						ret.put("token", token);
					}else{
						ret.put("result", SysCodeConstant.ERROR_REDIS_OPERATION);
						ret.put("token", "");
					}
				}else{
					//更新原有token的过期时间
					result = this.hset(key, TokenConstant.TOKEN_EXPIRE_STRING, String.valueOf(System.currentTimeMillis()/1000));
					if(Long.valueOf(result.toString()) >= 0){
						ret.put("result", SysCodeConstant.SUCCESS);
						ret.put("token", this.hget(key, TokenConstant.TOKEN_STRING));
					}else{
						ret.put("result", SysCodeConstant.ERROR_REDIS_OPERATION);
						ret.put("token", "");
					}
					//更新key过期时间
					this.setExpire(key, TokenConstant.TOKEN_EXPIRE_KEY);
					
				}
			}else{
				map.put(TokenConstant.TOKEN_STRING, token);
				map.put(TokenConstant.TOKEN_EXPIRE_STRING, String.valueOf(System.currentTimeMillis()/1000));
				map.put(TokenConstant.TOKEN_STATUS_STRING, String.valueOf(0));//status等于0位使用 1 已经使用
				result = this.setMap(key, map);
				this.setExpire(key, TokenConstant.TOKEN_EXPIRE_KEY);
				if(TokenConstant.SET_TOKEN_SUCCESS.equals(result)){
					ret.put("result", SysCodeConstant.SUCCESS);
					ret.put("token", token);
				}else{
					ret.put("result", SysCodeConstant.ERROR_REDIS_OPERATION);
					ret.put("token", "");
				}
			}
		}catch (RedisConnectionException e) {
			LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage());
			ret.put("result", SysCodeConstant.ERROR_REDIS_CONNECTION);
			ret.put("token", "");
			return ret;
		} catch (RedisOperationException e) {
			LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage());
			ret.put("result", SysCodeConstant.ERROR_REDIS_OPERATION);
			ret.put("token", "");
		}
		return ret;
	}

	public int validateToken(String key,String token) {
		//判断key是否已经使用
		try {
			if(this.existKey(key)){
				//判断token是否应使用
				if(this.hget(key, TokenConstant.TOKEN_STATUS_STRING).toString().equals(SysCodeConstant.SUCCESS.toString())){
					return TokenConstant.TOKEN_USE;
				}else{
					//判断token是否过期
					if((Integer.parseInt((String)this.hget(key, TokenConstant.TOKEN_EXPIRE_STRING))+TokenConstant.TOKEN_EXPIRE_VALUE) >= (System.currentTimeMillis()/1000)){
						//判断token是否正确
						if(this.hget(key, TokenConstant.TOKEN_STRING).equals(token)){
							this.hset(key, TokenConstant.TOKEN_STATUS_STRING, "1"); //标记为已经使用
							return (Integer)SysCodeConstant.SUCCESS;
						}else{
							return TokenConstant.TOKEN_ERROR;
						}
					}else{
						return TokenConstant.TOKEN_EXPIRE;
					}
				}
			}else{
				return TokenConstant.TOKEN_KEY_NOT_EXIST;
			}
		} catch (RedisConnectionException e) {
			LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
			return (Integer) SysCodeConstant.ERROR_REDIS_CONNECTION;
		} catch (RedisOperationException e) {
			LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
			return (Integer) SysCodeConstant.ERROR_REDIS_OPERATION;
		}
	}

	/**
	 * 获取缓存中的渠道信息的键值对
	 * @param key
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> getChannelInfoMap(String key){
		Map<String,String> resultMap = null;
		try {
			if(this.existKey(key)){
				resultMap = (Map<String, String>) this.hgetAll(key);
			}
			else{
				LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, "redis key:"+key+" not exist");
			}
		} 
		catch (RedisConnectionException e) {
			LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
		} 
		catch (RedisOperationException e) {
			LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
		}
		catch (Exception e){
			LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
		}
		return resultMap;
	}

    @Override
    public void writeChannelInfo(long cpId, long gameId, String version, Map<String, String> value) {
        try {
            String cp_id = String.valueOf(cpId);
            String game_id = String.valueOf(gameId);

            String cp_key = Common.getCpInfoKey(cpId);
            String cpGame_key = Common.getCpGameInfoKey(cpId, gameId);
            String cpGameV_key = Common.getChannelInfoKey(cpId, gameId, version);
            //如果该渠道的数据不存在，则构建
            if (!this.existKey(cp_id)){
                this.set(cp_key, game_id);
                LoggerUtil.info(LoginTokenCacheOperationDaoImpl.class, "Create cache key=" + cp_key + ", value=" + game_id);
            }
            //获取当前渠道下所有的游戏id字符串
            String cpGameVal = this.get(cp_key);

            //判断渠道下是否有该联运游戏,无则构建
            if (!cpGameVal.contains(game_id)) {
                cpGameVal += ("," + gameId);
                this.set(cp_key, cpGameVal);
                LoggerUtil.info(LoginTokenCacheOperationDaoImpl.class, "Update cache key=" + cp_key + ", value=" + cpGameVal);
            }

            //如果该联运游戏的数据不存在，则构建
            if (!this.existKey(cpGame_key)) {
                this.set(cpGame_key, version);
                LoggerUtil.info(LoginTokenCacheOperationDaoImpl.class, "Create cache key=" + cpGame_key + ", value=" + version);
            }
            //获取当前游戏所有SDK版本号字符串
            String cpGameVersion = this.get(cpGame_key);

            //判断该联运游戏下是否有对应的版本,无则构建
            if (!cpGameVersion.contains(version)) {
                cpGameVersion += ("," + version);
                this.set(cpGame_key, cpGameVersion);
                LoggerUtil.info(LoginTokenCacheOperationDaoImpl.class, "Update cache key=" + cpGame_key + ", value=" + cpGameVersion);
            }

            this.setMap(cpGameV_key, value);
            LoggerUtil.info(LoginTokenCacheOperationDaoImpl.class, "Create cache key=" + cpGameV_key + ", value=" + JSONUtil.objToJsonString(value));

        } catch (RedisConnectionException e) {
            LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
        } catch (RedisOperationException e) {
            LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
        } catch (Exception e) {
            LoggerUtil.error(LoginTokenCacheOperationDaoImpl.class, e.getMessage(), e);
        }
    }

}
