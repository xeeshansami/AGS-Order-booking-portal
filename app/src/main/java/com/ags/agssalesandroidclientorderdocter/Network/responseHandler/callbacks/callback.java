package com.ags.agssalesandroidclientorderdocter.Network.responseHandler.callbacks;

import com.ags.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;

public interface callback {
    void Success(String response);
    void Failure(ErrorResponse response);
}
