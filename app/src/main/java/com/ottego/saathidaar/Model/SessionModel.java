package com.ottego.saathidaar.Model;

public class SessionModel {
    public String member_id;
    public String firstName;
    public String lastName;
    public String email;
    public String enabled;
    public String phone;
    public String username;
    public String gender;
    public String results;
    public String profile_id;
    public String franchise_code;
    public String my_premium_status;
    public String profile_created_by;

    public SessionModel(String member_id, String firstName, String lastName, String email, String enabled, String phone, String username, String gender, String results, String profile_id, String franchise_code, String my_premium_status, String profile_created_by) {
        this.member_id = member_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = enabled;
        this.phone = phone;
        this.username = username;
        this.gender = gender;
        this.results = results;
        this.profile_id = profile_id;
        this.franchise_code = franchise_code;
        this.my_premium_status = my_premium_status;
        this.profile_created_by = profile_created_by;
    }
}











