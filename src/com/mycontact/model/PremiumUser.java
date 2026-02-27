package com.mycontact.model;


public class PremiumUser extends User {
    

    public PremiumUser(String email, String passwordHash, UserProfile profile) {
        super(email, passwordHash, profile);
    }


    @Override
    public String getUserType() {
        return "Premium Account";
    }
}