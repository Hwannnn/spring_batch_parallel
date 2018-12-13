package com.batch.partitioning.exam.job.parallelstep.test.config;

import com.batch.partitioning.exam.configration.common.DataSourceKey;
import com.batch.partitioning.exam.job.parallelstep.test.dao.TestDao;
import com.batch.partitioning.exam.job.parallelstep.test.model.Movie;
import com.batch.partitioning.exam.job.parallelstep.test.partitioner.TestPartitioner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TestConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private TestDao testDao;

	@Bean
	public Job movieJob() {
		return jobBuilderFactory.get("movieJob")
				.start(movieStepMBuilder())
				.build();
	}

	@Bean
	public Step movieStepMBuilder() {
		return stepBuilderFactory.get("movieStep.master")
				.partitioner("movieStep", new TestPartitioner())
				.partitionHandler(moviePartitionHandler())
				.build();
	}

	@Bean
	public PartitionHandler moviePartitionHandler() {
		TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
		retVal.setStep(movieStep());
		return retVal;
	}

	@Bean
	public Step movieStep() {
		return stepBuilderFactory.get("movieStep")
				.<Movie, String>chunk(1000)
				.reader(movieReader())
				.processor(movieProcessor())
				.writer(movieWriter())
				.build();
	}

	@Bean
	@StepScope
	public ItemReader<Movie> movieReader() {
		StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();

		String serviceKey = stepExecutionContext.getString("serviceKey");
		DataSourceKey.key.set(serviceKey);

		return new IteratorItemReader<>(testDao.selectMovieList());

	}

	@Bean
	@StepScope
	public ItemProcessor<Movie, String> movieProcessor() {
		StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
		String serviceKey = stepExecutionContext.getString("serviceKey");

		return movie -> "<title>" + serviceKey + ":" + movie.getTitle() + "</title>";
	}

	@Bean
	@StepScope
	public ItemWriter<? super String> movieWriter() {
		return movieTitleList -> {
			movieTitleList.forEach(movieTitle -> testDao.insertMovieTemp(movieTitle));

			DataSourceKey.key.remove();
		};
	}
}
