package com.ags.agssalesandroidclientorder.Models;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notifications implements Serializable {
    @SerializedName("CompanyCode")
    @Expose
    private String companyCode;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("Active")
    @Expose
    private String active;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("DurationStart")
    @Expose
    private String durationStart;
    @SerializedName("DurationEnd")
    @Expose
    private String durationEnd;
    @SerializedName("OfferExpiry")
    @Expose
    private String offerExpiry;
    @SerializedName("AddedBy")
    @Expose
    private String addedBy;
    @SerializedName("AddedOn")
    @Expose
    private String addedOn;
    @SerializedName("Pic_URL")
    @Expose
    private String picURL;
    @SerializedName("Title1")
    @Expose
    private String title1;
    @SerializedName("Title2")
    @Expose
    private String title2;
    @SerializedName("Title3")
    @Expose
    private String title3;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDurationStart() {
        return durationStart;
    }

    public void setDurationStart(String durationStart) {
        this.durationStart = durationStart;
    }

    public String getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(String durationEnd) {
        this.durationEnd = durationEnd;
    }

    public String getOfferExpiry() {
        return offerExpiry;
    }

    public void setOfferExpiry(String offerExpiry) {
        this.offerExpiry = offerExpiry;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

}
