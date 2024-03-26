package com.gussoft.customer.core.repository;

import com.gussoft.customer.core.models.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends R2dbcRepository<Customer, Long> {

  Mono<Customer> findByDni(String dni);
  Mono<Integer> countCustomerByDni(String dni);

}
