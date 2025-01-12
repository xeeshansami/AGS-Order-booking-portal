package com.ags.agssalesandroidclientorderdocter.Models;

import java.io.Serializable;

/**
 * Created by Asad on 10/1/2016.
 */
public class EntityCustomer implements Serializable {

    private int customerId;

    public boolean getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(boolean selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    private boolean selectedCustomer;
    private String customerName;
    private String customerBranch;

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    private String customerAddress;

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
