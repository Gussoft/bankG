package com.gussoft.customer.integration.tranfer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenericResponse<E> {

    public GenericResponse(String message) {
        this.responseMessage = message;
    }

    public GenericResponse(String message, E data) {
        this.responseMessage = message;
        this.data = data;
    }

    @ApiModelProperty(
        value = "Mensaje de la respuesta del servicio o representación textual del código de estado HTTP", 
        example = "200 | Eliminacion Exitosa, | CREATED",
        allowEmptyValue = false)
    @JsonProperty("message")
    private String responseMessage;

    @ApiModelProperty(value = "Información de la respuesta del servicio, puede ser un objeto json", 
        example = "200",
        allowEmptyValue = true)
    @JsonInclude(value = Include.NON_NULL)
    private E data;

}
