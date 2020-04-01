package com.ags.agssalesandroidclientorder.Models;

/**
 * Created by Asad on 10/3/2016.
 */
public class EntityProductDetails {

    private int productId;
    private String productName;
    private String productSize;
    private float productPrice;
    private int productQty;
    private int productBonus = 0;
    private int productDiscount = 0;
    private String productEnrtyFromAddress;
    private String latitude;

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
