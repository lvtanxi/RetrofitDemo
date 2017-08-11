package com.lv.rx2demo.api;


import com.lv.rx2demo.model.UpdateBean;

import io.reactivex.Flowable;
import retrofit2.http.POST;

/**
 * User: 吕勇
 * Date: 2016-06-03
 * Time: 10:24
 * Description:
 */
public interface ApiInterface {

    @POST("app/getNewest")
    Flowable<UpdateBean> dataVoid();

    @POST("app/getNewest")
    Flowable<String> dataString();

}
