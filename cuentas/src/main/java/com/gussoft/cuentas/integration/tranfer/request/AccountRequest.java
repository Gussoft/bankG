package com.gussoft.cuentas.integration.tranfer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountRequest implements Serializable {

  private Long id;
  @Positive(message = "IdCustomer is required")
  @NotNull
  private Long idCustomer;
  @NotBlank(message = "field AccountNumber is required")
  @NotNull
  private String accountNumber;
  @NotBlank(message = "field AccountType is required(CORRIENTE ó AHORROS)")
  @NotNull
  private String accountType;
  @PositiveOrZero(message = "InitialBalance is required")
  private BigDecimal initialBalance;
  private String status;

}
