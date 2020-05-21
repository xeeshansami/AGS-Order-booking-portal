package com.ags.agssalesandroidclientorder.Network.store;

import android.app.Application;

import com.ags.agssalesandroidclientorder.Network.APIClient;
import com.ags.agssalesandroidclientorder.Network.APIInterface;
import com.ags.agssalesandroidclientorder.Network.IOnConnectionTimeoutListener;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.responseHandler.handler.BaseHR;

import retrofit2.http.Query;


public class AGSStore extends Application implements IOnConnectionTimeoutListener {
    private static AGSStore store;

    APIInterface consumerAPI = APIClient.getClient(this).create(APIInterface.class);

    private AGSStore() {
    }

    public static AGSStore getInstance() {
        if (store == null)
            return new AGSStore();
        else
            return store;
    }

    public void getLogin(String username, String password, callback callback) {
        consumerAPI.getLogin(username, password).enqueue(new BaseHR(callback));
    }

    public void getCustomer(String branch, callback callback) {
        consumerAPI.getCustomers(branch).enqueue(new BaseHR(callback));
    }

    public void getSalesman(String branch, callback callback) {
        consumerAPI.getSalesman(branch).enqueue(new BaseHR(callback));
    }

    public void getSalesmanForCustomer(String branch, callback callback) {
        consumerAPI.getSalesmanForCustomer(branch).enqueue(new BaseHR(callback));
    }

    public void getProducts(String branch, callback callback) {
        consumerAPI.getProducts(branch).enqueue(new BaseHR(callback));
    }

    public void getProductsForSPO(String compID, String branch, callback callback) {
        consumerAPI.getProductsForSPO(compID, branch).enqueue(new BaseHR(callback));
    }

    public void getSelfCustomer(String branch, String AccountID, callback callback) {
        consumerAPI.getSelfCustomer(branch, AccountID).enqueue(new BaseHR(callback));
    }

    public void getSelfSalesman(String id, String branch, callback callback) {
        consumerAPI.getSelfSalesman(id, branch).enqueue(new BaseHR(callback));
    }

    public void setUpdatePwd(String pwd, String id, callback callback) {
        consumerAPI.setUpdatePwd(pwd, id).enqueue(new BaseHR(callback));
    }

    public void getProductOffers(String branch, callback callback) {
        consumerAPI.getProductOffers(branch).enqueue(new BaseHR(callback));
    }

    public void getLoginForPassword(String uname, String mobileNumber, callback callback) {
        consumerAPI.getLoginForPassword(uname, mobileNumber).enqueue(new BaseHR(callback));
    }

    public void postFeedBack(String userid, String userName, String Subject, String usercategory, String iMessage, String branch, callback callback) {
        consumerAPI.postFeedBack(userid, userName, Subject, usercategory, iMessage, branch).enqueue(new BaseHR(callback));
    }

    public void postUpdateProfile(String newName, String newEmail, String newNumber, String userID, callback callback) {
        consumerAPI.postUpdateProfile(newName, newEmail, newNumber, userID).enqueue(new BaseHR(callback));
    }

    public void postUpdateProfileForCustomer(String newname,
                                             String vLat,
                                             String vLong,
                                             String LicenceNo,
                                             String LicenceDate,
                                             String Prop,
                                             String vAddress,
                                             String userid, callback callback) {
        consumerAPI.postUpdateProfileForCustomer(newname, vLat, vLong, LicenceNo, LicenceDate, Prop, vAddress, userid).enqueue(new BaseHR(callback));
    }

    public void onConnectionTimeout() {

    }
}
