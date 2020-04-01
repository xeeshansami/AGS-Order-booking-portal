package com.ags.agssalesandroidclientorder.Models;

/**
 * Created by Asad on 10/1/2016.
 */
public class EntityProduct {

    private int productId;
    private String productName;
    private String productSize;
    private float productPrice;
    private String productCompany;
    public String Prod_Group_Name;

    public String getProd_Group_Name() {
        return Prod_Group_Name;
    }

    public void setProd_Group_Name(String prod_Group_Name) {
        Prod_Group_Name = prod_Group_Name;
    }

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

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany;
    }

}
