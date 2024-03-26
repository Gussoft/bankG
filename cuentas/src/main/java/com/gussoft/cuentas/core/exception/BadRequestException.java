package com.gussoft.cuentas.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

  private HttpStatus status;

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
