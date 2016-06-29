package com.lv.test.net;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 
* @Title: VolleyRequest.java 
* @Description: 网络请求
* @author 吕勇   
* @date 2016-1-29 上午9:22:35
 */
public class VolleyRequest<T> extends JsonObjectRequest {

    private  Map<String, String> headerResult;
	private   Map<String, String> headers;
	private String paramStr;

    /**
     * 初始化网络请求
     * @param url 网络地址
     * @param params Map集合的参数
     * @param responseListener 回调Listener
     */
    public VolleyRequest(String url, Map<String,Object> params,final VolleyResponseListener<T> responseListener){
    	 super(Method.POST, url,null,responseListener,new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(responseListener.showToast()){
					String message = VolleyErrorHelper.getErrorMessage(error);
					if(StrUtils.isNotEmpty(message)&&!TextUtils.equals("null", message)){
						//TODO 显示错误
					}

				}
				responseListener.closeDialog();
			}
		});
		setShouldCache(false);
		this.paramStr=VolleyManager.getVolleyManager().getGson().toJson(params);
		setRetryPolicy(new DefaultRetryPolicy(30 * 1000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
	@Override
	public byte[] getBody() {
		if(null!=paramStr)return paramStr.getBytes();
		return super.getBody();
	}

	@Override
	public Map<String, String> getHeaders() {
		return createHeaders();
	}

	private Map<String,String> createHeaders() {
		if(null==headers){
			headers = new HashMap<>();
			headers.put("Accept", "application/json");
			headers.put("Content-Type", "application/json; charset=UTF-8");
		}
		return headers;
	}


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        headerResult = response.headers;
        return super.parseNetworkResponse(response);
    }

    public Map<String, String> getHeaderResult() {
        return headerResult;
    }

}
