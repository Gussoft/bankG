package com.gussoft.cuentas.integration.expose;

import com.gussoft.cuentas.core.business.TransactionService;
import com.gussoft.cuentas.integration.tranfer.request.TransactionRequest;
import com.gussoft.cuentas.integration.tranfer.response.GenericResponse;
import com.gussoft.cuentas.integration.tranfer.response.TransactionResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class TransactionController {

  private final TransactionService service;

  @GetMapping("/transactions")
  public Flux<TransactionResponse> findAll(){
    return service.findAll();
  }

  @GetMapping("/transactions/{id}")
  public Mono<ResponseEntity<GenericResponse>> findById(@PathVariable Long id) {
    return service.findById(id)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @GetMapping("/transactions/account/{id}/reports")
  public Mono<GenericResponse> reportByAccountIdAndRangeDates(
    @PathVariable Long id,
    @RequestParam(name = "start") LocalDateTime start,
    @RequestParam(name = "end") LocalDateTime end) {
    return service.findByRangeDates(start, end, id)
      .collectList()
      .map(data -> new GenericResponse("OK", data))
      .onErrorResume(throwable -> Mono.just(new GenericResponse(throwable.getMessage())));
  }

  @GetMapping("/transactions/customer/{id}/accounts")
  public Mono<GenericResponse> reportByCustomerAccountDet(@PathVariable Long id) {
    return service.findByIdCustomer(id)
      .map(data -> new GenericResponse("OK", data))
      .onErrorResume(throwable -> Mono.just(new GenericResponse(throwable.getMessage())));
  }

  @GetMapping("/transactions/search")
  public Mono<ResponseEntity<GenericResponse>> findByAccountId(
    @RequestParam(name = "account", required = true) Long account) {
    return service.findByIdAccount(account)
      .map(data ->
        ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @PostMapping("/transactions")
  public Mono<ResponseEntity<GenericResponse>> create(
    @RequestBody @Valid TransactionRequest request) {
    return service.save(request, request.getPin())
      .map(data -> ResponseEntity
        .created(URI.create("/api/v1/transactions".concat("/" + data.getId())))
        .body(new GenericResponse("OK")))
      .onErrorResume(throwable ->
        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
