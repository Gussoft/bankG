package com.gussoft.cuentas.core.business.impl;

import static com.gussoft.cuentas.core.utils.Constrains.NOT_FOUND;
import static com.gussoft.cuentas.core.utils.Constrains.PIN_INVALID;
import static com.gussoft.cuentas.core.utils.Constrains.WITHDRAWAL;

import com.gussoft.cuentas.core.business.AccountService;
import com.gussoft.cuentas.core.business.CustomerService;
import com.gussoft.cuentas.core.business.TransactionService;
import com.gussoft.cuentas.core.exception.BadRequestException;
import com.gussoft.cuentas.core.exception.NotFoundException;
import com.gussoft.cuentas.core.mappers.AccountMapper;
import com.gussoft.cuentas.core.mappers.TransactionMapper;
import com.gussoft.cuentas.core.models.Account;
import com.gussoft.cuentas.core.models.Transaction;
import com.gussoft.cuentas.core.models.dto.Customer;
import com.gussoft.cuentas.core.repository.TransactionRepository;
import com.gussoft.cuentas.integration.tranfer.request.TransactionRequest;
import com.gussoft.cuentas.integration.tranfer.response.AccountDetResponse;
import com.gussoft.cuentas.integration.tranfer.response.AccountResponse;
import com.gussoft.cuentas.integration.tranfer.response.CustomerResponse;
import com.gussoft.cuentas.integration.tranfer.response.ReportRangeResponse;
import com.gussoft.cuentas.integration.tranfer.response.TransactionResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository repository;
  private final AccountService accountService;
  private final CustomerService customerService;

  private AccountMapper aMapper;
  private TransactionMapper mapper;

  @Override
  public Flux<TransactionResponse> findAll() {
    return repository.findAll()
      .map(mapper::responseToEntity)
      .sort(Comparator.comparing(TransactionResponse::getId));
  }

  @Override
  public Mono<TransactionResponse> findById(Long id) {
    return repository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .map(mapper::responseToEntity);
  }

  @Override
  public Mono<List<TransactionResponse>> findByIdAccount(Long idAccount) {
    return repository.findByIdAccount(idAccount)
      .switchIfEmpty(Mono.error(new NotFoundException(NOT_FOUND)))
      .collectList()
      .map(data -> data.stream()
        .map(mapper::responseToEntity)
        .collect(Collectors.toList()));
  }

  @Override
  public Mono<CustomerResponse> findByIdCustomer(Long id) {
    return customerService.findById(id)
      .flatMap(customer -> {
        List<AccountDetResponse> list = new ArrayList<>();
        Mono<List<Account>> accountsMono = accountService.findAccountByIdCustomer(id)
          .flatMap(account -> {
            AccountDetResponse response = new AccountDetResponse();
            response.setId(account.getId());
            response.setAccountType(account.getAccountType());
            response.setAccountNumber(account.getAccountNumber());
            response.setInitialBalance(account.getInitialBalance());
            response.setStatus(account.getStatus());
            return repository.findByIdAccount(account.getId()).collectList()
              .map(transactions -> {
                response.setTransaction(transactions);
                list.add(response);
                return account;
              });
          }).collectList();
        return accountsMono.map(accounts -> new CustomerResponse(customer, list));
      });
  }

  @Override
  public Flux<ReportRangeResponse> findByRangeDates(
    LocalDateTime init, LocalDateTime end, Long idAccount) {
    Mono<Account> accountMono = accountService.findById(idAccount)
      .map(aMapper::entityToResponse);
    Mono<Customer> customerMono = accountMono
      .flatMap(account -> customerService.findById(account.getIdCustomer()));
    Flux<Transaction> transactionFlux = repository.findByIdAccountAndCreateAtBetween(idAccount, init, end);
    return Mono.zip(accountMono, customerMono, transactionFlux.collectList())
      .flatMapMany(tuple -> {
        Account account = tuple.getT1();
        Customer customer = tuple.getT2();
        log.info("iniciando reporte de movimientos de : " + customer.getName());
        List<Transaction> transactions = tuple.getT3();
        return Flux.fromIterable(transactions)
          .map(transaction -> getReportRangeResponse(account, customer, transaction));
      });
  }

  @Override
  @Transactional
  public Mono<TransactionResponse> save(TransactionRequest request, String pin) {
    return verifyPin(pin, request.getIdAccount())
      .flatMap(valid -> {
        if (valid) {
          return accountService.updateAmount(
              request.getAmount(), request.getTransactionType(), request.getIdAccount())
            .flatMap(response -> {
              request.setBalance(response.getInitialBalance());
              return repository.save(mapper.entityToRequest(request))
                .map(mapper::responseToEntity);
            })
            .doOnError(err -> new BadRequestException(err.getMessage()));
        } else {
          return Mono.error(new BadRequestException(PIN_INVALID));
        }
      });
  }

  @Override
  public Mono<TransactionResponse> update(TransactionRequest request, Long id) {
    return null;
  }

  @Override
  public Mono<Void> delete(Long id) {
    return null;
  }

  private Mono<Boolean> verifyPin(String pin, Long idAccount) {
    return accountService.findById(idAccount)
      .flatMap(response -> customerService.findById(response.getIdCustomer()))
      .map(account -> account.getPassword().equalsIgnoreCase(pin))
      .defaultIfEmpty(false);
  }

  private ReportRangeResponse getReportRangeResponse(
    Account account, Customer customer, Transaction transaction) {
    ReportRangeResponse response = new ReportRangeResponse();
    response.setCreateAt(transaction.getCreateAt());
    response.setCustomer(customer.getName());
    response.setAccountNumber(account.getAccountNumber());
    response.setType(account.getAccountType());
    response.setStatus(true);
    response.setMovement(transaction.getAmount());
    if (transaction.getTransactionType().equalsIgnoreCase(WITHDRAWAL)) {
      response.setInitialBalance(transaction.getBalance().add(transaction.getAmount()));
      response.setMovement(transaction.getAmount().negate());
    } else {
      response.setInitialBalance(transaction.getBalance().subtract(transaction.getAmount()));
    }
    response.setAvailableBalance(transaction.getBalance());
    return response;
  }

}


/*
Mono<Customer> customerMono = customerService.findById(id);
    Flux<Account> accountMono = accountService.findAccountByIdCustomer(id);

    return Mono.zip(customerMono, accountMono.collectList())
      .flatMapMany(tuple -> {
        Customer customer = tuple.getT1();
        List<Account> accounts = tuple.getT2();
        CustomerResponse response = new CustomerResponse();
        accounts.stream().map(tran -> {
          Flux<Transaction> transactionFlux = repository.findByIdAccount(tran.getId());
        })
      })
 */