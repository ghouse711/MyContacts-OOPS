package com.mycontact.model;


public abstract class User {
    private String email;
    private String passwordHash;
    private UserProfile profile;


    public User(String email, String passwordHash, UserProfile profile) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.profile = profile;
    }

    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserProfile getProfile() { return profile; }
    

    public abstract String getUserType();
}