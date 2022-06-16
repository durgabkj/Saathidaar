package com.ottego.saathidaar.Model;

public class SessionModel {
    public String username;
    public String firstName;
    public String lastName;
    public String email;
    public String role;
    public String gender;
    public String phone;
    public String profilecreatedby;

    public SessionModel(String username, String firstName, String lastName, String email, String role, String gender, String phone, String profilecreatedby) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.phone = phone;
        this.profilecreatedby = profilecreatedby;
    }
}
