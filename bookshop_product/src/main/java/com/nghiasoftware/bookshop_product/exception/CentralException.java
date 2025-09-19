package com.nghiasoftware.bookshop_product.exception;

import com.nghiasoftware.bookshop_product.payload.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CentralException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse> handleProductNotFound(RuntimeException e) {
        BaseResponse response = new BaseResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
