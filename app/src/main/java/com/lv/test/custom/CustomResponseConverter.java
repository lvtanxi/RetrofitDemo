package com.lv.test.custom;

import com.google.gson.TypeAdapter;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 返回解析器
 */
class CustomResponseConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private final String name;

    CustomResponseConverter(TypeAdapter<T> adapter, String name) {
        this.adapter = adapter;
        this.name = name;
    }


    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        try {
            String body = responseBody.string();
            JSONObject json = new JSONObject(body);
            int code = json.optInt("code");
            if (code != 100)
                throw new RuntimeException(json.optString("msg"));
            if (json.has(name)){
                String jsonStr = json.getString(name);
                if(null==jsonStr||jsonStr.length()==2)
                    return null;
                return adapter.fromJson(jsonStr);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("程序异常!请稍候再试!");
        } finally {
            responseBody.close();
        }
    }
}