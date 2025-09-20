package com.nghiasoftware.bookshop_authentication.services;

import com.nghiasoftware.bookshop_authentication.payload.request.SignupRequest;

public interface AuthenticationServices {
    String signIn(String email, String password);
    void signUp(SignupRequest signupRequest);
}
