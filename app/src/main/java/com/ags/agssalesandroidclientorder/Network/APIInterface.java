package com.ags.agssalesandroidclientorder.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("Login?")
    Call<String> getLogin(@Query("uname") String uname, @Query("pwd") String password);

    @GET("customers?")
    Call<String> getCustomers(@Query("branch") String branch);

    @GET("products?")
    Call<String> getProducts(@Query("branch") String branch);

    @GET("products2?")
    Call<String> getProductsForSPO(@Query("compid") String compid, @Query("branch") String branch);

    @GET("salesman?")
    Call<String> getSalesman(@Query("branch") String branch);

    @GET("salesman2")
    Call<String> getSalesmanForCustomer(@Query("branch") String branch);

    @GET("customers2")
    Call<String> getSelfCustomer(@Query("branch") String branch, @Query("AccountID") String AccountID);

    @GET("salesmanItSelf")
    Call<String> getSelfSalesman(@Query("smid") String id, @Query("branch") String branch);

    @GET("ResetPass?")
    Call<String> setUpdatePwd(@Query("newpass") String newpass, @Query("userid") String userid);

    @GET("productsOffer?")
    Call<String> getProductOffers(@Query("branch") String branch);

    @GET("LoginForPassword?")
    Call<String> getLoginForPassword(@Query("uname") String uname, @Query("mobilenumb") String mobilenumb);

    @GET("SubmitFeedBack?")
    Call<String> postFeedBack(@Query("userid") String userid,
                              @Query("userName") String userName,
                              @Query("Subject") String Subject,
                              @Query("usercategory") String usercategory,
                              @Query("iMessage") String iMessage,
                              @Query("branchid") String branchid);
}