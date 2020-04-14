
package com.ags.agssalesandroidclientorder.Network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccessResponse implements Serializable {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("User_Category")
    @Expose
    private String userCategory;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("User_comp")
    @Expose
    private String userComp;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getUserComp() {
        return userComp;
    }

    public void setUserComp(String userComp) {
        this.userComp = userComp;
    }

}
