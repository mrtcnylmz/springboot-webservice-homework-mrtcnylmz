package com.mrtcnylmz.movietask.model;

public class Movie {
    private String Title;
    private String Year;
    private String ImdbId;
    private String Type;
	private String Poster;

	public String getPoster() {
		return Poster;
	}

	public void setPoster(String poster) {
		Poster = poster;
	}

	public String getTitle() {
		return Title;
	}
	
	public void setTitle(String title) {
		Title = title;
	}
	
	public String getYear() {
		return Year;
	}
	
	public void setYear(String year) {
		Year = year;
	}
	
	public String getImdbId() {
		return ImdbId;
	}
	
	public void setImdbId(String imdbId) {
		ImdbId = imdbId;
	}
	
	public String getType() {
		return Type;
	}
	
	public void setType(String type) {
		Type = type;
	}
}