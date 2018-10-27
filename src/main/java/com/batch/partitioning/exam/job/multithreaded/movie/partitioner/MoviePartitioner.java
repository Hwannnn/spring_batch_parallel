package com.batch.partitioning.exam.job.multithreaded.movie.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MoviePartitioner implements Partitioner {
	private Integer max;
	private Integer min;

	public MoviePartitioner(Integer max, Integer min) {
		this.max = max;
		this.min = min;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		int targetSize = (max - min) / gridSize + 1;

		Map<String, ExecutionContext> result = new HashMap<>();

		int number = 0;
		int start = min;
		int end = start + targetSize - 1;

		while (start <= max) {
			if (end >= max) {
				end = max;
			}

			ExecutionContext value = new ExecutionContext();
			value.putInt("minValue", start);
			value.putInt("maxValue", end);

			result.put("partition" + number, value);

			start += targetSize;
			end += targetSize;

			number++;
		}



		return result;
	}
}
