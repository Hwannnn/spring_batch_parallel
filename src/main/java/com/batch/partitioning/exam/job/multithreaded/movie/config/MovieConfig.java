package com.batch.partitioning.exam.job.multithreaded.movie.config;

import com.batch.partitioning.exam.job.multithreaded.movie.dao.MovieDao;
import com.batch.partitioning.exam.job.multithreaded.movie.model.Movie;
import com.batch.partitioning.exam.job.multithreaded.movie.reader.MovieReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Slf4j
@Configuration
public class MovieConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MovieDao movieDao;

	@Bean("movieJob")
	public Job buildMyJob(@Qualifier("taskExecutor") TaskExecutor taskExecutor) {
		return jobBuilderFactory.get("movieJob")
				.start(buildMyStep(taskExecutor))
				.build();
	}

/*	@Bean("movieStepMaster")
	public Step buildStepMaster(TaskExecutor taskExecutor) {
		return stepBuilderFactory.get("movieStep.master")
				.<String, String>partitioner("movieStep", buildPartitioner())
				.step(buildMyStep())
				.gridSize(10)
				.taskExecutor(taskExecutor)
				.build();
	}

	@Bean("moviePartitioner")
	public Partitioner buildPartitioner() {
		return new MoviePartitioner(10000, 1);
	}*/

	@Bean("movieStep")
	public Step buildMyStep(TaskExecutor taskExecutor) {
		return stepBuilderFactory.get("movieStep")
				.<Movie, String>chunk(100)
				.reader(readMovieList())
				.processor(processMovie())
				.writer(writerMovie())
				.taskExecutor(taskExecutor)
				.throttleLimit(2)
				.build();
	}

	@Bean("movieReader")
	@StepScope
	public ItemReader<Movie> readMovieList() {
		MovieReader reader = new MovieReader();
		reader.setPageSize(100);

		return reader;
	}

	@Bean("movieProcessor")
	@StepScope
	public ItemProcessor<Movie, String> processMovie() {
		return movie -> "<content>" + movie.getTitle() + "</content>";
	}

	@Bean("movieWriter")
	@StepScope
	public ItemWriter<? super String> writerMovie() {
		return movieTitleList -> movieTitleList.forEach(movieTitle -> movieDao.insertMovieTemp(movieTitle));
	}
}
