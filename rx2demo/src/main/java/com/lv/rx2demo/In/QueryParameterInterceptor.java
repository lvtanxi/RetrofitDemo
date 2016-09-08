package com.lv.rx2demo.In;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * User: 吕勇
 * Date: 2016-06-30
 * Time: 14:59
 * Description:添加公共参数(Get)
 */
public class QueryParameterInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                // Provide your custom parameter here
                .addQueryParameter("platform", "android")
                .addQueryParameter("version", "1.0.0")
                .build();
        Request request = originalRequest.newBuilder().url(modifiedUrl).build();
        return chain.proceed(request);
    }
}
