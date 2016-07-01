package com.lv.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lv.test.bean.Data;
import com.lv.test.net.LoadingSubscriber;
import com.lv.test.net.WidgetInterface;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WidgetInterface {
    private CompositeSubscription mCompositeSubscription;
    private int index = 0;
    private   SpannableString spannableString;
    private RelativeSizeSpan mRelativeSizeSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        index=1;
    }

    private void initView() {
        index=2;
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button2);
            button2.setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                httpTest();
                break;
            case R.id.button2:
                httpTest2();
                break;
            case R.id.button3:
                httpTest3();
                break;
        }
    }

    private void httpTest3() {
        addSubscription(ResetClient.getClient().testUserString(), new LoadingSubscriber<String>(this) {
            @Override
            protected void onSuccess(String s) {
                DLog.d(s);
            }
        });
    }

    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscription);
    }

    protected <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber) {
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        addSubscription(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }

/*    */

    /**
     * 可自定义线程
     *//*
   public  <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }*/
    private void httpTest() {
        addSubscription(ResetClient.getClient().testUser(), new LoadingSubscriber<List<Data>>(this) {
            @Override
            protected void onSuccess(List<Data> datas) {
            }
        });
      /*  OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new LoggingInterceptor());
        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .header("User-Agent", "OkHttp Example")
                .build();
         builder.build().newCall(request).enqueue(new Callback() {
         });*/

    }

    private void httpTest2() {
        addSubscription(ResetClient.getClient().testUser(),
                new LoadingSubscriber<List<Data>>(this) {
                    @Override
                    protected void onSuccess(List<Data> datas) {
                        DLog.d("MainActivity", "datas:" + datas);
                        Toast.makeText(MainActivity.this, datas.toString(), Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void notifyDialog(@NonNull String message) {

    }

    class MyClickableSpan extends ClickableSpan {

        private String content;

        public MyClickableSpan(String content) {
            this.content = content;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            Toast.makeText(MainActivity.this, "asdfa", Toast.LENGTH_SHORT).show();
        }
    }
}
