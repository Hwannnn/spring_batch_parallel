package com.batch.partitioning.exam.job.parallelstep.movie.config;

import com.batch.partitioning.exam.job.parallelstep.movie.dao.MovieDao;
import com.batch.partitioning.exam.job.parallelstep.movie.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

@Slf4j
@Configuration
public class MovieConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MovieDao movieDao;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Bean("movieJob")
	public Job buildMyJob() {
		return jobBuilderFactory.get("movieJob")
				.start(buildMovieSplitFlow())
				.next(buildMyStep4())
				.build()
				.build();
	}

	@Bean("movieSplitFlow")
	public Flow buildMovieSplitFlow() {
		return new FlowBuilder<SimpleFlow>("movieSplitFlow")
				.split(taskExecutor)
				.add(buildMovieFlow1(), buildMovieFlow2())
				.build();
	}

	@Bean("movieFlow1")
	public Flow buildMovieFlow1() {
		return new FlowBuilder<SimpleFlow>("movieFlow1")
				.start(buildMyStep1())
				.next(buildMyStep2())
				.build();
	}

	@Bean("movieFlow2")
	public Flow buildMovieFlow2() {
		return new FlowBuilder<SimpleFlow>("movieFlow2")
				.start(buildMyStep3())
				.build();
	}

	@Bean("movieStep1")
	public Step buildMyStep1() {
		return stepBuilderFactory.get("movieStep1")
				.tasklet(transferMovieTitleTasklet())
				.build();
	}

	@Bean("movieStep2")
	public Step buildMyStep2() {
		return stepBuilderFactory.get("movieStep2")
				.tasklet(transferMovieContentTasklet())
				.build();
	}

	@Bean("movieStep3")
	public Step buildMyStep3() {
		return stepBuilderFactory.get("movieStep3")
				.tasklet(maskingMovieTitleTasklet())
				.build();
	}

	@Bean("movieStep4")
	public Step buildMyStep4() {
		return stepBuilderFactory.get("movieStep4")
				.tasklet(maskingMovieContentTasklet())
				.build();
	}

	@Bean("movieTitleTransferTasklet")
	public Tasklet transferMovieTitleTasklet() {
		return (StepContribution contribution, ChunkContext chunkContext) -> {
			List<Movie> movieList = movieDao.selectMovieList();

			movieList.forEach(movie -> {
				//movie.setTitle("<title>" + movie.getTitle() + "</title>");
				//movieDao.insertMovieTemp(movie);
				log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			});

			return RepeatStatus.FINISHED;
		};
	}

	@Bean("movieContentTransferTasklet")
	public Tasklet transferMovieContentTasklet() {
		return (StepContribution contribution, ChunkContext chunkContext) -> {
			List<Movie> movieList = movieDao.selectMovieList();

			movieList.forEach(movie -> {
				//movie.setContent("<content>" + movie.getContent() + "</content>");
				//movieDao.insertMovieTemp(movie);
				log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			});

			return RepeatStatus.FINISHED;
		};
	}

	@Bean("movieTitleMaskingTasklet")
	public Tasklet maskingMovieTitleTasklet() {
		return (StepContribution contribution, ChunkContext chunkContext) -> {
			List<Movie> movieList = movieDao.selectMovieList();

			movieList.forEach(movie -> {
				//movie.setTitle("[masking title]");
				//movieDao.insertMovieTemp(movie);
				log.debug("#################################");
			});

			return RepeatStatus.FINISHED;
		};
	}

	@Bean("movieContentMaskingTasklet")
	public Tasklet maskingMovieContentTasklet() {
		return (StepContribution contribution, ChunkContext chunkContext) -> {
			List<Movie> movieList = movieDao.selectMovieList();

			movieList.forEach(movie -> {
				//movie.setContent("[masking content]");
				//movieDao.insertMovieTemp(movie);
				log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			});

			return RepeatStatus.FINISHED;
		};
	}
}
