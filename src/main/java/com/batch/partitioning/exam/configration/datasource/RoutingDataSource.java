package com.batch.partitioning.exam.configration.datasource;

import com.batch.partitioning.exam.configration.common.DataSourceKey;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceKey.key.get();
	}
}
