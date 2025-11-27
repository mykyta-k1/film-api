package com.filmapi.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NetworkErrorException extends ApiException {

  public NetworkErrorException(String message) {
    super(message);
  }
}
