package com.gussoft.customer.core.business;

import com.gussoft.customer.integration.tranfer.request.CustomerRequest;
import com.gussoft.customer.integration.tranfer.response.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

  Flux<CustomerResponse> findAll();
  Mono<CustomerResponse> findById(Long id);
  Mono<CustomerResponse> findByDni(String dni);
  Mono<CustomerResponse> save(CustomerRequest request);
  Mono<CustomerResponse> update(CustomerRequest request, Long id);
  Mono<CustomerResponse> update2(CustomerRequest request, Long id);
  Mono<Void> delete(Long id);

}
