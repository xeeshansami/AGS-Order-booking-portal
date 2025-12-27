package com.agsadil.agssalesandroidclientorderdocter.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.agsadil.agssalesandroidclientorderdocter.Models.Data;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Merged SessionManager + SharedPreferenceHandler
 * NOTHING REMOVED – ALL CODE PRESERVED
 */
public class SharedPreferenceHandler {

    // LogCat tag
    private static String TAG = SharedPreferenceHandler.class.getSimpleName();

    // SharedPreferences
    public static final String SHARED_PREF_NAME = "MyPref";
    public SharedPreferences pref;
    public Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    // ================= SessionManager KEYS =================
    public static final String SESSION_SHARED_PREF = "sessionId";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String CHECKED_REMEMBER = "CHECKED_REMEMBER";
    public static final String LOGGEDIN_GUEST_USER_SHARED_PREF = "loggedinAsGuestUser";
    public static final String DARK_MODE = "DARK_MODE";
    public static final String COLOR_MODE = "COLOR_MODE";
    public static final String PRODUCTS_LIST = "productsList";

    // ================= SharedPreferenceHandler KEYS =================
    public String username = "username";
    public String email = "email";
    public String contact = "contact";
    public String userid = "userid";
    public String password = "password";
    public String branch = "branch";
    public String category = "category";
    public String role = "role";
    public String CompID = "CompID";
    public String user_number = "user_number";
    public String user_forget_pwd_number = "user_forget_pwd_number";
    public String User_Category = "User_Category";

    // ================= Constructor =================
    public SharedPreferenceHandler(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    // ================= Common =================
    public void removeKey(String key) {
        if (pref.contains(key)) {
            editor.remove(key);
            editor.apply();
            editor.commit();
            Log.d(TAG, "Removed key: " + key);
        }
    }

    private String get(String key) {
        return pref.getString(key, null);
    }

    private void set(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void clearAll() {
        pref.edit().clear().commit();
    }

    // ================= Login & Session =================
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(LOGGEDIN_SHARED_PREF, isLoggedIn);
        editor.apply();
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(LOGGEDIN_SHARED_PREF, false);
    }

    public void setCheckedRemember(boolean isCheckedRemember) {
        editor.putBoolean(CHECKED_REMEMBER, isCheckedRemember);
        editor.apply();
        editor.commit();
    }

    public boolean isCheckedRemember() {
        return pref.getBoolean(CHECKED_REMEMBER, false);
    }

    public void setGuestUserLogin(boolean isGuestLoggedIn) {
        editor.putBoolean(LOGGEDIN_GUEST_USER_SHARED_PREF, isGuestLoggedIn);
        editor.apply();
        editor.commit();
    }

    public boolean isGuestUserLoggedIn() {
        return pref.getBoolean(LOGGEDIN_GUEST_USER_SHARED_PREF, false);
    }

    // ================= Theme =================
    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(DARK_MODE, isDarkMode);
        editor.apply();
        editor.commit();
    }

    public boolean isDarkMode() {
        return pref.getBoolean(DARK_MODE, false);
    }

    public void setColorMode(boolean isColor) {
        editor.putBoolean(COLOR_MODE, isColor);
        editor.apply();
        editor.commit();
    }

    public boolean isColorMode() {
        return pref.getBoolean(COLOR_MODE, false);
    }

    // ================= Products =================
    public void saveProductList(List<EntityProduct> productsList) {
        Gson gson = new Gson();
        String json = gson.toJson(productsList);
        editor.putString(PRODUCTS_LIST, json);
        editor.apply();
        editor.commit();
    }

    public List<EntityProduct> getProductList() {
        Gson gson = new Gson();
        String json = pref.getString(PRODUCTS_LIST, null);
        Type type = new TypeToken<List<EntityProduct>>() {}.getType();
        List<EntityProduct> productList = gson.fromJson(json, type);
        return productList != null ? productList : new ArrayList<>();
    }

    // ================= Selected Order IDs =================
    public List<Integer> getSelectedOrderIds() {
        String jsonGet = get("SelectedOrderId");
        Data jsonData = new Gson().fromJson(jsonGet, Data.class);
        return jsonData != null ? jsonData.myList : new ArrayList<>();
    }

    public void setSelectedOrderIds(List<Integer> SelectedOrderId) {
        final String jsonProds = new Gson().toJson(SelectedOrderId);
        set("SelectedOrderId", jsonProds);
    }

    // ================= User Data =================
    public String getemail() { return get(this.email); }
    public void setemail(String email) { set(this.email, email); }

    public String getcontact() { return get(this.contact); }
    public void setContact(String contact) { set(this.contact, contact); }

    public String getuserid() { return get(this.userid); }
    public void setuserid(String userid) { set(this.userid, userid); }

    public String getusername() { return get(this.username); }
    public void setusername(String username) { set(this.username, username); }

    public String getpassword() { return get(this.password); }
    public void setpassword(String password) { set(this.password, password); }

    public String getcategory() { return get(this.category); }
    public void setcategory(String category) { set(this.category, category); }

    public String getrole() { return get(this.role); }
    public void setrole(String role) { set(this.role, role); }

    public String getbranch() { return get(this.branch); }
    public void setbranch(String branch) { set(this.branch, branch); }

    public String getCompID() { return get(this.CompID); }
    public void setCompID(String compID) { set(this.CompID, compID); }

    public void setUserNumber(String number) { set(this.user_number, number); }
    public String getUserNumber() { return get(this.user_number); }

    public void setRandomNumber(String number) { set(this.user_forget_pwd_number, number); }
    public String getRandomNumber() { return get(this.user_forget_pwd_number); }

    public String getUser_Category() { return get(this.User_Category); }
    public void setUser_Category(String userCategory) { set(this.User_Category, userCategory); }
}
