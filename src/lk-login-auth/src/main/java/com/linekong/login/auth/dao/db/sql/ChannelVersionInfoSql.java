/**
 * 
 */
package com.linekong.login.auth.dao.db.sql;

/**
 * @author Administrator
 *
 */
public class ChannelVersionInfoSql {

	//根据游戏ID和版本号查询当前游戏的渠道号
	public static final String QUERY_CHANNEL_VERSION_INFO_SQL = "select ci.key, ld.value from UC_CP_LOGIN_DETAIL ld, UC_CONFIG_INFO ci where ld.config_id = ci.config_id and ld.cp_id = ? and ld.cp_game_id = ? and ld.cp_sdk_version = ?";
	//获取所有渠道验证的url
	public static final String QUERY_CHANNEL_VERIFY_URL_SQL = "SELECT ucld.VALUE FROM UC_CP_LOGIN_DETAIL ucld " +
            "WHERE ucld.CONFIG_ID=(SELECT uci.CONFIG_ID from UC_CONFIG_INFO uci WHERE uci.KEY='goUrl') and ucld.VALUE is not null " +
            "GROUP BY ucld.VALUE";

}
