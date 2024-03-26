package com.gussoft.cuentas.integration.tranfer.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRangeResponse {
  private LocalDateTime createAt;
  private String customer;
  private String accountNumber;
  private String type;
  private BigDecimal initialBalance;
  private Boolean status;
  private BigDecimal movement;
  private BigDecimal availableBalance;

}
