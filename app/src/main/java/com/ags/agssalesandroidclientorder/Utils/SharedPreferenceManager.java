package com.ags.agssalesandroidclientorder.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceManager {
    public static SharedPreferences sSharedPreferences;

    public static final SharedPreferenceManager sharedPrefManagerInstance = new SharedPreferenceManager();

    public static SharedPreferenceManager getInstance(Context context) {
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefManagerInstance;
    }

    public SharedPreferenceManager() {
    }

    public void storeStringInSharedPreferences(String key, String content) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(key, content);
        editor.apply();
    }

    public void removeStringInSharedPreferences(String key, String remove) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        if (key.equals("logout")) {
            editor.clear();
            editor.commit();
        } else if (remove.equals("remove")) {
            editor.remove(key).commit();
        }
    }


    private void storeLongInSharedPreferences(String key, long content) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putLong(key, content);
        editor.apply();
    }

    public void storeIntInSharedPreferences(String key, int content) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putInt(key, content);
        editor.apply();
    }

    private void storeFloatInSharedPreferences(String key, float content) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putFloat(key, content);
        editor.apply();
    }

    public static void storeBooleanInSharedPreferences(String key, boolean status) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putBoolean(key, status);
        editor.apply();
    }

    private long getLongFromSharedPreferences(String key) {
        return sSharedPreferences.getLong(key, 0L);
    }

    public int getIntFromSharedPreferences(String key) {
        return sSharedPreferences.getInt(key,0);
    }

    public static void setIntList(List<Integer> data) {
        //Set the values
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(Deleted_Count, json);
        editor.apply();
    }

    public List<Integer> getIntList() {
        Gson gson = new Gson();
        String json = sSharedPreferences.getString(Deleted_Count, null);
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private float getFloatFromSharedPreferences(String key) {
        return sSharedPreferences.getFloat(key, 0.0F);
    }

    public String getStringFromSharedPreferences(String key) {
        return sSharedPreferences.getString(key, "");
    }

    public boolean getBooleanFromSharedPreferences(String key) {
        return sSharedPreferences.getBoolean(key, false);
    }


    //Store and Retrieve order name
    public static final String SIGNUP_CONTROL = "SIGNUP_CONTROL";

    //Store and Retrieve order name
    public static final String ORDER_NAME = "order_name";

    //Store and Retrieve  BOOKING_ID
    public static final String BOOKING_ID = "bookingID";

    //Store and Retrieve serviceprovider name
    public static final String SERVICE_PROVIDER_NAME = "serviceprovider_name";


    //Store and Retrieve DATE
    public static final String DATE = "date";


    //Store and Retrieve BOOKING TYPE
    public static final String BOOKING_TYPE = "BOOKING_TYPE";

    //Store and Retrieve BOOKING TYPE
    public static final String TESTING_RATE = "TESTING_RATE";


    //Store and Retrieve DURATION
    public static final String DURATION = "DURATION";

    //Store and Retrieve net amount
    public static final String NET_AMOUNT = "NET_AMOUNT";

    //Store and Retrieve Delete Count
    public static final String Deleted_Count = "Deleted_Count";


    //Store and Retrieve Email Address
    public static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";

    //Store and Retrieve Reciever Id
    public static final String RECIEVER_ID = "RECIEVER_ID";

    //Store and Retrieve Reciever Id
    public static final String RECIEVER_USER_ID = "RECIEVER_USER_ID";

    //Store and Retrieve Total Amount
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";

    //Store and Retrieve Product Count
    public static final String PRODUCT_COUNT = "PRODUCT_COUNT";

    //Store and Retrieve Notification Count
    public static final String NOTIFICATION_COUNT = "NOTIFICATION_COUNT";

    //Store and Retrieve Chat Count
    public static final String CHAT_COUNT = "CHAT_COUNT";

    //Store and Retrieve Selected Product
    public static final String SELECTED_PRODUCTS = "SELECTED_PRODUCTS";

    //Store and Retrieve Total Order
    public static final String TOTAL_ORDERS = "TOTAL_ORDERS";

    //Store and Retrieve Selected Product
    public static final String SERVICE_PROVIDER_DETAILS = "SERVICE_PROVIDER_DETAILS";

    private static final String UDID = "udid";

    public void setUdid(String udid) {
        storeStringInSharedPreferences(UDID, udid);
    }

    public String getUdid() {

        return getStringFromSharedPreferences(UDID);

    }

    public <Order> void setOrder(ArrayList<Order> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        setOrderJson(json);
    }

    public void setOrderJson(String value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(TOTAL_ORDERS, value);
        editor.commit();
    }

    public <VendorProduct> void setVendorProducts(ArrayList<VendorProduct> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(json);
    }

    public void setVendorToJson(String value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(SERVICE_PROVIDER_DETAILS, value);
        editor.commit();
    }

    public void set(String value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(SELECTED_PRODUCTS, value);
        editor.commit();
    }



    public void setEmailAddress(String emailAddress) {
        storeStringInSharedPreferences(EMAIL_ADDRESS, emailAddress);
    }

    public void setTotalAmount(String totalAmount) {
        storeStringInSharedPreferences(TOTAL_AMOUNT, totalAmount);
    }

    public void setProductCountOfBaskit(int value) {
        storeIntInSharedPreferences(PRODUCT_COUNT, value);
    }

    public void setNotificationCount(int value) {
        storeIntInSharedPreferences(NOTIFICATION_COUNT, value);
    }

    public void setChatCount(int value) {
        storeIntInSharedPreferences(CHAT_COUNT, value);
    }

    public String getEmailAddress() {
        return getStringFromSharedPreferences(EMAIL_ADDRESS);
    }

    public String getTotalAmount() {
        return getStringFromSharedPreferences(TOTAL_AMOUNT);
    }

    public int getProductCountOfBaskit() {
        //If value is less than 0
        if (getIntFromSharedPreferences(PRODUCT_COUNT) < 0) {
            setProductCountOfBaskit(0);
            return 0;
        } else {
            //else return whole value
            return getIntFromSharedPreferences(PRODUCT_COUNT);
        }
    }


    public int getNotificationCount() {
        //If value is less than 0
        if (getIntFromSharedPreferences(NOTIFICATION_COUNT) < 0) {
            setProductCountOfBaskit(0);
            return 0;
        } else {
            //else return whole value
            return getIntFromSharedPreferences(NOTIFICATION_COUNT);
        }
    }

    public int getChatCount() {
        //If value is less than 0
        if (getIntFromSharedPreferences(CHAT_COUNT) < 0) {
            setProductCountOfBaskit(0);
            return 0;
        } else {
            //else return whole value
            return getIntFromSharedPreferences(CHAT_COUNT);
        }
    }

    //Store and Retrieve User password
    public static final String USER_PASSWORD = "USER_PASSWORD";

    public void setUserPassword(String password) {
        storeStringInSharedPreferences(USER_PASSWORD, password);
    }

    public String getUserPassword() {
        return getStringFromSharedPreferences(USER_PASSWORD);
    }

    //Is Fingerprint Authentication Enabled
    private static final String IS_FINGERPRINT_AUTH_ENABLED = "IS_FINGERPRINT_AUTH_ENABLED";

    public void setIsFingerprintAuthEnabled(boolean isEnabled) {
        storeBooleanInSharedPreferences(IS_FINGERPRINT_AUTH_ENABLED, isEnabled);
    }

    public boolean getIsFingerprintAuthEnabled() {
        return getBooleanFromSharedPreferences(IS_FINGERPRINT_AUTH_ENABLED);
    }

    //Store and Retrieve Email Address for Changing Password
    private static final String EMAIL_ADDRESS_FOR_CHANGE_PASSWORD = "EMAIL_ADDRESS_FOR_CHANGE_PASSWORD";

    public void setEmailAddressForChangePassword(String emailAddress) {
        storeStringInSharedPreferences(EMAIL_ADDRESS_FOR_CHANGE_PASSWORD, emailAddress);
    }

    public String getEmailAddressForChangePassword() {
        return getStringFromSharedPreferences(EMAIL_ADDRESS_FOR_CHANGE_PASSWORD);
    }

    //Store and Retrieve Password for Changing Password
    private static final String PASSWORD_FOR_CHANGE_PASSWORD = "PASSWORD_FOR_CHANGE_PASSWORD";

    public void setPasswordForChangePassword(String password) {
        storeStringInSharedPreferences(PASSWORD_FOR_CHANGE_PASSWORD, password);
    }

    public String getPasswordForChangePassword() {
        return getStringFromSharedPreferences(PASSWORD_FOR_CHANGE_PASSWORD);
    }

    public static final String USER_ID = "userID";

    public static final String ROLE_ID = "userRoleId";

    public static final String USER_TOKEN = "userToken";

    public void setToken(String token) {
        storeStringInSharedPreferences(USER_TOKEN, token);
    }

    public String getToken() {

        return getStringFromSharedPreferences(USER_TOKEN);

    }

    public void setUserId(int userId) {
        storeIntInSharedPreferences(USER_ID, userId);
    }

    public void setUserRoleId(int userRoleId) {
        storeIntInSharedPreferences(ROLE_ID, userRoleId);
    }


    public int getUserId() {
        return getIntFromSharedPreferences(USER_ID);

    }

    public int getUserRoleId() {
        return getIntFromSharedPreferences(ROLE_ID);

    }

    public static final String LOGIN_STATE = "isUserLoggedIn";

    public static void setIsUserLoggedIn(boolean isUserLoggedIn) {
        storeBooleanInSharedPreferences(LOGIN_STATE, isUserLoggedIn);
    }

    public boolean getUserLoggedIn() {
        return getBooleanFromSharedPreferences(LOGIN_STATE);
    }

    public static final String USER_VERIFICATION_STATE = "isVerifiedUser";

    public void setIsVerifiedUser(boolean isVerifiedUser) {
        storeBooleanInSharedPreferences(USER_VERIFICATION_STATE, isVerifiedUser);
    }

    public boolean getIsVerifiedUser() {
        return getBooleanFromSharedPreferences(USER_VERIFICATION_STATE);
    }

    private static final String EXCHANGE_LIST = "walletList";


    public void resetUserData() {
        storeBooleanInSharedPreferences(LOGIN_STATE, false);
        storeStringInSharedPreferences(EXCHANGE_LIST, "");
        storeStringInSharedPreferences(USER_TOKEN, "");
    }

    public static final String USER_NAME = "userName";

    public void setUserName(String userName) {
        storeStringInSharedPreferences(USER_NAME, userName);
    }

    public String getUserName() {
        return getStringFromSharedPreferences(USER_NAME);
    }

    private static final String USER_IMAGE = "userImage";
    private static final String USER_ADDRESS = "userAddress";

    public void setUserImage(String userImage) {
        storeStringInSharedPreferences(USER_IMAGE, userImage);
    }

    public void setUserAddress(String userAddress) {
        storeStringInSharedPreferences(USER_ADDRESS, userAddress);
    }

    private static final String USER = "user";


    public String getUser() {
        return sSharedPreferences.getString(USER, "");
    }


    public String getUserImage() {
        return getStringFromSharedPreferences(USER_IMAGE);
    }

    public String getUserAddress() {
        return getStringFromSharedPreferences(USER_ADDRESS);
    }

    public void setBaseUrl(String baseUrl) {
        storeStringInSharedPreferences("baseUrl", baseUrl);
    }

    public static final String LATITUDE = "latitude";

    public void setLatitude(Float latitude) {
        storeFloatInSharedPreferences(LATITUDE, latitude);
    }

    public Float getLatitude() {
        return getFloatFromSharedPreferences(LATITUDE);
    }

    public static final String LONGITUDE = "longitude";

    public void setLongitude(Float longitude) {
        storeFloatInSharedPreferences(LONGITUDE, longitude);
    }

    public Float getLongitude() {
        return getFloatFromSharedPreferences(LONGITUDE);
    }

    public static final String LANGUAGE = "language";

    public void setLanguage(String language) {
        storeStringInSharedPreferences(LANGUAGE, language);
    }

    public String getLanguage() {
        return getStringFromSharedPreferences(LANGUAGE);
    }

    public static final String PROVIDER_FOR_SEARCH = "searchByProvider";

    public void setProviderForSearch(String searchByProvider) {
        storeStringInSharedPreferences(PROVIDER_FOR_SEARCH, searchByProvider);
    }

    public String getProviderForSearch() {
        return getStringFromSharedPreferences(PROVIDER_FOR_SEARCH);
    }

    private static final String FCM_TOKEN = "fcmToken";

    public void setFcmToken(String token) {
        storeStringInSharedPreferences(FCM_TOKEN, token);
    }

    public String getFcmToken() {

        return getStringFromSharedPreferences(FCM_TOKEN);

    }

    public static final String USER_SOCIAL_TYPE = "userSocialType";

    public void setUserSocialType(String token) {
        storeStringInSharedPreferences(USER_SOCIAL_TYPE, token);
    }

    public String getUserSocialType() {

        return getStringFromSharedPreferences(USER_SOCIAL_TYPE);

    }

}
