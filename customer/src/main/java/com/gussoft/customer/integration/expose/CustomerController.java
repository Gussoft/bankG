package com.gussoft.customer.integration.expose;

import com.gussoft.customer.core.business.CustomerService;
import com.gussoft.customer.integration.tranfer.request.CustomerRequest;
import com.gussoft.customer.integration.tranfer.response.CustomerResponse;
import com.gussoft.customer.integration.tranfer.response.GenericResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
public class CustomerController {

  private final CustomerService service;

  @GetMapping("/customers")
  public Flux<CustomerResponse> findAll(){
    return service.findAll();
  }

  @GetMapping("/customers/{id}")
  public Mono<ResponseEntity<GenericResponse>> findById(@PathVariable Long id) {
    return service.findById(id)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @GetMapping("/customers/search")
  public Mono<ResponseEntity<GenericResponse>> findByDni(
    @RequestParam(name = "dni", required = true) String dni) {
    return service.findByDni(dni)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @PostMapping("/customers")
  public Mono<ResponseEntity<GenericResponse>> create(@RequestBody @Valid CustomerRequest request) {
    return service.save(request)
      .map(data -> ResponseEntity
        .created(URI.create("/api/v1/student".concat("/" + data.getId())))
        .body(new GenericResponse("OK")))
      .onErrorResume(throwable ->
        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new GenericResponse(throwable.getMessage()))));
  }

  @PutMapping("/customers/{id}")
  public Mono<ResponseEntity<GenericResponse>> update(
    @RequestBody @Valid CustomerRequest request, @PathVariable Long id) {
    return service.update(request, id)
      .map(data -> ResponseEntity.ok(new GenericResponse("OK", data)))
      .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GenericResponse(throwable.getMessage()))));
  }

  @DeleteMapping("/customers/{id}")
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
