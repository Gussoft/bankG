package com.gussoft.cuentas.integration.tranfer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse<E> {

    public GenericResponse(String message) {
        this.responseMessage = message;
    }

    public GenericResponse(String message, E data) {
        this.responseMessage = message;
        this.data = data;
    }

    public GenericResponse(String message, List<E> response) {
        this.responseMessage = message;
        this.response = response;
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

    @JsonInclude(value = Include.NON_NULL)
    private List<E> response;
}
