/**
 * 
 */
package com.linekong.login.auth.dao.db.sql;

/**
 * @author Administrator
 *
 */
public class LoginTokenInfoSql {


	//修改一条token记录，设置状态为使用状态 state=1
	public static final String UPDATE_LOGIN_TOKEN_INFO_STATE_SQL = "update LOGIN_TOKEN_LOG set state = 1 where user_id=? and token = ?";

	//添加或修改一条token值
    public static final String SAVE_OR_UPDATE_LOGIN_TOKEN_INFO_TOKEN_SQL = "merge into LOGIN_TOKEN_LOG t  using(" +
            "select #{channelId} as channelId, #{gameId} as gameId , #{version} as version, #{userId} as userId from dual" +
            ") t1 on( t.channel_id = t1.channelId and t.game_id = t1.gameId and t.version = t1.version and t.user_id = t1.userId)" +
            " when matched then" +
            " update set t.token = #{token}, t.state = 0 where  t.channel_id = #{channelId} and" +
            " t.game_id = #{gameId} and t.version = #{version} and t.user_id = #{userId}" +
            " when not matched then" +
            " insert (channel_id, game_id, version, user_id, token, state)" +
            " values(#{channelId}, #{gameId}, #{version}, #{userId}, #{token}, 0)";

	//验证登陆成功且token状态为已使用状态()做删除处理
	public static final String DELETE_LOGIN_TOKEN_INFO_SQL = "delete from LOGIN_TOKEN_LOG where state = 1";
	
	//验证token的状态
	public static final String QUERY_LOGIN_TOKEN_INFO_SQL = "select state from LOGIN_TOKEN_LOG where user_id=? and token=? ";

	//查询信息
	public static final String QUERY_LOGIN_TOKEN_CONTENT_SQL = "select token from LOGIN_TOKEN_LOG where channel_id=? and game_id=? and version=? and user_id=? and state=0";

	//查询appId
	public static final String QUERY_APPID_SQL = "select game_id from UC_CP_CHARGE_CONFIG where cp_game_id=? and cp_id=?";

}
