package com.gussoft.customer.core.mappers;

import com.gussoft.customer.core.models.Customer;
import com.gussoft.customer.core.utils.Util;
import com.gussoft.customer.integration.tranfer.request.CustomerRequest;
import com.gussoft.customer.integration.tranfer.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Util.class)
public interface CustomerMapper {

  CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

  Customer entityToRequest(CustomerRequest request);

  CustomerRequest requestToEntity(Customer request);

  Customer entityToResponse(CustomerResponse response);

  @Mapping(target = "dni", expression = "java(Util.maskSensitiveData(response.getDni(), 2))")
  @Mapping(target = "phone", expression = "java(Util.maskSensitiveData(response.getPhone(), 3))")
  CustomerResponse responseToEntity(Customer response);

}
