package com.lv.rx2demo.client;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lv.rx2demo.In.LoggingInterceptor;
import com.lv.rx2demo.In.ProtocolInterceptor;
import com.lv.rx2demo.In.QueryParameterInterceptor;
import com.lv.rx2demo.In.TokenInterceptor;
import com.lv.rx2demo.MainApplication;
import com.lv.rx2demo.api.ApiInterface;

import java.io.File;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * User: 吕勇
 * Date: 2016-09-08
 * Time: 10:38
 * Description:
 */
public class RetrofitClient {
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private  ApiInterface mApiInterface;
    private  Retrofit mRetrofit;
    private static final AtomicReference<RetrofitClient> INSTANCE = new AtomicReference<>();

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        for (; ; ) {
            RetrofitClient current = INSTANCE.get();
            if (current != null)
                return current;
            current = new RetrofitClient();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }
    private  Retrofit createRetrofit() {
        if (null == mRetrofit) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            File cacheFile = MainApplication.getInstance().getApplicationContext().getCacheDir();
            if (cacheFile != null) {
                File directory = new File(cacheFile, "HttpResponseCache");
                builder.cache(new Cache(directory, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
            }
            builder.addNetworkInterceptor(new QueryParameterInterceptor())
                    .addNetworkInterceptor(new TokenInterceptor())
                    .addInterceptor(new ProtocolInterceptor())
                    .addInterceptor(new LoggingInterceptor())
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            addHttps(builder);

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://wmapp.wumart.com/wmapp-server/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(builder.build())
                    .build();
        }
        return mRetrofit;
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

    public  ApiInterface getApiInterface() {
        if (mApiInterface == null)
            mApiInterface = createService(ApiInterface.class);
        return mApiInterface;
    }

    private  <T> T createService(Class<T> clz) {
        return (createRetrofit().create(clz));
    }
}  
