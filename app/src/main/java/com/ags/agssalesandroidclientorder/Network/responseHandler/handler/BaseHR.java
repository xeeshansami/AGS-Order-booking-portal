package com.ags.agssalesandroidclientorder.Network.responseHandler.handler;

import android.widget.Toast;

import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Utils.myApplication;

import retrofit2.Response;

public class BaseHR extends RH<String> {
    callback callBack;
    com.ags.agssalesandroidclientorder.Utils.myApplication myApplication;
    public BaseHR(callback callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onSuccess(Response<String> response) {
        myApplication=new myApplication();
        if(response.isSuccessful()) {
            callBack.Success(response.body());
        }else{
            Toast.makeText(myApplication.getBaseContext(), "something went wrong. from server, try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(ErrorResponse response) {
        callBack.Failure(response);
    }
}
