package com.mrtcnylmz.movietask.services;

import java.util.List;

import com.mrtcnylmz.movietask.model.Movie;

public interface IMovieSearchService {
	public List<Movie> search(String movieName);
	//public Movie findById(String Id);
}