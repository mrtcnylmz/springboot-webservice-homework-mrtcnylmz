package com.mrtcnylmz.movietask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Scanner;

@Component
public class MovieIDAddService implements IMovieIDAddService {

    public String stringResult = "";
	public String movieBook = "";
	private JsonNode responseNode;

	public boolean IDSearchIndexer(String id) {

		//MovieBook data file accessed here.
		File movieFile = new File("src/main/resources/MovieBook.txt");

		//If file exist, it is read line by line to check if the id is already in the file or not.
		if(movieFile.exists()){
			try {
				Scanner myReader = new Scanner(movieFile);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					String[] separated = data.split(",");
					if(separated[0].equals(id)){
						myReader.close();

						System.out.println("ID matches with file data, exit with false.");
						return false;
					}
				}
				myReader.close();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}

		//If no entry of the parameter id is found on file, necessary data gets fetched from the server.
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", "apikey 5qx4WWOHrr9PAwWFLCB7aj:7bQ5LKO3NWFCKPjrtn2lEK");
		headers.add("content-type", "application/json");
		String url = "https://api.collectapi.com/imdb/imdbSearchById?movieId=" + id;
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String r = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();

		//Here data becomes object, parsed and checked if the id was valid or not.
		try{
			responseNode =  objectMapper.readTree(r);
			if (!responseNode.get("success").asBoolean()){
				System.out.println("Failed attempt, please check ID again, exit with false.");
				return false;
			}
		}catch (JsonProcessingException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		//JsonNode to JsonNode. Less code, the better.
		JsonNode movieNode = responseNode.get("result");

		//Data parsed for console out.
		stringResult =
				"Movie Title: " + movieNode.get("Title").asText() + "\n" +
				"Movie IMDB ID: " + movieNode.get("imdbID").asText() + "\n" +
				"Movie Type: " + movieNode.get("Type").asText() + "\n" +
				"Movie Poster: " + movieNode.get("Poster").asText() + "\n" +
				"Movie Year: " + movieNode.get("Year").asText();

		//Data parsed for file and loaded to its appointed string.
		movieBook =
				movieNode.get("imdbID").asText() + "," +
				movieNode.get("Title").asText() + "," +
				movieNode.get("Year").asText() + "," +
				movieNode.get("Type").asText() + "," +
				movieNode.get("Poster").asText();

		//String with movie data being written to file here.
		try {
			FileWriter fWriter = new FileWriter("src/main/resources/MovieBook.txt",true);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			PrintWriter pWriter = new PrintWriter(bWriter);
			pWriter.println(movieBook);
			pWriter.close();
			bWriter.close();
			fWriter.close();
		} catch (IOException e) {
			System.out.println("Failed to Write File! " + e);
		}

		//true value return when new data fetched and written to file.
		System.out.println("Success");
		return true;
	}
}