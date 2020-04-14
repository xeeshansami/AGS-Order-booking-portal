package com.ags.agssalesandroidclientorder.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ags.agssalesandroidclientorder.Models.Data;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Asad on 10/11/2016.
 */
public class SharedPreferenceHandler {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SharedPreferenceHandler(Context context){
        pref = context.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }

    private String get(String key){
        return pref.getString(key, null);
    }

    private void set(String key, String value){
        editor.putString(key, value);

        editor.commit(); // commit changes
    }

    /* public methods */

    public List<Integer> getSelectedOrderIds(){
        String jsonGet = get("SelectedOrderId");
        Data jsonData = new Gson().fromJson(jsonGet, Data.class);
        return jsonData.myList;
    }

    public void setSelectedOrderIds(List<Integer> SelectedOrderId){

        final String jsonProds = new Gson().toJson(SelectedOrderId);
        set("SelectedOrderId", jsonProds);
    }

    public String getemail(){
        return get("email");
    }

    public void setemail(String email){
        set("email", email);
    }

    public String getcontact(){
        return get("contact");
    }

    public void setContact(String contact){
        set("contact", contact);
    }


    public String getuserid(){
        return get("userid");
    }

    public void setuserid(String userid){
        set("userid", userid);
    }

    public String getusername(){
        return get("username");
    }

    public void setusername(String username){
        set("username", username);
    }

    public String getpassword(){
        return get("password");
    }

    public void setpassword(String password){
        set("password", password);
    }

    public String getcategory(){
        return get("category");
    }

    public void setcategory(String category){
        set("category", category);
    }

    public String getUser_Category(){
        return get("User_Category");
    }

    public void setUser_Category(String User_Category){
        set("User_Category", User_Category);
    }

    public String getrole(){
        return get("role");
    }

    public void setrole(String role){
        set("role", role);
    }

    public String getbranch(){
        return get("branch");
    }

    public void setbranch(String branch){
        set("branch", branch);
    }

    public String getSingleData(){
        return get("singleData");
    }

    public void setSingleData(String branch){
        set("singleData", branch);
    }
    public void clearAll(){
        pref.edit().clear().commit();
    }
}
