package com.agsadil.agssalesandroidclientorderdocter.Network;

import android.content.Context;

import com.agsadil.agssalesandroidclientorderdocter.BuildConfig;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.ConnectivityInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 *
 */
public class APIClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofitLongTimeout = null;
    private static IOnConnectionTimeoutListener timeoutListener;

    /**
     * @param listener
     * @return
     */

    public static Retrofit getClient(Context context, IOnConnectionTimeoutListener listener) {
        timeoutListener = listener;

        if (retrofit == null) {
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory(context);
            X509TrustManager trustManager = getTrustManager(context);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if (sslSocketFactory != null && trustManager != null) {
                builder.sslSocketFactory(sslSocketFactory, trustManager);
            }

            builder.hostnameVerifier((hostname, session) -> true); // Optional: use strict hostname in prod

            builder.readTimeout(APIConstants.READ_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(APIConstants.WRITE_TIMEOUT, TimeUnit.SECONDS);
            builder.connectTimeout(APIConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS);
            builder.callTimeout(30, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            builder.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl url = original.url().newBuilder().build();
                Request request = original.newBuilder().url(url).build();
                return chain.proceed(request);
            });

            OkHttpClient client = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.baseUrl)
                    .client(client)
                    .addConverterFactory(SimpleXmlConverterFactory.create()) // or GsonConverterFactory.create()
                    .build();
        }

        return retrofit;
    }

    // Load the certificate and return the SSL Socket Factory
    private static SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream certInput = context.getResources().openRawResource(R.raw.agssukker); // your .cert/.pem file
            Certificate ca = cf.generateCertificate(certInput);
            certInput.close();

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Extract TrustManager from cert
    private static X509TrustManager getTrustManager(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream certInput = context.getResources().openRawResource(R.raw.agssukker);
            Certificate ca = cf.generateCertificate(certInput);
            certInput.close();

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            return (X509TrustManager) tmf.getTrustManagers()[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void onUrlChanged() {
        retrofit = null;
        retrofitLongTimeout = null;
    }
}
