package com.way.stock.rewards.datasource.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@PropertySource({ "classpath:database/database-${spring.profiles.active:local}.properties" })
@EnableTransactionManagement
public class DataSourceConfiguration {

	private Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

	@Autowired
	private Environment env;

	@Bean(name = "primaryDataSource")
	@Primary
	public DataSource primaryDataSource() throws PropertyVetoException {
		logger.info("Inside primaryDataSource");
		final ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
		dataSource.setDriverClass(env.getProperty("primary.driverClassName"));
		dataSource.setJdbcUrl(env.getProperty("primary.url"));
		dataSource.setUser(env.getProperty("primary.username"));
		dataSource.setPassword(env.getProperty("primary.password"));
		dataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("primary.minPoolSize")));
		dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("primary.maxPoolSize")));
		dataSource.setPreferredTestQuery(env.getProperty("primary.testQuery"));
		dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("primary.maxIdleTime")));
		dataSource.setMaxIdleTimeExcessConnections(Integer.parseInt(env.getProperty("primary.maxIdleTimeExcessConn")));
		dataSource.setMaxConnectionAge(Integer.parseInt(env.getProperty("primary.maxConnAge")));
		return dataSource;
	}
}
