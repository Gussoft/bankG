package com.gussoft.cuentas.core.repository;

import com.gussoft.cuentas.core.models.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends R2dbcRepository<Account, Long> {

  Mono<Account> findByAccountNumber(String accountNumber);
  Mono<Integer> countAccountByAccountNumber(String accountNumber);

  Flux<Account> findAccountByIdCustomer(Long idCustomer);
}
