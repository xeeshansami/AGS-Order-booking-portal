package com.ags.agssalesandroidclientorderdocter.Network;

import com.ags.agssalesandroidclientorderdocter.BuildConfig;
import com.ags.agssalesandroidclientorderdocter.Utils.ConnectivityInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
    public static Retrofit getClient(IOnConnectionTimeoutListener listener) {
        timeoutListener = listener;
        if (retrofit == null) {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("SSL");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            // Create an all-trusting hostname verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(allHostsValid);
            builder.readTimeout(APIConstants.READ_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(APIConstants.WRITE_TIMEOUT, TimeUnit.SECONDS);
            builder.connectTimeout(APIConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS);
            builder.callTimeout(30, TimeUnit.SECONDS);
            if (BuildConfig.DEBUG) {
//                if (MyApplication.getConsumerApplication() != null) {
//                    builder.addInterceptor(new ChuckInterceptor(MyApplication.getConsumerApplication()));
//                }
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    final Request original = chain.request();
                    final HttpUrl originalHttpUrl = original.url();

                    final HttpUrl url = originalHttpUrl.newBuilder()
                            .build();

                    // Request customization: add request headers
                    final Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    final Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

//            builder.addInterceptor(new ConnectivityInterceptor(MyApplication.getApplication()));
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            OkHttpClient client = builder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.baseUrl)
                    .client(client)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient(IOnConnectionTimeoutListener listener, long timeout) {
        timeoutListener = listener;
        if (retrofitLongTimeout == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder().readTimeout(timeout, TimeUnit.SECONDS).
                    writeTimeout(timeout, TimeUnit.SECONDS).connectTimeout(timeout, TimeUnit.SECONDS);

//            if (BuildConfig.DEBUG) {
//                if (MyApplication.getConsumerApplication() != null) {
//                    builder.addInterceptor(new ChuckInterceptor(MyApplication.getConsumerApplication()));
//                }
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
//            }
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    final Request original = chain.request();
                    final HttpUrl originalHttpUrl = original.url();

                    final HttpUrl url = originalHttpUrl.newBuilder()
                            .build();

                    // Request customization: add request headers
                    final Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    final Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            builder.addInterceptor(new ConnectivityInterceptor(MyApplication.getApplication()));

            OkHttpClient client = builder.build();

            retrofitLongTimeout = new Retrofit.Builder()
                    .baseUrl(Constant.baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofitLongTimeout;
    }

    private static Response OnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            if (BuildConfig.DEBUG) {
                /*LoggerUtil.printInfo(chain.request().url().toString());
                LoggerUtil.printInfo(chain.request().headers().toString());*/
            }
            Response response = chain.proceed(chain.request());
            return response;
        } catch (Exception exception) {
            exception.printStackTrace();
            if (timeoutListener != null)
                timeoutListener.onConnectionTimeout();
            return null;
        }
    }

    public static void onUrlChanged() {
        retrofit = null;
        retrofitLongTimeout = null;
    }
}
