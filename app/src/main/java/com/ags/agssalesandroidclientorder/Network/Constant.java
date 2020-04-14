package com.ags.agssalesandroidclientorder.Network;

import org.jetbrains.annotations.NotNull;

//import okhttp3.logging.HttpLoggingInterceptor;

public class Constant {
    final public static String baseUrl="http://mobile.agssukkur.com/agssalesclient.asmx/";
    public static final String BASE_URL_MAP = "";
    @NotNull
    public static final  String HEADER_TAG = "@";
    @NotNull
    public static final   String HEADER_POSTFIX = ": ";
    @NotNull
    public static final    String HEADER_TAG_PUBLIC = "public";

    public static final long API_CONNECT_TIMEOUT = 15000;
    public static final long API_READ_TIMEOUT = 15000;
    public static final long API_WRITE_TIMEOUT = 15000;

//    public static final HttpLoggingInterceptor.Level LOG_LEVEL_API = HttpLoggingInterceptor.Level.BODY;
}
