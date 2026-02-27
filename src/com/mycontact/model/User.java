package com.mycontact.model;

import java.util.ArrayList;
import java.util.List;


public abstract class User {
    private String email;
    private String passwordHash;
    private UserProfile profile;
    private List<Contact> contacts; // New field for UC4


    public User(String email, String passwordHash, UserProfile profile) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.profile = profile != null ? profile.getCopy() : null;
        this.contacts = new ArrayList<>(); // Initialize the empty contact list
    }

    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserProfile getProfile() {
        return profile != null ? profile.getCopy() : null;
    }

    public void setProfile(UserProfile newProfile) {
        if (newProfile != null) {
            this.profile = newProfile.getCopy();
        }
    }


    public void addContact(Contact contact) {
        if (contact != null) {
            this.contacts.add(contact);
        }
    }


    public List<Contact> getContacts() {
        return new ArrayList<>(this.contacts);
    }
    
    public abstract String getUserType();
}