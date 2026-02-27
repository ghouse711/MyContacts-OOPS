package com.mycontact.auth;

import com.mycontact.model.User;
import java.util.Optional;


public interface Authentication {
    
    Optional<User> authenticate(String email, String credentials);
}