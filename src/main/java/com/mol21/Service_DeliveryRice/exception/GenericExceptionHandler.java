package com.mol21.Service_DeliveryRice.exception;

import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import com.mol21.Service_DeliveryRice.utils.Global;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.mol21.Service_DeliveryRice.utils.Global.OPERACION_ERRONEA;
import static com.mol21.Service_DeliveryRice.utils.Global.RPTA_ERROR;

@RestControllerAdvice
public class GenericExceptionHandler {
    @ExceptionHandler(Exception.class)
    public GenericResponse genericResponse(Exception ex){
        return new GenericResponse(Global.TIPO_EX, RPTA_ERROR,OPERACION_ERRONEA,ex.getMessage());
    }
}
