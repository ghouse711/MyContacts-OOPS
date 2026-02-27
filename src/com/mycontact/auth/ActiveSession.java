package com.mycontact.auth;

import com.mycontact.model.User;


public class ActiveSession {
    
    private User loggedInUser;

    public void startSession(User user) {
        this.loggedInUser = user;
    }


    public void endSession() {
        this.loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }
}