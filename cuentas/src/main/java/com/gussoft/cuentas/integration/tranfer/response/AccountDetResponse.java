package com.gussoft.cuentas.integration.tranfer.response;

import com.gussoft.cuentas.core.models.Transaction;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetResponse implements Serializable {

  private Long id;
  private String accountNumber;
  private String accountType;
  private BigDecimal initialBalance;
  private String status;

  private List<Transaction> transaction;

}
