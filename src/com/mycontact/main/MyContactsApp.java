package com.mycontact.main;

import com.mycontact.model.FreeUser;
import com.mycontact.model.PremiumUser;
import com.mycontact.model.User;
import com.mycontact.model.UserProfile;
import com.mycontact.util.SecurityUtil;
import com.mycontact.util.ValidationUtil;

import java.util.Scanner;


public class MyContactsApp {
    

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to MyContacts App!");
        System.out.println("==========================");
        System.out.println("Register a new account:\n");
        
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Registration Failed: Invalid email format.");
            return;
        }
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        if (!ValidationUtil.isValidPassword(password)) {
            System.out.println("Registration Failed: Password must be at least 6 characters.");
            return;
        }
        
        System.out.print("Account Type (FREE/PREMIUM): ");
        String type = scanner.nextLine();
        
        // Hash the password securely
        String hashedPassword = SecurityUtil.hashPassword(password);
        
        // Create the profile
        UserProfile profile = new UserProfile(firstName, lastName);
        
        // Create the user object directly based on the requested type
        User newUser;
        if ("PREMIUM".equalsIgnoreCase(type)) {
            newUser = new PremiumUser(email, hashedPassword, profile);
        } else {
            newUser = new FreeUser(email, hashedPassword, profile);
        }
        
        System.out.println("\nRegistration Successful!");
        System.out.println("Name: " + newUser.getProfile().getFirstName() + " " + newUser.getProfile().getLastName());
        System.out.println("Account Type: " + newUser.getUserType());
        System.out.println("Secure Password Hash: " + newUser.getPasswordHash());
        
        scanner.close();
    }
}