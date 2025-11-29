package com.filmapi.film.model;

import lombok.Data;

@Data
public class EmailFilmContext {
  private String subject;
  private Film film;
}
