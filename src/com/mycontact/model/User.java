package com.mycontact.model;


public abstract class User {
    private String email;
    private String passwordHash;
    private UserProfile profile;


    public User(String email, String passwordHash, UserProfile profile) {
        this.email = email;
        this.passwordHash = passwordHash;
        // Store a defensive copy upon creation
        this.profile = profile != null ? profile.getCopy() : null;
    }


    public String getEmail() { return email; }


    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public UserProfile getProfile() {
        return profile != null ? profile.getCopy() : null;
    }

 
    public void setProfile(UserProfile newProfile) {
        if (newProfile != null) {
            this.profile = newProfile.getCopy();
        }
    }
    
public abstract String getUserType();
}