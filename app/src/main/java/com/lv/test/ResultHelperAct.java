package com.lv.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lv.test.bean.Data;
import com.lv.test.client.Retrofit3Client;
import com.lv.test.helper.RxResultHelper;
import com.lv.test.helper.RxSchedulers;
import com.lv.test.net.LoadingSubscriber;
import com.lv.test.net.WidgetInterface;

import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * User: 吕勇
 * Date: 2016-07-06
 * Time: 13:41
 * Description:
 */
public class ResultHelperAct extends AppCompatActivity implements WidgetInterface {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_he);
    }

    public void one(View view) {
        Retrofit3Client
                .getInstance()
                .mApiInterface
                .dataOne()
                .compose(RxResultHelper.<Data>handleResult())
                .compose(RxSchedulers.<Data>io_main())
                .subscribe(new LoadingSubscriber<Data>(this) {
                    @Override
                    protected void onSuccess(Data data) {
                        DLog.d(data);
                    }
                });

    }

    public void dataList(View view) {
        Retrofit3Client
                .getInstance()
                .mApiInterface
                .listData()
                .compose(RxResultHelper.<List<Data>>handleResult())
                .compose(RxSchedulers.<List<Data>>io_main())
                .subscribe(new LoadingSubscriber<List<Data>>(this) {
                    @Override
                    protected void onSuccess(List<Data> datas) {
                        DLog.d(datas);
                    }
                });
    }


    public void zip(View view) {
        Observable.zip(
                Retrofit3Client
                .getInstance()
                .mApiInterface
                .listData()
                .compose(RxResultHelper.<List<Data>>handleResult()),
                Retrofit3Client
                .getInstance()
                .mApiInterface
                .dataOne()
                .compose(RxResultHelper.<Data>handleResult()),
                new Func2<List<Data>, Data, Boolean>() {
            @Override
            public Boolean call(List<Data> datas, Data data) {
                DLog.d(datas);
                DLog.d(data);
                return true;
            }
        }).compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new LoadingSubscriber<Boolean>(this) {
                    @Override
                    protected void onSuccess(Boolean aBoolean) {
                    }
                });
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showSuccessToast(@NonNull String message) {

    }

    @Override
    public void showFailToast(@NonNull String message) {
        DLog.d(message);
    }

    @Override
    public void notifyDialog(@NonNull String message) {

    }


    public void voidData(View view) {
        Retrofit3Client
                .getInstance()
                .mApiInterface
                .dataVoid()
                .compose(RxResultHelper.<Void>handleResult())
                .compose(RxSchedulers.<Void>io_main())
                .subscribe(new LoadingSubscriber<Void>(this) {
                    @Override
                    protected void onSuccess(Void aVoid) {
                        DLog.d(">>>>>>>>");
                    }
                });
    }

}
