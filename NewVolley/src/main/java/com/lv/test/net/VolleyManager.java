package com.lv.test.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * User: 吕勇
 * Date: 2015-09-14
 * Time: 14:04
 * Description: Volley 的操作类
 */
public class VolleyManager {

    private static VolleyManager mVolleyManager;

    private Gson mGson;

    private RequestQueue mRequestQueue;

    private  Map<String,String> mHeaders;

    private VolleyManager() {
        mGson=new Gson();
        mHeaders=new HashMap<>();
    }

    /**
     * 初始化RequestQueue
     * @param context 内容上下文
     */
    public  void initRequestQueue(Context context) {
        if (mVolleyManager == null) {
            synchronized (RequestQueue.class) {
                if (mVolleyManager == null) {
                    mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
                }
            }
        }
    }

    public static VolleyManager getVolleyManager() {
        if(mVolleyManager==null)
            mVolleyManager=new VolleyManager();
        return mVolleyManager;
    }


    private  RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * 把网络任务添加进任务池
     * @param req 一个网络请求
     * @param tag 请求的标签tag
     */
    public <T> void addToRequestQueue(Request<T> req, Object tag) {
        req.setTag(null==tag? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * 取消请求
     * @param tag 取消请求的标签tag
     */
    public  void cancelPendingRequest(Object tag) {
        if (mRequestQueue != null&&tag!=null)
            mRequestQueue.cancelAll(tag);
    }

    /**
     * 设置请求头
     * @param headers
     */
    public  void setHeaders(Map<String,String> headers){
        getHeaders().clear();
        // TODO: 2016/6/6 添加 headers
        getHeaders().putAll(headers);
    }

    public Gson getGson() {
        return mGson;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }


}
