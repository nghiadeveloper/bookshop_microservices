package com.nghiasoftware.bookshop_product.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private int code;
    private String message;
    private Object data;
}
