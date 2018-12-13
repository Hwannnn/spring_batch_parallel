package com.batch.partitioning.exam.configration;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class JobConfiguration {
	@Bean
	public JobRepository jobRepository(@Qualifier("batchLogDataSource") DataSource batchLogDataSource,
										  @Qualifier("batchLogTransactionManager") PlatformTransactionManager batchLogTransactionManager) throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(batchLogDataSource);
		factory.setTransactionManager(batchLogTransactionManager);
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public JobLauncher jobLauncher(@Qualifier("jobRepository") JobRepository jobRepository) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.afterPropertiesSet();

		return jobLauncher;
	}

	@Bean
	public JobBuilderFactory jobBuilderFactory(@Qualifier("jobRepository") JobRepository jobRepository) {
		return new JobBuilderFactory(jobRepository);
	}

	@Bean
	public StepBuilderFactory stepBuilderFactory(@Qualifier("jobRepository") JobRepository jobRepository, @Qualifier("batchLogTransactionManager") PlatformTransactionManager transactionManager) {
		return new StepBuilderFactory(jobRepository, transactionManager);
	}

	@Bean("taskExecutor")
	public TaskExecutor taskExecutor(){
		return new SimpleAsyncTaskExecutor("spring_batch");
	}
}

