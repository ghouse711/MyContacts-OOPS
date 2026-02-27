package com.mycontact.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class SecurityUtil {


    public static String hashPassword(String password) {
        try {
            // Using MessageDigest as per core Java security requirements
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Critical Security Error: SHA-256 algorithm not found.");
            return null;
        }
    }
}