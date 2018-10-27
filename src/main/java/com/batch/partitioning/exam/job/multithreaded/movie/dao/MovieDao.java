package com.batch.partitioning.exam.job.multithreaded.movie.dao;


import com.batch.partitioning.exam.job.multithreaded.movie.model.Movie;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieDao {
	List<Movie> selectMovieList(@Param("offset") int offset, @Param("pageSize") int pageSize);

	void insertMovieTemp(String  movieTitle);
}
