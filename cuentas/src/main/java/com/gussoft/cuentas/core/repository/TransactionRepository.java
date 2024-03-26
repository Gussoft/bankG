package com.gussoft.cuentas.core.repository;

import com.gussoft.cuentas.core.models.Transaction;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {

  Flux<Transaction> findByIdAccountAndCreateAtBetween(
    Long idAccount, LocalDateTime startDate, LocalDateTime endDate);

  Flux<Transaction> findByIdAccount(Long idAccount);

}
