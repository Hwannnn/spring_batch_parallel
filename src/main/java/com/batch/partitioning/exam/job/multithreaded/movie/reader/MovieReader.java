package com.batch.partitioning.exam.job.multithreaded.movie.reader;

import com.batch.partitioning.exam.job.multithreaded.movie.dao.MovieDao;
import com.batch.partitioning.exam.job.multithreaded.movie.model.Movie;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MovieReader extends AbstractPagingItemReader<Movie> {
	@Autowired
	private MovieDao movieDao;

	@Override
	protected void doReadPage() {
		if (results == null) {
			results = new CopyOnWriteArrayList<>();
		} else {
			results.clear();
		}

		int page = getPage();
		int pageSize = getPageSize();

		List<Movie> movieList = movieDao.selectMovieList(page * pageSize, pageSize);
		results.addAll(movieList);
	}

	@Override
	protected void doJumpToPage(int itemIndex) {
	}
}
