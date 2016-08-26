package com.lv.test.in;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("lvtanxi", "lvtanxi")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method(originalRequest.method(), originalRequest.body());
        return chain.proceed(requestBuilder.build());
    }

}
