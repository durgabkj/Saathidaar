package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ottego.saathidaar.Model.SessionModel;

public class SessionManager {

public static final String KEY_MEMBER_ID="member_id";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PHONE1 = "phone";
    public static final String KEY_EMAIL1 = "email";
    public static final String KEY_ENABLE = "enabled";
    public static final String KEY_USERID = "username";


//USER DETAIL
    public static final String KEY_FNAME = "firstName";
    public static final String KEY_LNAME = "lastname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_ROLE = "role";
    public static final String KEY_CREATEDBY = "profilecreatedby";




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

        editor.putString(KEY_FIRSTNAME, model.firstName);
        editor.putString(KEY_LASTNAME, model.lastName);
        editor.putString(KEY_PHONE1, model.phone);
        editor.putString(KEY_EMAIL1, model.email);
        editor.putString(KEY_ENABLE, model.enabled);
        editor.putString(KEY_USERID, model.username);

        editor.commit();
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


}
