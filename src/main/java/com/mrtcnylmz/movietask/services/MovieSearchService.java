package com.mrtcnylmz.movietask.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mrtcnylmz.movietask.model.Movie;

@Component
public class MovieSearchService implements IMovieSearchService {

	public JsonNode responseNode;
	
	@Override
	public List<Movie> search(String movieName) {

		//Movie Data fetched from server.
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", "apikey 5qx4WWOHrr9PAwWFLCB7aj:7bQ5LKO3NWFCKPjrtn2lEK");
		headers.add("content-type", "application/json");
		String url = "https://api.collectapi.com/imdb/imdbSearchByName?query=" + movieName;
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String rBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Movie> movies = new ArrayList<Movie>();

		//Fetched data becames an object.
		try {
			responseNode =  objectMapper.readTree(rBody);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		//Node parsed further and checked for proper data type.
		JsonNode resultNode = this.responseNode.get("result");
		if(resultNode.isArray()) {
			ArrayNode moviesNode = (ArrayNode) resultNode;

			//Result gets iterated for individual movies.
			for(int i = 0; i < moviesNode.size(); i++) {
				JsonNode singleMovie = moviesNode.get(i);

				//Movie object gets set and added to movies List.
				Movie m = new Movie();

				String title = singleMovie.get("Title").asText();
				m.setTitle(title);

				String year = singleMovie.get("Year").asText();
				m.setYear(year);

				String type = singleMovie.get("Type").asText();
				m.setType(type);

				String imdbId = singleMovie.get("imdbID").asText();
				m.setImdbId(imdbId);

				String poster = singleMovie.get("Poster").asText();
				m.setPoster(poster);

				movies.add(m);

				//System Out prints.
				System.out.println("*******************");
				System.out.println((i+1) + ". Movie Title: " + singleMovie.get("Title"));
				System.out.println((i+1) + ". Movie IMDB ID: " +singleMovie.get("imdbID"));
			}
		}

		//Returns movies List.
		return movies;
	}
}