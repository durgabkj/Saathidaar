package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ottego.saathidaar.Model.HoroscopeModel;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.SessionModel;
import com.ottego.saathidaar.Model.SessionProfileDetailModel;
import com.ottego.saathidaar.Model.UserModel;

public class SessionManager {

    public static final String KEY_MEMBER_ID = "member_id";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PHONE1 = "phone";
    public static final String KEY_EMAIL1 = "email";
    public static final String KEY_ENABLE = "enabled";
    public static final String KEY_USERID = "username";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PROFILE_ID_Log = "profile_id";
    public static final String KEY_PROFILE_CreatedBy = "profilecreatedby";

//user data
    public static final String KEY_about_ourself = "about_ourself";
    public static final String KEY_Gender = "gender";

    private static final String PREF_NAME = "userData";
    private static final String IS_LOGIN = "isLogin";
    private static SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSession(SessionModel model) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MEMBER_ID, model.member_id);
        editor.putString(KEY_FIRSTNAME, model.firstName);
        editor.putString(KEY_LASTNAME, model.lastName);
        editor.putString(KEY_PHONE1, model.phone);
        editor.putString(KEY_EMAIL1, model.email);
        editor.putString(KEY_ENABLE, model.enabled);
        editor.putString(KEY_USERID, model.username);
        editor.putString(KEY_GENDER, model.gender);
        editor.putString(KEY_PROFILE_ID_Log, model.profile_id);
        editor.putString(KEY_PROFILE_CreatedBy, model.profilecreatedby);

        editor.commit();
    }


    public String getMemberId() {
        return pref.getString(KEY_MEMBER_ID, "");
    }

    public String getName() {
        return pref.getString(KEY_FIRSTNAME, " ");
    }

    public String getLastName() {
        return pref.getString(KEY_LASTNAME, "");
    }

    public String getPhone1() {
        return pref.getString(KEY_PHONE1, "");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL1, "");
    }
    public String getKeyGender() {
        return pref.getString(KEY_GENDER, "");
    }
    public String getKey_profile_id() {
        return pref.getString(KEY_PROFILE_ID_Log, "");
    }

    public String getKEY_PROFILE_CreatedBy() {
        return pref.getString(KEY_PROFILE_CreatedBy, "");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void logoutUser() {
        clearSession();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);

    }

    public void createSessionGender(String gender) {
        editor.putString(KEY_Gender, gender);
        editor.commit();
    }

    public String getUserGender() {
        return pref.getString(KEY_Gender, "");
    }

    public void createSessionDescription(String description) {
        editor.putString(KEY_about_ourself, description);
        editor.commit();
    }

    public String getAbout_Description() {
        return pref.getString(KEY_about_ourself, "");
    }
}

