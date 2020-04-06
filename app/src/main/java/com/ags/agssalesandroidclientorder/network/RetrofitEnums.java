package com.ags.agssalesandroidclientorder.network;


public enum RetrofitEnums {

    URL_MAPS(Constant.baseUrl),
    URL_HBL(Constant.baseUrl);


    RetrofitEnums(String url) {
        this.url = url;
    }

    String url;

    public String getUrl() {
        return url;
    }
}
