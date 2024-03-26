package com.gussoft.customer.integration.tranfer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

  private Long id;
  @NotBlank(message = "field Name is required")
  @NotNull
  private String name;
  @NotBlank(message = "field Gender is required")
  @NotNull
  private String gender;
  @Positive(message = "field Age is required, not zero")
  @NotNull
  private Integer age;
  @NotBlank(message = "field Dni is required")
  @NotNull
  private String dni;
  @NotBlank(message = "field Address is required")
  @NotNull
  private String address;
  @NotBlank(message = "field Phone is required")
  @NotNull
  private String phone;
  @NotBlank(message = "field Password is required")
  @NotNull
  private String password;
  private String state;

}
