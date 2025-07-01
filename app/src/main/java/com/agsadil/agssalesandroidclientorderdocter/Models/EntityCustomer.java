package com.agsadil.agssalesandroidclientorderdocter.Models;

import java.io.Serializable;

/**
 * Created by Asad on 10/1/2016.
 */
public class EntityCustomer implements Serializable {

    private int customerId;

    public int getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(int selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    private int selectedCustomer;
    private String customerName;
    private String customerBranch;

    public String getAccountCNIC() {
        return accountCNIC;
    }

    public void setAccountCNIC(String accountCNIC) {
        this.accountCNIC = accountCNIC;
    }

    public String getAccountLocation1() {
        return accountLocation1;
    }

    public void setAccountLocation1(String accountLocation1) {
        this.accountLocation1 = accountLocation1;
    }

    public String getAccountLocation2() {
        return accountLocation2;
    }

    public void setAccountLocation2(String accountLocation2) {
        this.accountLocation2 = accountLocation2;
    }

    private String accountCNIC;
    private String accountLocation1;
    private String accountLocation2;

    public String getAccountTaxRation() {
        return accountTaxRation;
    }

    public void setAccountTaxRation(String accountTaxRation) {
        this.accountTaxRation = accountTaxRation;
    }

    private String accountTaxRation;

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
