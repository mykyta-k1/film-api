package com.filmapi.film.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class Film {
  @JsonProperty("Title")
  private String title;

  @JsonProperty("Year")
  private String year;

  @JsonProperty("Rated")
  private String rated;

  @JsonProperty("Released")
  private String released;

  @JsonProperty("Runtime")
  private String runtime;

  @JsonProperty("Genre")
  private String genre;

  @JsonProperty("Director")
  private String director;

  @JsonProperty("Writer")
  private String writer;

  @JsonProperty("Actors")
  private String actors;

  @JsonProperty("Plot")
  private String plot;

  @JsonProperty("Language")
  private String language;

  @JsonProperty("Country")
  private String country;

  @JsonProperty("Awards")
  private String awards;

  @JsonProperty("Poster")
  private String poster;

  @JsonProperty("Ratings")
  private List<Rating> ratings;

  @JsonProperty("imdbRating")
  private String imdbRating;

  @JsonProperty("imdbVotes")
  private String imdbVotes;

  @JsonProperty("BoxOffice")
  private String boxOffice;
}
