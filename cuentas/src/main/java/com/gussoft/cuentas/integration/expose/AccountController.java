package com.gussoft.cuentas.integration.expose;

import com.gussoft.cuentas.core.business.AccountService;
import com.gussoft.cuentas.integration.tranfer.request.AccountRequest;
import com.gussoft.cuentas.integration.tranfer.response.AccountResponse;
import com.gussoft.cuentas.integration.tranfer.response.GenericResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Validated
@CrossOrigin("*")
public class AccountController {

  private final AccountService service;

  @GetMapping("/accounts")
  public Flux<AccountResponse> findAll(){
    return service.findAll();
  }

  @GetMapping("/accounts/{id}")
  public Mono<ResponseEntity<GenericResponse>> findById(@PathVariable Long id) {
    return service.findById(id)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @GetMapping("/accounts/search")
  public Mono<ResponseEntity<GenericResponse>> findByNumberAccount(
    @RequestParam(name = "numberAccount", required = true) String account) {
    return service.findByAccountNumber(account)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @PostMapping("/accounts")
  public Mono<ResponseEntity<GenericResponse>> create(@RequestBody @Valid AccountRequest request) {
    return service.save(request)
      .map(data -> ResponseEntity
        .created(URI.create("/api/v1/accounts".concat("/" + data.getId())))
        .body(new GenericResponse("OK")))
      .onErrorResume(throwable ->
        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new GenericResponse(throwable.getMessage()))));
  }

  @PutMapping("/accounts/{id}")
  public Mono<ResponseEntity<GenericResponse>> update(
    @RequestBody @Valid AccountRequest request, @PathVariable Long id) {
    return service.update(request, id)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @DeleteMapping("/accounts/{id}")
  public Mono<ResponseEntity<GenericResponse>> delete(@PathVariable Long id) {
    return service.delete(id)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK")))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<GenericResponse>> handleValidationExceptions(WebExchangeBindException ex) {
    StringBuilder errorMessage = new StringBuilder();
    ex.getFieldErrors().forEach(error ->
      errorMessage.append(error.getField())
        .append(": ")
        .append(error.getDefaultMessage())
        .append("; "));
    return Mono.just(ResponseEntity.badRequest()
      .body(new GenericResponse(errorMessage.toString())));
  }

}
