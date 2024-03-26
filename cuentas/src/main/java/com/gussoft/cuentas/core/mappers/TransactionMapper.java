package com.gussoft.cuentas.core.mappers;

import com.gussoft.cuentas.core.models.Transaction;
import com.gussoft.cuentas.integration.tranfer.request.TransactionRequest;
import com.gussoft.cuentas.integration.tranfer.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

  TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

  Transaction entityToRequest(TransactionRequest request);

  TransactionRequest requestToEntity(Transaction request);

  Transaction entityToResponse(TransactionResponse response);

  TransactionResponse responseToEntity(Transaction response);

}
