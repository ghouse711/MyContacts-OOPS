package com.mycontact.main;

import com.mycontact.auth.ActiveSession;
import com.mycontact.auth.Authentication;
import com.mycontact.auth.BasicAuth;
import com.mycontact.auth.OAuth;
import com.mycontact.exception.InvalidRegistrationException;
import com.mycontact.model.Contact;
import com.mycontact.model.FreeUser;
import com.mycontact.model.OrganizationContact;
import com.mycontact.model.PersonContact;
import com.mycontact.model.PremiumUser;
import com.mycontact.model.User;
import com.mycontact.model.UserProfile;
import com.mycontact.util.SecurityUtil;
import com.mycontact.util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
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

 * Description: User adds a new contact with name, phone numbers, email addresses, and optional fields.
 * OOP Concepts: Contact class hierarchy (Person, Organization), composition (Contact has PhoneNumber, Email objects)

 * @author Developer
 * @version 4.0

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
        // ... (Registration logic remains identical to UC3)
        System.out.println("\n--- Create a New Account ---");
        try {
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            if (!ValidationUtil.isValidEmail(email)) throw new InvalidRegistrationException("Invalid email format.");
            if (userDatabase.containsKey(email)) throw new InvalidRegistrationException("Email is already registered.");
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            if (!ValidationUtil.isValidPassword(password)) throw new InvalidRegistrationException("Password must be at least 6 characters.");
            
            System.out.print("Account Type (FREE/PREMIUM): ");
            String type = scanner.nextLine();
            
            String hashedPassword = SecurityUtil.hashPassword(password);
            UserProfile profile = new UserProfile(firstName, lastName);
            
            User newUser = "PREMIUM".equalsIgnoreCase(type) ? 
                new PremiumUser(email, hashedPassword, profile) : 
                new FreeUser(email, hashedPassword, profile);
            
            userDatabase.put(email, newUser);
            System.out.println("\nRegistration Successful! You can now log in.");
        } catch (InvalidRegistrationException e) {
            System.out.println("Registration Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during registration.");
        }
    }

    private static void handleLogin(Scanner scanner, Authentication authProvider) {
        // ... (Login logic remains identical to UC3)
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
            System.out.println("4. Manage Contacts"); // NEW MENU OPTION FOR UC4
            System.out.println("5. Logout");
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
                    contactManagementMenu(scanner);
                    break;
                case "5":
                    session.endSession();
                    System.out.println("Logged out successfully.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // ... (Profile view/update and password change methods remain identical to UC3)
    private static void viewProfile() {
        User user = session.getLoggedInUser();
        UserProfile profile = user.getProfile(); 
        System.out.println("\n--- Profile Details ---");
        System.out.println("Name: " + profile.getFirstName() + " " + profile.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Account Type: " + user.getUserType());
    }

    private static void updateProfile(Scanner scanner) {
        System.out.println("\n--- Update Profile ---");
        User user = session.getLoggedInUser();
        UserProfile currentProfile = user.getProfile(); 

        System.out.print("Enter New First Name (or press Enter to keep '" + currentProfile.getFirstName() + "'): ");
        String newFirst = scanner.nextLine();
        if (!newFirst.trim().isEmpty()) currentProfile.setFirstName(newFirst);

        System.out.print("Enter New Last Name (or press Enter to keep '" + currentProfile.getLastName() + "'): ");
        String newLast = scanner.nextLine();
        if (!newLast.trim().isEmpty()) currentProfile.setLastName(newLast);

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

    /**
     * Shows the Contact Management menu.
     * Allows adding and viewing different types of contacts.
     * @param scanner The scanner to read input
     */
    private static void contactManagementMenu(Scanner scanner) {
        boolean managing = true;
        
        while (managing) {
            System.out.println("\n--- Manage Contacts ---");
            System.out.println("1. Add Person Contact");
            System.out.println("2. Add Organization Contact");
            System.out.println("3. View All Contacts");
            System.out.println("4. Back to Dashboard");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            User currentUser = session.getLoggedInUser();

            switch (choice) {
                case "1":
                    System.out.print("Name: ");
                    String pName = scanner.nextLine();
                    System.out.print("Phone: ");
                    String pPhone = scanner.nextLine();
                    System.out.print("Email: ");
                    String pEmail = scanner.nextLine();
                    System.out.print("Relationship: ");
                    String pRel = scanner.nextLine();
                    
                    currentUser.addContact(new PersonContact(pName, pPhone, pEmail, pRel));
                    System.out.println("Person Contact added successfully.");
                    break;
                    
                case "2":
                    System.out.print("Organization Name: ");
                    String oName = scanner.nextLine();
                    System.out.print("Phone: ");
                    String oPhone = scanner.nextLine();
                    System.out.print("Email: ");
                    String oEmail = scanner.nextLine();
                    System.out.print("Website URL: ");
                    String oWeb = scanner.nextLine();
                    
                    currentUser.addContact(new OrganizationContact(oName, oPhone, oEmail, oWeb));
                    System.out.println("Organization Contact added successfully.");
                    break;
                    
                case "3":
                    List<Contact> contacts = currentUser.getContacts(); // Retrieves defensive copy
                    if (contacts.isEmpty()) {
                        System.out.println("Your contact list is empty.");
                    } else {
                        System.out.println("\n--- Your Contacts ---");
                        for (int i = 0; i < contacts.size(); i++) {
                            System.out.println((i + 1) + ". " + contacts.get(i).getDisplayDetails());
                        }
                    }
                    break;
                    
                case "4":
                    managing = false;
                    break;
                    
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}