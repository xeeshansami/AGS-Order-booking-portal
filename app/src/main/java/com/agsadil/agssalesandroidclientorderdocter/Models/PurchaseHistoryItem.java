package com.agsadil.agssalesandroidclientorderdocter.Models;

import com.google.gson.annotations.SerializedName;

public class PurchaseHistoryItem {
    @SerializedName("HItemCode")
    public String itemCode;

    @SerializedName("HItemName")
    public String itemName;

    @SerializedName("HRate")
    public String rate;

    @SerializedName("HQTY")
    public String qty;

    @SerializedName("HBONUS")
    public String bonus;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
