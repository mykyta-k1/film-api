package com.filmapi.film.application.controller;

import com.filmapi.film.application.contract.EmailService;
import com.filmapi.film.application.contract.FilmService;
import com.filmapi.film.model.EmailFilmContext;
import com.filmapi.film.model.Film;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/films")
@RequiredArgsConstructor
public class FilmController {

  private final FilmService filmService;
  private final EmailService emailService;

  @GetMapping("/{title}")
  public ResponseEntity<Film> getByTitle(@PathVariable String title) {
    return ResponseEntity.ok().body(filmService.searchByTitle(title));
  }

  @PostMapping("/send/{recipientEmail}")
  public void send(
      @PathVariable String recipientEmail, @RequestBody EmailFilmContext emailContext) {
    emailService.sendFilm(recipientEmail, emailContext.getSubject(), emailContext.getFilm());
  }
}
