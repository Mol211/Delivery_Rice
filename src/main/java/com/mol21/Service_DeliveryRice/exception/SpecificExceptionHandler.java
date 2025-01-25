package com.mol21.Service_DeliveryRice.exception;

import com.mol21.Service_DeliveryRice.utils.GenericResponse;
import com.mol21.Service_DeliveryRice.utils.Global;
import org.hibernate.JDBCException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpecificExceptionHandler {
    @ExceptionHandler(JDBCException.class)
    public GenericResponse sqlException(JDBCException ex) {
        return new GenericResponse(Global.TIPO_EX_SQL, Global.RPTA_ERROR, Global.OPERACION_ERRONEA, ex.getMessage());

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResponse validException(MethodArgumentNotValidException ex){
        return new GenericResponse(Global.TIPO_EX_VALID, Global.RPTA_ERROR, Global.OPERACION_ERRONEA, ex.getMessage());
    }
}
