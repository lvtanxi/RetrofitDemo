package com.lv.test;

import com.lv.test.bean.Data;
import com.lv.test.bean.TestBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * User: 吕勇
 * Date: 2016-06-03
 * Time: 10:24
 * Description:
 */
public interface ApiInterface {
    @GET("Xx")
    Observable<List<Data>> testUser();
    @POST("Xx")
    Observable<Void> testUserVoid();
    @POST("Xx")
    Observable<String> testUserString();
    @GET("Xx")
    Call<TestBean> testUser2();

    @POST("Xx")
    Observable<String> test();



    @POST("test")
    Observable<TestBean> testUserXX(@Body String TEX);

    @POST("Xx")
    Observable<TestBean> testUserArray(@Body Map<String,Object> map);



}
