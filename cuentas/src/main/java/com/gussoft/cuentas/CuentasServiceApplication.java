package com.gussoft.cuentas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories
@SpringBootApplication
public class CuentasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentasServiceApplication.class, args);
	}

}
