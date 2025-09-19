package com.nghiasoftware.bookshop_authentication.controller;

import com.nghiasoftware.bookshop_authentication.payload.request.AuthenticationRequest;
import com.nghiasoftware.bookshop_authentication.payload.response.BaseResponse;
import com.nghiasoftware.bookshop_authentication.services.AuthenticationServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationServices authenticationServices;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody AuthenticationRequest authenticationRequest) {
        String token = authenticationServices.signIn(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("Login successful");
        response.setData(token);

        return ResponseEntity.ok(response);
    }
}
