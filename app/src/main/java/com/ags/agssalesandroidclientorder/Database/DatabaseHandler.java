package com.ags.agssalesandroidclientorder.Database;

import com.ags.agssalesandroidclientorder.Models.EntityCustomer;
import com.ags.agssalesandroidclientorder.Models.EntityOrder;
import com.ags.agssalesandroidclientorder.Models.EntityOrderAndDetails;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorder.Models.EntitySalesman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Asad on 10/6/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 15;
    private static final String DATABASE_NAME = "agssalesdatabase";

    // region customer table and columns

    // Customer table name
    private static final String TABLE_CUSTOMER = "customer";

    // Customer table column names
    private static final String customerId = "customerId";
    private static final String customerName = "customerName";
    private static final String customerBranch = "customerBranch";

    // endregion

    // region customer salesmen and columns

    // Customer table name
    private static final String TABLE_SALEMAN = "saleman";

    // Customer table column names
    private static final String salemanId = "salemanId";
    private static final String salemanName = "salemanName";

    // endregion

    // region product table and columns

    // Customer table name
    private static final String TABLE_PRODUCT = "product";

    // Customer table column names
    private static final String productId = "productId";
    private static final String productName = "productName";
    private static final String productSize = "productSize";
    private static final String productPrice = "productPrice";
    private static final String productCompany = "productCompany";
    private static final String Prod_Group_Name = "Prod_Group_Name";

    // endregion

    // region order list

    private static final String TABLE_ORDER_LIST = "order_list";

    // order list column names
    private static final String orderListId = "orderListId";
    private static final String orderInvoiceNo = "InvoiceNo";
    private static final String orderListDate = "orderListDate";
    private static final String orderSalCode = "orderSalCode";
    private static final String orderCustCode = "orderCustCode";
    private static final String orderTotalGross = "orderTotalGross";
    private static final String orderTotalDiscount = "orderTotalDiscount";
    private static final String orderTotalNet = "orderTotalNet";
    private static final String orderTotalRemarks = "orderTotalRemarks";
    private static final String orderBranch = "orderBranch";
    private static final String orderBranchSerial = "orderBranchSerial";
    private static final String orderTownId = "orderTownId";
    private static final String orderLatitude = "orderLatitude";
    private static final String orderLongitude = "orderLongitude";
    private static final String orderAddress = "orderAddress";
    private static final String orderRemarks = "orderRemarks";
    private static final String orderSalName = "orderSalName";
    private static final String orderCustName = "orderCustName";
    private static final String orderCustAddress = "orderCustAddress";
    private static final String orderCreatedOn = "orderCreatedOn";
    private static final String orderStatus = "orderStatus";

    // endregion

    // region order list detail

    private static final String TABLE_ORDER_LIST_DETAIL = "order_list_detail";

    // order list detail column names
    private static final String orderListDetailId = "orderListDetailId";
    private static final String orderListDetailProdCode = "orderListDetailProdCode";
    private static final String orderListDetailProdName = "orderListDetailProdName";
    private static final String orderListDetailProdSize = "orderListDetailProdSize";
    private static final String orderListDetailProdRate = "orderListDetailProdRate";
    private static final String orderListDetailProdQty = "orderListDetailProdQty";
    private static final String orderListDetailProdBonus = "orderListDetailProdBonus";
    private static final String orderListDetailProdDiscount = "orderListDetailProdDiscount";
    private static final String orderListDetailProdAmount = "orderListDetailProdAmount";

    // endregion

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CUSTOMER = "create table " + TABLE_CUSTOMER + "("
                + customerId + " integer primary key, "
                + customerName + " text, "
                + customerBranch + " text" + ")";
        db.execSQL(CREATE_TABLE_CUSTOMER);
        String CREATE_TABLE_PRODUCT = "create table " + TABLE_PRODUCT + "("
                + productId + " integer primary key, "
                + productName + " text, "
                + productSize + " text,"
                + productPrice + " text,"
                + productCompany + " text,"
                + Prod_Group_Name + " text)";
        db.execSQL(CREATE_TABLE_PRODUCT);
        String CREATE_TABLE_SALEMAN = "create table " + TABLE_SALEMAN + "(" + salemanId + " integer primary key, " + salemanName + " text)";
        db.execSQL(CREATE_TABLE_SALEMAN);
        String CREATE_TABLE_ORDER_LIST = "create table " + TABLE_ORDER_LIST + "("
                /*00*/ + orderListId + " integer primary key AUTOINCREMENT, "
                /*01*/ + orderInvoiceNo + " text, "
                /*02*/ + orderListDate + " text,"
                /*03*/ + orderSalCode + " text,"
                /*04*/ + orderCustCode + " text,"
                /*05*/ + orderTotalGross + " text,"
                /*06*/ + orderTotalDiscount + " text,"
                /*07*/ + orderTotalNet + " text,"
                /*08*/ + orderTotalRemarks + " text,"
                /*09*/ + orderBranch + " text,"
                /*10*/ + orderBranchSerial + " text,"
                /*11*/ + orderTownId + " text,"
                /*12*/ + orderSalName + " text,"
                /*13*/ + orderCustName + " text,"
                /*14*/ + orderCustAddress + " text,"
                /*15*/ + orderCreatedOn + " text,"
                /*16*/ + orderLatitude + " text,"
                /*17*/ + orderLongitude + " text,"
                /*18*/ + orderAddress + " text,"
                /*19*/ + orderStatus + " text)";

        db.execSQL(CREATE_TABLE_ORDER_LIST);

        String CREATE_TABLE_ORDER_LIST_DETAIL = "create table " + TABLE_ORDER_LIST_DETAIL + "("
                + orderListDetailId + " integer primary key AUTOINCREMENT, "
                + orderListId + " integer, "
                + orderInvoiceNo + " text,"
                + orderListDetailProdCode + " text,"
                + orderListDetailProdName + " text,"
                + orderListDetailProdSize + " text,"
                + orderListDetailProdRate + " text,"
                + orderListDetailProdQty + " text,"
                + orderListDetailProdBonus + " text,"
                + orderListDetailProdDiscount + " text,"
                + orderListDetailProdAmount + " text" + ")";

        db.execSQL(CREATE_TABLE_ORDER_LIST_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP" + " TABLE IF EXISTS " + TABLE_SALEMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_LIST_DETAIL);

        onCreate(db);

    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ORDER_LIST);
        db.execSQL("delete from " + TABLE_ORDER_LIST_DETAIL);
    }

    public void ChangeStatusToPosted() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update " + TABLE_ORDER_LIST + " set " + orderStatus + " = '1'");
    }

    // region Customer
    public boolean updateSingleOrder(String orderID, String qty, String bonus, String discount) {
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "update " + TABLE_ORDER_LIST_DETAIL + " set orderListDetailProdQty = " + qty + "," +
                "orderListDetailProdBonus = " + bonus + ",orderListDetailProdDiscount  = " + discount + " where orderListDetailId = " + orderID + ";";
        database.execSQL(sql);
        return true;
    }

    public void addAllCustomers(EntityCustomer allCustomers) {

        SQLiteDatabase db = this.getWritableDatabase();
/*
        db.execSQL("delete from " + TABLE_CUSTOMER);*/

//        for (EntityCustomer customer : allCustomers) {

        String sql = "insert into " + TABLE_CUSTOMER + " values (" + allCustomers.getCustomerId() + ", '" + allCustomers.getCustomerName() + "','" + allCustomers.getCustomerBranch() + "');";
        db.execSQL(sql);

//        }

    }

    public EntityCustomer getCustomer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_CUSTOMER + " where " + customerId + " = " + id + " LIMIT 1;";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null)
            cursor.moveToFirst();

        EntityCustomer customer = new EntityCustomer();
        customer.setCustomerId(Integer.parseInt(cursor.getString(0)));
        customer.setCustomerName(cursor.getString(1));
        customer.setCustomerBranch(cursor.getString(2));

        // return contact
        return customer;

    }

    public List<EntityCustomer> getAllCustomers() {

        SQLiteDatabase db = this.getWritableDatabase();
        List<EntityCustomer> customerList = new ArrayList<EntityCustomer>();

        // Select all query
        String selectQuery = "select * from " + TABLE_CUSTOMER;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EntityCustomer customer = new EntityCustomer();

                customer.setCustomerId(Integer.parseInt(cursor.getString(0)));
                customer.setCustomerName(cursor.getString(1));
                customer.setCustomerBranch(cursor.getString(2));

                // Adding contact to list
                customerList.add(customer);
            } while (cursor.moveToNext());
        }

        return customerList;

    }

    public List<EntityCustomer> getAllCustomers(String hint) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<EntityCustomer> customerList = new ArrayList<EntityCustomer>();

        // Select all query
        String selectQuery = "select * from " + TABLE_CUSTOMER + " where (customerName || ' ' || customerBranch) like '%" + hint + "%'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EntityCustomer customer = new EntityCustomer();

                customer.setCustomerId(Integer.parseInt(cursor.getString(0)));
                customer.setCustomerName(cursor.getString(1));
                customer.setCustomerBranch(cursor.getString(2));

                // Adding contact to list
                customerList.add(customer);
            } while (cursor.moveToNext());
        }

        return customerList;

    }

    public int getCustomerCount() {

        String countQuery = "SELECT  * FROM " + TABLE_CUSTOMER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    // endregion

    // region Salesman

    public void addAllSalesMan(EntitySalesman allSalesMan) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from " + TABLE_SALEMAN);
//        for (EntitySalesman salesman : allSalesMan) {
        String sql = "insert into " + TABLE_SALEMAN + " values (" + allSalesMan.getSalesman_Id() + ", '" + allSalesMan.getSalesman_Name() + "');";
        db.execSQL(sql);
//        }

    }

    public EntitySalesman getSalesMan(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_SALEMAN + " where " + salemanId + " = " + id + " LIMIT 1;";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null)
            cursor.moveToFirst();

        EntitySalesman salesman = new EntitySalesman();

        salesman.setSalesman_Id(Integer.parseInt(cursor.getString(0)));
        salesman.setSalesman_Name(cursor.getString(1));

        // return salesman
        return salesman;

    }

    public List<EntitySalesman> getAllSalesman() {

        SQLiteDatabase db = this.getWritableDatabase();
        List<EntitySalesman> salesmanList = new ArrayList<EntitySalesman>();

        // Select all query
        String selectQuery = "select * from " + TABLE_SALEMAN;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EntitySalesman salesman = new EntitySalesman();

                salesman.setSalesman_Id(Integer.parseInt(cursor.getString(0)));
                salesman.setSalesman_Name(cursor.getString(1));

                // Adding contact to list
                salesmanList.add(salesman);
            } while (cursor.moveToNext());
        }

        return salesmanList;

    }

    public int getSalesmanCount() {

        String countQuery = "SELECT  * FROM " + TABLE_SALEMAN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    // endregion

    // region Products

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PRODUCT);
        db.execSQL("delete from " + TABLE_SALEMAN);
        db.execSQL("delete from " + TABLE_CUSTOMER);
    }

    public void addAllProducts(EntityProduct product) {

        SQLiteDatabase db = this.getWritableDatabase();

//        db.execSQL("delete from " + TABLE_PRODUCT);

//        for (EntityProduct product : allProducts) {

        String sql = "insert into " + TABLE_PRODUCT + " values (" + product.getProductId() + ", '" + product.getProductName() + "','" + product.getProductSize() + "', '" + product.getProductPrice() + "', '" + product.getProductCompany() + "', '" + product.getProd_Group_Name() + "');";
        db.execSQL(sql);
//        }
    }

    public EntityProduct getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_PRODUCT + " where " + productId + " = " + id + " LIMIT 1;";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null)
            cursor.moveToFirst();

        EntityProduct product = new EntityProduct();

        product.setProductId(Integer.parseInt(cursor.getString(0)));
        product.setProductName(cursor.getString(1));
        product.setProductSize(cursor.getString(2));
        product.setProductPrice(Float.parseFloat(cursor.getString(3)));
        product.setProductCompany(cursor.getString(4));
        product.setProd_Group_Name(cursor.getString(5));

        // return contact
        return product;

    }

    public List<EntityProduct> getAllProducts() {

        SQLiteDatabase db = this.getWritableDatabase();
        List<EntityProduct> productList = new ArrayList<EntityProduct>();

        // Select all query
        String selectQuery = "select * from " + TABLE_PRODUCT + "  order by productName";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EntityProduct product = new EntityProduct();

                product.setProductId(Integer.parseInt(cursor.getString(0)));
                product.setProductName(cursor.getString(1));
                product.setProductSize(cursor.getString(2));
                product.setProductPrice(Float.parseFloat(cursor.getString(3)));
                product.setProductCompany(cursor.getString(4));
                product.setProd_Group_Name(cursor.getString(5));

                // Adding contact to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        return productList;

    }

    public List<EntityProduct> getAllProducts(String hint) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<EntityProduct> productList = new ArrayList<EntityProduct>();

        // Select all query
        String selectQuery = "select * from " + TABLE_PRODUCT + " where (productName || ' ' || productSize || ' ' || productPrice || ' ' || productCompany || ' ' || Prod_Group_Name) like '%" + hint + "%' order by productName";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EntityProduct product = new EntityProduct();

                product.setProductId(Integer.parseInt(cursor.getString(0)));
                product.setProductName(cursor.getString(1));
                product.setProductSize(cursor.getString(2));
                product.setProductPrice(Float.parseFloat(cursor.getString(3)));
                product.setProductCompany(cursor.getString(4));
                product.setProd_Group_Name(cursor.getString(5));

                // Adding contact to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        return productList;

    }

    public ArrayList<EntityOrder> getAllOrders() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<EntityOrder> order = new ArrayList<EntityOrder>();

        String helloWorld = "SELECT " +
                "ol.orderListId, " +
                "ol.InvoiceNo, " +
                "ol.orderListDate," +
                "ol.orderSalName, " +
                "ol.orderCustName, " +
                "ol.orderTotalNet," +
                "ol.orderTotalNet," +
                "ol.orderTotalRemarks," +
                "case when ifnull(ol.orderStatus, '0') = '0' then 'Un Posted' else 'Posted' end as orderStatus," +
                "(select count(*) from order_list_detail as old where old.orderListId = ol.orderListId) as OrderDetailCount," +
                "ol.orderCreatedOn" +
                " FROM " + TABLE_ORDER_LIST + " as ol order by ol.orderListId desc";

        Cursor cursor = db.rawQuery(helloWorld, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    EntityOrder singleOrder = new EntityOrder();

                    singleOrder.setOrderId(cursor.getString(0));
                    singleOrder.setOrderSalName(cursor.getString(3));
                    singleOrder.setOrderCustName(cursor.getString(4));
                    singleOrder.setOrderStatus(cursor.getString(7));
                    singleOrder.setNetTotal(cursor.getString(5));
                    singleOrder.setTotalUniqueProducts(cursor.getString(8));
                    singleOrder.setorderCreatedOn(cursor.getString(9));
//                    singleOrder.setLocation(cursor.getString(16));
//                    singleOrder.setLocation1(cursor.getString(17));
//                    singleOrder.setOrderAddress(cursor.getString(18));

                    // return contact
                    order.add(singleOrder);

                } while (cursor.moveToNext());
            }

        }

        return order;

    }

    public ArrayList<EntityOrder> getAllOrders(String OrderStatus) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<EntityOrder> order = new ArrayList<EntityOrder>();

        String helloWorld = "SELECT " +
                "ol.orderListId, " +
                "ol.InvoiceNo, " +
                "ol.orderListDate," +
                "ol.orderSalName, " +
                "ol.orderCustName, " +
                "ol.orderTotalNet," +
                "ol.orderTotalRemarks," +
                "case when ifnull(ol.orderStatus, '0') = '0' then 'Un Posted' else 'Posted' end as orderStatus," +
                "(select count(*) from order_list_detail as old where old.orderListId = ol.orderListId) as OrderDetailCount," +
                "ol.orderCreatedOn" +
                " FROM " + TABLE_ORDER_LIST + " as ol where ifnull(ol.orderStatus, '0') = '" + OrderStatus + "' order by ol.orderListId desc";

        Cursor cursor = db.rawQuery(helloWorld, null);
        Log.d("query", helloWorld);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    EntityOrder singleOrder = new EntityOrder();

                    singleOrder.setOrderId(cursor.getString(0));
                    singleOrder.setOrderSalName(cursor.getString(3));
                    singleOrder.setOrderCustName(cursor.getString(4));
                    singleOrder.setOrderStatus(cursor.getString(7));
                    singleOrder.setNetTotal(cursor.getString(5));
                    singleOrder.setTotalUniqueProducts(cursor.getString(8));
                    singleOrder.setorderCreatedOn(cursor.getString(9));
//                    singleOrder.setLocation(cursor.getString(15));
//                    singleOrder.setLocation1(cursor.getString(16));
//                    singleOrder.setOrderAddress(cursor.getString(17));

                    // return contact
                    order.add(singleOrder);

                } while (cursor.moveToNext());
            }

        }

        return order;

    }

    public int getAllOrdersCount() {

        String countQuery = "SELECT  * FROM " + TABLE_ORDER_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int getAllPostedOrdersCount() {

        String countQuery = "SELECT  * FROM " + TABLE_ORDER_LIST + " where orderStatus = '1'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int getAllUnPostedOrdersCount() {

        String countQuery = "SELECT  * FROM " + TABLE_ORDER_LIST + " where ifnull(orderStatus,'0') = '0'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int getProductCount() {

        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    // endregion

    // region order and details

    public void CreateOrder(EntityOrder order) {

        SQLiteDatabase db = this.getWritableDatabase();

        int lastId = 0;
        String sql = "insert into " + TABLE_ORDER_LIST + " (" +
                orderListDate + ", " +
                orderSalCode + ", " +
                orderCustCode + ", " +
                orderTotalGross + ", " +
                orderTotalDiscount + ", " +
                orderTotalNet + ", " +
                orderTotalRemarks + ", " +
                orderBranch + ", " +
                orderTownId + ", " +
                orderSalName + ", " +
                orderCustName + ", " +
                orderCustAddress + ", " +
                orderLatitude + "," +
                orderLongitude + "," +
                orderAddress + "," +
                orderCreatedOn + ") " +
                " values (date('now'), '" +
                order.getSaleMenCode() +
                "','" + order.getCustomerCode() +
                "', '" + order.getNetTotal() +
                "', '" + order.getNetDiscount() +
                "', '" + order.getNetTotal() +
                "', '" + order.getRemarks() +
                "', '" + order.getBranch() +
                "', '1', '" + order.getOrderSalName() +
                "', '" + order.getOrderCustName() +
                "', '" + order.getOrderCustAddress() +
                "', '" + order.getLocation() +
                "','" + order.getLocation1() +
                "','" + order.getOrderAddress() +
                "','" + order.getorderCreatedOn() +
                "');";
        db.execSQL(sql);

        String getLastId = "select " + orderListId + " from " + TABLE_ORDER_LIST + " order by " + orderListId + " desc limit 1";
        Cursor c = db.rawQuery(getLastId, null);

        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        for (EntityProductDetails productDetails : order.getAllProducts()) {

            String sqlDetails = "insert into " + TABLE_ORDER_LIST_DETAIL + " (" + orderListId + ", " + orderListDetailProdCode + ", " + orderListDetailProdName + ", " + orderListDetailProdSize + ", " + orderListDetailProdRate + ", " + orderListDetailProdQty + ", " + orderListDetailProdBonus + ", " + orderListDetailProdDiscount + ", " + orderListDetailProdAmount + ")" +
                    " values (" + lastId + ", '" + productDetails.getProductId() + "', '" + productDetails.getProductName() + "', '" + productDetails.getProductSize() + "', '" + productDetails.getProductPrice() + "', '" + productDetails.getProductQty() + "', '" + productDetails.getProductBonus() + "', '" + productDetails.getProductDiscount() + "', '" + productDetails.getItemValue() + "' );";

            db.execSQL(sqlDetails);
        }


    }

    public int getOrderCount() {

        String countQuery = "SELECT  count(*) FROM " + TABLE_ORDER_LIST + " where ifnull(orderStatus, '0') = '0'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    public String getTotalAmount() {
        String sumQuery = "select ifnull(sum(" + orderListDetailProdAmount + "), '0.00') from " + TABLE_ORDER_LIST_DETAIL + " as old inner join order_list as ol on old.orderListId = ol.orderListId and ifnull(ol.orderStatus, '0') = '0'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sumQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();

            return cursor.getString(0) + " Rs";
        } else {
            return "0.00" + " Rs";
        }
    }


    // endregion

    // region select order and details for synch

    public List<EntityOrderAndDetails> getOrderAndDetails(String branch) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<EntityOrderAndDetails> orderAndDetailsList = new ArrayList<EntityOrderAndDetails>();

        String helloWorld = "SELECT " +
                "ol.orderListId, " +
                "ol.InvoiceNo, " +
                "ol.orderListDate," +
                "ol.orderSalCode, " +
                "ol.orderCustCode, " +
                "ol.orderTotalGross," +
                "ol.orderTotalDiscount," +
                "ol.orderTotalNet," +
                "ol.orderTotalRemarks," +
                "'" + branch + "' as orderBranch," +
                "ol.orderBranchSerial," +
                "ol.orderTownId," +
                "old.orderListDetailId," +
                "old.orderListId as orderListId_FK," +
                "old.orderListDetailProdCode," +
                "old.orderListDetailProdName," +
                "old.orderListDetailProdSize," +
                "old.orderListDetailProdRate," +
                "old.orderListDetailProdQty," +
                "old.orderListDetailProdBonus," +
                "old.orderListDetailProdDiscount," +
                "old.orderListDetailProdAmount," +
                "ol.orderSalName," +
                "ol.orderCustName," +
                "ol.orderCustAddress," +
                "ol.orderCreatedOn," +

                "ol.orderLatitude," +
                "ol.orderLongitude," +
                "ol.orderAddress" +
                " FROM " + TABLE_ORDER_LIST + " as ol inner join " + TABLE_ORDER_LIST_DETAIL + " as old on ol." + orderListId +
                " = old." + orderListId + " and ifnull(ol.orderStatus, '0') = '0'";
        Cursor cursor = db.rawQuery(helloWorld, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    EntityOrderAndDetails orderAndDetails = new EntityOrderAndDetails();

                    orderAndDetails.setOrderListId(cursor.getString(0));
                    orderAndDetails.setOrderInvoiceNo(cursor.getString(1));
                    orderAndDetails.setOrderListDate(cursor.getString(2));
                    orderAndDetails.setOrderSalCode(cursor.getString(3));
                    orderAndDetails.setOrderCustCode(cursor.getString(4));
                    orderAndDetails.setOrderTotalGross(cursor.getString(5));
                    orderAndDetails.setOrderTotalDiscount(cursor.getString(6));
                    orderAndDetails.setOrderTotalNet(cursor.getString(7));
                    orderAndDetails.setOrderTotalRemarks(cursor.getString(8));
                    orderAndDetails.setOrderBranch(cursor.getString(9));
                    orderAndDetails.setOrderBranchSerial(cursor.getString(10));
                    orderAndDetails.setOrderTownId(cursor.getString(11));
                    orderAndDetails.setOrderListDetailId(cursor.getString(12));
                    orderAndDetails.setOrderListId_FK(cursor.getString(13));
                    orderAndDetails.setOrderListDetailProdCode(cursor.getString(14));
                    orderAndDetails.setOrderListDetailProdName(cursor.getString(15));
                    orderAndDetails.setOrderListDetailProdSize(cursor.getString(16));
                    orderAndDetails.setOrderListDetailProdRate(cursor.getString(17));
                    orderAndDetails.setOrderListDetailProdQty(cursor.getString(18));
                    orderAndDetails.setOrderListDetailProdBonus(cursor.getString(19));
                    orderAndDetails.setOrderListDetailProdDiscount(cursor.getString(20));
                    orderAndDetails.setOrderListDetailProdAmount(cursor.getString(21));
                    orderAndDetails.setOrderSalName(cursor.getString(22));
                    orderAndDetails.setOrderCustName(cursor.getString(23));
                    orderAndDetails.setOrderCustAddress(cursor.getString(24));
                    orderAndDetails.setOrderCreatedOn(cursor.getString(25));
                    orderAndDetails.setLocation(cursor.getString(26));
                    orderAndDetails.setLocation1(cursor.getString(27));
                    orderAndDetails.setOrderAddress(cursor.getString(28));

                    // return contact
                    orderAndDetailsList.add(orderAndDetails);

                } while (cursor.moveToNext());
            }

        }
        return orderAndDetailsList;
    }

    public List<EntityProductDetails> GetOrderDetails(int orderId) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<EntityProductDetails> orderAndDetailsList = new ArrayList<EntityProductDetails>();

        String helloWorld = "select orderListDetailProdName, orderListDetailProdSize, orderListDetailProdRate, orderListDetailProdQty, orderListDetailProdBonus, orderListDetailProdDiscount, orderListDetailProdAmount,orderListDetailId from order_list_detail where orderListId = " + orderId;
        Cursor cursor = db.rawQuery(helloWorld, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    EntityProductDetails orderAndDetails = new EntityProductDetails();
                    orderAndDetails.setProductId(Integer.parseInt(cursor.getString(7)));
                    orderAndDetails.setProductName(cursor.getString(0));
                    orderAndDetails.setProductSize(cursor.getString(1));
                    orderAndDetails.setProductPrice(Float.valueOf(cursor.getString(2)));
                    orderAndDetails.setProductQty(Integer.parseInt(cursor.getString(3)));
                    orderAndDetails.setProductBonus(Integer.parseInt(cursor.getString(4)));
                    orderAndDetails.setProductDiscount(Integer.parseInt(cursor.getString(5)));

                    // return contact
                    orderAndDetailsList.add(orderAndDetails);

                } while (cursor.moveToNext());
            }

        }
        return orderAndDetailsList;
    }

    public boolean delete(int value) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (value == 0) {
            String orderList = "delete from " + TABLE_ORDER_LIST;
            String orderDetailList = "delete from " + TABLE_ORDER_LIST_DETAIL;
            db.execSQL(orderList);
            db.execSQL(orderDetailList);
            return true;
        } else if (value == 1) {
            String customers = "delete from " + TABLE_CUSTOMER;
            String products = "delete from " + TABLE_PRODUCT;
            String salesman = "delete from " + TABLE_SALEMAN;
            db.execSQL(customers);
            db.execSQL(products);
            db.execSQL(salesman);
            return true;
        } else {
            return false;
        }


    }

    public void deleteOrdersOlderThenSevenDays() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlDetail = "delete from order_list_detail where orderListId in (select orderListId from order_list where (julianday('now') - julianday(orderListDate)) >= 7 and ifnull(orderStatus, '0') = '1');";
        db.execSQL(sqlDetail);

        SQLiteDatabase dbNew = this.getReadableDatabase();

        String sql = "delete from order_list where (julianday('now') - julianday(orderListDate)) >= 7 and ifnull(orderStatus, '0') = '1'";
        dbNew.execSQL(sql);

    }

    public boolean deleteOrders(List<Integer> selectedOrders) {


        for (Iterator<Integer> i = selectedOrders.iterator(); i.hasNext(); ) {
            SQLiteDatabase db = this.getReadableDatabase();
            Integer item = i.next();

            String sql = "delete from " + TABLE_ORDER_LIST + " where orderListId = " + item + ";";
            db.execSQL(sql);
            String sqlDetail = "delete from " + TABLE_ORDER_LIST_DETAIL + " where orderListId = " + item + ";";
            db.execSQL(sqlDetail);
        }

        return true;
    }

    public boolean unPostOrders(List<Integer> selectedOrders) {


        for (Iterator<Integer> i = selectedOrders.iterator(); i.hasNext(); ) {

            SQLiteDatabase db = this.getReadableDatabase();
            Integer item = i.next();

            String sql = "update " + TABLE_ORDER_LIST + " set orderStatus = '0' where orderListId = " + item + ";";
            db.execSQL(sql);
        }

        return true;
    }


    // endregion


}
