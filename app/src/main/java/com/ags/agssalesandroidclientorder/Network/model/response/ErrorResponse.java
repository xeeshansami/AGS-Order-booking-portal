package com.ags.agssalesandroidclientorder.Network.model.response;

import java.io.Serializable;


public class ErrorResponse implements Serializable {

    String status;
    String message;
    boolean success;

    public ErrorResponse(String status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
