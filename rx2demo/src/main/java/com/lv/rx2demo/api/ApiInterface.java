package com.lv.rx2demo.api;


import com.lv.rx2demo.model.Data;
import com.lv.rx2demo.model.TestBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * User: 吕勇
 * Date: 2016-06-03
 * Time: 10:24
 * Description:
 */
public interface ApiInterface {

    @POST("OneData")
    Observable<Data> dataOne();

    @POST("OneData")
    Observable<TestBean> dataOne2();


    @POST("OneData")
    Observable<Void> dataVoid();

    @POST("OneData")
    Observable<List<Data>> dataString();

    @POST("OneData")
    Observable<String> dataString(@Header("wode") String authorization, @Body Map<String, String> map) ;


}
