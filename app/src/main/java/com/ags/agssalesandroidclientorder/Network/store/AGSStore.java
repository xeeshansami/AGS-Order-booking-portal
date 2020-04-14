package com.ags.agssalesandroidclientorder.Network.store;

import android.app.Application;

import com.ags.agssalesandroidclientorder.Network.APIClient;
import com.ags.agssalesandroidclientorder.Network.APIInterface;
import com.ags.agssalesandroidclientorder.Network.IOnConnectionTimeoutListener;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.responseHandler.handler.BaseHR;


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

    public void getSalesmanForCustomer(callback callback) {
        consumerAPI.getSalesmanForCustomer().enqueue(new BaseHR(callback));
    }

    public void getProducts(String branch, callback callback) {
        consumerAPI.getProducts(branch).enqueue(new BaseHR(callback));
    }

    public void getProductsForSPO(String compID, callback callback) {
        consumerAPI.getProductsForSPO(compID).enqueue(new BaseHR(callback));
    }

    public void getSelfCustomer(String branch, String AccountID, callback callback) {
        consumerAPI.getSelfCustomer(branch, AccountID).enqueue(new BaseHR(callback));
    }

    public void getSelfSalesman(String id, callback callback) {
        consumerAPI.getSelfSalesman(id).enqueue(new BaseHR(callback));
    }

    @Override
    public void onConnectionTimeout() {

    }
}
