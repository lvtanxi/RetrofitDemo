package com.lv.rx2demo.In;

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
        Response response = chain.proceed(chain.request());
        MediaType mediaType = MediaType.parse("application/json; chartset='utf-8'");
        String data = parseDataFromBody(response.body().string());
        return response.newBuilder().body(ResponseBody.create(mediaType, data)).build();
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
