package com.mycontact.model;


public class FreeUser extends User {
    

    public FreeUser(String email, String passwordHash, UserProfile profile) {
        super(email, passwordHash, profile);
    }


    @Override
    public String getUserType() {
        return "Free Account";
    }
}