package com.batch.partitioning.exam.job.parallelstep.movie.dao;


import com.batch.partitioning.exam.job.parallelstep.movie.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieDao {
	List<Movie> selectMovieList();

	void insertMovieTemp(Movie movie);
}
