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
    Call<String> getProductsForSPO(@Query("compid") String branch);

    @GET("salesman?")
    Call<String> getSalesman(@Query("branch") String branch);

    @GET("salesman2")
    Call<String> getSalesmanForCustomer();

    @GET("customers2")
    Call<String> getSelfCustomer(@Query("branch") String branch,@Query("AccountID") String AccountID);

    @GET("salesmanItSelf")
    Call<String> getSelfSalesman(@Query("smid") String id);
}
