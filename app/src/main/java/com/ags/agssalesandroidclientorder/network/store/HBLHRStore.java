package com.ags.agssalesandroidclientorder.network.store;

import android.app.Application;

import com.ags.agssalesandroidclientorder.network.APIInterface;
import com.ags.agssalesandroidclientorder.network.IOnConnectionTimeoutListener;
import com.ags.agssalesandroidclientorder.network.RetrofitEnums;


public class HBLHRStore extends Application implements IOnConnectionTimeoutListener {

    private static HBLHRStore consumerStore;
    APIInterface globalBaseUrl /*= RetrofitBuilder.INSTANCE.getRetrofitInstance(GlobalClass.applicationContext, RetrofitEnums.URL_HBL)*/;

    public static HBLHRStore getInstance() {
        if (consumerStore == null)
            return new HBLHRStore();
        else
            return consumerStore;
    }
/*

    //:TODO get all nearest locations
    public void getHospitals(RetrofitEnums url, HospitalRequest responseHospitals, HospitalCallBack signInUserCallback) {
        APIInterface consumerAPI = RetrofitBuilder..getRetrofitInstance(GlobalClass.applicationContext, url);
        consumerAPI.getHospitals(responseHospitals.getLat(), responseHospitals.getLng(), responseHospitals.getOS()).enqueue(new HospitalBaseHR(signInUserCallback));
    }

    //:TODO get all discount location
    public void getDiscountLocations(RetrofitEnums url, HospitalRequest responseHospitals, HospitalCallBack hospitalCallBack) {
        APIInterface privateInstanceRetrofit = RetrofitBuilder.INSTANCE.getRetrofitInstance(GlobalClass.applicationContext, url);
        privateInstanceRetrofit.getDiscountLocations(responseHospitals.getLat(), responseHospitals.getLng(), responseHospitals.getOS()).enqueue(new HospitalBaseHR(hospitalCallBack));
    }
*/


    @Override
    public void onConnectionTimeout() {

    }
}
