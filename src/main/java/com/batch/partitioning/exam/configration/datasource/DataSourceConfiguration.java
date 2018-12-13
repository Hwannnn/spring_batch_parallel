package com.batch.partitioning.exam.configration.datasource;

import com.batch.partitioning.exam.configration.common.TestService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:jdbc.properties")
public class DataSourceConfiguration {
	@Bean(name = "multiDataSource")
	public DataSource setMultiDataSource() {
		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put(TestService.MY.getKey(), setMyDataSource());
		dataSourceMap.put(TestService.YOUR.getKey(), setYourDataSource());

		RoutingDataSource routingDataSource = new RoutingDataSource();
		routingDataSource.setTargetDataSources(dataSourceMap);

		return routingDataSource;
	}

	@Bean("myDataSource")
	@ConfigurationProperties(prefix = "spring.my.datasource")
	public DataSource setMyDataSource() {
		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
	}

	@Bean("yourDataSource")
	@ConfigurationProperties(prefix = "spring.your.datasource")
	public DataSource setYourDataSource() {
		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
	}
}
