package com.gussoft.customer.core.exception;

import com.gussoft.customer.integration.tranfer.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseStatusExceptionHandler {

  @ExceptionHandler({WebExchangeBindException.class, ServerWebInputException.class})
  public Mono<ResponseEntity<GenericResponse>> handleValidationExceptions(Exception ex, WebRequest request) {
    StringBuilder errorMessage = new StringBuilder();
    if (ex instanceof WebExchangeBindException) {
      WebExchangeBindException webExchangeBindException = (WebExchangeBindException) ex;
      webExchangeBindException.getFieldErrors()
        .forEach(error ->
          errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
    } else if (ex instanceof ServerWebInputException) {
      errorMessage.append(ex.getMessage());
    }
    return Mono.just(ResponseEntity.badRequest().body(new GenericResponse(errorMessage.toString())));
  }

}
