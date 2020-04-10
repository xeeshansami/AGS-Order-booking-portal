package com.ags.agssalesandroidclientorder.Models;

import java.util.List;

/**
 * Created by Asad on 10/8/2016.
 */
public class EntityOrder {
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private String orderId;
    private String orderDate;
    private String saleMenCode;
    private String customerCode;
    private String Salesmen;
    private String customer;
    private String netTotal;
    private String netDiscount;
    private String Branch;
    private String Remarks;

    private String orderSalName;
    private String orderCustName;
    private String orderCustAddress;
    private String orderCreatedOn;
    private String orderStatus;
    private List<EntityProductDetails> allProducts;
    private String TotalUniqueProducts;
    private String location;
    private String Location1;
    private String orderAddress;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation1() {
        return Location1;
    }

    public void setLocation1(String location1) {
        this.Location1 = location1;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getorderCreatedOn() {
        return orderCreatedOn;
    }

    public void setorderCreatedOn(String orderCreatedOn) {
        this.orderCreatedOn = orderCreatedOn;
    }



    public String getOrderSalName() {
        return orderSalName;
    }

    public void setOrderSalName(String orderSalName) {
        this.orderSalName = orderSalName;
    }

    public String getOrderCustName() {
        return orderCustName;
    }

    public void setOrderCustName(String orderCustName) {
        this.orderCustName = orderCustName;
    }

    public String getOrderCustAddress() {
        return orderCustAddress;
    }

    public void setOrderCustAddress(String orderCustAddress) {
        this.orderCustAddress = orderCustAddress;
    }



    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }



    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getNetDiscount() {
        return netDiscount;
    }

    public void setNetDiscount(String netDiscount) {
        this.netDiscount = netDiscount;
    }



    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }



    public String getSaleMenCode() {
        return saleMenCode;
    }

    public void setSaleMenCode(String saleMenCode) {
        this.saleMenCode = saleMenCode;
    }





    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getSalesmen() {
        return Salesmen;
    }

    public void setSalesmen(String salesmen) {
        Salesmen = salesmen;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public List<EntityProductDetails> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<EntityProductDetails> allProducts) {
        this.allProducts = allProducts;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String OrderId) {
        this.orderId = OrderId;
    }

    public String getTotalUniqueProducts() {
        return TotalUniqueProducts;
    }

    public void setTotalUniqueProducts(String totalUniqueProducts) {
        TotalUniqueProducts = totalUniqueProducts;
    }
}
