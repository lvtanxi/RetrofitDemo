package com.lv.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lv.test.bean.Data;
import com.lv.test.bean.TestBean;
import com.lv.test.client.Retrofit2Client;
import com.lv.test.client.Retrofit3Client;
import com.lv.test.client.RetrofitClient;
import com.lv.test.helper.RxResultHelper;
import com.lv.test.helper.RxSchedulers;
import com.lv.test.net.LoadingSubscriber;
import com.lv.test.net.WidgetInterface;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WidgetInterface {
    private CompositeSubscription mCompositeSubscription;
    private int index = 0;
    private SpannableString spannableString;
    private RelativeSizeSpan mRelativeSizeSpan;

    public static void startMainActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        index = 1;
    }

    private void initView() {
        index = 2;
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
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
            case R.id.button4:
                httpTest4();
                break;
            case R.id.button5:
                httpTest5();
                break;
        }
    }

    private void httpTest5() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("keys", new String[]{"1", "2"});
        addSubscription(RetrofitClient.getApiInterface().testUserArray(objectMap), new LoadingSubscriber<TestBean>(this) {
            @Override
            protected void onSuccess(TestBean testBean) {
                DLog.d(">>>>>>>>>>>>" + testBean);
            }
        });
    }

    private void httpTest4() {
        Observable.zip(Retrofit2Client.getInstance().mApiInterface.testUser(), Retrofit2Client.getInstance().mApiInterface.testUserXX("sadf"), new Func2<List<Data>, TestBean, TestBean>() {
            @Override
            public TestBean call(List<Data> datas, TestBean testBean) {
                DLog.d((null == datas) + ".." + (testBean == null));
                return null;
            }
        }).onErrorReturn(new Func1<Throwable, TestBean>() {
            @Override
            public TestBean call(Throwable throwable) {
                DLog.d(throwable.getMessage());
                return null;
            }
        }).doOnNext(new Action1<TestBean>() {
            @Override
            public void call(TestBean testBean) {
                DLog.d(Thread.currentThread().getName());
            }
        }).compose(RxSchedulers.<TestBean>io_main())
                .subscribe(new LoadingSubscriber<TestBean>(this) {
                    @Override
                    protected void onSuccess(TestBean testBean) {
                        DLog.d(">>>>>>>>>>>>" + (testBean == null));
                    }
                });
    }

    private void httpTest3() {
        addSubscription(Retrofit2Client.getInstance().mApiInterface.testUserString(), new LoadingSubscriber<String>(this) {
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
        Subscription subscription = observable.compose(RxSchedulers.<T>io_main())
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
        addSubscription(RetrofitClient.getApiInterface().testUser(),
                new LoadingSubscriber<List<Data>>(this) {
                    @Override
                    protected void onSuccess(List<Data> datas) {
                        DLog.d(datas);
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
        addSubscription(RetrofitClient.getApiInterface().testUser(),
                new LoadingSubscriber<List<Data>>(this) {
                    @Override
                    protected void onSuccess(List<Data> datas) {
                        DLog.d("MainActivity", "datas:" + datas);
                    }
                });
    }

    public void newHeader(View view) {
        Map<String, String> param = new ArrayMap<>();
        param.put("k1", "v1");
        param.put("k2", "v2");
        param.put("k3", "v3");
        Map<String, String> param2 = new ArrayMap<>();
        param.put("boy1", "v1");
        param.put("boy2", "v2");
        param.put("boy3", "v3");

        addSubscription(Retrofit3Client.getInstance()
                .mApiInterface.dataString(param2)
                .compose(RxResultHelper.<String>handleResult2())
                .subscribe(new LoadingSubscriber<String>(this) {
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                    }
                }));
    }

    public void onUploadFile(View view) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "123.jpg");
        RequestBody requestBody = RequestBody.create(MediaType.parse("enctype=multipart/form-data"), file);
        Map<String,RequestBody> params =new ArrayMap<>();
        params.put(1+file.getName(),requestBody);
        params.put(2+file.getName(),requestBody);
        params.put(3+file.getName(),requestBody);
        params.put(4+file.getName(),requestBody);
        addSubscription(Retrofit3Client.getInstance().mApiInterface.upload(params)
                .compose(RxResultHelper.<Void>handleResult2())
                .subscribe(new LoadingSubscriber<Void>(this) {
                    @Override
                    protected void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DLog.e(e);
                        Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showSuccessToast(@NonNull String message) {
        DLog.d(message);
    }

    @Override
    public void showFailToast(@NonNull String message) {
        DLog.d(message);
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
