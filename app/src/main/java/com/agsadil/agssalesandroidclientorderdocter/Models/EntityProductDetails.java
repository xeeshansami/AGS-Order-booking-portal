package com.agsadil.agssalesandroidclientorderdocter.Models;

import java.io.Serializable;

/**
 * Created by Asad on 10/3/2016.
 */
public class EntityProductDetails implements Serializable {

    public boolean isProductSelected() {
        return productSelected;
    }

    public void setProductSelected(boolean productSelected) {
        this.productSelected = productSelected;
    }

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany;
    }

    public String getProd_Group_Name() {
        return Prod_Group_Name;
    }

    public void setProd_Group_Name(String prod_Group_Name) {
        Prod_Group_Name = prod_Group_Name;
    }

    public String getProd_salestax() {
        return prod_salestax;
    }

    public void setProd_salestax(String prod_salestax) {
        this.prod_salestax = prod_salestax;
    }

    public String getProd_Offer() {
        return prod_Offer;
    }

    public void setProd_Offer(String prod_Offer) {
        this.prod_Offer = prod_Offer;
    }

    public String getProd_OfferLimit() {
        return prod_OfferLimit;
    }

    public void setProd_OfferLimit(String prod_OfferLimit) {
        this.prod_OfferLimit = prod_OfferLimit;
    }

    private String productCompany;
    public String Prod_Group_Name;
    public String prod_salestax;
    public String prod_Offer;
    public String prod_OfferLimit;
    private boolean productSelected;
    private int productId;
    private String productName;
    private String productSize;
    private float productPrice;
    private int productQty;
    private int productBonus = 0;
    private int productDiscount = 0;
    private String productEnrtyFromAddress;
    private String latitude;
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public String getProductEnrtyFromAddress() {
        return productEnrtyFromAddress;
    }

    public void setProductEnrtyFromAddress(String productEnrtyFromAddress) {
        this.productEnrtyFromAddress = productEnrtyFromAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String longitude;
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public int getProductBonus() {
        return productBonus;
    }

    public void setProductBonus(int productBonus) {
        this.productBonus = productBonus;
    }

    public int getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(int productDiscount) {
        this.productDiscount = productDiscount;
    }

    public float getItemValue(){

        float total = productQty * productPrice;

        if(productDiscount > 0){
            total -= (total * productDiscount) / 100;
        }

        return total;
    }

}
