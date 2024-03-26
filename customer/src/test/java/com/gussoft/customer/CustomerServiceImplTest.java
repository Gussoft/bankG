package com.gussoft.customer;

import static com.gussoft.customer.core.utils.Constrains.NOT_FOUND;
import static com.gussoft.customer.core.utils.Constrains.NOT_RECORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.gussoft.customer.core.business.impl.CustomerServiceImpl;
import com.gussoft.customer.core.exception.NotFoundException;
import com.gussoft.customer.core.mappers.CustomerMapper;
import com.gussoft.customer.core.models.Customer;
import com.gussoft.customer.core.repository.CustomerRepository;
import com.gussoft.customer.integration.tranfer.request.CustomerRequest;
import com.gussoft.customer.integration.tranfer.response.CustomerResponse;
import java.util.Collections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

  @Spy
  private CustomerRepository repository;

  @Spy
  private CustomerMapper mapper;

  @InjectMocks
  private CustomerServiceImpl service;

  public static Customer customer;
  public static CustomerRequest request;
  public static CustomerResponse response;

  @BeforeAll
  public static void setUp() {
    customer = new Customer(1L, "1234", "Activo");
    customer.setName("Guss");
    customer.setDni("1010");
    customer.setPhone("959500");
    customer.setAge(20);
    request = new CustomerRequest();
    request.setId(1L);
    request.setName("Guss");
    request.setDni("1010");
    request.setPhone("959500");
    request.setAge(20);
    response = new CustomerResponse();
    response.setId(1L);
    response.setName("Guss");
    response.setDni("1010");
    response.setPhone("959500");
    response.setAge(20);
  }

  @Test
  void findAllOkTest() {
    when(repository.findAll()).thenReturn(Flux.just(customer));
    when(mapper.responseToEntity(any(Customer.class))).thenReturn(response);

    StepVerifier.create(service.findAll())
      .expectNextSequence(Collections.singleton(response))
      .verifyComplete();
  }

  @Test
  void findByIdOkTest() {
    when(repository.findById(anyLong())).thenReturn(Mono.just(customer));
    when(mapper.responseToEntity(any(Customer.class))).thenReturn(response);

    StepVerifier.create(service.findById(1L))
      .expectNext(response)
      .verifyComplete();
  }

  @Test
  void findByIdEmptyTest() {
    when(repository.findById(anyLong())).thenReturn(Mono.empty());

    StepVerifier.create(service.findById(1L))
      .expectErrorMatches(throwable -> throwable instanceof NotFoundException
        && throwable.getMessage().equals(NOT_FOUND))
      .verify();
  }

  @Test
  void findByDniOkTest() {
    when(repository.findByDni(anyString())).thenReturn(Mono.just(customer));
    when(mapper.responseToEntity(any(Customer.class))).thenReturn(response);

    StepVerifier.create(service.findByDni("1010"))
      .expectNext(response)
      .verifyComplete();
  }

  @Test
  void saveCustomerOkTest() {
    when(mapper.entityToRequest(any(CustomerRequest.class))).thenReturn(customer);
    when(mapper.responseToEntity(any(Customer.class))).thenReturn(response);
    when(repository.countCustomerByDni(any(String.class))).thenReturn(Mono.just(0));
    when(repository.save(any(Customer.class))).thenReturn(Mono.just(customer));

    StepVerifier.create(service.save(request))
      .assertNext(customer -> assertEquals("1010", customer.getDni()))
      .expectComplete()
      .verify();
  }

  @Test
  void saveCustomerErrorTest() {
    when(repository.countCustomerByDni(any(String.class))).thenReturn(Mono.just(1));
    StepVerifier.create(service.save(request))
      .expectErrorMatches(throwable -> throwable instanceof NotFoundException
        && throwable.getMessage().equals(NOT_RECORD))
      .verify();
  }

  @Test
  void updateCustomerOkTest() {
    when(repository.findById(anyLong())).thenReturn(Mono.just(customer));
    when(repository.countCustomerByDni(anyString())).thenReturn(Mono.just(0));
    when(repository.save(any(Customer.class))).thenReturn(Mono.just(customer));
    when(mapper.entityToRequest(any(CustomerRequest.class))).thenReturn(customer);
    when(mapper.responseToEntity(any(Customer.class))).thenReturn(response);

    StepVerifier.create(service.update(request, 1L))
      .expectNext(response)
      .verifyComplete();
  }

  @Test
  void updateCustomerErrorTest() {
    when(repository.findById(anyLong())).thenReturn(Mono.just(customer));
    when(repository.countCustomerByDni(anyString())).thenReturn(Mono.just(1));

    StepVerifier.create(service.update(request, 1L))
      .expectErrorMatches(throwable -> throwable instanceof NotFoundException
        && throwable.getMessage().equals(NOT_RECORD))
      .verify();
  }

  @Test
  void deleteFindByIdExistTest() {
    when(repository.findById(anyLong())).thenReturn(Mono.just(customer));
    when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

    StepVerifier.create(service.delete(1L))
      .verifyComplete();
  }

}
