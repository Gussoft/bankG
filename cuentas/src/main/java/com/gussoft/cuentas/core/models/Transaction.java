package com.gussoft.cuentas.core.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "transaction", schema = "bankg")
@Getter
@Setter
@AllArgsConstructor
public class Transaction implements Serializable {

  @Id
  private Long id;
  private Long idAccount;
  private LocalDateTime createAt;
  private String transactionType;
  private BigDecimal amount;
  private BigDecimal balance;

}
