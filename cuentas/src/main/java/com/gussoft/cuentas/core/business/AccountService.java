package com.gussoft.cuentas.core.business;

import com.gussoft.cuentas.core.models.Account;
import com.gussoft.cuentas.integration.tranfer.request.AccountRequest;
import com.gussoft.cuentas.integration.tranfer.response.AccountResponse;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

  Flux<AccountResponse> findAll();
  Mono<AccountResponse> findById(Long id);
  Mono<AccountResponse> findByAccountNumber(String number);

  Flux<Account> findAccountByIdCustomer(Long idCustomer);
  Mono<AccountResponse> save(AccountRequest request);
  Mono<AccountResponse> update(AccountRequest request, Long id);
  Mono<AccountResponse> updateAmount(BigDecimal amount, String operation, Long id);
  Mono<Void> delete(Long id);

}
