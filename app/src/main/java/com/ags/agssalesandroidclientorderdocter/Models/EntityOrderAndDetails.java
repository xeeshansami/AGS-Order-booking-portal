package com.ags.agssalesandroidclientorderdocter.Models;

/**
 * Created by Asad on 10/10/2016.
 */
public class EntityOrderAndDetails {

    private String orderListId;
    private String orderInvoiceNo;
    private String orderListDate;
    private String orderSalCode;
    private String orderCustCode;
    private String orderTotalGross;
    private String orderTotalDiscount;
    private String orderTotalNet;
    private String orderTotalRemarks;

    private String orderSalName;
    private String orderCustName;
    private String orderCustAddress;
    private String orderCreatedOn;
    private String location;
    private String Location1;

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

    private String orderAddress;

    public String getOrderCreatedOn() {
        return orderCreatedOn;
    }

    public void setOrderCreatedOn(String orderCreatedOn) {
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


    private String orderBranch;
    private String orderBranchSerial;
    private String orderTownId;

    private String orderListDetailId;

    public String getOrderListId_FK() {
        return orderListId_FK;
    }

    public void setOrderListId_FK(String orderListId_FK) {
        this.orderListId_FK = orderListId_FK;
    }

    private String orderListId_FK;



    private String orderListDetailProdCode;
    private String orderListDetailProdName;
    private String orderListDetailProdSize;
    private String orderListDetailProdRate;
    private String orderListDetailProdQty;
    private String orderListDetailProdBonus;

    public String getOrderListDetailProdCode() {
        return orderListDetailProdCode;
    }

    public void setOrderListDetailProdCode(String orderListDetailProdCode) {
        this.orderListDetailProdCode = orderListDetailProdCode;
    }

    public String getOrderListDetailProdDiscount() {
        return orderListDetailProdDiscount;
    }

    public void setOrderListDetailProdDiscount(String orderListDetailProdDiscount) {
        this.orderListDetailProdDiscount = orderListDetailProdDiscount;
    }

    public String getOrderListId() {
        return orderListId;
    }

    public void setOrderListId(String orderListId) {
        this.orderListId = orderListId;
    }

    public String getOrderInvoiceNo() {
        return orderInvoiceNo;
    }

    public void setOrderInvoiceNo(String orderInvoiceNo) {
        this.orderInvoiceNo = orderInvoiceNo;
    }

    public String getOrderListDate() {
        return orderListDate;
    }

    public void setOrderListDate(String orderListDate) {
        this.orderListDate = orderListDate;
    }

    public String getOrderSalCode() {
        return orderSalCode;
    }

    public void setOrderSalCode(String orderSalCode) {
        this.orderSalCode = orderSalCode;
    }

    public String getOrderCustCode() {
        return orderCustCode;
    }

    public void setOrderCustCode(String orderCustCode) {
        this.orderCustCode = orderCustCode;
    }

    public String getOrderTotalGross() {
        return orderTotalGross;
    }

    public void setOrderTotalGross(String orderTotalGross) {
        this.orderTotalGross = orderTotalGross;
    }

    public String getOrderTotalDiscount() {
        return orderTotalDiscount;
    }

    public void setOrderTotalDiscount(String orderTotalDiscount) {
        this.orderTotalDiscount = orderTotalDiscount;
    }

    public String getOrderTotalNet() {
        return orderTotalNet;
    }

    public void setOrderTotalNet(String orderTotalNet) {
        this.orderTotalNet = orderTotalNet;
    }

    public String getOrderTotalRemarks() {
        return orderTotalRemarks;
    }

    public void setOrderTotalRemarks(String orderTotalRemarks) {
        this.orderTotalRemarks = orderTotalRemarks;
    }

    public String getOrderBranch() {
        return orderBranch;
    }

    public void setOrderBranch(String orderBranch) {
        this.orderBranch = orderBranch;
    }

    public String getOrderBranchSerial() {
        return orderBranchSerial;
    }

    public void setOrderBranchSerial(String orderBranchSerial) {
        this.orderBranchSerial = orderBranchSerial;
    }

    public String getOrderTownId() {
        return orderTownId;
    }

    public void setOrderTownId(String orderTownId) {
        this.orderTownId = orderTownId;
    }

    public String getOrderListDetailId() {
        return orderListDetailId;
    }

    public void setOrderListDetailId(String orderListDetailId) {
        this.orderListDetailId = orderListDetailId;
    }

    public String getOrderListDetailProdName() {
        return orderListDetailProdName;
    }

    public void setOrderListDetailProdName(String orderListDetailProdName) {
        this.orderListDetailProdName = orderListDetailProdName;
    }

    public String getOrderListDetailProdSize() {
        return orderListDetailProdSize;
    }

    public void setOrderListDetailProdSize(String orderListDetailProdSize) {
        this.orderListDetailProdSize = orderListDetailProdSize;
    }

    public String getOrderListDetailProdRate() {
        return orderListDetailProdRate;
    }

    public void setOrderListDetailProdRate(String orderListDetailProdRate) {
        this.orderListDetailProdRate = orderListDetailProdRate;
    }

    public String getOrderListDetailProdQty() {
        return orderListDetailProdQty;
    }

    public void setOrderListDetailProdQty(String orderListDetailProdQty) {
        this.orderListDetailProdQty = orderListDetailProdQty;
    }

    public String getOrderListDetailProdBonus() {
        return orderListDetailProdBonus;
    }

    public void setOrderListDetailProdBonus(String orderListDetailProdBonus) {
        this.orderListDetailProdBonus = orderListDetailProdBonus;
    }

    public String getOrderListDetailProdAmount() {
        return orderListDetailProdAmount;
    }

    public void setOrderListDetailProdAmount(String orderListDetailProdAmount) {
        this.orderListDetailProdAmount = orderListDetailProdAmount;
    }

    private String orderListDetailProdDiscount;
    private String orderListDetailProdAmount;



}
