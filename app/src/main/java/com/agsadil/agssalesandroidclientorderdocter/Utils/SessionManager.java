package com.agsadil.agssalesandroidclientorderdocter.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Keys for SharedPreferences
    public static final String SHARED_PREF_NAME = "MyPref";
    public static final String SESSION_SHARED_PREF = "sessionId";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String LOGGEDIN_GUEST_USER_SHARED_PREF = "loggedinAsGuestUser";
    public static final String DARK_MODE = "DARK_MODE";
    public static final String COLOR_MODE = "COLOR_MODE";
    public static final String PRODUCTS_LIST = "productsList";  // Key to store products list

    // SharedPreferences and Editor
    public SharedPreferences pref;
    public Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(LOGGEDIN_SHARED_PREF, isLoggedIn);
        editor.apply();
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }
    public void removeKey(String key) {
        if (pref.contains(key)) {
            editor.remove(key);
            editor.apply();
            editor.commit();
            Log.d(TAG, "Removed key: " + key);
        } else {
            Log.d(TAG, "Key not found: " + key);
        }
    }
    public boolean isLoggedIn() {
        return pref.getBoolean(LOGGEDIN_SHARED_PREF, false);
    }

    public void setGuestUserLogin(boolean isGuestLoggedIn) {
        editor.putBoolean(LOGGEDIN_GUEST_USER_SHARED_PREF, isGuestLoggedIn);
        editor.apply();
        editor.commit();
        Log.d(TAG, "Guest User login session modified!");
    }

    public boolean isGuestUserLoggedIn() {
        return pref.getBoolean(LOGGEDIN_GUEST_USER_SHARED_PREF, false);
    }

    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(DARK_MODE, isDarkMode);
        editor.apply();
        editor.commit();
        Log.d(TAG, "DarkMode modified!");
    }

    public boolean isDarkMode() {
        return pref.getBoolean(DARK_MODE, false);
    }

    public void setColorMode(boolean isColor) {
        editor.putBoolean(COLOR_MODE, isColor);
        editor.apply();
        editor.commit();
        Log.d(TAG, "ColorMode modified!");
    }

    public boolean isColorMode() {
        return pref.getBoolean(COLOR_MODE, false);
    }

    // Save the list of products
    public void saveProductList(List<EntityProduct> productsList) {
        Gson gson = new Gson();
        String json = gson.toJson(productsList);  // Convert list to JSON string
        editor.putString(PRODUCTS_LIST, json);  // Save it in SharedPreferences
        editor.apply();
        editor.commit();
        Log.d(TAG, "Product list saved!");
    }

    // Retrieve the list of products
    public List<EntityProduct> getProductList() {
        Gson gson = new Gson();
        String json = pref.getString(PRODUCTS_LIST, null);  // Get the JSON string from SharedPreferences
        Type type = new TypeToken<List<EntityProduct>>() {}.getType();  // Define the type of the list
        List<EntityProduct> productList = gson.fromJson(json, type);  // Convert JSON back to list
        return productList != null ? productList : new ArrayList<>();  // Return the list or an empty list if null
    }
}
