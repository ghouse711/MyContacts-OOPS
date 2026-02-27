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
 * 
 * @author Developer
 * @version 2.0
 */
public class MyContactsApp {
    
    private static Map<String, User> userDatabase = new HashMap<>();
    private static ActiveSession session = new ActiveSession();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("Welcome to MyContacts App!");
        System.out.println("==========================");

        while (running) {
            System.out.println("\n1. Register");
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
            
            // Validation utilizing our custom exception
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
            // Gracefully catch and report domain-specific errors
            System.out.println("Registration Failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch-all for unexpected errors
            System.out.println("An unexpected error occurred during registration.");
        }
    }


    private static void handleLogin(Scanner scanner, Authentication authProvider) {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Credential (Password/Token): ");
        String credential = scanner.nextLine();

        Optional<User> loginResult = authProvider.authenticate(email, credential);

        if (loginResult.isPresent()) {
            session.startSession(loginResult.get());
            System.out.println("\nLogin Successful!");
            System.out.println("Welcome back, " + session.getLoggedInUser().getProfile().getFirstName() + "!");
            session.endSession();
        } else {
            System.out.println("\nLogin Failed: Invalid credentials.");
        }
    }
}