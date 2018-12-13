package com.batch.partitioning.exam.job.parallelstep.test.dao;


import com.batch.partitioning.exam.job.parallelstep.test.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestDao {
	List<Movie> selectMovieList();

	void insertMovieTemp(String title);
}
