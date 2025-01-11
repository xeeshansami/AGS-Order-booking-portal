package com.ags.agssalesandroidclientorderdocter.Utils;

import java.io.IOException;

/**
 * Created by subhan on 11/22/18.
 */

public class NoInternetConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "No internet connection";    }
}

