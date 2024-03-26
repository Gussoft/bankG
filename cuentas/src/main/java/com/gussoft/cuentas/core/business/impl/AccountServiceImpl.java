package com.gussoft.cuentas.core.business.impl;

import static com.gussoft.cuentas.core.utils.Constrains.INSUFFICIENT_BALANCE;
import static com.gussoft.cuentas.core.utils.Constrains.NOT_FOUND;
import static com.gussoft.cuentas.core.utils.Constrains.NOT_RECORD;
import static com.gussoft.cuentas.core.utils.Constrains.WITHDRAWAL;

import com.gussoft.cuentas.core.business.AccountService;
import com.gussoft.cuentas.core.exception.BadRequestException;
import com.gussoft.cuentas.core.exception.NotFoundException;
import com.gussoft.cuentas.core.mappers.AccountMapper;
import com.gussoft.cuentas.core.models.Account;
import com.gussoft.cuentas.core.repository.AccountRepository;
import com.gussoft.cuentas.integration.tranfer.request.AccountRequest;
import com.gussoft.cuentas.integration.tranfer.response.AccountResponse;
import java.math.BigDecimal;
import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

  private final AccountRepository repository;
  private AccountMapper mapper;

  @Override
  public Flux<AccountResponse> findAll() {
    return repository.findAll()
      .map(mapper::responseToEntity)
      .sort(Comparator.comparing(AccountResponse::getId));
  }

  @Override
  public Mono<AccountResponse> findById(Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .map(mapper::responseToEntity);
  }

  @Override
  public Mono<AccountResponse> findByAccountNumber(String number) {
    return repository.findByAccountNumber(number)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .map(mapper::responseToEntity);
  }

  @Override
  public Flux<Account> findAccountByIdCustomer(Long idCustomer) {
    return repository.findAccountByIdCustomer(idCustomer)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)));
  }

  @Override
  public Mono<AccountResponse> save(AccountRequest request) {
    return repository.countAccountByAccountNumber(request.getAccountNumber())
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
  public Mono<AccountResponse> update(AccountRequest request, Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .flatMap(data -> {
        request.setId(id);
        request.setAccountNumber(data.getAccountNumber());
        return repository.save(mapper.entityToRequest(request))
          .map(mapper::responseToEntity);
      });
  }

  @Override
  @Modifying
  @Transactional
  public Mono<AccountResponse> updateAmount(BigDecimal amount, String operation, Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .flatMap(data -> {
        BigDecimal currentAmount = data.getInitialBalance();
        log.info("balance inicial:" + currentAmount);
        if (operation.equalsIgnoreCase(WITHDRAWAL)) { //RETIRO
          if (amount.compareTo(currentAmount) > 0) {
            return Mono.error(new BadRequestException(INSUFFICIENT_BALANCE));
          }
          data.setInitialBalance(currentAmount.subtract(amount));
        } else {
          data.setInitialBalance(currentAmount.add(amount));
        }
        log.info("balance final:" + data.getInitialBalance());
        return repository.save(data).map(mapper::responseToEntity);
      });
  }

  @Override
  public Mono<Void> delete(Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .flatMap(data -> repository.deleteById(id));
  }

}
