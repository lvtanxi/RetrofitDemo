package com.lv.test.net;

import android.text.TextUtils;

import com.android.volley.Response;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author 吕勇
 * @Title: VolleyResponseListener.java
 * @Description: 解析请求结果
 * @date 2016-1-26 下午2:55:34
 */
public class VolleyResponseListener<T> implements Response.Listener<JSONObject> {


    private WidgetInterface mWidgetInterface;

    private String node;

    private boolean showDialog;

    protected boolean success;


    /**
     * 泛型的类型
     */
    protected Type mType;

    /**
     * 很关键的方法获取泛型的类型
     *
     * @param subclass 类的class
     * @return 泛型的类型
     */
    public Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) return null;
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public VolleyResponseListener(WidgetInterface widgetInterface) {
        this(widgetInterface, true);
    }

    public VolleyResponseListener(WidgetInterface widgetInterface, boolean showDialog) {
        this(widgetInterface, null, showDialog);
    }

    public VolleyResponseListener(WidgetInterface widgetInterface, String node) {
        this(widgetInterface, node, true);
    }

    public VolleyResponseListener(WidgetInterface widgetInterface, String node, boolean showDialog) {
        this.showDialog = showDialog;
        this.mWidgetInterface = widgetInterface;
        onStart();
        this.node = node;
    }


    //显示toast
    public boolean showToast() {
        return true;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(JSONObject response) {
        try {
            if ((response == null || "".equals(response.toString())) && showToast()) {
                mWidgetInterface.showFailToast("抱歉，服务器返回错误");
                return;
            }
            String code = response.optString("code");
            success = code.startsWith("10");
            if (!success) {
                String msg = response.optString("msg");
                if (StrUtils.isNotEmpty(msg) && !TextUtils.equals("null", msg) && showToast())
                    mWidgetInterface.showFailToast(msg);
                return;
            }
            mType = getSuperclassTypeParameter(getClass());
            T fromJson = null;
            if (mType != null && !mType.toString().equals(Void.class)) {
                if (TextUtils.isEmpty(node)) {
                    fromJson = VolleyManager.getVolleyManager().getGson().fromJson(response.toString(), mType);
                } else if (!TextUtils.isEmpty(response.optString(node))) {
                    if (mType.toString().startsWith("class java.lang"))
                        fromJson = (T) response.get(node);
                    else
                        fromJson = VolleyManager.getVolleyManager().getGson().fromJson(response.optString(node), mType);
                }
                if (null != fromJson)
                    onSuccess(fromJson);
            }
            onSuccessWithNull(fromJson);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDialog();
        }
    }

    /**
     * 这个方法是返回时可以为空
     */
    protected void onSuccessWithNull(T t) {

    }

    public void closeDialog() {
        if (showDialog)
            mWidgetInterface.hideLoadingView();
        onFinsh();
    }

    public void onSuccess(T t) {

    }


    public void onFinsh() {

    }

    public void onStart() {
        if (showDialog)
            mWidgetInterface.showLoadingView();
    }

}
