package com.lv.test;

import com.lv.test.bean.Data;
import com.lv.test.helper.RestResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * User: 吕勇
 * Date: 2016-06-03
 * Time: 10:24
 * Description:
 */
public interface ApiInterface2 {
    @GET("ListData")
    Observable<RestResult<List<Data>>> listData();

    @POST("OneData")
    Observable<RestResult<Data>> dataOne();


    @POST("OneData")
    Observable<RestResult<Void>> dataVoid();

    @POST("OneData")
    Observable<RestResult<String>> dataString();

    @POST("OneData")
    Observable<RestResult<String>> dataString(@Header("wode") String authorization, @Body Map<String,String> map) ;
}
