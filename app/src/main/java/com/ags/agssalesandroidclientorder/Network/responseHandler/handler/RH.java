package com.ags.agssalesandroidclientorder.Network.responseHandler.handler;


import com.ags.agssalesandroidclientorder.Network.IOnConnectionTimeoutListener;
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class RH<T> implements Callback<T> {
    IOnConnectionTimeoutListener iOnConnectionTimeoutListener;
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful())
            onSuccess(response);
    }
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            iOnConnectionTimeoutListener.onConnectionTimeout();
        } else {
            onFailure(new ErrorResponse("error", false, t.getMessage()));
        }
    }
    protected abstract void onSuccess(Response<T> response);
    public abstract void onFailure(ErrorResponse response);
}