package com.mycontact.model;

/**
 * OrganizationContact
 * Represents a business or organization contact.
 * @author Developer
 * @version 1.0
 */
public class OrganizationContact extends Contact {
    
    private String website;

    /**
     * Constructor to create an organization contact.
     * @param name        The organization name
     * @param phoneNumber The phone number
     * @param email       The email
     * @param website     The website URL
     */
    public OrganizationContact(String name, String phoneNumber, String email, String website) {
        super(name, phoneNumber, email);
        this.website = website;
    }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    @Override
    public String getContactType() {
        return "Organization";
    }

    @Override
    public String getDisplayDetails() {
        return super.getDisplayDetails() + " | Website: " + website;
    }
}