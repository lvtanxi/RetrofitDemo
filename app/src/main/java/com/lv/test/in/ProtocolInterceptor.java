package com.lv.test.in;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lv.test.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public  class ProtocolInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isNetworkAvailable()) {
            throw new IOException("没有网");
        }
        Response response = chain.proceed(chain.request());
        MediaType mediaType = MediaType.parse("application/json; chartset='utf-8'");
        String data = parseDataFromBody(response.body().string());
        return response.newBuilder().body(ResponseBody.create(mediaType, data)).build();
    }
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) MainApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    private String parseDataFromBody(String body) throws IOException {
        JSONObject json;
        try {
            json = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IOException("数据解析错误,请稍后重试!");
        }

        int code = json.optInt("code");
        String message = json.optString("message","");

        switch (code) {
            case 100:
                return json.optString("data");
            case 101:
            case 102:
            default:
                throw new RuntimeException(message);
        }
    }
}
