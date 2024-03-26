package com.gussoft.cuentas.integration.tranfer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionRequest implements Serializable {

  private Long id;
  @Positive(message = "IdAccount is required")
  @NotNull
  private Long idAccount;
  @NotNull
  private LocalDateTime createAt;
  @NotBlank(message = "TransactionType is required(RETIRO ó DEPOSITO)")
  @NotNull
  private String transactionType;
  @Positive(message = "Amount Only Number Positive < 0")
  @NotNull
  private BigDecimal amount;
  private BigDecimal balance;

  private String pin;

}
