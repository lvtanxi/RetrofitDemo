package com.lv.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lv.test.bean.Data;
import com.lv.test.net.LoadingSubscriber;
import com.lv.test.net.WidgetInterface;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WidgetInterface {
    private CompositeSubscription mCompositeSubscription;
    private Button button;
    private Button button2;
    private TextView textView;
    private int index = 0;
    private   SpannableString spannableString;
    private RelativeSizeSpan mRelativeSizeSpan;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textView.setText(spannableString);
            index++;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textView1);
      /*  SpannableString spannableString = new SpannableString("为文字设置点击事件");
        MyClickableSpan clickableSpan = new MyClickableSpan("http://www.jianshu.com/users/dbae9ac95c78");
        spannableString.setSpan(clickableSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.parseColor("#36969696"));
        textView.setText(spannableString);*/
        /*  SpannableString spannableString = new SpannableString("为文字设置超链接");
        URLSpan urlSpan = new URLSpan("http://www.jianshu.com/users/dbae9ac95c78");
        spannableString.setSpan(urlSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.parseColor("#36969696"));*/

        textView.setText("为文字设置超链接");

        spannableString = new SpannableString("为文字设置超链接");
        mRelativeSizeSpan = new RelativeSizeSpan(1.5f);

        final Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                String s = textView.getText().toString();
                if (index < s.length()) {
                    spannableString.setSpan(mRelativeSizeSpan, index, index+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    mHandler.sendEmptyMessage(0);
                } else {
                    DLog.d("asdf");
                    timer.cancel();
                }

            }
        }, 2000, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.button2:
                httpTest2();
                break;
        }
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
                DLog.d(datas);
            }
        });

    }

    private void httpTest2() {
        addSubscription(ResetClient.getClient().testUserVoid(),
                new LoadingSubscriber<Void>(this) {
                    @Override
                    protected void onSuccess(Void aVoid) {
                        DLog.d(aVoid);
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
            textView.append(content);
        }
    }
}
