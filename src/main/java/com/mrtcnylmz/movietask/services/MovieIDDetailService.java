package com.mrtcnylmz.movietask.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrtcnylmz.movietask.model.Movie;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
public class MovieIDDetailService implements IMovieIDDetailService{
    private JsonNode responseNode;

    @Override
    public Movie search(String id) {

        //Movie data file accessed here.
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
                        System.out.println("ID found in file.");

                        //Movie object gets set and gets returned.
                        Movie m = new Movie();

                        String title = separated[1];
                        m.setTitle(title);

                        String year = separated[2];
                        m.setYear(year);

                        String type = separated[3];
                        m.setType(type);

                        String imdbId = separated[0];
                        m.setImdbId(imdbId);

                        String poster = separated[4];
                        m.setPoster(poster);

                        return m;
                    }
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        //If we are here that means the id is not in the file.

        //Movie Data fetched from server.
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
                System.out.println("Failed attempt, please check ID again.");
            }
        }catch (JsonProcessingException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //JsonNode to JsonNode for less code.
        JsonNode movieNode = responseNode.get("result");

        //Movie object gets set and gets returned.
        Movie m = new Movie();

        String title = movieNode.get("Title").asText();
        m.setTitle(title);

        String year = movieNode.get("Year").asText();
        m.setYear(year);

        String type = movieNode.get("Type").asText();
        m.setType(type);

        String imdbId = movieNode.get("imdbID").asText();
        m.setImdbId(imdbId);

        String poster = movieNode.get("Poster").asText();
        m.setPoster(poster);

        //Returns detailed movie data from server.
        System.out.println("ID not found in the file and instead fetched from server.");
        return m;
    }
}
