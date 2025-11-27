package com.filmapi.film.application.service;

import com.filmapi.film.application.contract.FilmService;
import com.filmapi.film.model.Film;
import com.filmapi.shared.exception.NetworkErrorException;
import com.filmapi.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FilmServiceImpl implements FilmService {

  private final RestClient restClient;
  private final String API_KEY;

  public FilmServiceImpl(RestClient restClient, @Value("${api.key}") String API_KEY) {
    this.restClient = restClient;
    this.API_KEY = API_KEY;
  }

  @Override
  public Film searchByTitle(String title) {
    return fetchByTitle(title);
  }

  private Film fetchByTitle(String title) {
    try {
      Film resp =
          restClient
              .get()
              .uri(
                  uriBuilder ->
                      uriBuilder.queryParam("apikey", API_KEY).queryParam("t", title).build())
              .retrieve()
              .body(Film.class);

      if (resp == null || resp.getTitle() == null) {
        throw new ResourceNotFoundException("Film not found");
      }

      return resp;
    } catch (Exception e) {
      throw new NetworkErrorException("Server error, try again");
    }
  }
}
