package com.batch.partitioning.exam.configration.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.batch.partitioning.exam.job.h2log", sqlSessionTemplateRef = "batchLogSqlSessionTemplate")
public class BatchLogDataSourceConfiguration {
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.batch.log.datasource")
	public DataSource batchLogDataSource() {
		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
	}

	@Bean
	@Primary
	public SqlSessionFactory batchLogSqlSessionFactory(@Qualifier("batchLogDataSource") DataSource batchLogDataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(batchLogDataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/job/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	@Primary
	public SqlSessionTemplate batchLogSqlSessionTemplate(@Qualifier("batchLogSqlSessionFactory") SqlSessionFactory batchLogSqlSessionFactory) {
		return new SqlSessionTemplate(batchLogSqlSessionFactory);
	}

	@Bean
	@Primary
	public PlatformTransactionManager batchLogTransactionManager(@Qualifier("batchLogDataSource") DataSource batchLogDataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(batchLogDataSource);
		return dataSourceTransactionManager;
	}
}
