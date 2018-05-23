package com.linekong.login.auth.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linekong.login.auth.utils.JSONUtil;
import org.springframework.stereotype.Repository;

import com.linekong.login.auth.dao.OperationDataBaseDao;
import com.linekong.login.auth.dao.db.DataBaseDMLTemplate;
import com.linekong.login.auth.dao.db.sql.ChannelVersionInfoSql;
import com.linekong.login.auth.dao.db.sql.LoginTokenInfoSql;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.ValidateTokenFormBean;

@Repository("operationDataBaseDaoImpl")
public class OperationDataBaseDaoImpl extends DataBaseDMLTemplate implements OperationDataBaseDao {

    private static final String INSERT_OR_UPDATE_SQL;
    private static final List<String> keyList = new ArrayList<>();

    static {
        INSERT_OR_UPDATE_SQL = precompiledSql(LoginTokenInfoSql.SAVE_OR_UPDATE_LOGIN_TOKEN_INFO_TOKEN_SQL, keyList);
    }

	@Override
	public int insertLogLoginTokenPOJO(Map<String, Object> paramMap) {
        List<Object> params = new ArrayList<>();
        for (String key: keyList) {
            params.add(paramMap.get(key));
        }

        int result = this.insertOrUpdate(dataSource, INSERT_OR_UPDATE_SQL, params);

		if(result == 0){
			LoggerUtil.error(OperationDataBaseDaoImpl.class, "insertOrUpdate LoginTokenInfoPOJO is fail! " + JSONUtil.objToJsonString(paramMap));
			result = (int)SysCodeConstant.ERROR_DB;
		}
		return result;
	}

	@Override
	public int updateLogLoginTokenPOJO(ValidateTokenFormBean pojo) {
		Object param[] = {pojo.getUserId(), pojo.getToken()};
		int result = this.update(dataSource, LoginTokenInfoSql.UPDATE_LOGIN_TOKEN_INFO_STATE_SQL, param);
		if(result == 0){
			LoggerUtil.error(OperationDataBaseDaoImpl.class, "update LoginTokenInfoPOJO is fail! userId=" + pojo.getUserId() +
					", token=" + pojo.getToken());
		}
		return result;
	}

	@Override
	public int deleteLogLoginTokenPOJO() {
		return super.delete(dataSource, LoginTokenInfoSql.DELETE_LOGIN_TOKEN_INFO_SQL, null);
	}

	@Override
	public Object validateToken(Object param[]) {
		return super.validateToken(dataSource, param, LoginTokenInfoSql.QUERY_LOGIN_TOKEN_INFO_SQL);
	}

	@Override
	public Map<String, String> queryChannelInfo(long cpId, long gameId, String version){
		Object param[] = {cpId, gameId, version};

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(ChannelVersionInfoSql.QUERY_CHANNEL_VERSION_INFO_SQL);
			for (int i = 0; i < param.length; i++) {
				ps.setObject(i+1, param[i]);
			}
			rs = ps.executeQuery();
			while(rs.next()){
				putValToMap(rs, resultMap);
			}
		} catch (SQLException e) {
			LoggerUtil.error(DataBaseDMLTemplate.class, ChannelVersionInfoSql.QUERY_CHANNEL_VERSION_INFO_SQL+":"+e.getMessage(),e);
		}finally{
			this.close(conn, null, ps,rs);
		}
		return resultMap;

	}

    @Override
    public String queryTokenByUser(long cpId, long gameId, String version, String userId) {
        Object param[] = {cpId, gameId, version, userId};
        Object result = this.query(dataSource, LoginTokenInfoSql.QUERY_LOGIN_TOKEN_CONTENT_SQL, param);
        return result == null ? null : String.valueOf(result);
    }

    @Override
    public List<Object> queryAllVerifyUrl() {
        return this.queryAll(dataSource, ChannelVersionInfoSql.QUERY_CHANNEL_VERIFY_URL_SQL, null);
    }

    @Override
	public long queryAppId(long channelId, long gameId) {
		Object param[] = {gameId, channelId};
		Object ret=super.query(dataSource, LoginTokenInfoSql.QUERY_APPID_SQL, param);
		if (ret == null)
			return Long.MIN_VALUE;
		long result = Long.parseLong(String.valueOf(ret));
		return result;
	}

	/**
	 * 将结果集key，value列值放入map
	 * @param rs
	 * @param resultMap
	 * @throws SQLException
	 */
	private void putValToMap(ResultSet rs, Map<String, String> resultMap) throws SQLException {
		resultMap.put(rs.getString("key"), rs.getString("value"));
	}

    /**
     * 将SAVE_OR_UPDATE_LOGIN_TOKEN_INFO_TOKEN_SQL中占位符替换，转化成预编译sql格式
     * 并将映射key值存入list中
     * @return
     */
    private static String precompiledSql(String sql, List<String> keyList) {
        String[] sqlSplice = sql.split(" ");
        StringBuilder sb = new StringBuilder();
        String sqlPart = null;
        String key = null;

        for (int i = 0, len = sqlSplice.length; i < len; i ++) {
            sqlPart = sqlSplice[i].trim();
            if (sqlPart.matches(".*\\#\\{.*\\}.*")) {
                key = sqlPart.replaceAll(".*\\#\\{", "").replaceAll("\\}.*", "").trim();
                keyList.add(key);
                sqlPart = sqlPart.replaceAll("\\#\\{.*\\}", "?");
                sb.append(sqlPart).append(" ");
                continue;
            }
            sb.append(sqlSplice[i]).append(" ");
        }
        return sb.toString();
    }
}
