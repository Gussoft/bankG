package com.gussoft.customer.integration.tranfer.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerResponse {

  private Long id;
  private String name;
  private String gender;
  private Integer age;
  private String dni;
  private String address;
  private String phone;
  private String password;
  private String state;

}
