package com.gussoft.cuentas.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
      .title("Account Service")
      .description("Spring WebFlux, R2dbc, PostgreSQL Microservice")
      .contact(new Contact()
        .email("gussoft16@gmail.com")
        .name("Gustavo Huaman")
        .url("https://github.com/Gussoft")
      );
    return new OpenAPI().info(info);
  }
}
