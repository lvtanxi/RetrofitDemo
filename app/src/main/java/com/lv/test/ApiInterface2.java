package com.lv.test;

import com.lv.test.bean.Data;
import com.lv.test.helper.RestResult;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    Observable<RestResult<String>> dataString(@Body Map<String,String> map);

    @POST("OneData")
    Observable<RestResult<String>> dataString(@Header("wode") String authorization, @Body Map<String,String> map) ;

    @Multipart
    @POST("OneData")//不建议使用，缺乏灵活性
    Observable<RestResult<Void>> upload(@Part("fileName") String des, @Part("file\\; filename=\\hahah.png") RequestBody file);

    @Multipart
    @POST("OneData")//单个和多个文件都可以上传
    Observable<RestResult<Void>> upload(@PartMap Map<String,RequestBody> params);

}
