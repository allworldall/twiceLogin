package com.linekong.login.auth.dao.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;

@Component
public class DataSourceConfigureFactory {
	//数据注入
	@Autowired
	protected DruidDataSource dataSource;
	
	public DruidDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DruidDataSource dataSource) {
		this.dataSource = dataSource;
	}
}
