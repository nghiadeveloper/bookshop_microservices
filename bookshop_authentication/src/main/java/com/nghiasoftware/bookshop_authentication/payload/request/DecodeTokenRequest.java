package com.nghiasoftware.bookshop_authentication.payload.request;

import lombok.Data;

@Data
public class DecodeTokenRequest {
    private String token;
}
