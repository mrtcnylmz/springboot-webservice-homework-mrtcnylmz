package com.mrtcnylmz.movietask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieController {
	
	@Autowired
	private IMovieSearchService service;

	@Autowired
	private IMovieIDAddService idService;

	@Autowired
	private IMovieIDDetailService detailService;

	@GetMapping("/movies/search")
	@ResponseBody
	public List<Movie> search(@RequestParam(name = "name_search") String name) {
		return this.service.search(name);
	}

	@PostMapping("/movies/saveToList/{id}")
	@ResponseBody
	public String IDSearch(@PathVariable(name = "id") String id){
		if(this.idService.IDSearchIndexer(id)){
			return true + " -> Movie successfully added to the MovieBook file.";
		}else return false + " -> Either the movie is in the file or an error occurred.";
	}

	@PostMapping("/movies/detail/{id}")
        public Movie addToList(@PathVariable(name = "id") String id){
		return this.detailService.search(id);
	}
}
