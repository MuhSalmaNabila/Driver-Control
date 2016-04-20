package com.ditrol.tugasakhir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User id (make variable public to access from outside)
    public static final String KEY_ID = "id";

    // User name (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";

    // Password (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Plat motor(make variable public to access from outside)
    public static final String KEY_PLAT_MOTOR = "plat_motor";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Nomor Hp (make variable public to access from outside)
    public static final String KEY_NO_HP = "no_hp";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Create login session
     * */
    public void createLoginSession(String id, String username, String password, String plat_motor, String email, String no_hp){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing user id in pref
        editor.putString(KEY_ID, id);

        // Storing username in pref
        editor.putString(KEY_USERNAME, username);

        // Storing password in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing plat motor in pref
        editor.putString(KEY_PLAT_MOTOR, plat_motor);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing nomer hp in pref
        editor.putString(KEY_NO_HP, no_hp);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user id
        user.put(KEY_ID, pref.getString(KEY_ID, null));

        // user name
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        // user password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user plat motor
        user.put(KEY_PLAT_MOTOR, pref.getString(KEY_PLAT_MOTOR, null));

        // user email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user nomor hp
        user.put(KEY_NO_HP, pref.getString(KEY_NO_HP, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}