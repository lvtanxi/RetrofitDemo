package com.lv.test.client;


import com.lv.test.ApiInterface;
import com.lv.test.MainApplication;
import com.lv.test.custom.CustomConverterFactory;
import com.lv.test.custom.StringConverterFactory;
import com.lv.test.in.CacheInterceptor;
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

/**
 * User: 吕勇
 * Date: 2016-02-24
 * Time: 09:23
 * Description:
 */
public class RetrofitClient {
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static Retrofit sRetrofit;
    private static ApiInterface sApiInterface;

    private static Retrofit createRetrofit() {
        if (null == sRetrofit) {
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
                   // .addInterceptor(new LoggingInterceptor())
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            addHttps(builder);

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            builder.cookieJar(new JavaNetCookieJar(cookieManager));

            sRetrofit = new Retrofit.Builder()
                    .baseUrl("http://10.13.2.166:8080/TestWeb/")
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(CustomConverterFactory.create())
                   // .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(builder.build())
                    .build();
        }
        return sRetrofit;
    }


    private static void addHttps(OkHttpClient.Builder builder) {
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
            builder.sslSocketFactory(sc.getSocketFactory());
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

    public static ApiInterface getApiInterface() {
        if (sApiInterface == null)
            sApiInterface = createService(ApiInterface.class);
        return sApiInterface;
    }

    private static <T> T createService(Class<T> clz) {
        return (createRetrofit().create(clz));
    }

}
