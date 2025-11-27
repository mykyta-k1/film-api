package com.filmapi.film.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rating {
  @JsonProperty("Source")
  private String source;

  @JsonProperty("Value")
  private String value;
}
