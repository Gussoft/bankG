package com.gussoft.cuentas.core.business;

import com.gussoft.cuentas.core.models.dto.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
  Mono<Customer> findById(Long id);

}
