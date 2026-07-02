package com.agsadil.agssalesandroidclientorderdocter.Network;


public class APIConstants {
//    public static String BASE_URL = BuildConfig.;

    // Shorter timeouts so an unreachable/slow backend surfaces an error quickly
    // instead of leaving the app on a loading spinner for 90 seconds.
    public static final long CONNECT_TIMEOUT = 20;
    public static final long WRITE_TIMEOUT = 45;
    public static final long READ_TIMEOUT = 45;

    public static final long READ_TIMEOUT_LONG = 300L;
    public static final String TAG = "LogValue";


}
