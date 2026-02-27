package com.mycontact.auth;

import com.mycontact.model.User;
import com.mycontact.util.SecurityUtil;

import java.util.Map;
import java.util.Optional;


public class BasicAuth implements Authentication {

    private Map<String, User> userDatabase;


    public BasicAuth(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
    }


    public Optional<User> authenticate(String email, String password) {
        User user = userDatabase.get(email);
        
        
        if (user != null) {
            String hashedInput = SecurityUtil.hashPassword(password);
            if (user.getPasswordHash().equals(hashedInput)) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty(); 
    }
}