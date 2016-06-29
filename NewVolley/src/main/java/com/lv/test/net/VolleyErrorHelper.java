package com.lv.test.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * User: 吕勇
 * Date: 2016-02-03
 * Time: 09:16
 * Description:错误监听类
 */
public class VolleyErrorHelper {

    /**
     * 获取错误信息
     * @param error VolleyError的日志
     * @return 错误提示
     */
    public static String getErrorMessage(Object error) {
        if(error instanceof TimeoutError){
            return "连接超时，请稍候再试";
        }else if (isNetworkProblem(error)) {
            return "服务器忙，请稍候再试";
        } else if (isServerProblem(error)) {
            return handleServerError(error);
        }else if (null!=error&&error.toString().contains("Bad URL")) {
            return "无效的URL地址";
        }
        return "网络异常,请稍后再试！";
    }



    /**
     * 是不是网络错误
     * @param error VolleyError的日志
     * @return true or false
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError);
    }

    /**
     * 是不是服务器错误
     * @param error VolleyError的日志
     * @return true or false
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * 解析服务器错误原因
     * @param err VolleyError的日志
     * @return 错误提示
     */
    private static String handleServerError(Object err) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {
                case 404:
                    return "无效的URL地址";
                case 422:
                    return "请求格式正确，但是由于含有语义错误，无法响应";
                case 401:
                    return "服务器验证已经拒绝了访问";
                case 408:
                    return "请求超时，请稍候再试";
                default:
                    return "服务器忙，请稍候再试";
            }
        }
        return "网络异常,请稍后再试~！";
    }
}
