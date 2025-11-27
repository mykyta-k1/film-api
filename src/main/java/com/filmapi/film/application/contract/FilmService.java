package com.filmapi.film.application.contract;

import com.filmapi.film.model.Film;

public interface FilmService {

  Film searchByTitle(String title);
}
