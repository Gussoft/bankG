package com.gussoft.customer.core.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "customer", schema = "bankg")
@Getter
@Setter
@AllArgsConstructor
public class Customer extends Person implements Serializable {

  @Id
  private Long id;

  private String password;
  private String state;

}
