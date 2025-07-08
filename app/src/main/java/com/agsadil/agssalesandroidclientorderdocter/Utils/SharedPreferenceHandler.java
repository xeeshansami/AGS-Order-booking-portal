package com.agsadil.agssalesandroidclientorderdocter.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.agsadil.agssalesandroidclientorderdocter.Models.Data;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Asad on 10/11/2016.
 */
public class SharedPreferenceHandler {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public String username="username";
    public String MyPref="MyPref";
    public String email="email";
    public String contact="contact";
    public String userid="userid";
    public String password="password";
    public String branch="branch";
    public String category="category";
    public String role="role";
    public String CompID="CompID";
    public String user_number="user_number";
    public String user_forget_pwd_number="user_forget_pwd_number";
    public String User_Category="User_Category";

    public SharedPreferenceHandler(Context context){
        pref = context.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }
    public void removeKey(String key) {
        if (pref.contains(key)) {
            editor.remove(key);
            editor.apply();
            editor.commit();
        } else {
        }
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
        return get(this.email);
    }

    public void setemail(String email){
        set(this.email, email);
    }

    public String getcontact(){
        return get(this.contact);
    }

    public void setContact(String contact){
        set(this.contact, contact);
    }


    public String getuserid(){
        return get(this.userid);
    }

    public void setuserid(String userid){
        set(this.userid, userid);
    }

    public String getusername(){
        return get(this.username);
    }

    public void setusername(String username){
        set(this.username, username);
    }

    public String getpassword(){
        return get(this.password);
    }

    public void setpassword(String password){
        set(this.password, password);
    }

    public String getcategory(){
        return get(this.category);
    }

    public void setcategory(String category){
        set(this.category, category);
    }

    public String getUser_Category(){
        return get(this.User_Category);
    }
    public void setUserNumber(String category){
        set(this.user_number, category);
    }

    public String getUserNumber(){
        return get(this.user_number);
    }

    public void setRandomNumber(String category){
        set(this.user_forget_pwd_number, category);
    }

    public String getRandomNumber(){
        return get(this.user_forget_pwd_number);
    }

    public void setUser_Category(String User_Category){
        set(this.User_Category, User_Category);
    }

    public String getrole(){
        return get(this.role);
    }

    public void setrole(String role){
        set(this.role, role);
    }

    public String getbranch(){
        return get(this.branch);
    }

    public void setbranch(String branch){
        set(this.branch, branch);
    }

    public String getCompID(){
        return get(this.CompID);
    }

    public void setCompID(String branch){
        set(this.CompID, branch);
    }
    public void clearAll(){
        pref.edit().clear().commit();
    }
}
