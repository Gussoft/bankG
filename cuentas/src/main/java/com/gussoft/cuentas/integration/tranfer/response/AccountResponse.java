package com.gussoft.cuentas.integration.tranfer.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponse implements Serializable {

  private Long id;
  private Long idCustomer;
  private String accountNumber;
  private String accountType;
  private BigDecimal initialBalance;
  private String status;

}
