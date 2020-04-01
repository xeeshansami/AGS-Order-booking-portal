package com.ags.agssalesandroidclientorder.Models;

/**
 * Created by Asad on 10/1/2016.
 */
public class EntityCustomer {

    private int customerId;
    private String customerName;
    private String customerBranch;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerBranch() {
        return customerBranch;
    }

    public void setCustomerBranch(String customerBranch) {
        this.customerBranch = customerBranch;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }



}
