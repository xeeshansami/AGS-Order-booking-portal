package com.agsadil.agssalesandroidclientorderdocter.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("CustomerPurchaseHistoryQuery")
    Call<ResponseBody> getPurchaseHistory(
            @Field("branch") String branch,
            @Field("CustomerId") String customerId
    );
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

    // ===== Password-reset / OTP flow (PLACEHOLDER GET APIs) =====
    // TODO: replace the endpoint paths below with the real ones when provided.
    // 1) Verify that the user (by username/userid) owns this mobile number.
    @GET("VerifyUserForReset?")
    Call<String> verifyUserForReset(@Query("mobile") String mobile, @Query("userid") String userid);

    // 2) Update the password after OTP verification.
    @GET("ResetPasswordTwo?")
    Call<String> resetPassword(@Query("newpwd") String newpwd, @Query("userid") String userid);

    @GET("SubmitFeedBack?")
    Call<String> postFeedBack(@Query("userid") String userid,
                              @Query("userName") String userName,
                              @Query("Subject") String Subject,
                              @Query("usercategory") String usercategory,
                              @Query("iMessage") String iMessage,
                              @Query("branchid") String branchid);

    @GET("UpdateProfileTwo?")
    Call<String> postUpdateProfile(@Query("newname") String newname,
                                   @Query("newEmail") String newEmail,
                                   @Query("newMobile") String newMobile,
                                   @Query("userid") String userid);

    @GET("APIAdvertisement")
    Call<String> getNotifications();



    @GET("UpdateProfileTwo?")
    Call<String> postUpdateProfileForCustomer(@Query("newname") String newname,
                                              @Query("newEmail") String newEmail,
                                              @Query("newMobile") String newMobile,
                                              @Query("userid") String userid,
                                              @Query("vLat") String vLat,
                                              @Query("vLong") String vLong,
                                              @Query("cnicnumber") String cnicnumber,
                                              @Query("Prop") String Prop,
                                              @Query("vAddress") String vAddress,
                                              @Query("LicenceNo") String LicenceNo,
                                              @Query("UpdatedBYUser") String UpdatedByUser,
                                              @Query("ContactNo") String ContactNo,
                                              @Query("LicenceDate") String LicenceDate,
                                              @Query("strnNumber") String strnNumber
    );
}