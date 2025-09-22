package com.nghiasoftware.bookshop_gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nghiasoftware.bookshop_gateway.payload.request.AuthenDecodeRequest;
import com.nghiasoftware.bookshop_gateway.payload.response.AuthenticationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            HttpClient client = HttpClient.newHttpClient();

            AuthenDecodeRequest decodeRequest = new AuthenDecodeRequest();
            decodeRequest.setToken(token);
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(decodeRequest);

            HttpRequest requestAuthen = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/auth/decode"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            try {
                HttpResponse<String> responseAuthen = client.send(requestAuthen, HttpResponse.BodyHandlers.ofString());
                AuthenticationResponse authenResponse = objectMapper.readValue(responseAuthen.body(), AuthenticationResponse.class);

                List<GrantedAuthority> listAuthor = new ArrayList<>();
                for(String role : authenResponse.getData()) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    listAuthor.add(authority);
                }

                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(new UsernamePasswordAuthenticationToken("", "", listAuthor));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        filterChain.doFilter(request, response);
    }

}
