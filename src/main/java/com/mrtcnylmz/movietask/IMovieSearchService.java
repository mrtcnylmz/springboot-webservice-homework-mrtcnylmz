package com.mrtcnylmz.movietask;

import java.util.List;

public interface IMovieSearchService {
	public List<Movie> search(String movieName);
	//public Movie findById(String Id);
}