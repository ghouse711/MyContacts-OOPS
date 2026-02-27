package com.mycontact.util;

import java.util.regex.Pattern;


public class ValidationUtil {
    
    // Standard format rule for emails
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);


    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return PATTERN.matcher(email).matches();
    }


    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}