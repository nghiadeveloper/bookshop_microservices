package com.nghiasoftware.bookshop_authentication.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignupRequest {

    @Email(message = "Email is not valid")
    private String email;

    private String fullName;

    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;

    private String address;

}
