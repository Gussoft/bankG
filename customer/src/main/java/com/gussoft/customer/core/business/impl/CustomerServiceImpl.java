package com.gussoft.customer.core.business.impl;

import static com.gussoft.customer.core.utils.Constrains.NOT_FOUND;
import static com.gussoft.customer.core.utils.Constrains.NOT_RECORD;

import com.gussoft.customer.core.business.CustomerService;
import com.gussoft.customer.core.exception.NotFoundException;
import com.gussoft.customer.core.mappers.CustomerMapper;
import com.gussoft.customer.core.repository.CustomerRepository;
import com.gussoft.customer.integration.tranfer.request.CustomerRequest;
import com.gussoft.customer.integration.tranfer.response.CustomerResponse;
import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository repository;
  private CustomerMapper mapper;

  @Override
  public Flux<CustomerResponse> findAll() {
    return repository.findAll()
      .map(mapper::responseToEntity)
      .sort(Comparator.comparing(CustomerResponse::getId));
  }

  @Override
  public Mono<CustomerResponse> findById(Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .map(mapper::responseToEntity);
  }

  @Override
  public Mono<CustomerResponse> findByDni(String dni) {
    return repository.findByDni(dni)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .map(mapper::responseToEntity);
  }

  @Override
  @Transactional
  public Mono<CustomerResponse> save(CustomerRequest request) {
    return repository.countCustomerByDni(request.getDni())
      .flatMap(count -> {
        if (count > 0) {
          return Mono.error(new NotFoundException(NOT_RECORD));
        } else {
          return repository.save(mapper.entityToRequest(request))
            .map(mapper::responseToEntity);
        }
      });
  }

  @Override
  public Mono<CustomerResponse> update(CustomerRequest request, Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .flatMap(data -> repository.countCustomerByDni(request.getDni()))
      .flatMap(count -> {
        if (count > 0) {
          return Mono.error(new NotFoundException(NOT_RECORD));
        } else {
          request.setId(id);
          return repository.save(mapper.entityToRequest(request))
            .map(mapper::responseToEntity);
        }
      });
  }

  @Override
  public Mono<Void> delete(Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .flatMap(data -> repository.deleteById(data.getId()));
  }
}
