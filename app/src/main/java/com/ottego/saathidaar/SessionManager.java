package com.ottego.saathidaar;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

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

    public void createSessionLogin(String id) {
        editor.putBoolean(IS_LOGIN, true);
        //editor.putString(KEY_ID, id);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
