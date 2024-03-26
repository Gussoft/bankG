package com.gussoft.cuentas.core.mappers;

import com.gussoft.cuentas.core.models.Account;
import com.gussoft.cuentas.core.utils.Util;
import com.gussoft.cuentas.integration.tranfer.request.AccountRequest;
import com.gussoft.cuentas.integration.tranfer.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Util.class)
public interface AccountMapper {

  AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

  Account entityToRequest(AccountRequest request);

  AccountRequest requestToEntity(Account request);

  Account entityToResponse(AccountResponse response);

  @Mapping(target = "accountNumber", expression = "java(Util.maskSensitiveData(response.getAccountNumber(), 4))")
  AccountResponse responseToEntity(Account response);

}
