package com.lv.test.client;

import com.lv.test.ApiInterface2;
import com.lv.test.MainApplication;
import com.lv.test.custom.StringConverterFactory;
import com.lv.test.in.CacheInterceptor;
import com.lv.test.in.LoggingInterceptor;
import com.lv.test.in.QueryParameterInterceptor;
import com.lv.test.in.TokenInterceptor;

import java.io.File;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * User: 吕勇
 * Date: 2016-07-04
 * Time: 17:26
 * Description:
 */
public class Retrofit3Client {
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    public ApiInterface2 mApiInterface;

    public Retrofit3Client() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        File cacheFile = MainApplication.getInstance().getApplicationContext().getCacheDir();
        if (cacheFile != null) {
            File directory = new File(cacheFile, "HttpResponseCache");
            builder.cache(new Cache(directory, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
        }
        builder.addInterceptor(new CacheInterceptor())
                .addNetworkInterceptor(new QueryParameterInterceptor())
                .addNetworkInterceptor(new TokenInterceptor())
                //.addInterceptor(new ProtocolInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        addHttps(builder);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        Retrofit  mRetrofit = new Retrofit.Builder()
                .baseUrl("http://10.13.0.48:8080/TestWeb/")
                .addConverterFactory(StringConverterFactory.create())
                //.addConverterFactory(CustomConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build();

        mApiInterface=mRetrofit.create(ApiInterface2.class);
    }
    private  void addHttps(OkHttpClient.Builder builder) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }}, new SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory(), new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            });
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    private static class SingletonHolder {
        private static final Retrofit3Client INSTANCE = new Retrofit3Client();
    }

    public static Retrofit3Client getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
