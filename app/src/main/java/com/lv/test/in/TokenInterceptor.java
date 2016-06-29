package com.lv.test.in;


import com.lv.test.DLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private String mAuthorization;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (loadAuthorization() != null) {
            request = request.newBuilder().addHeader("Authorization", mAuthorization).build();
        }
        Response response = chain.proceed(request);
        storeAuthorization(response.header("WWW-Authenticate", null));
        return response;
    }

    private String loadAuthorization() {
        if (mAuthorization == null) {
            //TODO 添加请求头
            mAuthorization="lvtanxi";
            DLog.d(mAuthorization);
        }
        return mAuthorization;
    }

    private void storeAuthorization(String authorization) {
        if (authorization == null) {
            return;
        }
        //TODO 缓存请求头
    }
}
