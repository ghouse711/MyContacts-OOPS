package com.mycontact.model;


public class PersonContact extends Contact {
    
    private String relationship; // e.g., Friend, Family, Colleague


    public PersonContact(String name, String phoneNumber, String email, String relationship) {
        super(name, phoneNumber, email);
        this.relationship = relationship;
    }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    @Override
    public String getContactType() {
        return "Person";
    }

    @Override
    public String getDisplayDetails() {
        return super.getDisplayDetails() + " | Relation: " + relationship;
    }
}