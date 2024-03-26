package com.gussoft.cuentas.integration.tranfer.response;

import com.gussoft.cuentas.core.models.dto.Customer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

  private Customer customer;
  private List<AccountDetResponse> account;

}
