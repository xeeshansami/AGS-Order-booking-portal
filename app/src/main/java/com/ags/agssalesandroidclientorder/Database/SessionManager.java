package com.ags.agssalesandroidclientorder.Database;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "cloudChef";

    //This would be used to store the email of current logged in user
    public static final String SESSION_SHARED_PREF = "sessionId";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";



    // Shared Preferences
    public SharedPreferences pref;

    public Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(LOGGEDIN_SHARED_PREF, isLoggedIn);

        //Apply changes
        editor.apply();
        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(LOGGEDIN_SHARED_PREF, false);
    }

}