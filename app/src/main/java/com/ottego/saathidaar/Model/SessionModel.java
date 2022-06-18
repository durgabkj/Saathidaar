package com.ottego.saathidaar.Model;

public class SessionModel {
    public String member_id;
    public String firstName;
    public String lastName;
    public String email;
    public String enabled;
    public String phone;
    public String username;
    public String results;

    public SessionModel(String member_id, String firstName, String lastName, String email, String enabled, String phone, String username, String results) {
        this.member_id = member_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = enabled;
        this.phone = phone;
        this.username = username;
        this.results = results;
    }
}
