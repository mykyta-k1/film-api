package com.filmapi.film.application.contract;

import com.filmapi.film.model.Film;

public interface EmailService {

  void sendFilm(String recipientEmail, String subject, Film film);
}
