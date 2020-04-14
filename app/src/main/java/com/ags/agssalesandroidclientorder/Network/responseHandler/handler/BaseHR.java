package com.ags.agssalesandroidclientorder.Network.responseHandler.handler;

import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;

import retrofit2.Response;

public class BaseHR extends RH<String> {
    callback callBack;

    public BaseHR(callback callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onSuccess(Response<String> response) {
        callBack.Success(response.body());
    }

    @Override
    public void onFailure(ErrorResponse response) {
        callBack.Failure(response);
    }
}
