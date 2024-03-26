package com.gussoft.cuentas.core.models;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "account", schema = "bankg")
@Getter
@Setter
@AllArgsConstructor
public class Account implements Serializable {

  @Id
  private Long id;
  private Long idCustomer;
  private String accountNumber;
  private String accountType;
  private BigDecimal initialBalance;
  private String status;

}
