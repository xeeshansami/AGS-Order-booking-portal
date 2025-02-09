package com.agsadil.agssalesandroidclientorderdocter.Network.responseHandler.callbacks;

import com.agsadil.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;

public interface callback {
    void Success(String response);
    void Failure(ErrorResponse response);
}
