package com.mycontact.auth;

import com.mycontact.model.User;

import java.util.Map;
import java.util.Optional;


public class OAuth implements Authentication {

    private Map<String, User> userDatabase;


    public OAuth(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public Optional<User> authenticate(String email, String token) {
        System.out.println("\n[System] Connecting to external OAuth provider for " + email + "...");
        System.out.println("[System] Validating secure token: " + token);
        
        // Simulate a successful login if the token is valid and user exists
        if (token != null && !token.trim().isEmpty()) {
            User user = userDatabase.get(email);
            if (user != null) {
                System.out.println("[System] OAuth validation successful (Mocked).");
                return Optional.of(user);
            }
        }
        
        System.out.println("[System] OAuth validation failed: User not found or invalid token.");
        return Optional.empty();
    }
}