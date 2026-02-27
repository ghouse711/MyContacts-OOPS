package com.mycontact.main;

import com.mycontact.auth.ActiveSession;
import com.mycontact.auth.Authentication;
import com.mycontact.auth.BasicAuth;
import com.mycontact.auth.OAuth;
import com.mycontact.exception.InvalidRegistrationException;
import com.mycontact.model.FreeUser;
import com.mycontact.model.PremiumUser;
import com.mycontact.model.User;
import com.mycontact.model.UserProfile;
import com.mycontact.util.SecurityUtil;
import com.mycontact.util.ValidationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * MyContactsApp
 * Main entry point. Handles the interactive menu loop and managing user sessions.
 * 
 * Description: User creates an account with email, password, and profile information.
 * OOP Concepts: User class with encapsulation (private fields), validation logic, password hashing
 * Description: User logs in with credentials to access their contact list.
 * OOP Concepts: Authentication interface, concrete implementations (BasicAuth, OAuth), polymorphism
 * Description: User updates profile information, changes password, or manages preferences.
 * OOP Concepts: User class with setter methods, validation encapsulated in methods

 * @author Developer
 * @version 3.0
 */


public class MyContactsApp {
    
    private static Map<String, User> userDatabase = new HashMap<>();
    private static ActiveSession session = new ActiveSession();

    /**
     * Entry point for the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("Welcome to MyContacts App!");
        System.out.println("==========================");

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register");
            System.out.println("2. Login (Password)");
            System.out.println("3. Login (OAuth Token)");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleRegistration(scanner);
                    break;
                case "2":
                    handleLogin(scanner, new BasicAuth(userDatabase));
                    break;
                case "3":
                    handleLogin(scanner, new OAuth(userDatabase));
                    break;
                case "4":
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }


    private static void handleRegistration(Scanner scanner) {
        System.out.println("\n--- Create a New Account ---");
        
        try {
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            if (!ValidationUtil.isValidEmail(email)) {
                throw new InvalidRegistrationException("Invalid email format.");
            }
            
            if (userDatabase.containsKey(email)) {
                throw new InvalidRegistrationException("Email is already registered.");
            }
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            
            if (!ValidationUtil.isValidPassword(password)) {
                throw new InvalidRegistrationException("Password must be at least 6 characters.");
            }
            
            System.out.print("Account Type (FREE/PREMIUM): ");
            String type = scanner.nextLine();
            
            String hashedPassword = SecurityUtil.hashPassword(password);
            UserProfile profile = new UserProfile(firstName, lastName);
            
            User newUser;
            if ("PREMIUM".equalsIgnoreCase(type)) {
                newUser = new PremiumUser(email, hashedPassword, profile);
            } else {
                newUser = new FreeUser(email, hashedPassword, profile);
            }
            
            userDatabase.put(email, newUser);
            System.out.println("\nRegistration Successful! You can now log in.");
            
        } catch (InvalidRegistrationException e) {
            System.out.println("Registration Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during registration.");
        }
    }


    private static void handleLogin(Scanner scanner, Authentication authProvider) {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Credential (Token/Password): "); 
        String credential = scanner.nextLine();

        Optional<User> loginResult = authProvider.authenticate(email, credential);

        if (loginResult.isPresent()) {
            session.startSession(loginResult.get());
            System.out.println("\nLogin Successful!");
            System.out.println("Welcome back, " + session.getLoggedInUser().getProfile().getFirstName() + "!");
            
            // Navigate to the secure user dashboard
            loggedInMenu(scanner);
        } else {
            System.out.println("\nLogin Failed: Invalid credentials.");
        }
    }


    private static void loggedInMenu(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn && session.isLoggedIn()) {
            System.out.println("\n--- User Dashboard ---");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Change Password");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewProfile();
                    break;
                case "2":
                    updateProfile(scanner);
                    break;
                case "3":
                    changePassword(scanner);
                    break;
                case "4":
                    session.endSession();
                    System.out.println("Logged out successfully.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private static void viewProfile() {
        User user = session.getLoggedInUser();
        UserProfile profile = user.getProfile(); // This gets a defensive copy!
        
        System.out.println("\n--- Profile Details ---");
        System.out.println("Name: " + profile.getFirstName() + " " + profile.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Account Type: " + user.getUserType());
    }

    /**
     * Updates the user's first and last name.
     * @param scanner The scanner to read input
     */
    private static void updateProfile(Scanner scanner) {
        System.out.println("\n--- Update Profile ---");
        User user = session.getLoggedInUser();
        
        // Retrieve a defensive copy so we don't accidentally corrupt state if validation fails
        UserProfile currentProfile = user.getProfile(); 

        System.out.print("Enter New First Name (or press Enter to keep '" + currentProfile.getFirstName() + "'): ");
        String newFirst = scanner.nextLine();
        if (!newFirst.trim().isEmpty()) {
            currentProfile.setFirstName(newFirst);
        }

        System.out.print("Enter New Last Name (or press Enter to keep '" + currentProfile.getLastName() + "'): ");
        String newLast = scanner.nextLine();
        if (!newLast.trim().isEmpty()) {
            currentProfile.setLastName(newLast);
        }

        // Apply the validated and modified copy back to the user object
        user.setProfile(currentProfile);
        System.out.println("Profile updated successfully!");
    }


    private static void changePassword(Scanner scanner) {
        System.out.println("\n--- Change Password ---");
        User user = session.getLoggedInUser();

        System.out.print("Enter Current Password: ");
        String currentPass = scanner.nextLine();
        String currentHash = SecurityUtil.hashPassword(currentPass);

        if (currentHash == null || !user.getPasswordHash().equals(currentHash)) {
            System.out.println("Password change failed: Incorrect current password.");
            return;
        }

        System.out.print("Enter New Password: ");
        String newPass = scanner.nextLine();

        if (!ValidationUtil.isValidPassword(newPass)) {
            System.out.println("Password change failed: New password must be at least 6 characters.");
            return;
        }

        user.setPasswordHash(SecurityUtil.hashPassword(newPass));
        System.out.println("Password changed successfully!");
    }
}