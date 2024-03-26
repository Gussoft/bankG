package com.gussoft.cuentas.integration.tranfer.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponse implements Serializable {

  private Long id;
  private Long idAccount;
  private LocalDateTime createAt;
  private String transactionType;
  private BigDecimal amount;
  private BigDecimal balance;

}
