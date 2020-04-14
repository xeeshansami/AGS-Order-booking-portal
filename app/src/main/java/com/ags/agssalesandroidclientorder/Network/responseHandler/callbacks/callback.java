package com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks;

import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;

public interface callback {
    void Success(String response);
    void Failure(ErrorResponse response);
}
