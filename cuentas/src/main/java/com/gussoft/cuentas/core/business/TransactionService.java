package com.gussoft.cuentas.core.business;

import com.gussoft.cuentas.integration.tranfer.request.TransactionRequest;
import com.gussoft.cuentas.integration.tranfer.response.CustomerResponse;
import com.gussoft.cuentas.integration.tranfer.response.ReportRangeResponse;
import com.gussoft.cuentas.integration.tranfer.response.TransactionResponse;
import java.time.LocalDateTime;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

  Flux<TransactionResponse> findAll();
  Mono<TransactionResponse> findById(Long id);
  Mono<List<TransactionResponse>> findByIdAccount(Long idAccount);
  Mono<CustomerResponse> findByIdCustomer(Long id);
  Flux<ReportRangeResponse> findByRangeDates(
    LocalDateTime init, LocalDateTime end, Long idAccount);
  Mono<TransactionResponse> save(TransactionRequest request , String pin);
  Mono<TransactionResponse> update(TransactionRequest request, Long id);
  Mono<Void> delete(Long id);

}
