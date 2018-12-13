package com.batch.partitioning.exam.job.parallelstep.test.partitioner;

import com.batch.partitioning.exam.configration.common.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestPartitioner implements Partitioner {
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> result = new HashMap<>();

		for (TestService testService : TestService.values()) {
			log.debug("service key - {}", testService.getKey());

			ExecutionContext value = new ExecutionContext();
			value.putString("serviceKey", testService.getKey());

			result.put(testService.getKey() + ":partition", value);
		}

		return result;
	}
}
