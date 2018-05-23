package com.linekong.login.auth.dao.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.linekong.login.auth.dao.impl.OperationDataBaseDaoImpl;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.TokenConstant;
import com.linekong.login.auth.utils.log.LoggerUtil;

public class DataBaseDMLTemplate extends DataSourceConfigureFactory{
	/**
	 * 查询返回单个值方法
	 * @param DataSource dataSource  数据源
	 * @param String	 sql         sql语句
	 * @param Ojbect[]   param       参数数组
	 * @return Object result
	 */
	public Object query (DruidDataSource dataSource,String sql,Object param[]){
		Object result = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < param.length; i++) {
				if(param[i] != null){
					ps.setObject(i+1,param[i]);
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				result = rs.getObject(1);
			}
		} catch (SQLException e) {
			LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage(), e);
		}finally{
			this.close(conn, null, ps,rs);
		}
		return result;
	}

    /**
     * 查询返回单列值方法
     * @param dataSource
     * @param sql
     * @param param
     * @return
     */
    public List<Object> queryAll (DruidDataSource dataSource,String sql,Object param[]){
        List<Object> resultList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; param != null && i < param.length; i++) {
                if(param[i] != null){
                    ps.setObject(i+1,param[i]);
                }
            }
            rs = ps.executeQuery();
            while(rs.next()){
                resultList.add(rs.getObject(1));
            }
        } catch (SQLException e) {
            LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage(), e);
        }finally{
            this.close(conn, null, ps,rs);
        }
        return resultList;
    }

	/**
	 * 查询返回单个值方法
	 * @param DataSource dataSource  数据源
	 * @param String	 sql         sql语句
	 * @param Ojbect[]   param       参数数组
	 * @return Object result
	 */
	public Object validateToken(DruidDataSource dataSource,Object param[], String sqlString) {
		Object result = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{

			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sqlString);
			for (int i = 0; i < param.length; i++) {
				if(param[i] != null){
					ps.setObject(i+1,param[i] );
				}
			}
			rs = ps.executeQuery();
			while(rs.next()){
				Object state = rs.getObject(1);
				if(state == null){
					result = TokenConstant.TOKEN_EXPIRE;
					LoggerUtil.error(OperationDataBaseDaoImpl.class, "query param= " + JSONUtil.objToJsonString(param[4]) + " is error!");
				}else{
					int stateValue = Integer.valueOf(String.valueOf(state));
					if(stateValue == 1){
						result = TokenConstant.TOKEN_USE;
					}else if(stateValue == 0){
						result = SysCodeConstant.SUCCESS;
					}
				}
				
			}
		}catch(Exception ex){
			LoggerUtil.error(OperationDataBaseDaoImpl.class, ex.getMessage(), ex);
			result = SysCodeConstant.ERROR_DB;
		}finally{
			this.close(conn, null, ps,rs);
		}
		return result;
	}
	
	/**
	 * 添加数据
	 * @param DataSource dataSource  数据源
	 * @param String	 sql         sql语句
	 * @param Ojbect[]   param       参数数组
	 * @return Integer result
	 */
	public int insert(DruidDataSource dataSource,String sql,Object param[]){
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < param.length; i++) {
				ps.setObject(i+1,param[i] );
			}
			//rs = ps.executeQuery();
			result = ps.executeUpdate();
			if(result > 0){
				conn.commit();
			}else{
				this.rollback(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage(), e);
		}finally{
			this.close(conn, null, ps,null);
		}
		return result;
	}

    /**
     * 插入或更新一条token
     * @param dataSource
     * @param sql
     * @return
     */
	public int insertOrUpdate (DruidDataSource dataSource, String sql, List<Object> params){
        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object value: params) {
                ps.setObject(index, value);
                index ++;
            }
            result = ps.executeUpdate();
            if(result > 0){
                conn.commit();
            }else{
                this.rollback(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage(), e);
        }finally{
            this.close(conn, null, ps,null);
        }
        return result;
    }

	/**
	 * 更新数据
	 * @param DataSource dataSource  数据源
	 * @param String	 sql         sql语句
	 * @param Ojbect[]   param       参数数组
	 * @return Integer result
	 */
	public int update(DruidDataSource dataSource,String sql,Object param[]){
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < param.length; i++) {
				ps.setObject(i+1,param[i] );
			}
			//rs = ps.executeQuery();
			result = ps.executeUpdate();
			if(result > 0){
				conn.commit();
			}else{
				this.rollback(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage(), e);
		}finally{
			this.close(conn, null, ps,null);
		}
		return result;
	}
	
	
	/**
	 * 删除数据
	 * @param DataSource dataSource  数据源
	 * @param String	 sql         sql语句
	 * @param Ojbect[]   param       参数数组
	 * @return Integer result
	 */
	public int delete(DruidDataSource dataSource,String sql,Object param[]){
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			if(param != null){
				for (int i = 0; i < param.length; i++) {
					ps.setObject(i+1,param[i] );
				}
			}
			//rs = ps.executeQuery();
			result = ps.executeUpdate();
			if(result > 0){
				conn.commit();
			}else{
				this.rollback(conn);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage(), e);
		}finally{
			this.close(conn, null, ps,null);
		}
		return result;
	}
	
	/**
	 * 调用存储过程Function进行添加数据
	 * @param DataSource   dataSource  数据源
	 * @param String	   funName     存储过程函数名称
	 * @param Object[]     param       调用参数数组
	 * @return Integer
	 */
	public int invokeIntFunction(DruidDataSource dataSource,String funName,Object param[]){
		int result = 0;
		Connection conn = null;
		CallableStatement call = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			call = conn.prepareCall("{?=call "+funName+"}");
			call.registerOutParameter(1, java.sql.Types.INTEGER);
			for (int i = 0; i < param.length; i++) {
				call.setObject(i+2, param[i]);
			}
			call.execute();
			result = call.getInt(1);
			if(result > 0){
				conn.commit();
			}else{
				this.rollback(conn);
			}
		} catch (SQLException e) {
			LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage(), e);
		}finally{
			this.close(conn, call, null, null);
		}
		return result;
	}
	/**
	 * 数据库连接关闭
	 * @param Connection 			conn
	 * @param CallableStatement		call
	 * @param ResultSet				rs
	 */
	public void close(Connection conn,CallableStatement call, PreparedStatement ps,ResultSet rs){
		try {
			if(conn != null){
				conn.close();
			}
			if(call != null){
				call.close();
			}
			if(rs != null){
				rs.close();
			}
			if(ps != null){
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage());
		}
	}
	/**
	 * 数据库回滚操作
	 */
	public void rollback(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			LoggerUtil.error(DataBaseDMLTemplate.class, e.getMessage());
		}
	}

}
